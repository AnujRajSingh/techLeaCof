package com.ulap.util.tecleaApi.Algorithm;

import java.util.*;

/**
 * Represents a date where all the elements (like month or day of month) might
 * not be set. Whereas PartialDates with missing elements are considered valid,
 * PartialDates are considered invalid if one or more elements are set with
 * values that could not be parsed or were outside their allowable range.
 */
@SuppressWarnings({"NumberEquality"})
public class PartialDate
{
  private static final Set<String> weekdayNames = new HashSet<String>(Arrays.asList("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"));
  private static final Set<String> weekdayAbbreviationNames = new HashSet<String>(Arrays.asList("SUN", "MON", "TUES", "WED", "THURS", "FRI", "SAT"));
  private static final Integer INVALID = -1;
  private static final int FIFTY_YEARS = (int) (50 * 365.25); // Shaves off half a day, which is ok, given that not every 100 years is a leap year.
  private static final Map<String, Integer> MONTHS_MAP = new HashMap<String, Integer>();
  private static final List<Integer> DAYS_IN_MONTH = new ArrayList<Integer>();
  Integer year = null;
  Integer month = null;
  boolean numericMonth = false;
  Integer day = null;
  String weekday = null;

  static
  {
    // TODO: Add common international month names
    MONTHS_MAP.put("JANUARY", 1);
    MONTHS_MAP.put("FEBRUARY", 2);
    MONTHS_MAP.put("MARCH", 3);
    MONTHS_MAP.put("APRIL", 4);
    MONTHS_MAP.put("MAY", 5);
    MONTHS_MAP.put("JUNE", 6);
    MONTHS_MAP.put("JULY", 7);
    MONTHS_MAP.put("AUGUST", 8);
    MONTHS_MAP.put("SEPTEMBER", 9);
    MONTHS_MAP.put("OCTOBER", 10);
    MONTHS_MAP.put("NOVEMBER", 11);
    MONTHS_MAP.put("DECEMBER", 12);
    MONTHS_MAP.put("JAN", 1);
    MONTHS_MAP.put("FEB", 2);
    MONTHS_MAP.put("MAR", 3);
    MONTHS_MAP.put("APR", 4);
    MONTHS_MAP.put("MAY", 5);
    MONTHS_MAP.put("JUN", 6);
    MONTHS_MAP.put("JUL", 7);
    MONTHS_MAP.put("AUG", 8);
    MONTHS_MAP.put("SEPT", 9);
    MONTHS_MAP.put("SEP", 9);
    MONTHS_MAP.put("OCT", 10);
    MONTHS_MAP.put("NOV", 11);
    MONTHS_MAP.put("DEC", 12);
    DAYS_IN_MONTH.add(31);
    DAYS_IN_MONTH.add(28);
    DAYS_IN_MONTH.add(31);
    DAYS_IN_MONTH.add(30);
    DAYS_IN_MONTH.add(31);
    DAYS_IN_MONTH.add(30);
    DAYS_IN_MONTH.add(31);
    DAYS_IN_MONTH.add(31);
    DAYS_IN_MONTH.add(30);
    DAYS_IN_MONTH.add(31);
    DAYS_IN_MONTH.add(30);
    DAYS_IN_MONTH.add(31);
  }

  PartialDate()
  {
  }

  void setYear(String year)
  {
    setYear(parseInt(year));
  }

  void setYear(Integer year)
  {
    this.year = year;
  }

  void setMonth(String month)
  {
    numericMonth = false;
    this.month = MONTHS_MAP.get(month.toUpperCase());
    if (this.month == null)
    {
      setMonth(parseInt(month));
    }
  }

  void setMonth(Integer month)
  {
    if (month > 0 && month <= 12)
    {
      this.month = month;
      numericMonth = true;
    }
    else
    {
      this.month = INVALID;
    }
  }

  void setDay(String day)
  {
    try
    {
      this.day = Integer.valueOf(day);
      setDay(this.day);
    }
    catch (NumberFormatException ignore)
    {
      this.day = INVALID;
    }
  }

  void setDay(Integer day)
  {
    this.day = day > 0 && day <= 31 ? day : INVALID;
  }

  private int parseInt(String s)
  {
    try
    {
      return Integer.valueOf(s);
    }
    catch (NumberFormatException ignore)
    {
      return INVALID;
    }
  }

  void parse(String[] elements, int[] types)
  {
    int typesLength = types.length;
    int i = 0;
    for (String element : elements)
    {
      if (!"".equals(element))
      {
        int elementType = types[i];
        switch (elementType)
        {
          case DateParser.DAY:
            setDay(element);
            break;
          case DateParser.YEAR:
            setYear(element);
            break;
          case DateParser.MONTH:
            setMonth(element);
            break;
          default:
            break;
        }
        if (++i == typesLength)
        {
          break;
        }
      }
    }
  }

  public boolean isValid()
  {
    if (year == null || year == INVALID || month == INVALID || day == INVALID)
    {
      return false;
    }
    if (weekday != null && !validWeekdayNames(weekday))
    {
      return false;
    }
    if (month == null)
    {
      if (day != null)
      {
        return false;
      }
    }
    else if (day != null && !validDaysInMonth(month, day, year))
    {
      return false;
    }
    return true;
  }

  private static boolean validWeekdayNames(String weekday)
  {
    String weekDayUpperCase = weekday.toUpperCase();
    return weekdayNames.contains(weekDayUpperCase) || weekdayAbbreviationNames.contains(weekDayUpperCase);
  }

  private static boolean validDaysInMonth(int month, int day, int year)
  {
    int monthLength = getMonthLength(month, year);
    return day > 0 && day <= monthLength;
  }

  private static int getMonthLength(int month, int year)
  {
    return month == 2 ? determineLeapYear(year) : DAYS_IN_MONTH.get(month - 1);
  }

  static int determineLeapYear(int year)
  {
    // Unfortunately, 1900 was a leap year, so providing 00 had better mean 2000.
    return (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) ? 29 : 28;
  }

  /**
   * Converts a PartialDate to a java.util.Date with valid Year Month and Day.
   * Missing elements are calculate such that the returned date will be as close
   * as possible to the provided date.
   * <p/>
   * If both this date and the provided date are missing elements, the returned
   * date will be the lowest possible date that is still as closes as possible
   * to the provided date.
   * <p/>
   * If both this date and the provided date have a two-digit year, this
   * assumption is that they are of the same century. Otherwise if this date has
   * a two-digit year, the year will be set to the century that results in the
   * closest result - always less than 50 years.
   * <p/>
   * If either this or the provided date are invalid null is returned.
   *
   * @param other The reference date that will be used for calculating the
   *              missing elements in this date.
   * @return A java.util.Date that is as close as possible to the provided date
   *         or null if either this or the provided date are invalid.
   */
  public Date getFullDateClosestTo(PartialDate other)
  {
    if (!(isValid() && other.isValid()))
    {
      return null;
    }
    Integer thisYear = year;
    Integer thisMonth = month;
    Integer thisDay = day;
    Integer otherYear = other.year;
    Integer otherMonth = other.month;
    Integer otherDay = other.day;
    // a null year is not valid. No need to check that.
    if (thisYear < 100 && otherYear > 999)
    {
      // This is trickier than looking just at the years.
      // Months and days matter when years off by 50.
      // So just pick an answer, try it out, adjust as necessary, proceed.
      Integer savedYear = thisYear;
      year = thisYear + otherYear / 100 * 100;
      Date thisDate = getFullDateClosestTo(other);
      Date otherDate = other.getFullDateClosestTo(this);
      long deltaInDays = getDateDifferenceInDays(thisDate, otherDate);
      if (deltaInDays > FIFTY_YEARS)
      {
        thisYear = year + 100;
      }
      else if (deltaInDays < (-1 * FIFTY_YEARS))
      {
        thisYear = year - 100;
      }
      else
      {
        // We guessed right. No need to recalculate.
        year = savedYear;
        return thisDate;
      }
      year = savedYear;
    }

    if (thisMonth == null)
    {
      if (thisYear > otherYear)
      {
        thisMonth = 1;
      }
      else if (thisYear < otherYear)
      {
        thisMonth = 12;
      }
      else if (otherMonth == null)
      {
        thisMonth = 1;
        otherMonth = 1;
      }
      else
      {
        thisMonth = otherMonth;
      }
    }
    if (thisDay == null)
    {
      if (thisYear > otherYear)
      {
        thisDay = 1;
      }
      else if (thisYear < otherYear || thisMonth < otherMonth)
      {
        thisDay = getMonthLength(thisMonth, thisYear);
      }
      else if (thisMonth > otherMonth || otherDay == null)
      {
        thisDay = 1;
      }
      else
      {
        thisDay = otherDay;
      }
    }
    return new GregorianCalendar(thisYear, thisMonth - 1, thisDay).getTime();
  }

  /**
   * Calculates otherDate minus thisDate
   *
   * @param thisDate  The subtrahend
   * @param otherDate The minuend
   * @return otherDate minus thisDate expressed in units of days
   */
  static long getDateDifferenceInDays(Date thisDate, Date otherDate)
  {
//  The following says Apr 10 minus Mar 10 = 30 (because of daylight savings time!)
//    return (otherDate.getTime() - thisDate.getTime()) / 1000 / 3600 / 24;
    return Math.round(((double) otherDate.getTime() - thisDate.getTime()) / 1000 / 3600 / 24);
  }

  public Integer getYear()
  {
    return year;
  }

  public Integer getMonth()
  {
    return month;
  }

  public boolean isNumericMonth()
  {
    return numericMonth;
  }

  public Integer getDay()
  {
    return day;
  }

  public String getWeekday()
  {
    return weekday;
  }

  @Override
  public String toString()
  {
    return "" +
        (month == INVALID ? "MM" : month) +
        '/' +
        (day == INVALID ? "DD" : day) +
        '/' +
        (year == INVALID ? "YYYY" : year);
  }
}
