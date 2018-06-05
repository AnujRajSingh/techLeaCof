package com.ulap.util.tecleaApi.Algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateParser
{
  private static final Map<String, Pattern> patternMap = new HashMap<String, Pattern>();
  static final int YEAR = 1;
  static final int MONTH = 2;
  static final int DAY = 3;
  static final int WEEKDAY = 4;
  static final int HOURS = 5;
  static final int MINUTES = 6;
  static final int SECONDS = 7;
  static final int TIME_ZONE = 8;
  private static final int[] ORDER_YEAR = new int[]{YEAR};
  private static final int[] ORDER_MONTH_YEAR = new int[]{MONTH, YEAR};
  private static final int[] ORDER_YEAR_MONTH = new int[]{YEAR, MONTH};
  private static final int[] ORDER_YEAR_MONTH_DAY = new int[]{YEAR, MONTH, DAY};
  private static final int[] ORDER_MONTH_DAY_YEAR = new int[]{MONTH, DAY, YEAR};
  private static final int[] ORDER_DAY_MONTH_YEAR = new int[]{DAY, MONTH, YEAR};
  private static final int[] ORDER_WEEKDAY_DAY_MONTH_YEAR = new int[]{WEEKDAY, DAY, MONTH, YEAR};
  private static final int[] ORDER_ASC_TIME = new int[]{WEEKDAY, MONTH, DAY, HOURS, MINUTES, SECONDS, TIME_ZONE, YEAR};
  private static final String YYYY = "YYYY";
  private static final String YYYYMM = "YYYYMM";
  private static final String MONTH_YEAR = "Month Year";
  private static final String SLASHED_DASHED_DOTED_YYYYMMDD = "SlashedDashedDotedYYYYMMDD";
  private static final String SLASHED_DASHED_DOTED_MMDDYYYY = "SlashedDashedDotedMMDDYYYY";
  private static final String SLASHED_DASHED_DOTED_MMDDYY = "SlashedDashedDotedMMDDYY";
  private static final String DDMMMYYYY = "DDMMMYYYY";
  private static final String DDMMMYY = "DDMMMYY";
  private static final String MMMDDYYYY = "MMMDDYYYY";
  private static final String MMMDDYY = "MMMDDYY";
  private static final String YYYYMMMDD = "YYYYMMMDD";
  private static final String RFC_822 = "RFC822";
  private static final String W3CDTF = "W3CDTF";
  private static final String ASCTIME = "ASCTIME";
  private static final String DASHED_YYYYMMMDD = "DashedYYYYMMMDD";
  private static final String SLASHED_DASHED_DOTED_MMYYYY = "SlashedDashedDotedMMYYYY";
  private static final String YYYYMMDD = "YYYYMMDD";
  private static final String MMDDYY = "MMDDYY";
  private static final String MMDDYYYY = "MMDDYYYY";
  private static final String DDMMYY = "DDMMYY";
  private static final String DDMMYYYY = "DDMMYYYY";
  private static final String REGEX_DAY = "([12][0-9]|3[01]{1,2}|0?[1-9])";
  private static final String REGEX_MM = "(1[012]{1,2}|0?[1-9])";
  private static final String REGEX_NN = "[0-9]{2}";
  private static final String REGEX_NNNN = "[0-9]{4}";
  private static final String REGEX_LLL = "[A-Za-z]{3}";
  private static final String REGEX_WORD = "[A-Za-z]+";

  private boolean preferEuropeanFormat;

  static
  {
    patternMap.put(YYYY, Pattern.compile("^" + REGEX_NNNN + "$"));
    patternMap.put(MONTH_YEAR, Pattern.compile("^" + REGEX_WORD + "[ ,\\-/]+(" + REGEX_NN + "|" + REGEX_NNNN + ")$"));
    patternMap.put(YYYYMM, Pattern.compile("^" + REGEX_NNNN + "[ -/.](" + REGEX_LLL + "|" + REGEX_MM + ")$"));
    // TODO: Replace these slash patterns with more specific month and day patterns
    patternMap.put(SLASHED_DASHED_DOTED_YYYYMMDD, Pattern.compile("^" + REGEX_NNNN + "[\\-/.]" + REGEX_MM + "[\\-/.]" + REGEX_DAY + "$"));
    patternMap.put(SLASHED_DASHED_DOTED_MMDDYYYY, Pattern.compile("^[0-9]{1,2}[-/.][0-9]{1,2}[-/.]" + REGEX_NNNN + "$"));
    patternMap.put(SLASHED_DASHED_DOTED_MMDDYY, Pattern.compile("^[0-9]{1,2}[-/.][0-9]{1,2}[-/.]" + REGEX_NN + "$"));
    patternMap.put(DDMMMYYYY, Pattern.compile("^[0-9]{1,2}[ \\-]" + REGEX_WORD + "[ \\-]" + REGEX_NNNN + "$"));
    patternMap.put(DDMMMYY, Pattern.compile("^[0-9]{1,2}[ \\-]" + REGEX_WORD + "[ \\-]" + REGEX_NN + "$"));
    patternMap.put(MMMDDYYYY, Pattern.compile("^" + REGEX_WORD + " " + REGEX_DAY + ",? (" + REGEX_NNNN + ")$"));
    patternMap.put(MMMDDYY, Pattern.compile("^" + REGEX_WORD + " " + REGEX_DAY + ",? (" + REGEX_NN + ")$"));
    patternMap.put(YYYYMMMDD, Pattern.compile("^" + REGEX_NNNN + " " + REGEX_WORD + " " + REGEX_DAY + "$"));
    patternMap.put(RFC_822, Pattern.compile("^" + REGEX_LLL + ", " + REGEX_DAY + " " + REGEX_LLL + " (" + REGEX_NN + "|" + REGEX_NNNN + ") " + REGEX_NN + ":" + REGEX_NN + ":" + REGEX_NN + " " + REGEX_LLL + "$"));
    patternMap.put(W3CDTF, Pattern.compile("^" + REGEX_NNNN + "-" + REGEX_MM + "-" + REGEX_DAY + "T" + REGEX_NN + ":" + REGEX_NN + ":" + REGEX_NN + "(Z|(-" + REGEX_NN + ":" + REGEX_NN + "))$"));
    patternMap.put(ASCTIME, Pattern.compile("^" + REGEX_LLL + " " + REGEX_LLL + " " + REGEX_DAY + " " + REGEX_NN + ":" + REGEX_NN + ":" + REGEX_NN + " " + REGEX_LLL + " " + REGEX_NNNN + "$"));
    patternMap.put(DASHED_YYYYMMMDD, Pattern.compile("^" + REGEX_NNNN + "-" + REGEX_WORD + "-" + REGEX_DAY + "$"));
    patternMap.put(SLASHED_DASHED_DOTED_MMYYYY, Pattern.compile("^" + REGEX_MM + "[-/.]" + REGEX_NNNN + "$"));
    patternMap.put(YYYYMMDD, Pattern.compile("^" + REGEX_NNNN + REGEX_MM + REGEX_DAY + "$"));
    patternMap.put(MMDDYY, Pattern.compile("^" + REGEX_MM + REGEX_DAY + REGEX_NN + "$"));
    patternMap.put(MMDDYYYY, Pattern.compile("^" + REGEX_MM + REGEX_DAY + REGEX_NNNN + "$"));
    patternMap.put(DDMMYY, Pattern.compile("^" + REGEX_DAY + REGEX_MM + REGEX_NN + "$"));
    patternMap.put(DDMMYYYY, Pattern.compile("^" + REGEX_DAY + REGEX_MM + REGEX_NNNN + "$"));
  }

  public DateParser()
  {
  }

  public void setPreferEuropeanFormat(boolean preferEuropeanFormat)
  {
    this.preferEuropeanFormat = preferEuropeanFormat;
  }

  public PartialDate parseDate(String suspectDate)
  {
    PartialDate suspectMap = new PartialDate();
    determineDateFormat(suspectDate, suspectMap);
    return suspectMap;
  }

  private boolean determineDateFormat(String date, PartialDate dateMap)
  {
//  Thu, 01 Jan 04 19:48:21 GMT or Thu, 01 Jan 2004 19:48:21 GMT
    Pattern dateFormat = patternMap.get(RFC_822);
    Matcher dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseRFC822(date, dateMap);
    }
//  2003-12-31T10:14:55-08:00 or 2003-12-31T10:14:55Z
    dateFormat = patternMap.get(W3CDTF);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseW3CDTF(date, dateMap);
    }
//      Pattern 2005-12 or 2005/12 or 2005.12
    dateFormat = patternMap.get(YYYYMM);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedYearMonth(date, dateMap);
    }
//      Pattern 2005
    dateFormat = patternMap.get(YYYY);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedYear(date, dateMap);
    }
//      Pattern  Thu Jan 04 19:48:21 GMT 2004
    dateFormat = patternMap.get(ASCTIME);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseASCTIME(date, dateMap);
    }
    if (preferEuropeanFormat)
    {
      if (parseEuFormats(date, dateMap))
      {
        return true;
      }
      if (parseNeutralFormats(date, dateMap))
      {
        return true;
      }
      if (parseUsFormats(date, dateMap))
      {
        return true;
      }
    }
    else
    {
      if (parseUsFormats(date, dateMap))
      {
        return true;
      }
      if (parseNeutralFormats(date, dateMap))
      {
        return true;
      }
      if (parseEuFormats(date, dateMap))
      {
        return true;
      }
    }
    return false;
  }

  private static boolean parseUsFormats(String date, PartialDate dateMap)
  {
//      Pattern 123104
    Pattern dateFormat = patternMap.get(MMDDYY);
    Matcher dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseMMDDYY(date, dateMap) ||
          parseYearMMDD(date, dateMap);
    }
//      Pattern 12312004
    dateFormat = patternMap.get(MMDDYYYY);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseMMDDYYYY(date, dateMap) ||
          parseYearMMDD(date, dateMap);
    }
//      Pattern 2/01/2005 or 2-01-2005 or 2.01.2005
    dateFormat = patternMap.get(SLASHED_DASHED_DOTED_MMDDYYYY);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedMonthDayYear(date, dateMap);
    }
//      Pattern 12/31/05 or 12-31-05 or 12.31.05
    dateFormat = patternMap.get(SLASHED_DASHED_DOTED_MMDDYY);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedMonthDayYear(date, dateMap);
    }
//      Pattern 20051231
    dateFormat = patternMap.get(YYYYMMDD);
    dateMatch = dateFormat.matcher(date);
    return dateMatch.find() && (parseYearMMDD(date, dateMap) || parseMMDDYYYY(date, dateMap));
  }

  private static boolean parseEuFormats(String date, PartialDate dateMap)
  {
//      Pattern 123104
    Pattern dateFormat = patternMap.get(DDMMYY);
    Matcher dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseDDMMYY(date, dateMap) ||
          parseYearMMDD(date, dateMap);
    }
//      Pattern 12312004
    dateFormat = patternMap.get(DDMMYYYY);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseDDMMYYYY(date, dateMap) ||
          parseYearMMDD(date, dateMap);
    }
//      Pattern 2/01/2005 or 2-01-2005 or 2.01.2005
    dateFormat = patternMap.get(SLASHED_DASHED_DOTED_MMDDYYYY);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedDayMonthYear(date, dateMap);
    }
//      Pattern 12/31/05 or 12-31-05 or 12.31.05
    dateFormat = patternMap.get(SLASHED_DASHED_DOTED_MMDDYY);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedDayMonthYear(date, dateMap);
    }
//      Pattern 20051231
    dateFormat = patternMap.get(YYYYMMDD);
    dateMatch = dateFormat.matcher(date);
    return dateMatch.find() && (parseYearMMDD(date, dateMap) || parseDDMMYear(date, dateMap));
  }

  private static boolean parseNeutralFormats(String date, PartialDate dateMap)
  {
//      Pattern 2005-Dec-31
    Pattern dateFormat = patternMap.get(DASHED_YYYYMMMDD);
    Matcher dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedYearMonthDay(date, dateMap) ||
          parseSlashedDashedDotedMonthDayYear(date, dateMap);
    }
//      Pattern 12/2005 or 12-2005 or 12.2005
    dateFormat = patternMap.get(SLASHED_DASHED_DOTED_MMYYYY);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedMMYYYY(date, dateMap);
    }
//      Pattern 12 May 06
    dateFormat = patternMap.get(DDMMMYY);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedDayMonthYear(date, dateMap);
    }
//      Pattern 12 May 2006
    dateFormat = patternMap.get(DDMMMYYYY);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedDayMonthYear(date, dateMap);
    }
//      Pattern May 18 2006
    dateFormat = patternMap.get(MMMDDYYYY);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedMonthDayYear(date, dateMap);
    }
//      Pattern May 18 06
    dateFormat = patternMap.get(MMMDDYY);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedMonthDayYear(date, dateMap);
    }
//      Pattern 2006 May 18
    dateFormat = patternMap.get(YYYYMMMDD);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedYearMonthDay(date, dateMap);
    }
//      Pattern May 2006
    dateFormat = patternMap.get(MONTH_YEAR);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedMonthYear(date, dateMap);
    }
//      Pattern 2005/12/31 or 2005-12-31 or 2005.12.31
    dateFormat = patternMap.get(SLASHED_DASHED_DOTED_YYYYMMDD);
    dateMatch = dateFormat.matcher(date);
    if (dateMatch.find())
    {
      return parseSlashedDashedDotedYearMonthDay(date, dateMap);
    }
//      Pattern 12/31/05 or 12-31-05 or 12.31.05
    dateFormat = patternMap.get(SLASHED_DASHED_DOTED_MMDDYY);
    dateMatch = dateFormat.matcher(date);
    return dateMatch.find() && parseSlashedDashedDotedYearMonthDay(date, dateMap);
  }

  private static boolean parseSlashedDashedDotedYear(String date, PartialDate dateMap)
  {
    dateMap.parse(new String[]{date}, ORDER_YEAR);
    return dateMap.isValid();
  }

  private static boolean parseSlashedDashedDotedYearMonth(String date, PartialDate dateMap)
  {
    dateMap.parse(date.split("[ ,\\-/]"), ORDER_YEAR_MONTH);
    return dateMap.isValid();
  }

  private static boolean parseSlashedDashedDotedMonthYear(String date, PartialDate dateMap)
  {
    dateMap.parse(date.split("[ ,\\-/]"), ORDER_MONTH_YEAR);
    return dateMap.isValid();
  }

  private static boolean parseSlashedDashedDotedYearMonthDay(String date, PartialDate dateMap)
  {
    dateMap.parse(date.split("[ \\-/.]"), ORDER_YEAR_MONTH_DAY);
    return dateMap.isValid();
  }

  private static boolean parseW3CDTF(String date, PartialDate dateMap)
  {
    dateMap.parse(date.split("[ TZ:\\-]"), ORDER_YEAR_MONTH_DAY);
    return dateMap.isValid();
  }

  private static boolean parseSlashedDashedDotedMonthDayYear(String date, PartialDate dateMap)
  {
    dateMap.parse(date.split("[ ,\\-/.]"), ORDER_MONTH_DAY_YEAR);
    return dateMap.isValid();
  }

  private static boolean parseSlashedDashedDotedDayMonthYear(String date, PartialDate dateMap)
  {
    dateMap.parse(date.split("[ \\-/.]"), ORDER_DAY_MONTH_YEAR);
    return dateMap.isValid();
  }

  private static boolean parseRFC822(String date, PartialDate dateMap)
  {
    dateMap.parse(date.split("[ ,:]"), ORDER_WEEKDAY_DAY_MONTH_YEAR);
    return dateMap.isValid();
  }

  private static boolean parseASCTIME(String date, PartialDate dateMap)
  {
    dateMap.parse(date.split("[ ,:]"), ORDER_ASC_TIME);
    return dateMap.isValid();
  }

  private static boolean parseSlashedDashedDotedMMYYYY(String date, PartialDate dateMap)
  {
    int length = date.length();
    dateMap.setMonth(date.substring(0, length - 5));
    dateMap.setYear(date.substring(length - 4, length));
    return dateMap.isValid();
  }

  private static boolean parseYearMMDD(String date, PartialDate dateMap)
  {
    int length = date.length();
    dateMap.setYear(date.substring(0, length - 4));
    dateMap.setMonth(date.substring(length - 4, length - 2));
    dateMap.setDay(date.substring(length - 2, length));
    return dateMap.isValid();
  }

  private static boolean parseMMDDYY(String date, PartialDate dateMap)
  {
    int length = date.length();
    dateMap.setYear(date.substring(length - 2, length));
    dateMap.setDay(date.substring(length - 4, length - 2));
    dateMap.setMonth(date.substring(0, length - 4));
    return dateMap.isValid();
  }

  private static boolean parseDDMMYY(String date, PartialDate dateMap)
  {
    int length = date.length();
    dateMap.setYear(date.substring(length - 2, length));
    dateMap.setMonth(date.substring(length - 4, length - 2));
    dateMap.setDay(date.substring(0, length - 4));
    return dateMap.isValid();
  }

  private static boolean parseMMDDYYYY(String date, PartialDate dateMap)
  {
    int length = date.length();
    dateMap.setYear(date.substring(length - 4, length));
    dateMap.setDay(date.substring(length - 6, length - 4));
    dateMap.setMonth(date.substring(0, length - 6));
    return dateMap.isValid();
  }

  private static boolean parseDDMMYYYY(String date, PartialDate dateMap)
  {
    int length = date.length();
    dateMap.setYear(date.substring(length - 4, length));
    dateMap.setMonth(date.substring(length - 6, length - 4));
    dateMap.setDay(date.substring(0, length - 6));
    return dateMap.isValid();
  }

  private static boolean parseDDMMYear(String date, PartialDate dateMap)
  {
    int length = date.length();
    dateMap.setDay(date.substring(0, 2));
    dateMap.setMonth(date.substring(2, 4));
    dateMap.setYear(date.substring(4, length));
    return dateMap.isValid();
  }
}
