package com.ulap.util.tecleaApi.Algorithm;

import java.util.Date;
import java.util.Map;


public class DateMatch implements ConfigurableMatchAlgorithm
{
  static final String DATE_REQUIRE_MONTH = "requireMonth";
  static final String DATE_REQUIRE_DAY = "requireDay";
  static final String DATE_MATCH_TRANSPOSED_MONTH_DAY = "matchTransposedMonthDay";
  static final String DATE_PREFER_DDMMYYYY = "preferMMDDYYYY";
  static final String DATE_OVERALL_RANGE = "overallRange";
  static final String DATE_YEAR_RANGE = "yearRange";
  static final String DATE_MONTH_RANGE = "monthRange";
  static final String DATE_DAY_RANGE = "dayRange";

  private static final String NAME = "Date Match";
  private boolean matchTransposedMonthDay;
  private boolean requireMonth;
  private boolean requireDay;
  private int overallRange;
  private int yearRange;
  private int monthRange;
  private int dayRange;
  private DateParser dateParser;

  public DateMatch()
  {
    dateParser = new DateParser();
  }

  public String getName()
  {
    return NAME;
  }

  @Override
  public void configure(Map<String, String> options)
  {
    if (options != null)
    {
      matchTransposedMonthDay = parseBoolean(options.get(DATE_MATCH_TRANSPOSED_MONTH_DAY), false);
      dateParser.setPreferEuropeanFormat(parseBoolean(options.get(DATE_PREFER_DDMMYYYY), false));
      requireMonth = parseBoolean(options.get(DATE_REQUIRE_MONTH), false);
      requireDay = parseBoolean(options.get(DATE_REQUIRE_DAY), false);
      overallRange = parseInt(options.get(DATE_OVERALL_RANGE));
      yearRange = parseInt(options.get(DATE_YEAR_RANGE));
      monthRange = parseInt(options.get(DATE_MONTH_RANGE));
      dayRange = parseInt(options.get(DATE_DAY_RANGE));
    }
  }

  private static boolean parseBoolean(String string, boolean defaultValue)
  {
    return string == null ? defaultValue : Boolean.parseBoolean(string);
  }

  private static int parseInt(String string)
  {
    return string == null ? 0 : Integer.parseInt(string);
  }

  @Override
  public double compare(String suspectDate, String candidateDate)
  {
    PartialDate suspectMap = dateParser.parseDate(suspectDate);
    PartialDate candidateMap = dateParser.parseDate(candidateDate);
    return determineMatch(suspectMap, candidateMap) ? 100 : 0;
  }

  private boolean determineMatch(PartialDate suspectMap, PartialDate candidateMap)
  {
    if (overallRange == 0)
    {
      return suspectMap.isValid() &&
          candidateMap.isValid() &&
          compareYear(suspectMap, candidateMap) &&
          (compareMonth(suspectMap, candidateMap) && compareDay(suspectMap, candidateMap) || compareTransposed(suspectMap, candidateMap));
    }
    else
    {
      Date suspectDate = suspectMap.getFullDateClosestTo(candidateMap);
      Date candidateDate = candidateMap.getFullDateClosestTo(suspectMap);
      if (suspectDate != null && candidateDate != null)
      {
        if (Math.abs(PartialDate.getDateDifferenceInDays(suspectDate, candidateDate)) <= overallRange)
        {
          return true;
        }
        else if (matchTransposedMonthDay && (isTransposedInRange(suspectMap, candidateMap) || isTransposedInRange(candidateMap, suspectMap)))
        {
          return true;
        }
      }
      return false;
    }
  }

  private boolean isTransposedInRange(PartialDate mapToTranspose, PartialDate otherMap)
  {
    if (mapToTranspose.numericMonth && mapToTranspose.day != null)
    {
      PartialDate transposedMap = new PartialDate();
      transposedMap.setYear(mapToTranspose.year);
      transposedMap.setMonth(mapToTranspose.day);
      transposedMap.setDay(mapToTranspose.month);
      if (transposedMap.isValid() && otherMap.isValid())
      {
        Date transposedDate = transposedMap.getFullDateClosestTo(otherMap);
        Date otherDate = otherMap.getFullDateClosestTo(transposedMap);
        return transposedDate != null
            && otherDate != null
            && Math.abs(PartialDate.getDateDifferenceInDays(transposedDate, otherDate)) <= overallRange;
      }
    }
    return false;
  }

  private boolean compareYear(PartialDate suspectMap, PartialDate candidateMap)
  {
    Integer suspectYear = suspectMap.year;
    Integer candidateYear = candidateMap.year;
    if (suspectYear == null || candidateYear == null)
    {
      return false;
    }
    else if (suspectYear > 100 && candidateYear > 100)
    {
      return withinRange(yearRange, suspectYear, candidateYear);
    }
    return withinRange(yearRange, suspectYear % 100, candidateYear % 100);
  }

  private boolean compareMonth(PartialDate suspectMap, PartialDate candidateMap)
  {
    Integer suspectMonth = suspectMap.month;
    Integer candidateMonth = candidateMap.month;
    if (suspectMonth == null || candidateMonth == null)
    {
      return !requireMonth;
    }
    else if (monthWithinRange(suspectMonth, candidateMonth))
    {
      return true;
    }
    return false;
  }

  private boolean compareDay(PartialDate suspectMap, PartialDate candidateMap)
  {
    Integer suspectDay = suspectMap.day;
    Integer candidateDay = candidateMap.day;
    if (suspectDay == null || candidateDay == null)
    {
      return !requireDay;
    }
    return withinRange(dayRange, suspectDay, candidateDay);
  }

  private boolean compareTransposed(PartialDate suspectMap, PartialDate candidateMap)
  {
    if (matchTransposedMonthDay)
    {
      Integer suspectDay = suspectMap.day;
      Integer candidateDay = candidateMap.day;
      Integer suspectMonth = suspectMap.month;
      Integer candidateMonth = candidateMap.month;
      if (suspectDay == null || candidateDay == null)
      {
        return false;
      }
      if (candidateMap.numericMonth && monthWithinRange(suspectMonth, candidateDay) && withinRange(dayRange, suspectDay, candidateMonth))
      {
        return true;
      }
      if (suspectMap.numericMonth && monthWithinRange(suspectDay, candidateMonth) && withinRange(dayRange, suspectMonth, candidateDay))
      {
        return true;
      }
    }
    return false;
  }

  private static boolean withinRange(int range, Integer suspect, Integer candidate)
  {
    return Math.abs(suspect - candidate) <= range;
  }

  private boolean monthWithinRange(Integer suspectMonth, Integer candidateMonth)
  {
    int difference = Math.abs(suspectMonth - candidateMonth);
    return (difference > 6 ? 12 - difference : difference) <= monthRange;
  }
}