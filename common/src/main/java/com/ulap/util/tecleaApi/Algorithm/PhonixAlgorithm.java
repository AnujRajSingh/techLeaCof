package com.ulap.util.tecleaApi.Algorithm;

import java.util.BitSet;

public class PhonixAlgorithm extends AbstractHashingMatchAlgorithm
{
  public static final PhonixAlgorithm INSTANCE = new PhonixAlgorithm();
  public static final String PHONIX_KEY = "Z0000000";
  public static final int PHONIX_LENGTH = 8;
  public static final int NON = 1; /* no condition */
  public static final int VOC = 2; /* vowel needed */
  public static final int CON = 3; /* consonant needed */

  public static final int START = 1; /* condition refers to beginning of Name */
  public static final int MIDDLE = 2; /* condition refers to middle of Name */
  public static final int END = 3;/* condition refers to EndPos of Name */
  public static final int ALL = 4; /* condition refers to whole Name */
  public static final String PHONIX_ALGORITHM = "Phonix Algorithm";

  @Override
  public String getName()
  {
    // TODO Auto-generated method stub
    return PHONIX_ALGORITHM;
  }

  @Override
  public String getHashCode(String inputValue)
  {
    String hash;
    if (inputValue != null)
    {
      hash = phonix(inputValue.toUpperCase());
    }
    else
    {
      hash = "";
    }

    return (hash != null) ? hash : "";
  }

  @Override
  protected MatchAlgorithm getHashCodeMatchAlgorithm()
  {
    return ExactMatch.INSTANCE;
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

  private static boolean isVowel(char c)
  {
    return (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y' || c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' || c == 'Y');
  }

  /*
   *  ***************************************************************************
   *  NAME : isAlpha
   *  INPUT : char c --- char to be examined
   *  OUTPUT : BOOLEAN --- true or false
   *  FUNCTION: isAlpha checks if c is an alphabet either in upper case or in lower
   *  case. 
   *  ***************************************************************************
   */

  private static boolean isAlpha(char c)
  {
    return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
  }

  /*
   * ***************************************************************************
   * NAME : PhonixCode
   * INPUT : String s --- string to be calculated
   * OUTPUT : String  --- phonix code for Name
   * FUNCTION: This method calculates a eight-letter phonix code for the string
   * Name and returns this code in the string Key.
   * ***************************************************************************
   */

  public String phonixCode(String s)
  {
    if (s.isEmpty())
    {
      return "";
    }
    int[] code = new int[26];
    char lastLetter;
    char[] name = s.toCharArray();
    /* set default key */
    char[] key = (PHONIX_KEY).toCharArray();

    /* set letter values */
    code[0] = 0;
    code[1] = 1;
    code[2] = 2;
    code[3] = 3;
    code[4] = 0;
    code[5] = 7;
    code[6] = 2;
    code[7] = 0;
    code[8] = 0;
    code[9] = 2;
    code[10] = 2;
    code[11] = 4;
    code[12] = 5;
    code[13] = 5;
    code[14] = 0;
    code[15] = 1;
    code[16] = 2;
    code[17] = 6;
    code[18] = 8;
    code[19] = 3;
    code[20] = 0;
    code[21] = 7;
    code[22] = 0;
    code[23] = 8;
    code[24] = 0;
    code[25] = 8;

    /* keep first letter or replace it with '$' */
    key[0] = isVowel(name[0]) ? '$' : name[0];
    lastLetter = name[0];
    /* NOTE: Gadd replaces vowels being the first letter of the */
    /* word with a 'v'. Due to the implementation of WAIS all */
    /* letters will be lowercased. Therefore '$' is used instead */
    /* of 'v'. */

    /* scan rest of string */
    int strIndex = 1;
    for (int index = 1; index < PHONIX_LENGTH && strIndex < name.length;)
    {
      if (isAlpha(name[strIndex]))
      {
        if (lastLetter != name[strIndex])
        {
          lastLetter = name[strIndex];

          /* ignore letters with code 0 except as separators */
          if (code[Character.toUpperCase(name[strIndex]) - 'A'] != 0)
          {
            key[index] = (char)('0' + code[Character.toUpperCase(name[strIndex]) - 'A']);
            index++;
          }
        }

      }
      strIndex++;
    }
    return new String(key);
  }

  /*
   * ***************************************************************************
   * NAME : phonixReplace1 
   * INPUT : int where --- replace OldStr only if it occurs at this position 
   * String name --- string to work, String OldStr --- old letter group to delete 
   * String NewStr --- new letter group to insert, 
   * int CondPre --- condition referring to letter before   , 
   * int CondPost --- condition referring to letter after OldStr 
   * OUTPUT : String --- Name with 
   * replaced letter group FUNCTION: This procedure replaces the letter
   * group OldStr with the letter group NewStr in the string Name, regarding the 
   * position of OldStr where (START, MIDDLE, END, ALL) and the conditions
   * CondPre and CondPost (NON, VOC, CON). EXAMPLE : 
   * phonixReplace1(START, "WAWA", "W", "V", NON, NON) replaces only the first W 
   * with a V because of
   * the condition START. EXAMPLE : phonixReplace1(START, "WAWA", "W", "V", NON, CON) 
   * replaces neither the first W with a V (because of the condition
   * CON, i.e. a consonant must follow the W) nor the second W 
   * (because of the condition START).
   *  ***************************************************************************
   */

  void phonixReplace1(int where, StringBuilder name, String oldString, String newString, int condPre, int condPost)
  {

    int position = 0;
    int oldStringPosition;
    int count = -1;
    do
    {
      oldStringPosition = name.indexOf(oldString, position);
      int endPosition = name.length() - oldString.length();
      boolean okayPre = false, okayPost = false;
      if (oldStringPosition >= 0)
      {
        count++;
        int letterPre = oldStringPosition - 1;
        int letterPost = oldStringPosition + oldString.length();
        switch (condPre)
        {
          case NON:
            okayPre = true;
            break;
          case VOC:
            okayPre = letterPre >= 0 && isVowel(name.charAt(letterPre));
            break;
          case CON:
            okayPre = letterPre >= 0 && !isVowel(name.charAt(letterPre));
            break;
          default:
            okayPre = false;
            break;
        }

        switch (condPost)
        {
          case NON:
            okayPost = true;
            break;
          case VOC:
            okayPost = letterPost < name.length() && isVowel(name.charAt(letterPost));
            break;
          case CON:
            okayPost = letterPost < name.length() && !isVowel(name.charAt(letterPost));
            break;
          default:
            okayPost = false;
            break;
        }
      }
      /**
       * This is BAD
       * TODO: replace ++count == 0 with oldStringPosition
       * count was introduced to keep output consistent with the way it was before. To be fixed in a separate bug
       */
      if (oldStringPosition >= 0 && okayPre && okayPost &&
              ((where == START && count == 0) ||
                      (where == MIDDLE && oldStringPosition != 0 && oldStringPosition != endPosition) ||
                      (where == END && oldStringPosition == endPosition) ||
                      (where == ALL)))
      {
        name.replace(oldStringPosition, oldStringPosition + oldString.length(), newString);
        position = oldStringPosition;
      }
      else
      {
        position++;
      }

    }
    while (oldStringPosition >= 0);
  }

  boolean checkBits(BitSet flags, String key)
  {
    boolean flag = true;
    for (char ch : key.toCharArray())
    {
      if (ch >= 'A')
      {
        flag &= flags.get(ch - 'A');
      }
    }
    return flag;
  }

  void setBits(BitSet flags, String key)
  {

    for (char ch : key.toCharArray())
    {
      if (ch >= 'A')
      {
        flags.set(ch - 'A');
      }
    }
  }


  void phonixReplace2(int where, StringBuilder name, String oldString, String newString, BitSet flags)
  {
    if (!checkBits(flags, oldString))
    {
      return;
    }
    phonixReplace2(where, name, oldString, newString);

    setBits(flags, newString);
  }

  /**
   * ***************************************************************************
   * NAME : phonixReplace2 INPUT : int where --- replace OldStr only if it occurs
   * at this position
   * char *Name --- string to work
   * char *OldStr --- old letter group to delete char
   * *NewStr --- new letter group to insert OUTPUT :
   * char *Name --- Name with replaced letter group FUNCTION:
   * This Method replaces the letter group OldStr with the letter group NewStr
   * in the string Name, regarding the position of OldStr where (START,
   * MIDDLE, END, ALL). EXAMPLE : phonixReplace2(START, "WAWA", "W", "V") replaces
   * only the first W with a V because of the condition START.
   * <p/>
   * ***************************************************************************
   */
  void phonixReplace2(int where, StringBuilder name, String oldString, String newString)
  {
    int position = 0;
    int oldStringPosition;
    int count = -1;
    do
    {

      oldStringPosition = name.indexOf(oldString, position);
      int endPosition = name.length() - oldString.length();

      /**
       * This is BAD
       * TODO: replace ++count == 0 with oldStringPosition
       * count was introduced to keep output consistent with the way it was before. To be fixed in a separate bug
       */
      if (oldStringPosition >= 0 &&
              ((where == START && ++count == 0) ||
                      (where == MIDDLE && oldStringPosition != 0 && oldStringPosition != endPosition) ||
                      (where == END && oldStringPosition == endPosition) ||
                      (where == ALL)))
      {
        name.replace(oldStringPosition, oldStringPosition + oldString.length(), newString);
        position = oldStringPosition;
      }
      else
      {
        position++;
      }
    }
    while (oldStringPosition >= 0);
  }

  /*
   * ***************************************************************************
   * NAME : Phonix INPUT : char *Name --- string to calculate phonix code for OUTPUT : 
   * char *Key --- phonix code of Name FUNCTION: Phonix calculates
   * the phonix code for the string Name.
   ****************************************************************************
   */
  public String phonix(String name)
  {
    if (name == null || name.equals(""))
    {
      return "";
    }

    StringBuilder newName = new StringBuilder(name.length()*2);
    newName.append(name.toUpperCase());
    /* replace letter groups according to Gadd's definition */
    convertToPhonixString(newName);

    /* calculate Key for NewName */
    return phonixCode(newName.toString());

  }

  //BitSet flags = new BitSet(26);
  //setBits(flags, newName.toString());


  public void convertToPhonixString(StringBuilder newName)
  {

    BitSet flags = new BitSet(26);
    setBits(flags, newName.toString());

    phonixReplace2(ALL, newName, "DG", "G", flags);
    phonixReplace2(ALL, newName, "CO", "KO", flags);
    phonixReplace2(ALL, newName, "CA", "KA", flags);
    phonixReplace2(ALL, newName, "CU", "KU", flags);
    phonixReplace2(ALL, newName, "CY", "SI", flags);
    phonixReplace2(ALL, newName, "CI", "SI", flags);
    phonixReplace2(ALL, newName, "CE", "SE", flags);
    phonixReplace1(START, newName, "CL", "KL", NON, VOC);
    phonixReplace2(ALL, newName, "CK", "K", flags);
    phonixReplace2(END, newName, "GC", "K", flags);
    phonixReplace2(END, newName, "JC", "K", flags);
    phonixReplace1(START, newName, "CHR", "KR", NON, VOC);
    phonixReplace1(START, newName, "CR", "KR", NON, VOC);
    phonixReplace2(START, newName, "WR", "R", flags);
    phonixReplace2(ALL, newName, "NC", "NK", flags);
    phonixReplace2(ALL, newName, "CT", "KT", flags);
    phonixReplace2(ALL, newName, "PH", "F", flags);
    phonixReplace2(ALL, newName, "AA", "AR", flags);
    phonixReplace2(ALL, newName, "SCH", "SH", flags);
    phonixReplace2(ALL, newName, "BTL", "TL", flags);
    phonixReplace2(ALL, newName, "GHT", "T", flags);
    phonixReplace2(ALL, newName, "AUGH", "ARF", flags);
    phonixReplace1(MIDDLE, newName, "LJ", "LD", VOC, VOC);
    phonixReplace2(ALL, newName, "LOUGH", "LOW", flags);
    phonixReplace2(START, newName, "Q", "KW", flags);
    phonixReplace2(START, newName, "KN", "N", flags);
    phonixReplace2(END, newName, "GN", "N", flags);
    phonixReplace2(ALL, newName, "GHN", "N", flags);
    phonixReplace2(END, newName, "GNE", "N", flags);
    phonixReplace2(ALL, newName, "GHNE", "NE", flags);
    phonixReplace2(END, newName, "GNES", "NS", flags);
    phonixReplace2(START, newName, "GN", "N", flags);
    phonixReplace1(MIDDLE, newName, "GN", "N", NON, CON);
    phonixReplace1(END, newName, "GN", "N", NON, NON); /* NON,CON */
    phonixReplace2(START, newName, "PS", "S", flags);
    phonixReplace2(START, newName, "PT", "T", flags);
    phonixReplace2(START, newName, "CZ", "C", flags);
    phonixReplace1(MIDDLE, newName, "WZ", "Z", VOC, NON);
    phonixReplace2(MIDDLE, newName, "CZ", "CH", flags);
    phonixReplace2(ALL, newName, "LZ", "LSH", flags);
    phonixReplace2(ALL, newName, "RZ", "RSH", flags);
    phonixReplace1(MIDDLE, newName, "Z", "S", NON, VOC);
    phonixReplace2(ALL, newName, "ZZ", "TS", flags);
    phonixReplace1(MIDDLE, newName, "Z", "TS", CON, NON);
    phonixReplace2(ALL, newName, "HROUG", "REW", flags);
    phonixReplace2(ALL, newName, "OUGH", "OF", flags);
    phonixReplace1(MIDDLE, newName, "Q", "KW", VOC, VOC);
    phonixReplace1(MIDDLE, newName, "J", "Y", VOC, VOC);
    phonixReplace1(START, newName, "YJ", "Y", NON, VOC);
    phonixReplace2(START, newName, "GH", "G", flags);
    phonixReplace1(END, newName, "E", "GH", VOC, NON);
    phonixReplace2(START, newName, "CY", "S", flags);
    phonixReplace2(ALL, newName, "NX", "NKS", flags);
    phonixReplace2(START, newName, "PF", "F", flags);
    phonixReplace2(END, newName, "DT", "T", flags);
    phonixReplace2(END, newName, "TL", "TIL", flags);
    phonixReplace2(END, newName, "DL", "DIL", flags);
    phonixReplace2(ALL, newName, "YTH", "ITH", flags);
    phonixReplace1(START, newName, "TJ", "CH", NON, VOC);
    phonixReplace1(START, newName, "TSJ", "CH", NON, VOC);
    phonixReplace1(START, newName, "TS", "T", NON, VOC);
    //TO-DO : check below line , algorithm(C code) says it should be phonixReplce1 , but due to insufficient argument I changed it to phonixRepace2
    phonixReplace2(ALL, newName, "TCH", "CH", flags);
    phonixReplace1(MIDDLE, newName, "WSK", "VSKIE", VOC, NON);
    phonixReplace1(END, newName, "WSK", "VSKIE", VOC, NON);
    phonixReplace1(START, newName, "MN", "N", NON, VOC);
    phonixReplace1(START, newName, "PN", "N", NON, VOC);
    phonixReplace1(MIDDLE, newName, "STL", "SL", VOC, NON);
    phonixReplace1(END, newName, "STL", "SL", VOC, NON);
    phonixReplace2(END, newName, "TNT", "ENT", flags);
    phonixReplace2(END, newName, "EAUX", "OH", flags);
    phonixReplace2(ALL, newName, "EXCI", "ECS", flags);
    phonixReplace2(ALL, newName, "X", "ECS", flags);
    phonixReplace2(END, newName, "NED", "ND", flags);
    phonixReplace2(ALL, newName, "JR", "DR", flags);
    phonixReplace2(END, newName, "EE", "EA", flags);
    phonixReplace2(ALL, newName, "ZS", "S", flags);
    phonixReplace1(MIDDLE, newName, "R", "AH", VOC, CON);
    phonixReplace1(END, newName, "R", "AH", VOC, NON); /* VOC,CON */
    phonixReplace1(MIDDLE, newName, "HR", "AH", VOC, CON);
    phonixReplace1(END, newName, "HR", "AH", VOC, NON); /* VOC,CON */
    phonixReplace1(END, newName, "HR", "AH", VOC, NON);
    phonixReplace2(END, newName, "RE", "AR", flags);
    phonixReplace1(END, newName, "R", "AH", VOC, NON);
    phonixReplace2(ALL, newName, "LLE", "LE", flags);
    phonixReplace1(END, newName, "LE", "ILE", CON, NON);
    phonixReplace1(END, newName, "LES", "ILES", CON, NON);
    phonixReplace2(END, newName, "E", "", flags);
    phonixReplace2(END, newName, "ES", "S", flags);
    phonixReplace1(END, newName, "SS", "AS", VOC, NON);
    phonixReplace1(END, newName, "MB", "M", VOC, NON);
    phonixReplace2(ALL, newName, "MPTS", "MPS", flags);
    phonixReplace2(ALL, newName, "MPS", "MS", flags);
    phonixReplace2(ALL, newName, "MPT", "MT", flags);
  }

}
