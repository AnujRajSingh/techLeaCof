package com.ulap.util.tecleaApi.Algorithm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class AcronymAlgorithm implements MatchAlgorithm
{
  private static final String NAME = "Acronym Algorithm";
  private static Set<String> softTerms = new HashSet();

  static
  {//TODO: Replace with table manager
    softTerms.add("AND");
    softTerms.add("OF");
    softTerms.add("&");
    softTerms.add("THE");
    softTerms.add("TO");
  }

  private static Map reference;

  private AcronymAlgorithm()
  {
    if (reference == null)
    {
      reference = new HashMap();
      loadReference("BusinessAcronym.properties");
    }
  }

  public static AcronymAlgorithm INSTANCE = new AcronymAlgorithm();

  public String getName()
  {
    return NAME;
  }

  public double compare(String suspect, String candidate) throws UnsupportedEncodingException
  {
    if (suspect == null || candidate == null)
    {
      return 0;
    }

    // remove the '.' and spaces
    suspect = cleanTerm(suspect);
    candidate = cleanTerm(candidate);

    String[] suspectTerms = suspect.split("[ ]+");
    String[] candidateTerms = candidate.split("[ ]+");

    if (suspectTerms.length > 1 && candidateTerms.length > 1)
    {
      suspectTerms = cleanSoftTerms(suspectTerms);
      candidateTerms = cleanSoftTerms(candidateTerms);
      return EntropyAlgorithm.INSTANCE.compare(suspectTerms, candidateTerms);
    }
    else if (suspectTerms.length == 1 && candidateTerms.length > 1)
    {
      return compareSingleTermWithMultiples(suspectTerms[0], candidateTerms);
    }
    else if (candidateTerms.length == 1 && suspectTerms.length > 1)
    {
      return compareSingleTermWithMultiples(candidateTerms[0], suspectTerms);
    }
    else if (suspectTerms.length == 1 && candidateTerms.length == 1)
    {
      if (suspect.length() == 1)
      {
        return toAcronym(candidateTerms).equals(suspect) ? 100.0 : 0.0;
      }
      else if (candidate.length() == 1)
      {
        return toAcronym(suspectTerms).equals(candidate) ? 100.0 : 0.0;
      }
      else
      {
        return suspect.equals(candidate) ? 100.0 : 0.0;
      }

    }
    return 0.0;
  }

  private String[] cleanSoftTerms(String[] termsToClean) {
    List<String> termList = new ArrayList<>();
    String cleansedTerm;
    for (int i = 0; i < termsToClean.length; i++) {
      cleansedTerm = termsToClean[i];
      for (String term : softTerms) {
        cleansedTerm = cleansedTerm.replaceAll("(?i)" + term, "");
      }
      if (cleansedTerm != null && cleansedTerm.length() > 0) {
        termList.add(cleansedTerm);
      }
    }
    return termList.toArray(new String[termList.size()]);
  }


  private String cleanTerm(String termToClean)
  {
    return termToClean.replaceAll("[.]", "").toUpperCase().trim();
  }


  private String[] cleanAndSplitTerm(String termToClean)
  {
    return cleanTerm(termToClean).split("[ ]+");
  }

  private double compareSingleTermWithMultiples(String singleTerm, String[] multipleTerms) throws UnsupportedEncodingException
  {
//    String acr = toAcronym(multipleTerms.split("[ ]+"));
    String acr = toAcronym(multipleTerms);
    String[] cleansedTerms = cleanSoftTerms(multipleTerms);
    String cleansedAcr = toAcronym(cleansedTerms);

    if (singleTerm.equals(acr) || singleTerm.equals(cleansedAcr))
    {
      return 100;
    }
    else
    {
      List ref = (List)reference.get(singleTerm);
      if (ref != null)
      {
        double maxScore = 0.0;
        for (Object aRef : ref)
        {
          String[] referenceName = cleanAndSplitTerm((String)aRef);
//          double score = EntropyAlgorithm.INSTANCE.compare(referenceName, multipleTerms);
          double score = EntropyAlgorithm.INSTANCE.compare(referenceName, multipleTerms);
          maxScore = score > maxScore ? score : maxScore;
        }
        return maxScore;
      }
      else
      {
        return EntropyAlgorithm.INSTANCE.compare(new String[]{singleTerm}, cleansedTerms);
      }
    }
  }

  private static String toAcronym(String[] input)
  {
    StringBuilder acronym = new StringBuilder();
    for (String terms : input)
    {
      if (terms.length() > 0)
      {
        acronym.append(terms.substring(0, 1));
      }
    }
    return acronym.toString();
  }

  //TODO: Replace this with table manager.
  public String loadReference(String file)
  {
    StringBuilder sb = new StringBuilder();
    InputStream is = null;
    String line;
    try
    {
      is = getClass().getResourceAsStream(file);
      BufferedReader din = new BufferedReader(new InputStreamReader(is));
      while ((line = din.readLine()) != null)
      {
        String data = line.trim().replaceAll("[ ]+", " ");
        String[] values = data.split("[;]");
        String acronym = (values[0]).toUpperCase();
        String name = (values[1]).toUpperCase();

        if (reference.containsKey(acronym))
        {
          List referenceList = (List)reference.get(acronym);
          referenceList.add(name);
        }
        else
        {
          List referenceList = new ArrayList();
          referenceList.add(name);
          reference.put(acronym, referenceList);
        }
      }
    }
    catch (Exception ignore)
    {
      // TODO: Do not swallow this exception.
    }
    finally
    {
      try
      {
        if (is != null)
        {
          is.close();
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    return sb.toString().trim();
  }
}
