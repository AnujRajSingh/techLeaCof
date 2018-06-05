package com.ulap.util.tecleaApi.Algorithm;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Kullback Leibler Distance: Providing a distance between two texts (word frequency) using Kullback-Leibler distance algorithm.
 * Brigette Biggi suggested a back off smoothing method which keeps the probability distributions sums 
 * to 1 and also allows operating on the entire vocabulary.


 */
public class KullbackLeibler implements MatchAlgorithm 
{
  public static final MatchAlgorithm INSTANCE = new KullbackLeibler();
  private static final String NAME = "Kullback Leibler";

  @Override
  public String getName()
  {
    return NAME;
  }

  /**
   * Kullback Leibler Distance.
   * 
   * @param suspect String to be compared to candidate.
   * @param candidate String to be compared to suspect.
   * @return double similarity score as a percentage.
   */
  @Override
  public double compare(String suspect, String candidate) throws UnsupportedEncodingException
  {
    double similarityValue = 1.0 - calculateKLDistance(suspect, candidate);
    return (double)(Math.round(similarityValue * 10000)) / 100;
  }

  //calculate normalized KL distance between suspect and candidate
  private double calculateKLDistance(String suspect, String candidate)
  {
    KLWordTokenMap suspectTokenMap = new KLWordTokenMap(suspect);
    KLWordTokenMap candidateTokenMap = new KLWordTokenMap(candidate);

    Map<String, Integer> susTokenMap = suspectTokenMap.getTokenMap();
    Map<String, Integer> canTokenMap = candidateTokenMap.getTokenMap();

    if(suspectTokenMap.isEmpty() && candidateTokenMap.isEmpty() )
      return 0.0;  

    // if only one string is empty then distance or divergence is large  
    if( (suspectTokenMap.isEmpty() && !candidateTokenMap.isEmpty()) || (!suspectTokenMap.isEmpty() && candidateTokenMap.isEmpty()))
      return 1.0;  

    // count the common keys/tokens in suspect from candidate (asymmetric difference)
    int commonTokenCount = countAsymmetricDifference(susTokenMap, canTokenMap);

    // calculate epsilon. It should not be zero
    double epsilon = Math.min(suspectTokenMap.getMinimumTermProbabilty(), candidateTokenMap.getMinimumTermProbabilty()) * 0.001;
    // calculate gamma - for suspect
    double gamma = 1.0 - (suspectTokenMap.size() - commonTokenCount) * epsilon ;
    // calculate gamma2 - for candidate 
    double gamma2 = 1.0 - (candidateTokenMap.size() - countAsymmetricDifference(canTokenMap, susTokenMap)) * epsilon ;


    double distance = 0.0;
    double distanceWithBlank = 0.0;
    double probOfSuspet = 0.0;
    double probOfCandidate = 0.0;

    Set<Entry<String, Integer>> suspectEntries = susTokenMap.entrySet();
    for (Entry<String, Integer> suspectEntry : suspectEntries)
    {
      String key = suspectEntry.getKey();
      probOfSuspet = suspectEntry.getValue().doubleValue()/suspectTokenMap.getFrequecySum();

      // if the token string in present in candidate then calculate probability
      if( canTokenMap.containsKey(key))
      {
        probOfCandidate = gamma * ( canTokenMap.get(key).doubleValue() /candidateTokenMap.getFrequecySum());
      }
      else
      {
        // set a very small term probability, if term is not present in candidate as per Brigette Biggi's research paper
        probOfCandidate = epsilon;
      }

      double klDivOfToken = (probOfSuspet - probOfCandidate) * Math.log(probOfSuspet/probOfCandidate);

      distance += klDivOfToken;
      // distance With Blank or empty word
      distanceWithBlank += (probOfSuspet - epsilon) * Math.log(probOfSuspet/epsilon);
    }

    Set<Entry<String, Integer>> candidateEntries = candidateTokenMap.getTokenMap().entrySet();
    for (Entry<String, Integer> candidateEntry : candidateEntries)
    {
      String candidatekey = candidateEntry.getKey();
      if(!susTokenMap.containsKey(candidatekey))
      {
        probOfCandidate = gamma2 * ( canTokenMap.get(candidatekey).doubleValue() /candidateTokenMap.getFrequecySum());
        double klDivOfToken = (probOfCandidate - epsilon) * Math.log(probOfCandidate/epsilon);
        distance += klDivOfToken;

        distanceWithBlank += klDivOfToken; // also add this KLDist to the distanceWithBlank for proper normalization
      }
    }

    // relative distance
    return (distance/distanceWithBlank) ;
  }

  private int countAsymmetricDifference(Map<String, Integer> suspectTokenMap, Map<String, Integer> candidateTokenMap)
  {
    int count = 0;
    Set<Entry<String, Integer>> suspectEntries = suspectTokenMap.entrySet();
    for (Entry<String, Integer> entry : suspectEntries)
    {
      if(candidateTokenMap.containsKey(entry.getKey()))
      {
        count++;
      }
    }
    return count;
  }

}
