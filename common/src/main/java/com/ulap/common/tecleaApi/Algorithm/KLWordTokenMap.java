package com.ulap.util.tecleaApi.Algorithm;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class KLWordTokenMap 
{
  private Map<String, Integer> tokenMap;
  private int size = 0;
  private int frequecySum = 0;

  public KLWordTokenMap(String inputString)
  {
    // not handling stop words
    String []  stringTokens = getStringTokens(inputString);
    tokenMap = new HashMap<String, Integer>();

    if( stringTokens != null)
    {
      //    Set<WordToken> wordTokenSet = new HashSet<WordToken>();
      for(String str : stringTokens)
      {
        str = str.trim().toLowerCase();

        if(!tokenMap.keySet().contains(str))
        {
          tokenMap.put(str, new Integer(1));
          frequecySum++; // increment by one 
        }
        else
        {
          int count = ((Integer)tokenMap.get(str)).intValue();
          tokenMap.put(str, new Integer(count + 1));
          frequecySum++; // increment by one 
        }
      }

      size = tokenMap.size();
    }
  }

  public boolean isEmpty()
  {
    return tokenMap.isEmpty() ? true : false ;
  }

  public Map<String, Integer> getTokenMap()
  {
    return tokenMap;
  }

  public int size()
  {
    return size;
  }

  public int getFrequecySum()
  {
    return frequecySum;
  }

  private String[] getStringTokens(String name)
  {
    // TODO : verify blank strings . Is it required ?
    if(name == null || name.trim().length() == 0)
    {
      return null;
    }
    Pattern p = Pattern.compile("\\s+");
    String[] tokens = p.split(name.trim().toLowerCase());
    return tokens;
  }

  //  Returns the minimum term probability among the tokens
  public double getMinimumTermProbabilty()
  {
    Collection<Integer> values = this.tokenMap.values();

    if(values.isEmpty() || this.size == 0)
    {
      return 0.0; // or 0.000001
    }

    double minValue = (Collections.min(values).doubleValue());
    return (minValue/this.frequecySum);
  }

}
