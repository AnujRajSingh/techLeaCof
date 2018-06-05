package com.ulap.util.tecleaApi.Algorithm;

import java.io.UnsupportedEncodingException;

public class NumericString implements MatchAlgorithm
{
  private static final String NAME = "Numeric String";

  public NumericString()
  {
  }

  public static NumericString INSTANCE = new NumericString();

  public String getName()
  {
    return NAME;
  }

  public double compare(String suspect, String candidate) throws UnsupportedEncodingException
  {
    StringBuilder suspectNumeric = new StringBuilder();
    StringBuilder suspectString = new StringBuilder();

    StringBuilder candidateNumeric = new StringBuilder();
    StringBuilder candidateString = new StringBuilder();

    separate(suspect, suspectNumeric, suspectString);
    separate(candidate, candidateNumeric, candidateString);

    double numericScore = 0.0;
    if (suspectNumeric.toString().equals(candidateNumeric.toString()))
    {
      numericScore = 100.0;
    }
    double editScore = EditDistance.INSTANCE.compare(suspectString.toString(), candidateString.toString());
    double charFreq = CharacterFrequency.INSTANCE.compare(suspectString.toString(), candidateString.toString());
    return (numericScore + (editScore + charFreq) / 2) / 2;
  }

  private void separate(String input, StringBuilder numeric, StringBuilder string)
  {
    if (input == null || input.length() == 0)
    {
      return;
    }
    for (char inputChar : input.toCharArray())
    {
      if (Character.isDigit(inputChar))
      {
        numeric.append(inputChar);
      }
      else
      {
        string.append(inputChar);
      }
    }
  }
}
