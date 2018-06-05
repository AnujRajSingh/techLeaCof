package com.ulap.util.tecleaApi.Algorithm;

/**

 * To change this template use Options | File Templates.
 * <p> Determines if two strings are the same.</p>
 */
public class ExactMatch implements MatchAlgorithm
{
  public static final MatchAlgorithm INSTANCE = new ExactMatch();

  private static final String NAME = "Exact Match";

  private ExactMatch()
  {
  }

  public String getName()
  {
    return NAME;
  }

  /**
   * Compares two strings and returns a score as a percentage.
   *
   * @param suspect   String to be matched to a candidate String.
   * @param candidate String to be matched to a suspect String.
   * @return integer algorithm score as a percentage.
   */
  public double compare(String suspect, String candidate)
  {
    return AlgorithmUtilities.checkExactMatch(suspect, candidate);
  }
}
