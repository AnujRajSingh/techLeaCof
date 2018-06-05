package com.ulap.util.tecleaApi.Algorithm;

/**


 * <p>Determines whether that two strings are the same if one string
 * is found within the other string.</p>
 */
public class Substring implements MatchAlgorithm
{
  public static final Substring INSTANCE = new Substring();

  private static final String NAME = "Substring";

  private Substring()
  {
  }

  public String getName()
  {
    return NAME;
  }

  /**
   * Compares two strings and returns a score as a percentage.
   *
   * @param suspect   String.
   * @param candidate String.
   * @return integer algorithm score as a percentage.
   */
  public double compare(String suspect, String candidate)
  {
    if (suspect.length() > candidate.length())
    {
      return suspect.toLowerCase().indexOf(candidate.toLowerCase()) == -1 ? 0 : 100;
    }
    else
    {
      return candidate.toLowerCase().indexOf(suspect.toLowerCase()) == -1 ? 0 : 100;
    }
  }
}
