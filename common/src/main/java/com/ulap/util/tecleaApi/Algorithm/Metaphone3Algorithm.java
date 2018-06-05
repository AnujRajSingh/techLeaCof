package com.ulap.util.tecleaApi.Algorithm;

/**
 * 

 *
 */
public class Metaphone3Algorithm extends AbstractHashingMatchAlgorithm
{
  private static final String METAPHONE3 = "Metaphone3";

  @Override
  protected MatchAlgorithm getHashCodeMatchAlgorithm()
  {
    return ExactMatch.INSTANCE;
  }

  public String getHashCode(String inputValue)
  {
    String hash = (inputValue != null) ? encode(inputValue) : "";

    return (hash != null) ? hash : "";
  }

  public String getName()
  {
    return METAPHONE3;
  }
  
  /*
   * Returns the Metaphone3 Code for String inputValue
   */
  private String encode(String inputValue)
  {
    Metaphone3 metaphone3 = new Metaphone3();
    // TODO: should we use SetEncodeExact(true) ?
    // metaphone3.SetEncodeVowels(true);
    metaphone3.SetEncodeExact(true);
    
    metaphone3.SetWord(inputValue);
    metaphone3.Encode();
    
    return metaphone3.GetMetaph();
  }

}
