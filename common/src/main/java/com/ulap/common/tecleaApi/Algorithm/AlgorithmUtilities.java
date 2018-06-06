/*
 * Copyright: Copyright (c) 2006
 * Company: Group1 Software, Inc.
 * All Rights Reserved
 *
 * This software and documentation is the confidential and proprietary
 * information of Group1 Software, Inc. ("Confidential Information").
 *
 */

package com.ulap.util.tecleaApi.Algorithm;


public final class AlgorithmUtilities
{
  private AlgorithmUtilities()
  {
  }

  /**
   * Determines if the suspect and candidate strings are an Exact Match.
   *
   * @param suspect
   * @param candidate
   * @return integer score as a percentage.
   */
  public static double checkExactMatch(String suspect, String candidate)
  {
    double score;
    score = 0;
    if (suspect == null || suspect.length() == 0)
    {
      score = 0;
    }
    else if (suspect.equalsIgnoreCase(candidate))
    {
      score = 100;
    }

    return score;
  }

  // to do: determine if this method is still needed.
  public static double differenceScoring(int c_length, int t_length, double score)
  {
    return score / (c_length + t_length) * 100;
  }

  /**
   * This method determines the sameness between the suspect and candidate strings
   * and returns a score based on percentage.
   *
   * @param c_length
   * @param t_length
   * @param score
   * @return int Rscore
   */
  public static double getScore(int c_length, int t_length, double score)
  {
    int o;
    if (c_length > t_length)
    {
      o = c_length - t_length;
    }
    else
    {
      o = t_length - c_length;
    }
    double dRscore = score / (c_length + t_length - o);
    double rScore = 100 - (dRscore * 100);
    return rScore < 0 ? 0 : rScore;
  }
}
