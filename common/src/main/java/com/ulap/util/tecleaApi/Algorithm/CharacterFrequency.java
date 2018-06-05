package com.ulap.util.tecleaApi.Algorithm;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * <p>Determines the frequency of occurrence of each character in a string and
 * compares the overall frequencies between two strings.</p>
 */
public class CharacterFrequency implements MatchAlgorithm
{
  public static final MatchAlgorithm INSTANCE = new CharacterFrequency();

  private static final String NAME = "Character Frequency";

  private CharacterFrequency()
  {
  }

  public String getName()
  {
    return NAME;
  }

  /**
   * Compares the suspect and candidate strings and returns a score based on a percentage.
   *
   * @param suspect   String.
   * @param candidate String.
   * @return integer score as a percentage.
   */
  public double compare(String suspect, String candidate) throws UnsupportedEncodingException
  {
    Map<Character, Counter> characterMap = new HashMap<Character, Counter>();
    char[] longChars;
    char[] shortChars;
    if (suspect.length() > candidate.length())
    {
      longChars = suspect.toUpperCase().toCharArray();
      shortChars = candidate.toUpperCase().toCharArray();
    }
    else
    {
      shortChars = suspect.toUpperCase().toCharArray();
      longChars = candidate.toUpperCase().toCharArray();
    }
    for (char ch : longChars)
    {
      Counter counter = characterMap.get(ch);

      if (counter == null)
      {
        counter = new Counter();
        characterMap.put(ch, counter);
      }
      counter.increment();
    }
    double score = longChars.length;
    for (char ch : shortChars)
    {
      Counter counter = characterMap.get(ch);

      if (counter != null && counter.value() > 0)
      {
        counter.decrement();
        score--;
      }
    }
    double rScore;
    if (longChars.length + shortChars.length == score)
    {
      rScore = 0;
    }
    else
    {
      rScore = (1 - (score / longChars.length)) * 100;
    }
    return rScore;
  }

  private static class Counter
  {
    private int _value;

    public Counter()
    {
      _value = 0;
    }

    public void increment()
    {
      _value++;
    }

    public void decrement()
    {
      _value--;
    }

    public int value()
    {
      return _value;
    }
  }
}
