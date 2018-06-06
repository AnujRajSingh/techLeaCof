package com.ulap.util.tecleaApi.Algorithm;

import org.apache.commons.codec.language.DoubleMetaphone;

public class DoubleMetaphoneAlgorithm extends AbstractHashingMatchAlgorithm
{
  public static final DoubleMetaphoneAlgorithm INSTANCE = new DoubleMetaphoneAlgorithm();
  private DoubleMetaphone doubleMetaphone;

  private DoubleMetaphoneAlgorithm()
  {
    doubleMetaphone = new DoubleMetaphone();
  }

  @Override
  protected MatchAlgorithm getHashCodeMatchAlgorithm()
  {
    return ExactMatch.INSTANCE;
  }

  public String getHashCode(String inputValue)
  {
    String hash = (inputValue != null) ? doubleMetaphone.encode(inputValue) : "";

    return (hash != null) ? hash : "";
  }

  public String getName()
  {
    return "Double Metaphone";
  }

}
