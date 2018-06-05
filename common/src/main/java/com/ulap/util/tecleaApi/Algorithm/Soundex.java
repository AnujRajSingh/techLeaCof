package com.ulap.util.tecleaApi.Algorithm;

import java.util.HashMap;
import java.util.Map;

/**


 *
 * <p>Attempts to match two strings on the basis of their phonetic quality.
 * Names that have different spellings but sound similar when spoken.</p>
 */
public class Soundex extends AbstractHashingMatchAlgorithm
{
  private static Map<Character, Character> soundexValue = new HashMap<Character, Character>();

  public static final Soundex INSTANCE = new Soundex();

  private static final String NAME = "Soundex";

  private Soundex()
  {
  }

  public String getName()
  {
    return NAME;
  }

  protected MatchAlgorithm getHashCodeMatchAlgorithm()
  {
    return ExactMatch.INSTANCE;
  }

  /**
   * Creates a Soundex code.
   *
   * @param suspect String.
   * @return Soundex code as a String.
   */
  public String getHashCode(String suspect)
  {
    // test for null string
    if ((suspect == null) || (suspect.length() == 0))
    {
      return "";
    }

    // length of suspect.
    int suspectLength = suspect.length();

    // test for singel letter
    if (suspectLength == 1)
    {
      return suspect.toUpperCase();
    }

    char n_i; // current character in the string.
    char n_j; // previous character in the string.
    // Step 1

    StringBuilder s_Score = new StringBuilder();
    char[] suspectChars = suspect.toUpperCase().toCharArray();

    // Step 2

    // Move first letter
    s_Score.append(suspectChars[0]);
    int scorePosition = 1;
    int j = 2;
    for (; scorePosition <= 3 && j <= suspectLength; j++)
    {
      n_i = suspectChars[j - 1];
      n_j = suspectChars[j - 2];
      // get Soundex code value for previous character in the string.
      char nj_rc = sCodeChar(n_j);
      // get Soundex code value for the current character in the string.
      char rc = sCodeChar(n_i);

      // do not append the soundex value when adjacent characters contain the same soundex value.
      if (rc != '0' && nj_rc == rc)
      {

      }
      // append the soundex value when adjacent characters do not contain the same soundex value.
      else if (rc != '0' && n_j != n_i)
      {
        s_Score.append(rc);
        scorePosition++;
      }
      else if (j > suspectLength)
      {
        s_Score.setCharAt(scorePosition, '0');
        scorePosition++;
      }
    }
    for (; scorePosition <= 3; scorePosition++)
    {
      s_Score.append('0');
    }
    return s_Score.toString();
  }

  /**
   * Soundex code values.
   * Retain the first letter of the name, and drop all occurrences of a, e, h, i, o, u, w, y in other positions.
   * Assign the following numbers to the remaining letters after the first:
   * 1. b, f, p, v;
   * 2. c, g, j, k, q, s, x, z;
   * 3. d, t;
   * 4. l;
   * 5. m, n;
   * 6. r.
   * If two or more letters with the same code were adjacent in the original name (before step 1), omit all but the first.
   * Convert to the form ``letter, digit, digit, digit'' by adding trailing zeros (if there are less than three digits), or by dropping rightmost digits (if there are more than three).
   *
   * @param n char  character value in the input String.
   * @return soundex code value for the character as a String.
   */
  private static char sCodeChar(char n)
  {
    Character soundexString = soundexValue.get(n);
    if (soundexString == null)
    {
      return '0';
    }
    return soundexString;
  }

  static
  {
    soundexValue.put('A', '0');
    soundexValue.put('B', '1');
    soundexValue.put('C', '2');
    soundexValue.put('D', '3');
    soundexValue.put('E', '0');
    soundexValue.put('F', '1');
    soundexValue.put('G', '2');
    soundexValue.put('H', '0');
    soundexValue.put('I', '0');
    soundexValue.put('J', '2');
    soundexValue.put('K', '2');
    soundexValue.put('L', '4');
    soundexValue.put('M', '5');
    soundexValue.put('N', '5');
    soundexValue.put('O', '0');
    soundexValue.put('P', '1');
    soundexValue.put('Q', '2');
    soundexValue.put('R', '6');
    soundexValue.put('S', '2');
    soundexValue.put('T', '3');
    soundexValue.put('U', '0');
    soundexValue.put('V', '1');
    soundexValue.put('W', '0');
    soundexValue.put('X', '2');
    soundexValue.put('Y', '0');
    soundexValue.put('Z', '2');
  }
}
