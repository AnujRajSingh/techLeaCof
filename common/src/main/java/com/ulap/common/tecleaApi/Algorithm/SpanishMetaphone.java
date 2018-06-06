package com.ulap.util.tecleaApi.Algorithm;

import java.util.Arrays;

/**


 */
public class SpanishMetaphone extends AbstractHashingMatchAlgorithm
{
  public static final SpanishMetaphone INSTANCE = new SpanishMetaphone();

  private static final String NAME = "Spanish Metaphone";

  private final static String vowels = "AEIOU";
  private final static String spanishVowels = "EI";
  private final static String duplicate = "DFJKMNPRSTV";
  // private final static String spanishCharacterReplace = "�������bz";
  private final static char spanishCharacterReplace[] = new char[9];
  private final static char spanishCharacter[] = new char[9];

  static
  {
    spanishCharacter[0] = 'v';
    spanishCharacter[1] = 's';
    spanishCharacter[2] = 'a';
    spanishCharacter[3] = 'e';
    spanishCharacter[4] = 'i';
    spanishCharacter[5] = 'n';
    spanishCharacter[6] = 'o';
    spanishCharacter[7] = 'u';
    spanishCharacter[8] = 'u';
    spanishCharacterReplace[0] = '�';
    spanishCharacterReplace[1] = '�';
    spanishCharacterReplace[2] = '�';
    spanishCharacterReplace[3] = '�';
    spanishCharacterReplace[4] = '�';
    spanishCharacterReplace[5] = '�';
    spanishCharacterReplace[6] = '�';
    spanishCharacterReplace[7] = 'b';
    spanishCharacterReplace[8] = 'z';
    Arrays.sort(spanishCharacterReplace);
  }

  private SpanishMetaphone()
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
    char[] input;
    char letter;
    int insz;
    int wrsz;
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

    input = suspect.toLowerCase().toCharArray();
    input = replaceSpanishCharacters(input);


    work = new StringBuilder(40); // work area
    code = new StringBuilder(10);
    Position position = new Position();
    insz = 0;
    work.append(input);
    wrsz = work.length();

    // work not has string with initial letters exceptions fixed
    while (insz < MaxLen && position.position < wrsz)
    {
      letter = work.charAt(position.position);
      if (duplicate.indexOf(letter) >= 0 && position.position > 0 && work.charAt(position.position - 1) == letter)
      {
        position.position++;
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
            insz = metaphoneFirstVowel(letter, insz, code, position);
            break;
          case 'C':
            insz = metaphoneCCase(insz, work, code, wrsz, position);
            break;
          case 'G':
            insz = metaphoneGCase(insz, work, code, wrsz, position);
            break;
          case 'H':
            insz = metaphoneHCase(insz, work, code, position);
            break;
          case 'J':
            insz = metaphoneJCase(insz, code);
            break;
          case 'B':
          case 'D':
          case 'F':
          case 'K':
          case 'M':
          case 'N':
          case 'P':
          case 'R':
          case 'S':
          case 'T':
          case 'W':
          case 'Y':
            insz = metaphoneNotTrans(letter, insz, code);
            break;
          case 'L':
            insz = metaphoneLCase(insz, work, code, position);
            break;
          case 'Q':
            insz = metaphoneQCase(insz, work, code, position);
            break;
          case 'V':
            insz = metaphoneVCase(insz, code);
            break;
          case 'X':
            insz = metaphoneXCase(insz, work, code, position);
            break;
          case 'Z':
            insz = metaphoneZCase(insz, code);
            break;
        } //end switch
        position.position++;
      }  // end else of not equal C
      if (insz > 4)
      {
        code.setLength(4);
      }
    }
    return code.toString();
  }

  private static char[] replaceSpanishCharacters(char[] input)
  {
    StringBuilder returnInput = new StringBuilder();
    int end = input.length;
    int where = 0;
    char letter;
    while (where < end)
    {
      letter = input[where++];
      int whereSpanish = Arrays.binarySearch(spanishCharacterReplace, letter);
      if (whereSpanish >= 0)
      {
        returnInput.append(spanishCharacter[whereSpanish]);
      }
      else
      {
        returnInput.append(letter);
      }
    }
    String returnString = returnInput.toString().toUpperCase();
    return returnString.toCharArray();
  }

  /**
   * Only use vowels if the first letter in the string.
   *
   * @param letter character in a String.
   * @param insz   integer that calculates size of the metaphone code.
   * @param code   StringBuilder that contains metaphone code.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneFirstVowel(char letter, int insz, StringBuilder code, Position position)
  {
    if (position.position == 0)
    {
      code.append(letter);
      insz++;
    }
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
   * @return metaphone code size as an integer.
   */
  private static int metaphoneCCase(int insz, StringBuilder work, StringBuilder code, int wrsz, Position position)
  {
    String holdS;

    // C special cases

    holdS = work.toString();
    if (position.position + 1 < work.length())
    {
      if (work.charAt(position.position + 1) == 'C')
      {
        code.append('X');
        insz++;
        position.position++;
        return insz; // change CC to X
      }
    }

    if ((position.position + 1 < wrsz) &&
            (spanishVowels.indexOf(work.charAt(position.position + 1)) >= 0))
    {
      code.append('S');
      insz++;
      position.position++;
      return insz; // change CI and CE to S
    }

    if (holdS.indexOf("CH", position.position) == position.position)
    {
      code.append('C');
      position.position++;
      insz++;
      code.append('H');
      position.position++;
      insz++;
      return insz;
    }
    else
    {
      code.append('K');
      insz++; //else change C to K
      return insz;
    }
  }

  /**
   * Letter G transformations:
   * <br>G to J   if before i or e
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param work StringBuilder that contains new input string after initial letter
   *             exceptions are handled.
   * @param code StringBuilder that contains metaphone code.
   * @param wrsz integer length of the String.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneGCase(int insz, StringBuilder work, StringBuilder code, int wrsz, Position position)
  {
    if (position.position + 1 < wrsz && spanishVowels.indexOf(work.charAt(position.position + 1)) >= 0)
    {
      code.append('J');
      insz++;
      position.position++;
      return insz; // change GI and GE to J
    }
    else if (position.position + 2 < wrsz && work.charAt(position.position + 1) == 'U' && work.charAt(position.position + 2) == 'A')
    {
      code.append('G');
      insz++;
      code.append('W');
      insz++;
      position.position++;
      position.position++;
      return insz; // change GUA to GW
    }
    else if (position.position + 2 < wrsz && work.charAt(position.position + 1) == 'U' && work.charAt(position.position + 2) == 'O')
    {
      code.append('G');
      insz++;
      code.append('W');
      insz++;
      position.position++;
      position.position++;
      return insz; // change GUO to GW
    }
    code.append('G');
    insz++;
    return insz;
  }

  /**
   * Letter H transformations:
   * <br> if vowel follows the "H" is silent, use vowel
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param work StringBuilder that contains new input string after initial letter
   *             exceptions are handled.
   * @param code StringBuilder that contains metaphone code.
   * @param position position
   * @return metaphone code size as an integer.
   */
  private static int metaphoneHCase(int insz, StringBuilder work, StringBuilder code, Position position)
  {
    if (position.position + 1 < work.length())
    {
      if (vowels.indexOf(work.charAt(position.position + 1)) >= 0)
      {
        code.append(work.charAt(position.position + 1)); //if vowel follows the "H" is silent, use vowel
        position.position++;
        insz++;
      }
    }
    return insz;
  }

  /**
   * Letter J transformations:
   * <br> J to H
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param code StringBuilder that contains metaphone code.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneJCase(int insz, StringBuilder code)
  {
    code.append('H');
    insz++;
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
   * Letter L transformations:
   * <br> LL to Y
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param code StringBuilder that contains metaphone code.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneLCase(int insz, StringBuilder work, StringBuilder code, Position position)
  {
    if (position.position + 1 < work.length())
    {
      if (work.charAt(position.position + 1) == 'L')
      {
        code.append('Y');
        position.position++;
      }
      else
      {
        code.append('L');
      }
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
  private static int metaphoneQCase(int insz, StringBuilder work, StringBuilder code, Position position)
  {
    if (position.position + 1 < work.length())
    {
      if (work.charAt(position.position + 1) == 'U')
      {
        position.position++;
      }
    }
    code.append('K'); //transform Q to K
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
    code.append('B');
    insz++;
    return insz;
  }

  /**
   * Letter X transformations: X to KS.
   *
   * @param insz integer that calculates size of the metaphone code.
   * @param code StringBuilder that contains metaphone code.
   * @return metaphone code size as an integer.
   */
  private static int metaphoneXCase(int insz, StringBuilder work, StringBuilder code, Position position)
  {
    if (position.position + 1 < work.length())
    {
      if (work.charAt(position.position + 1) == 'O')
      {
        code.append('S');
        position.position++;
        insz++;
        return insz;
      }
    }
    code.append('X');
    insz++;
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

  class Position
  {
    int position = 0;
  }
}
