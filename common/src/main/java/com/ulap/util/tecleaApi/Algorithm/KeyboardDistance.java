package com.ulap.util.tecleaApi.Algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * Determines the distance between two keys on a keyboard. /
 * </p>
 */
public class KeyboardDistance implements ConfigurableMatchAlgorithm
{
  private static final String KEYBOARD_TYPE_OPTION = "layout";
  private static final KeyboardType DEFAULT_TYPE = KeyboardType.QWERTY;
  private static Map<KeyboardType, KeyboardLayout> KEYBOARD_LAYOUTS;

  static
  {
    KEYBOARD_LAYOUTS = new HashMap<KeyboardType, KeyboardLayout>();
    KEYBOARD_LAYOUTS.put(KeyboardType.QWERTY, new QWERTYKeyboardLayout());
    KEYBOARD_LAYOUTS.put(KeyboardType.QWERTZ, new QWERTZKeyboardLayout());
    KEYBOARD_LAYOUTS.put(KeyboardType.AZERTY, new AZERTYKeyboardLayout());
  }

  private KeyboardType keyboardType = DEFAULT_TYPE;
  private KeyboardLayout keyboardLayout = KEYBOARD_LAYOUTS.get(DEFAULT_TYPE);

  public KeyboardDistance()
  {
  }

  public String getName()
  {
    return "Keyboard Distance [Layout = " + keyboardType + "]";
  }

  public KeyboardType getKeyboardType()
  {
    return keyboardType;
  }

  public void setKeyboardType(KeyboardType kt)
  {
    this.keyboardType = kt;
    this.keyboardLayout = KEYBOARD_LAYOUTS.get(this.keyboardType);
  }

  public KeyboardLayout getKeyboardLayout()
  {
    return keyboardLayout;
  }

  public void configure(Map<String, String> options)
  {
    if (options != null)
    {
      String typeName = options.get(KEYBOARD_TYPE_OPTION);
      if (typeName != null)
      {
        KeyboardType kt = KeyboardType.valueOf(typeName);
        if (kt == null)
        {
          throw new IllegalArgumentException("Unknown keyboard type: " + typeName);
        }
        setKeyboardType(kt);
      }
    }
  }

  public double compare(String suspect, String candidate)
  {
    char[] suspectChars = suspect.toLowerCase().toCharArray();
    char[] candidateChars = candidate.toLowerCase().toCharArray();

    int n = suspectChars.length;
    int z = candidateChars.length;

    int longest = (n >= z) ? n : z;

    int[] cx, cy, sx, sy;
    sx = new int[longest];
    sy = new int[longest];
    cx = new int[longest];
    cy = new int[longest];

    int ws = getGrid(suspectChars, sx, sy);
    int wc = getGrid(candidateChars, cx, cy);
    double rScore = getScore(wc, ws, longest, cx, cy, sx, sy);

    return rScore;
  }

  //   private static int getGrid(char[] suspectChars, int n, int ws, int longest,
  //                              int[] sx, int[] sy) throws AlgorithmException {
  private int getGrid(char[] suspectChars, int[] sx, int[] sy)
  {
    int ws = 0;
    for (int i = 0; i < suspectChars.length; i++)
    {
      char ch = suspectChars[i];
      // if ch is blank, treat it like a space
      KeyboardLocation location = keyboardLayout.getKeyboardLocation(ch);
      if (location != null)
      {
        sx[i] = location.x + 1;
        sy[i] = location.y + 1;
        ws += location.rScore;
      }
    }
    // if ch is blank, treat it like a space
    if (suspectChars.length == 0 && sx.length != 0 && sy.length != 0)
    {
      KeyboardLocation location = keyboardLayout.getKeyboardLocation(' ');
      if (location != null)
      {
        sx[0] = location.x + 1;
        sy[0] = location.y + 1;
        ws += location.rScore;
      }
    }
    return ws;
  }

  private double getScore(int wc, int ws, int longest, int[] cx, int[] cy, int[] sx, int[] sy)
  {
    int kScore = 0;

    for (int m = 0; m <= (longest - 1); m++)
    {
      int cxC = cx[m];
      int cyC = cy[m];
      int sxC = sx[m];
      int syC = sy[m];

      // if space use y from other string
      if (sxC == 0)
      {
        sxC = cxC;
      }
      else if (cxC == 0)
      {
        cxC = sxC;
      }
      // @todo Use abs() method after installing jdk 1.4.2
      // calculate distance between keys
      if (sxC == cxC) // in same column
      {
        if (syC > cyC) // don't want negative kScores
        {
          kScore = ((syC - cyC) * 2) + kScore;
        }
        else
        {
          kScore = ((cyC - syC) * 2) + kScore;
        }
      }
      else if (syC == cyC) // in same row
      {
        if (sxC > cxC) // don't want negative kScores
        {
          kScore = ((sxC - cxC) * 2) + kScore;
        }
        else
        {
          kScore = ((cxC - sxC) * 2) + kScore;
        }
      }
      else if (sxC > cxC)
      {
        if (syC > cyC)
        {
          if ((syC - 1) == cyC)
          {
            kScore = ((sxC - cxC) * 2) + kScore;
          }
          else
          {
            kScore = (((sxC - cxC) + (syC - cyC)) * 2) + kScore;
          }
        }
        else
        {
          if ((cyC - 1) == syC)
          {
            kScore = ((sxC - cxC) * 2) + kScore;
          }
          else
          {
            kScore = (((sxC - cxC) + (cyC - syC)) * 2) + kScore;
          }
        }
      }
      else if (sxC < cxC)
      {
        if (syC > cyC)
        {
          if ((syC - 1) == cyC)
          {
            kScore = ((cxC - sxC) * 2) + kScore;
          }
          else
          {
            kScore = (((cxC - sxC) + (syC - cyC)) * 2) + kScore;
          }
        }
        else
        {
          if ((cyC - 1) == syC)
          {
            kScore = ((cxC - sxC) * 2) + kScore;
          }
          else
          {
            kScore = (((cxC - sxC) + (cyC - syC)) * 2) + kScore;
          }
        }
      }
    }

    if (kScore < 0)
    {
      kScore = kScore * -1;
    }
    else if (kScore == 0)
    {
      return 100;
    }
    double rScore = 100 - ((kScore * 100) / (ws + wc));
    return rScore;
  }
}
