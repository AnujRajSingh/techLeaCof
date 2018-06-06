package com.ulap.util.tecleaApi.Algorithm;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Euclidean Distance: Providing a similarity measure between two strings using the vector space of combined terms as the dimensions.

 */

// Also refer http://en.wikipedia.org/wiki/Euclidean_distance
public class EuclideanDistance implements MatchAlgorithm
{
  public static final MatchAlgorithm INSTANCE = new EuclideanDistance();

  private static final String NAME = "Euclidean Distance";

  @Override
  public String getName()
  {
    return NAME;
  }

  /**
   * Providing a similarity measure between two strings using the vector space of combined terms as the dimensions.
   * 
   * @param suspect String to be compared to candidate.
   * @param candidate String to be compared to suspect.
   * @return double match score as a percentage.
   */
  @Override
  public double compare(String suspect, String candidate) throws UnsupportedEncodingException
  {
    double similarityValue = getEuclideanDistanceSimilarity(suspect, candidate);
    return (double)(Math.round(similarityValue * 10000)) / 100;
  }

  private double getEuclideanDistanceSimilarity(String suspect, String candidate)
  {
    String[] suspectTokens = getStringTokens(suspect);
    String[] candidateTokens = getStringTokens(candidate);

    double totalPossible = Math.sqrt((suspectTokens.length) + (candidateTokens.length));

    Set<String> allTokens = new HashSet<String>();
    allTokens.addAll(Arrays.asList(suspectTokens));
    allTokens.addAll(Arrays.asList(candidateTokens));

    int suspectMatchCounter, candidateMatchCounter;
    double totalDistance = 0.0;
    for (String token : allTokens)
    {
      suspectMatchCounter = 0;
      candidateMatchCounter = 0;
      // Is suspect has that token ?
      for (int i = 0; i < suspectTokens.length; i++)
      {
        if (token.equalsIgnoreCase(suspectTokens[i]))
        {
          suspectMatchCounter++;
        }
      }

      // Is candidate has that token ?
      for (int i = 0; i < candidateTokens.length; i++)
      {
        if (token.equalsIgnoreCase(candidateTokens[i]))
        {
          candidateMatchCounter++;
        }
      }

      totalDistance += ((suspectMatchCounter - candidateMatchCounter) * (suspectMatchCounter - candidateMatchCounter));
    }

    totalDistance = Math.sqrt(totalDistance);

    // calculate Euclidean similarity value
    return (totalPossible - totalDistance) / totalPossible;
  }

  private String[] getStringTokens(String name)
  {
    // TODO : verify blank strings
    Pattern p = Pattern.compile("\\s+");
    // Ignore casing while matching. We are also ignoring case in Jaro Winkler.
    String[] tokens = p.split(name.trim().toLowerCase());
    return tokens;
  }
}
