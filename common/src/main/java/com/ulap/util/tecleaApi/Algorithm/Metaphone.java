package com.ulap.util.tecleaApi.Algorithm;

/**

 * To change this template use Options | File Templates.
 * <p>Attempts to match two strings on the basis of their phonetic quality.
 * Names that have different spellings but sound similar when spoken.
 * Developed to respond to the deficiencies in the Soundex algorithm.</p>
 */
public class Metaphone extends AbstractHashingMatchAlgorithm
{
  public static final Metaphone INSTANCE = new Metaphone();

  private static final String NAME = "Metaphone";

  private final static String vowels = "AEIOU";
  private final static String fvowels = "EIY";
  private final static String varson = "CSPTG";

  private Metaphone()
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
   * Creates a Metaphone code from the suspect string.
   *
   * @param suspect String.
   * @return Metaphone code as a String.
   */
  public String getHashCode(String suspect)
  {
    char[] input = null;
    char letter;
    int insz;
    int wrsz;
    int n;
    StringBuilder work;
    StringBuilder code;
    final int MaxLen = 4;


    // test for null string
    if ((suspect == null) || (suspect.length() == 0))
    {
      return "";
    }
    // test for single letter
    if (suspect.length() == 1)
    {
      return suspect.toUpperCase();
    }

    input = suspect.toUpperCase().toCharArray();

    work = new StringBuilder(40); // work area
    code = new StringBuilder(10);
    n = 0;
    insz = 0;

    metaphoneIntial(input, work);

    wrsz = work.length();

    // work not has string with inital letters exceptions fixed
    while ((insz < MaxLen) &&
        (n < wrsz))
    {
      letter = work.charAt(n);
      // remove duplicate letters except C
      if ((letter != 'C') &&
          (n > 0) &&
          (work.charAt(n - 1) == letter))
      {
        n++;
      }
      else
      { // not a duplicate letter
        switch (letter)
        {
          // only use vowels if first letter
          case 'A':
          case 'E':
          case 'I':
          case 'O':
          case 'U':
            insz = metaphoneFirstVowel(letter, insz, code, n);
            break;
          case 'B':
            insz = metaphoneBCase(letter, insz, work, code, wrsz, n);
            break;
          case 'C':
            insz = metaphoneCCase(insz, work, code, wrsz, n);
            break;
          case 'D':
            insz = metaphoneDCase(insz, work, code, wrsz, n);
            break;
          case 'G':
            insz = metaphoneGCase(insz, work, code, wrsz, n);
            break;
          case 'H':
            insz = metaphoneHCase(insz, work, code, wrsz, n);
            break;
          case 'F':
          case 'J':
          case 'L':
          case 'M':
          case 'N':
          case 'R':
            insz = metaphoneNotTrans(letter, insz, code);
            break;
          case 'K':
            insz = metaphoneKCase(letter, insz, work, code, n);
            break;
          case 'P':
            insz = metaphonePCase(letter, insz, work, code, wrsz, n);
            break;
          case 'Q':
            insz = metaphoneQCase(insz, code);
            break;
          case 'S':
            insz = metaphoneSCase(insz, work, code, n);
            break;
          case 'T':
            insz = metaphoneTCase(insz, work, code, n);
            break;
          case 'V':
            insz = metaphoneVCase(insz, code);
            break;
          case 'W':
          case 'Y':
            insz = metaphoneWYCase(letter, insz, work, code, wrsz, n);
            break; //W and Y silent if not followed by vowel
          case 'X':
            insz = metaphoneXCase(insz, code);
            break;
          case 'Z':
            insz = metaphoneZCase(insz, code);
            break;
        } //end switch
        n++;
      }  // end else of not equal C
      if (insz > 4)
      {
        code.setLength(4);
      }
    }
    return code.toString();
  }

  /**
   * Handle initial letter exceptions.
   *
   * @param input character array of suspect String.
   * @param work  StringBuffer that contains new input string after initial letter
   *              exceptions are handled.
   */
  private static void metaphoneIntial(char[] input, StringBuilder work)
  {
    switch (input[0])
    {
      case 'K':
      case 'G':
      case 'P':
        if (input[1] == 'N')
        {
          work.append(input, 1, input.length - 1);
        }
        else
        {
          work.append(input);
        }
        break;
      case 'A':
        if (input[1] == 'E')
        {
          work.append(input, 1, input.length - 1);
        }
        else
        {
          work.append(input);
        }
        break;
      case 'W':
        if (input[1] == 'R')
        {
          work.append(input, 1, input.length - 1);
          break;
        }
        if (input[1] == 'H')
        {
          work.append(input, 1, input.length - 1);
          work.setCharAt(0, 'W');
        }
        else
        {
          work.append(input);
        }
        break;
      case 'X':
        input[0] = 'S';
        work.append(input);
        break;
      default:
        work.append(input);
    }
  }

  /**
   * Only use vowels if the first letter in the string.
   *
   * @param letter character in a String.
   * @param insz   integer that calculates size of the metaphone code.
   * @param code   StringBuffer that contains metaphone code.
   * @param n      integer index position.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneFirstVowel(char letter, int insz, StringBuilder code,
                                         int n)
  {
    if (n == 0)
    {
      code.append(letter);
      insz++;
    }
    return insz;
  }

  /**
   * Letter B transformations: B to B unless at the end of a word after "m".
   *
   * @param letter character in a String.
   * @param insz   integer that calculates size of the metaphone code.
   * @param work   StringBuilder that contains new input string after initial letter
   *               exceptions are handled.
   * @param code   StringBuilder that contains metaphone code.
   * @param wrsz   integer length of the String.
   * @param n      integer index position.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneBCase(char letter, int insz, StringBuilder work,
                                    StringBuilder code, int wrsz, int n)
  {
    if ((n > 0) &&
        (n + 1 == wrsz) &&
        (work.charAt(n - 1) == 'M'))
    {
      // do not append B character - code.append(letter);
    }
    else
    {
      code.append(letter);
    }
    insz++;
    return insz;
  }

  /**
   * Letter C transformations:
   * <br>C to X   (sh) if -cia- or -ch-
   * <br>C to S   if -ci-, -ce- or -cy-
   * <br>C to K   otherwise, including -sch-
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param work StringBuilder that contains new input string after initial letter
   *             exceptions are handled.
   * @param code StringBuilder that contains metaphone code.
   * @param wrsz integer length of the String.
   * @param n    integer index position.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneCCase(int insz, StringBuilder work, StringBuilder code,
                                    int wrsz, int n)
  {
    String holdS = null;

    // C special cases
    if ((n > 0) &&
        (work.charAt(n - 1) == 'S') && (n + 1 < wrsz) &&
        (fvowels.indexOf(work.charAt(n + 1)) >= 0))
    {
      return insz;  // discard SCI, SCE or SCY
    }
    holdS = work.toString();
    if (holdS.indexOf("CIA", n) == n)
    {
      code.append('X');
      insz++;
      return insz; // change CIA to X
    }
    if ((n + 1 < wrsz) &&
        (fvowels.indexOf(work.charAt(n + 1)) >= 0))
    {
      code.append('S');
      insz++;
      return insz; // change CI, CE and CY to S
    }
    if ((n > 0) &&
        (holdS.indexOf("SCH", n - 1) == n - 1))
    {
      code.append('K');
      insz++;
      return insz; // change SCH to SK
    }
    if (holdS.indexOf("CH", n) == n)
    {
      if ((n == 0) &&
          (wrsz >= 3) &&
          (vowels.indexOf(work.charAt(2)) < 0))
      {
        code.append('K');  //change CH to K
      }
      else
      {
        code.append('X'); //change CH followed by a vowel to X
      }
      insz++;
      return insz;
    }
    else
    {
      code.append('K');
      insz++; //else change C to K
      return insz;
    }
    //return insz;
  }

  /**
   * Letter D transformations:
   * <br>D to J  if in -dge-, -dgy- or -dgi-
   * <br>D to T  otherwise
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param work StringBuilder that contains new input string after initial letter
   *             exceptions are handled.
   * @param code StringBuilder that contains metaphone code.
   * @param wrsz integer length of the String.
   * @param n    integer index position.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneDCase(int insz, StringBuilder work, StringBuilder code,
                                    int wrsz, int n)
  {
    //transform D
    if ((n + 2 < wrsz) &&
        (work.charAt(n + 1) == 'G') &&
        (fvowels.indexOf(work.charAt(n + 2)) >= 0))
    {
      code.append('J');
      n += 2; // change DGE, DGI and DGY to J
    }
    else
    {
      code.append('T'); // change D to T
    }
    insz++;
    return insz;
  }

  /**
   * Letter G transformations:
   * <br>G to " " silent if in -gh- and not at end or before a vowel,
   * in -gn- or -gned- (also see dge etc. above)
   * <br>G to J   if before i or e or y if not double gg
   * <br>G to K   otherwise
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param work StringBuilder that contains new input string after initial letter
   *             exceptions are handled.
   * @param code StringBuilder that contains metaphone code.
   * @param wrsz integer length of the String.
   * @param n    integer index position.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneGCase(int insz, StringBuilder work, StringBuilder code,
                                    int wrsz, int n)
  {
    String holdS = null;

    // GH silent
    if ((n + 2 == wrsz) &&
        (work.charAt(n + 1) == 'H'))
    {
      return insz;
    }
    if ((n + 2 < wrsz) &&
        (work.charAt(n + 1) == 'H') &&
        (vowels.indexOf(work.charAt(n + 2)) < 0))
    {
      return insz;
    }
    holdS = work.toString();
    if ((n > 0) &&
        (holdS.indexOf("GN", n) == n) ||
        (holdS.indexOf("GNED", n) == n))
    {
      return insz;
    }
    boolean doubleG;
    if ((n > 0) &&
        (work.charAt(n - 1) == 'G'))
    {
      doubleG = true;
    }
    else
    {
      doubleG = false;
    }
    /** Make sure G is not the first character in the string.
     *  Make sure the position of G in the string is less than the size of the string.
     *  Check to see if the character before the 'G' is a 'D'.
     *  Check to see if the vowels "EIY" follow the 'G'.
     *  Added this if statement to make sure that if 'DGE' is in a string, the rules for GE,GI or GY
     *  do not get executed.
     *  This prevents 'DGE' from being converted to 'JJ'. 'DGE' should only get converted to J
     *  under the metaphoneDCase method.
     */
    if ((n > 0) && (n + 1 < wrsz) && (work.charAt(n - 1) == 'D') && (fvowels.indexOf(work.charAt(n + 1)) >= 0))
    {
      return insz; // do not append 'J'.
    }
    if ((n + 1 < wrsz) &&
        (fvowels.indexOf(work.charAt(n + 1)) >= 0) &&
        (!doubleG))
    {
      code.append('J');
    }
    else
    {
      code.append('K');
    }
    insz++;
    return insz;
  }

  /**
   * Letter H transformations:
   * <br>H to " "     silent if after vowel and no vowel follows
   * <br>H remains H  otherwise
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param work StringBuilder that contains new input string after initial letter
   *             exceptions are handled.
   * @param code StringBuilder that contains metaphone code.
   * @param wrsz integer length of the String.
   * @param n    integer index position.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneHCase(int insz, StringBuilder work, StringBuilder code,
                                    int wrsz, int n)
  {
    if (n + 1 == wrsz)
    {
      return insz; //silent H at end of word
    }
    if ((n > 0) &&
        (varson.indexOf(work.charAt(n - 1)) >= 0))
    {
      return insz;
    }
    if (vowels.indexOf(work.charAt(n + 1)) >= 0)
    {
      code.append('H'); //silent if no vowel follows
      insz++;
    }
    return insz;
  }

  /**
   * Constants that do not get transformed:
   *
   * @param letter character in a String.
   * @param insz   integer that calculates size of the metaphone code.
   * @param code   StringBuilder that contains metaphone code.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneNotTrans(char letter, int insz, StringBuilder code)
  {
    code.append(letter);
    insz++;
    return insz;
  }

  /**
   * Letter K transformations:
   * <br>K to " " silent if after "c"
   * <br>K remains K otherwise
   *
   * @param letter character in a String.
   * @param insz   integer that calculates size of the metaphone code.
   * @param work   StringBuilder that contains new input string after initial letter
   *               exceptions are handled.
   * @param code   StringBuilder that contains metaphone code.
   * @param n      integer index position.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneKCase(char letter, int insz, StringBuilder work,
                                    StringBuilder code, int n)
  {
    if (n > 0)
    { //not first
      if (work.charAt(n - 1) != 'C')
      {
        code.append(letter);
      }
    }
    else
    {
      code.append(letter); //first
    }
    // moved insz outside of else statement. When the if statement was true, insz was not getting incremented.
    insz++;
    return insz;
  }

  /**
   * Letter P transformations:
   * <br>P to F       if before "h"
   * <br>P remains P  otherwise
   *
   * @param letter character in a String.
   * @param insz   integer that calculates size of the metaphone code.
   * @param work   StringBuilder that contains new input string after initial letter
   *               exceptions are handled.
   * @param code   StringBuilder that contains metaphone code.
   * @param wrsz   integer length of the String.
   * @param n      integer index position.
   * @return metaphone code size as an integer.
   */
  private static int metaphonePCase(char letter, int insz, StringBuilder work, StringBuilder code,
                                    int wrsz, int n)
  {
    if ((n + 1 < wrsz) &&
        (work.charAt(n + 1) == 'H'))
    {
      code.append('F'); //change PH to F
    }
    else
    {
      code.append(letter);
    }
    insz++;
    return insz;
  }

  /**
   * Letter Q transformations - Q to K:
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param code StringBuilder that contains metaphone code.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneQCase(int insz, StringBuilder code)
  {
    code.append('K'); //transform Q to K
    insz++;
    return insz;
  }

  /**
   * Letter S transformations:
   * <br>S to X       (sh) if before "h" or in -sio- or -sia-
   * <br>S remains S  otherwise
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param work StringBuilder that contains new input string after initial letter
   *             exceptions are handled.
   * @param code StringBuilder that contains metaphone code.
   * @param n    integer index position.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneSCase(int insz, StringBuilder work,
                                    StringBuilder code, int n)
  {
    String holdS = null;

    holdS = work.toString();
    if ((holdS.indexOf("SH", n) == n) ||
        (holdS.indexOf("SIO", n) == n) ||
        (holdS.indexOf("SIA", n) == n))
    {
      code.append('X'); //transform SH, SIO, SIA to X
    }
    else
    {
      code.append('S');
    }
    insz++;
    return insz;
  }

  /**
   * Letter T transformations:
   * <br>T to X       (sh) if -tia- or -tio-
   * <br>T to 0       (th) if before "h"
   * <br>T to " "     silent if in -tch-
   * <br>T remains T  otherwise
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param work StringBuilder that contains new input string after initial letter
   *             exceptions are handled.
   * @param code StringBuilder that contains metaphone code.
   * @param n    integer index position.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneTCase(int insz, StringBuilder work,
                                    StringBuilder code, int n)
  {
    String holdS = null;

    holdS = work.toString();
    if ((holdS.indexOf("TIA", n) == n) ||
        (holdS.indexOf("TIO", n) == n))
    {
      code.append('X'); //transform TIA, TIO to X
      insz++;
      return insz;
    }
    if (holdS.indexOf("TCH", n) == n)
    {
      return insz;//T silent if THC
    }
    if (holdS.indexOf("TH", n) == n)
    {
      code.append('0'); //transform TH to zero
    }
    else
    {
      code.append('T');
    }
    insz++;
    return insz;
  }

  /**
   * Letter V transformations - V to F:
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param code StringBuilder that contains metaphone code.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneVCase(int insz, StringBuilder code)
  {
    code.append('F');
    insz++;
    return insz;
  }

  /**
   * Letter Y and W transformations: used if followed by a vowel.
   *
   * @param letter character in a String.
   * @param insz   integer that calculates size of the metaphone code.
   * @param work   StringBuilder that contains new input string after initial letter
   *               exceptions are handled.
   * @param code   StringBuilder that contains metaphone code.
   * @param wrsz   integer length of the String.
   * @param n      integer index position.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneWYCase(char letter, int insz, StringBuilder work,
                                     StringBuilder code, int wrsz, int n)
  {
    if ((n + 1 < wrsz) &&
        (vowels.indexOf(work.charAt(n + 1)) >= 0))
    {
      code.append(letter);
      insz++;
    }
    return insz;
  }

  /**
   * Letter X transformations: X to KS.
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param code StringBuilder that contains metaphone code.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneXCase(int insz, StringBuilder code)
  {
    //transform X into KS
    code.append('K');
    code.append('S');
    insz += 2;
    return insz;
  }

  /**
   * Letter Z transformations: Z to S.
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param code StringBuilder that contains metaphone code.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneZCase(int insz, StringBuilder code)
  {
    code.append('S'); //transform Z to S
    insz++;
    return insz;
  }
}
