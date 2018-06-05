package com.ulap.util.tecleaApi.Algorithm;

/**
 * To change this template use Options | File Templates.
 * <p> Edit Distance is a measure of the similarity between two strings,
 * The distance is the number of deletions, insertions, or substitutions required to transform
 * one string into the other.
 * </p>
 */
public class EditDistance implements MatchAlgorithm
{
  public static final MatchAlgorithm INSTANCE = new EditDistance();

  private static final String NAME = "Edit Distance";

  private EditDistance()
  {
  }

  public String getName()
  {
    return NAME;
  }

  /**
   * Compares two strings to determine the number of character changes needed to make
   * the strings the same and returns a score as a percentage.
   *
   * @param suspect   String to be compared to candidate.
   * @param candidate String to be compared to suspect.
   * @return integer match score as a percentage.
   */
  public double compare(String suspect, String candidate)
  {
    double rScore;
    int d[][]; // matrix
    int suspectIter; // iterates through suspect
    int candidateIter; // iterates through candidate
    char s_i; // ith character of suspect
    char c_j; // jth character of candidate
    int cost; // cost
    double score; // edit distance score

    // Step 1

    int suspectLength = (suspect != null) ? suspect.length() : 0;
    int candidateLength = (candidate != null) ? candidate.length() : 0;

    if (suspectLength == 0 || candidateLength == 0)
    {
      return 0;
    }
    char[] suspectLowerCase = suspect.toLowerCase().toCharArray();
    char[] candidateLowerCase = candidate.toLowerCase().toCharArray();
    d = new int[suspectLength + 1][candidateLength + 1];

    // Step 2

    for (suspectIter = 0; suspectIter <= suspectLength; suspectIter++)
    {
      d[suspectIter][0] = suspectIter;
    }
    for (candidateIter = 0; candidateIter <= candidateLength; candidateIter++)
    {
      d[0][candidateIter] = candidateIter;
    }

    // Step 3
    for (suspectIter = 1; suspectIter <= suspectLength; suspectIter++)
    {
      s_i = suspectLowerCase[suspectIter - 1];

      // Step 4

      for (candidateIter = 1; candidateIter <= candidateLength; candidateIter++)
      {
        c_j = candidateLowerCase[candidateIter - 1];

        // Step 5

        if (s_i == c_j)
        {
          cost = 0;
        }
        else
        {
          cost = 1;
        }

        // Step 6

        d[suspectIter][candidateIter] = minimum(d[suspectIter - 1][candidateIter] + 1, d[suspectIter][candidateIter - 1] + 1, d[suspectIter - 1][candidateIter - 1] + cost);

        // Step 7

      }
    }

    // Step 8
    score = d[suspectLength][candidateLength];

    if (suspectLength == score)
    {
      rScore = 0;
    }
    else
    {
      if (suspectLength > candidateLength)
      {
        candidateLength = suspectLength;
      }
      rScore = (1.0 - (score / candidateLength)) * 100;
    }
    return rScore;
  }

  /**
   * Get the minimum of three values.
   *
   * @param a integer value.
   * @param b integer value.
   * @param c integer value.
   * @return integer of the minimum value.
   */
  private static int minimum(int a, int b, int c)
  {
    int mi;

    mi = a;
    if (b < mi)
    {
      mi = b;
    }
    if (c < mi)
    {
      mi = c;
    }
    return mi;
  }
}
