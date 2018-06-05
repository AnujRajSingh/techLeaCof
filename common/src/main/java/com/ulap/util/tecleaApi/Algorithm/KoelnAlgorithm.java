package com.ulap.util.tecleaApi.Algorithm;

import org.apache.commons.codec.language.ColognePhonetic;

public class KoelnAlgorithm extends AbstractHashingMatchAlgorithm
{

  public static final KoelnAlgorithm INSTANCE = new KoelnAlgorithm();

  private ColognePhonetic koeln;

  private KoelnAlgorithm()
  {
    koeln = new ColognePhonetic();
  }

  @Override
  public String getName()
  {
    return "Koeln Algorithm";
  }


  @Override
  public String getHashCode(String inputValue)
  {
    return koeln.encode(inputValue);
  }

  @Override
  protected MatchAlgorithm getHashCodeMatchAlgorithm()
  {
    return ExactMatch.INSTANCE;
  }

}
