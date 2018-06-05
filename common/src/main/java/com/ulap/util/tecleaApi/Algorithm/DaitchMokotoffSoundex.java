package com.ulap.util.tecleaApi.Algorithm;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.ulap.util.tecleaApi.Algorithm.DaitchMokotoffSoundexEntry.*;

/**
 * This is an implementation of the Daitch-Mokotoff Soundex algorithm. This is
 * an improvement over the classical Soundex for Jewish and Yiddish names
 * 
 * The algorithm is described at http://www.jewishgen.org/InfoFiles/soundex.html
 * A reference implementation is provided at http://www.jewishgen.org/jos/jossound.htm
 * 
 *
 */
public class DaitchMokotoffSoundex implements MatchAlgorithm
{
  private static final String NAME = "Daitch-Mokotoff Soundex";
  private static final String[] PADDING = { "0", "00", "000", "0000", "00000", "000000" };

  public static final DaitchMokotoffSoundex INSTANCE = new DaitchMokotoffSoundex();

  private DaitchMokotoffSoundex()
  {

  }

  Set<String> getCodes(String suspect)
  {
    Set<String> result = new HashSet<String>();

    if ((suspect == null) || (suspect.length() == 0))
    {
      return result;
    }

    suspect = suspect.toLowerCase();
    DaitchMokotoffSoundexEntry[] entries = new DaitchMokotoffSoundexEntry[suspect.length()];
    int pos = 0;
    int entryCount = 0;
    List<StringBuilder> codes = new ArrayList<StringBuilder>();
    while (pos < suspect.length())
    {
      int length = 1; // 1 for non alphabet
      DaitchMokotoffSoundexEntry newEntry = lookup(suspect, pos);
      if (newEntry != null)
      {
        entries[entryCount++] = newEntry;
        length = newEntry.length();
      }
      pos += length;
    }
    DaitchMokotoffSoundexEntry entry = entries[0];
    if (entry == null)
    {
      return result;
    }
    // Start of name
    if (entry.hasAlternate())
    {
      for (String choice : entry.getAlternate())
      {
        codes.add(new StringBuilder(getDMSoundexEntry(choice).getFirst()));
      }
    }
    else
    {
      codes.add(new StringBuilder(entry.getFirst()));
    }
    String previous = entry.getName();
    for (int i = 1; i < entryCount && !codes.isEmpty(); i++)
    {
      entry = entries[i];
      boolean beforeVowel = (i + 1 < entryCount) && entries[i + 1].isVowel();
      if (!entry.hasAlternate())
      {
        if (!entry.getName().equals(previous))
        {
          String value = beforeVowel ? entry.getBeforeVowel() : entry.getOther();
          for (StringBuilder code : codes)
          {
            code.append(value);
          }
        }
        previous = entry.getName();
      }
      else
      {
        List<StringBuilder> newCodes = new ArrayList<StringBuilder>();
        for (StringBuilder code : codes)
        {
          String[] alternates = entry.getAlternate();
          StringBuilder codeCopy = new StringBuilder(code);

          DaitchMokotoffSoundexEntry choice = getDMSoundexEntry(alternates[0]);
          String value = beforeVowel ? choice.getBeforeVowel() : choice.getOther();
          code.append(value);
          choice = getDMSoundexEntry(alternates[1]);
          value = beforeVowel ? choice.getBeforeVowel() : choice.getOther();
          codeCopy.append(value);
          newCodes.add(codeCopy);
        }

        codes.addAll(newCodes);
      }
    }
    for (StringBuilder code : codes)
    {
      if (code.length() < 6)
      {
        code.append(PADDING[6 - code.length()]);
      }
      result.add(code.substring(0, 6));
    }

    return result;
  }

  private DaitchMokotoffSoundexEntry lookup(String name, int pos)
  {
    Character ch = name.charAt(pos);
    String[] prefixes = getStartsWith(ch);
    DaitchMokotoffSoundexEntry entry = null;
    if (prefixes != null)
    {
      for (String prefix : prefixes)
      {
        if (name.regionMatches(pos, prefix, 0, prefix.length()))
        {
          entry = getDMSoundexEntry(prefix);
          break;
        }
      }
    }
    return entry;
  }

  @Override
  public String getName()
  {
    return NAME;
  }

  @Override
  public double compare(String suspect, String candidate) throws UnsupportedEncodingException
  {
    if (Collections.disjoint(getCodes(suspect), getCodes(candidate)))
    {
      return 0;
    }
    else
    {
      return 100;
    }

  }
}
