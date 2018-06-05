package com.ulap.util.tecleaApi.Algorithm;

import org.apache.commons.codec.language.Nysiis;

public class NysiisAlgorithm extends AbstractHashingMatchAlgorithm
{

  public static final NysiisAlgorithm INSTANCE = new NysiisAlgorithm();
  
  private Nysiis nysiis ;
  
  private NysiisAlgorithm()
  {
    nysiis = new Nysiis();
  }
  
  
  @Override
  public String getName()
  {
    return "Nysiis Algorithm";
  }

  @Override
  public String getHashCode(String inputValue)
  {
    String hash = (inputValue != null) ? nysiis.encode(inputValue) : "";

    return (hash != null) ? hash : "";
  }

  @Override
  protected MatchAlgorithm getHashCodeMatchAlgorithm()
  {
    return ExactMatch.INSTANCE;
  }



}
