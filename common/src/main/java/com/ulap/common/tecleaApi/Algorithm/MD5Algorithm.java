package com.ulap.util.tecleaApi.Algorithm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Algorithm extends AbstractHashingMatchAlgorithm
{
  public static final MD5Algorithm INSTANCE = new MD5Algorithm();

  private static final String NAME = "MD5";
  
  private static final short HASHLENGTH = 32;
  
  private static final String ZERO = "0";
  
  private static final short HEXADECIMAL_RADIX = 16;

  private MD5Algorithm()
  {
  }

  public String getName()
  {
    return NAME;
  }

  protected MatchAlgorithm getHashCodeMatchAlgorithm()
  {
    return MD5Algorithm.INSTANCE;
  }

  public String getHashCode(String suspect)
  {
    MessageDigest digest = null;
    String hashtext = "";
    try
    {
      digest = MessageDigest.getInstance(NAME);
      digest.update(suspect.getBytes());
      hashtext = new BigInteger(1, digest.digest()).toString(HEXADECIMAL_RADIX);
      while (hashtext.length() < HASHLENGTH)
      {
        hashtext = ZERO + hashtext;
      }
    }
    catch (NoSuchAlgorithmException e)
    {
      // Don't want to pass on the exception;not expecting to reach here
      // Printing stack for debugging only
      e.printStackTrace();
    }
    return hashtext;
  }
}
