package com.ulap.util.tecleaApi.Algorithm;


public class SyllableAlignmentAlgorithm implements MatchAlgorithm
{
  public static final int s1 = 1;
  public static final int s2 = -1;
  public static final int s3 = -4;
  public static final int s4 = 6;
  public static final int s5 = -2;
  public static final int g1 = -1;
  public static final int g2 = -3;
  public static final MatchAlgorithm INSTANCE = new SyllableAlignmentAlgorithm();

  private static final String NAME = "Syllable Alignment";

  private SyllableAlignmentAlgorithm()
  {

  }

  /**
   * The Preprocessing Step: Method to get phonetic String corresponding to original word.
   */
  String getPhonixTransform(String name)
  {
    StringBuilder str = new StringBuilder(name.length() * 2);
    str.append(name.toUpperCase());

    //Using Existing Phonix algorithm to get Phonetic Code
    PhonixAlgorithm.INSTANCE.convertToPhonixString(str);
    String s = removeRepeatingLetters(str);
    return s.toLowerCase().trim();
  }

  private String removeRepeatingLetters(StringBuilder str)
  {
    if (str == null || str.length() == 0)
    {
      return "";
    }
    String s = str.toString();
    char[] c = s.toCharArray();
    char[] key = new char[s.length()];
    char lastLetter = c[0];
    key[0] = lastLetter;
    int index = 1;
    for (int i = 1; i < c.length; i++)
    {
      if (lastLetter != c[i])
      {
        lastLetter = c[i];
        key[index] = lastLetter;
        index++;
      }
    }
    // TODO Auto-generated method stub
    return new String(key);
  }

  /*
   * Substitution Function
   *
   *  S(x,y)   = s1 if x=y else s2
   *  S(x^,y)  = s3
   *  S(x^,y^) = s4 if x^=y^ else s5
   */
  int substitutionFunction(char c1, char c2)
  {
    if ((Character.isUpperCase(c1) && !Character.isUpperCase(c2)) || (Character.isUpperCase(c2) && !Character.isUpperCase(c1)))
    {
      return s3;
    }
    if (Character.isUpperCase(c1) && Character.isUpperCase(c2))
    {
      return (c1 == c2) ? s4 : s5;
    }

    //if character is not in upper case as well as lower case , it could be some special character or number etc .... applying first condition in
    //that case which is normal characters other than syllable start character
    return (c1 == c2) ? s1 : s2;
  }

  /*
   * Substitution Function
   *
   *  g(x,-)   = g1
   *  g(x^,-)  = g2
   */
  int gapFunction(char c)
  {
    return !Character.isUpperCase(c) ? g1 : (Character.isUpperCase(c) ? g2 : 0);

  }

  /**
   * Segmentation Step: This Step converts the String into concatenation of syllables. e.g : stevens will be represented as STeVenS . Where capital
   * letters represents starting letter of each syllable. Method to find Syllable Code. e.g : stevens will be represented as STeVenS stevenson will be
   * represented as STeVenSon (Syllables are : S , T , Ven , Son) Capital letters will Represent Syllable start.
   */
  String convertWordToSyllables(String name)
  {
    if (name == null || name.length() == 0)
    {
      return "";
    }
    name = name.toLowerCase();
    char[] c = name.toCharArray();

    int length = c.length;

    boolean vowelFlag = false; // trailing vowel / diphthong flag. Whether the previous character was diphthong or not
    for (int i = 0; i < length;)
    {
      int len = isVowelOrDiphthong(i, name);

      if (i == 0)
      {
        c[i] = Character.toUpperCase(c[i]);
      }
      else
      {
        if (len == 0) //It is a consonant
        {
          if (vowelFlag) // previous was vowel / diphthong
          {
            if ( !(i + 1 >= length) && isVowelOrDiphthong(name, i + 1) )
            {
              c[i] = Character.toUpperCase(c[i]);
            }
          }
          else
          {
            c[i] = Character.toUpperCase(c[i]);
          }
          //do something more
        }

      }

      if (len > 0)
      {
        i += len;
        vowelFlag = true;
      }
      else
      {
        i += 1;
        vowelFlag = false;
      }

    }

    //Make first character Syallable start
    return new String(c);
  }

  /*
   *  ***************************************************************************
   *  NAME : IsVowel
   *  INPUT : char c --- char to be examined
   *  OUTPUT : int --- 1 or 0
   *  FUNCTION: IsVowel checks if c is an uppercase vowel or an uppercase Y. If c
   *  is one of those chars IsVowel will return a 1, else it will return
   *  a 0.
   *  ***************************************************************************
   */

  /**
   * DO NOT CHANGE ORDER OF VOWELS DIPTHONGS
   */
  private static String[] VOWELS_DIPTHONGS = {
      "ow", "ou", "ie", "igh", "oi", "oo", "ea", "ane", "eer", "air", "ure", "a", "e", "i", "o", "u", "y"
  };

  private int isVowelOrDiphthong(int pos, String word)
  {
    for (String prefix : VOWELS_DIPTHONGS)
    {
      if (word.regionMatches(pos, prefix, 0, prefix.length()))
      {
        return prefix.length();
      }
    }

    return 0;
  }

  private  boolean isVowelOrDiphthong(String str, int pos)
  {
    char c = str.charAt(pos);
    return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y' || c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U'
            || c == 'Y';
  }

  /**
   * Method to compare two String
   */
  public double compare(String suspect, String candidate)
  {
    if (suspect == null || candidate == null || suspect.length() == 0 || candidate.length() == 0)
    {
      return 0;
    }
    // Preprocessing Step :
    String a = getPhonixTransform(suspect);
    String b = getPhonixTransform(candidate);

    // Segmentation Step : Output of preprocessing step passed to Segmentation step

    String s1 = convertWordToSyllables(a);
    String s2 = convertWordToSyllables(b);

    // Alignment and Similarity Calculation Step:
    int m = s1.length();
    int n = s2.length();

    int[][] M1 = this.getAligmentMatrix(s1, s1);
    int[][] M2 = this.getAligmentMatrix(s1, s2);

    if (M2[m][n] <= 0 || M1[m][m] <= 0)
    {
      return 0;
    }
    // Final score : calculate M2[m][n] for suspect and candidate and relative maximum score for suspect to itself is M1[m][n] , so calculate percentage.
    return ((double)M2[m][n] / (double)M1[m][m]) * 100;

  }

  // Method to construct Syllable Alignment Matrix which will be used for Alignment purpose
  //                _
  //               |
  //               |  M[i-1,j-1]  +   s(S1[i],S2[j])
  //  M[i,j]=  max |  M[i-1,j]    +   g(S1[i],-)
  //               |  M[i,j-1]    +   g(S2[j],-)
  //               |_
  //
  //  Initial Condition :
  //
  //       M[0,0]  =  0;
  //       M[i,0]  =  M[i-1,0]  + g(S1[i],-)
  //       M[0,j]  =  M[0,j-1]  + g(S2[j],-)
  //
  //
  // @param suspect
  // @param candidate
  // @return
  int[][] getAligmentMatrix(String suspect, String candidate)
  {
    if (suspect == null || candidate == null) return null;
    int m = suspect.length();
    int n = candidate.length();
    char[] s1a = suspect.toCharArray();
    char[] s2a = candidate.toCharArray();
    int[][] m1 = new int[m + 1][n + 1];
    m1[0][0] = 0;
    int p, q;
    for (p = 1; p <= m; p++)
    {
      m1[p][0] = m1[p - 1][0] + gapFunction(s1a[p - 1]);
    }
    for (q = 1; q <= n; q++)
    {
      m1[0][q] = m1[0][q - 1] + gapFunction(s2a[q - 1]);
    }
    for (int i = 1; i <= m; i++)
    {
      for (int j = 1; j <= n; j++)
      {
        int a = m1[i - 1][j - 1] + substitutionFunction(s1a[i - 1], s2a[j - 1]);
        int b = m1[i - 1][j] + gapFunction(s1a[i - 1]);
        int c = m1[i][j - 1] + gapFunction(s2a[j - 1]);
        m1[i][j] = Math.max(Math.max(a, b), c);
      }
    }

    return m1;
  }

  @Override
  public String getName()
  {
    return NAME;
  }
}
