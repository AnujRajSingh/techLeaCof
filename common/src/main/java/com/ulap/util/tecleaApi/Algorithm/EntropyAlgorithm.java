package com.ulap.util.tecleaApi.Algorithm;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class EntropyAlgorithm
{
  public static EntropyAlgorithm INSTANCE = new EntropyAlgorithm();

  private static final String NAME = "Entropy Matching";
  private static double THRESHOLD = 80.0;

  EntropyAlgorithm()
  {
  }

  public String getName()
  {
    return NAME;
  }

  public double compare(String suspect, String candidate) throws UnsupportedEncodingException
  {
    if (suspect == null || candidate == null)
    {
      return 0.0;
    }
    suspect = suspect.replaceAll("[ ]+", " ").toUpperCase();
    candidate = candidate.replaceAll("[ ]+", " ").toUpperCase();

    String delimiter = " ";
    String[] suspectArray = suspect.split("[" + delimiter + "]+");
    String[] candidateArray = candidate.split("[" + delimiter + "]+");

    return compare(suspectArray, candidateArray);
  }

  public double compare(String[] suspectTerms, String[] candidateTerms) throws UnsupportedEncodingException
  {
    if (suspectTerms == null || candidateTerms == null || suspectTerms.length == 0 || candidateTerms.length == 0)
    {
      return 0.0;
    }
    int consecutive = lcs(suspectTerms, candidateTerms);
    return 100 * Math.sqrt(consecutive) / Math.sqrt(suspectTerms.length > candidateTerms.length ? suspectTerms.length : candidateTerms.length);

  }


  private short lcs(String[] A, String[] B) throws UnsupportedEncodingException
  {
    short[][] L = new short[A.length + 1][B.length +1];

    for (short[] row : L)
    {
      Arrays.fill(row, (short)-1);
    }
    return subProblem((short)0, (short)0, A, B, L);
  }

  private short subProblem(short i, short j, String[] A, String[] B, short[][] L) throws UnsupportedEncodingException
  {
    if (i >= A.length || j >= B.length)
    {
      return 0;
    }

    if ( L[i][j] < 0)
    {
      //Use edit distance only when exact match fails
      if (A[i].equals(B[j]) || EditDistance.INSTANCE.compare(A[i], B[j]) > THRESHOLD || A[i].startsWith(B[j]) || B[j].startsWith(A[i]))
      {
        L[i][j] =(short)(1 + subProblem((short)(i + 1), (short)(j + 1), A, B, L));
      }
      else
      {
        L[i][j] = max(subProblem((short)(i + 1), j, A, B, L), subProblem(i, (short)(j + 1), A, B, L));
      }
    }
    return L[i][j];
  }

  private short max(short a, short b)
  {
    return (a >= b) ? a : b;
  }
}
