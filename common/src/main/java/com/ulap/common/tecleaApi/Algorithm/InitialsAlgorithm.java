package com.ulap.util.tecleaApi.Algorithm;

import java.io.UnsupportedEncodingException;

public class InitialsAlgorithm implements MatchAlgorithm
{
  private String NAME = "Initials Algorithm";

  private InitialsAlgorithm()
  {
  }

  public static InitialsAlgorithm INSTANCE = new InitialsAlgorithm();

  public String getName()
  {
    return NAME;
  }

  public double compare(String driver, String candidate) throws UnsupportedEncodingException
  {
    if (driver == null || candidate == null)
    {
      return 0;
    }
    driver = driver.trim();
    candidate = candidate.trim();
    int suspectLength = driver.length();
    int candidateLength = candidate.length();
    if (suspectLength == 0 || candidateLength == 0)
    {
      return 0;
    }

    String initial = ((suspectLength >= candidateLength ? candidate : driver).toUpperCase().replaceAll("[.]", "")); //Shortest is the initial
    String name = (suspectLength < candidateLength ? candidate : driver).toUpperCase().replaceAll("[.]", ""); //Shortest is the initial
    String[] nameTerms = name.split("[ ]+");
    String[] initialTerms = initial.split("[ ]+");
    if (nameTerms.length > 1 && initialTerms.length > 1)//If both have more than 1 term in string then not match
    {
      return 0;
    }
    if ((nameTerms[0].length() > 1 && initialTerms[0].length() == 1) || (nameTerms[0].length() == 1 && initialTerms[0].length() > 1))
    {//If one string has 1 character and the other has more than 1 character
      if (nameTerms[0].charAt(0) == initialTerms[0].charAt(0))
      {
        return 100;
      }
    }
    if (nameTerms[0].length() == 1 && initialTerms[0].length() == 1)
    {
      if (nameTerms[0].equalsIgnoreCase(initialTerms[0]))
      {
        return 100;
      }
    }
    return 0;
  }
}
