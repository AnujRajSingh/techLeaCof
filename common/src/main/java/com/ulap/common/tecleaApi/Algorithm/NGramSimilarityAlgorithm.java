package com.ulap.util.tecleaApi.Algorithm;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class NGramSimilarityAlgorithm implements ConfigurableMatchAlgorithm
{

  public static final String NGRAM_VALUE = "ngramValue";
  public static final String DROP_NOISE_CHAR_FLAG = "dropNoiseCharFlag";
  public static final String DROP_SPACE_FLAG = "dropSpaceFlag";

  private int nGramVal = 2;
  private boolean dropNoiseCharFlag = false;
  private boolean dropSpaceFlag = false;

  public int getnGramVal()
  {
    return nGramVal;
  }

  public boolean isDropNoiseCharFlag()
  {
    return dropNoiseCharFlag;
  }

  public boolean isDropSpaceFlag()
  {
    return dropSpaceFlag;
  }

  @Override
  public String getName()
  {
    return "NGram Similarity";
  }

  public double compare(String suspect, String candidate, boolean casingFlag) throws UnsupportedEncodingException
  {
    if (!casingFlag)
    {
      String lowerCaseSuspect = suspect != null ? suspect.toLowerCase() : "";
      String lowerCaseCandidate = suspect != null ? candidate.toLowerCase() : "";
      return compare(lowerCaseSuspect, lowerCaseCandidate);
    }
    return compare(suspect, candidate);
  }

  @Override
  public double compare(String _suspect, String _candidate) throws UnsupportedEncodingException
  {
    if (_suspect == null || _candidate == null)
    {
      return 0;
    }
    _suspect = _suspect != null ? _suspect.toUpperCase() : "";
    _candidate = _candidate != null ? _candidate.toUpperCase() : "";

    Map<String, Integer> sMap = new HashMap<String, Integer>();
    Map<String, Integer> cMap = new HashMap<String, Integer>();
    if (dropNoiseCharFlag)
    {
      //retaining only digits and alphabets , language independent
      _suspect = _suspect.replaceAll("[^\\p{IsDigit}\\p{IsAlphabetic}]", " ");
      _candidate = _candidate.replaceAll("[^\\p{IsDigit}\\p{IsAlphabetic}]", " ");
    }
    if (dropSpaceFlag)
    {
      //dropping spaces
      _suspect = _suspect.replaceAll("\\s+", "");
      _candidate = _candidate.replaceAll("\\s+", "");
    }
    String[] _suspectSplit = _suspect.split("\\s+");
    String[] _candidateSplit = _candidate.split("\\s+");
    for (String str : _suspectSplit)
    {
      int s = str.length();
      populateGramsInMap(str, s, nGramVal, sMap);
    }
    for (String str : _candidateSplit)
    {
      int c = str.length();
      populateGramsInMap(str, c, nGramVal, cMap);
    }
    int res = 0;

    for (Map.Entry<String, Integer> entry : sMap.entrySet())
    {
      String key = entry.getKey();
      int value = entry.getValue();
      if (cMap.containsKey(key))
      {
        res += value * cMap.get(key);
      }

    }
    float sMag = magnitude(sMap);
    float cMag = magnitude(cMap);

    float ans = (res) / (sMag * cMag);
    return ans * 100;
  }

  //getting the magnitude of all values in a map. e.g. for 2 values a and b magnitude=sqrt(a^2+b^2)
  public static float magnitude(Map<String, Integer> m)
  {
    float mag = 0;
    for (Map.Entry<String, Integer> entry : m.entrySet())
    {
      mag += entry.getValue() * entry.getValue();
    }
    return (float)Math.sqrt(mag);
  }

  public static Map<String, Integer> populateGramsInMap(String str, int len, int nGramVal, Map<String, Integer> m)
  {
    int i = 0;
    if (len < nGramVal)
    {
      if (!m.containsKey(str))
      {
        m.put(str, 1);
      }
      else
      {
        m.put(str, m.get(str) + 1);
      }
      return m;
    }
    while (i <= len - nGramVal)
    {
      String key = str.substring(i, i + nGramVal);
      if (!m.containsKey(key))
      {
        m.put(key, 1);
      }
      else
      {
        m.put(key, m.get(key) + 1);
      }
      i++;
    }
    return m;
  }

  @Override
  public void configure(Map<String, String> options)
  {
    if (options != null)
    {

      String ngramOptionVal = (String)(options.get(NGRAM_VALUE));
      try
      {
        if (ngramOptionVal != null)
        {
          Integer i = Integer.parseInt(ngramOptionVal);
          if (i != null && i > 1)
          {
            nGramVal = i;
          }
        }
      }
      catch (NumberFormatException e)
      {
        throw new IllegalArgumentException("Invalid n-Gram Value :" + ngramOptionVal);
      }
      dropNoiseCharFlag = Boolean.parseBoolean(options.get(DROP_NOISE_CHAR_FLAG));
      dropSpaceFlag = Boolean.parseBoolean(options.get(DROP_SPACE_FLAG));
    }
  }
}
