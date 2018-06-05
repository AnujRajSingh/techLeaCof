package com.ulap.util.tecleaApi.Algorithm;

import org.apache.lucene.search.spell.NGramDistance;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class NGramDistanceAlgorithm implements ConfigurableMatchAlgorithm
{
  private NGramDistance ngramDistance;

  public static final String NGRAM_VALUE = "ngramValue";

  private int nGramVal = 2;

  public int getnGramVal()
  {
    return nGramVal;
  }

  @Override
  public String getName()
  {
    return "NGram Distance";
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
  public double compare(String suspect, String candidate) throws UnsupportedEncodingException
  {
    ngramDistance = new NGramDistance(nGramVal);

    if (suspect == null || suspect.length() == 0)
    {
      return 0;
    }
    if (candidate == null || candidate.length() == 0)
    {
      return 0;
    }
    return ngramDistance.getDistance(suspect, candidate) * 100;
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
    }
  }
}
