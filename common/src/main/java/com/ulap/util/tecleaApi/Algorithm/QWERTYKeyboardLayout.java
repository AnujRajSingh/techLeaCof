package com.ulap.util.tecleaApi.Algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**



 */
public class QWERTYKeyboardLayout implements KeyboardLayout
{
  private static final String NAME = "QWERTY";
  private static Map<Character, KeyboardLocation> pcKeyboard = new HashMap<Character, KeyboardLocation>();

  public String getName()
  {
    return NAME;
  }

  public KeyboardLocation getKeyboardLocation(char charObj)
  {
    return pcKeyboard.get(charObj);
  }

  public Set<Character> getCharacters()
  {
    return pcKeyboard.keySet();
  }

  static
  {
    pcKeyboard.put(' ', new KeyboardLocation(-1, 4, 6));

    pcKeyboard.put('`', new KeyboardLocation(0, 0, 15));
    pcKeyboard.put('~', new KeyboardLocation(0, 0, 15));
    pcKeyboard.put('1', new KeyboardLocation(1, 0, 14));
    pcKeyboard.put('!', new KeyboardLocation(1, 0, 14));
    pcKeyboard.put('2', new KeyboardLocation(2, 0, 13));
    pcKeyboard.put('@', new KeyboardLocation(2, 0, 13));
    pcKeyboard.put('3', new KeyboardLocation(3, 0, 12));
    pcKeyboard.put('#', new KeyboardLocation(3, 0, 12));
    pcKeyboard.put('4', new KeyboardLocation(4, 0, 11));
    pcKeyboard.put('$', new KeyboardLocation(4, 0, 11));
    pcKeyboard.put('5', new KeyboardLocation(5, 0, 10));
    pcKeyboard.put('%', new KeyboardLocation(5, 0, 10));
    pcKeyboard.put('6', new KeyboardLocation(6, 0, 10));
    pcKeyboard.put('^', new KeyboardLocation(6, 0, 10));
    pcKeyboard.put('7', new KeyboardLocation(7, 0, 10));
    pcKeyboard.put('&', new KeyboardLocation(7, 0, 10));
    pcKeyboard.put('8', new KeyboardLocation(8, 0, 11));
    pcKeyboard.put('*', new KeyboardLocation(8, 0, 11));
    pcKeyboard.put('9', new KeyboardLocation(9, 0, 12));
    pcKeyboard.put('(', new KeyboardLocation(9, 0, 12));
    pcKeyboard.put('0', new KeyboardLocation(10, 0, 13));
    pcKeyboard.put(')', new KeyboardLocation(10, 0, 13));
    pcKeyboard.put('-', new KeyboardLocation(11, 0, 14));
    pcKeyboard.put('_', new KeyboardLocation(11, 0, 14));
    pcKeyboard.put('=', new KeyboardLocation(12, 0, 15));
    pcKeyboard.put('+', new KeyboardLocation(12, 0, 15));

    pcKeyboard.put('q', new KeyboardLocation(1, 1, 14));
    pcKeyboard.put('w', new KeyboardLocation(2, 1, 13));
    pcKeyboard.put('e', new KeyboardLocation(3, 1, 12));
    pcKeyboard.put('r', new KeyboardLocation(4, 1, 11));
    pcKeyboard.put('t', new KeyboardLocation(5, 1, 10));
    pcKeyboard.put('y', new KeyboardLocation(6, 1, 9));
    pcKeyboard.put('u', new KeyboardLocation(7, 1, 9));
    pcKeyboard.put('i', new KeyboardLocation(8, 1, 10));
    pcKeyboard.put('o', new KeyboardLocation(9, 1, 11));
    pcKeyboard.put('p', new KeyboardLocation(10, 1, 12));
    pcKeyboard.put('[', new KeyboardLocation(11, 1, 13));
    pcKeyboard.put('{', new KeyboardLocation(11, 1, 13));
    pcKeyboard.put(']', new KeyboardLocation(12, 1, 14));
    pcKeyboard.put('}', new KeyboardLocation(12, 1, 14));
    pcKeyboard.put('\\', new KeyboardLocation(13, 1, 15));
    pcKeyboard.put('|', new KeyboardLocation(13, 1, 15));

    pcKeyboard.put('a', new KeyboardLocation(1, 2, 15));
    pcKeyboard.put('s', new KeyboardLocation(2, 2, 14));
    pcKeyboard.put('d', new KeyboardLocation(3, 2, 13));
    pcKeyboard.put('f', new KeyboardLocation(4, 2, 12));
    pcKeyboard.put('g', new KeyboardLocation(5, 2, 11));
    pcKeyboard.put('h', new KeyboardLocation(6, 2, 10));
    pcKeyboard.put('j', new KeyboardLocation(7, 2, 10));
    pcKeyboard.put('k', new KeyboardLocation(8, 2, 11));
    pcKeyboard.put('l', new KeyboardLocation(9, 2, 12));
    pcKeyboard.put(';', new KeyboardLocation(10, 2, 13));
    pcKeyboard.put(':', new KeyboardLocation(10, 2, 13));
    pcKeyboard.put('\'', new KeyboardLocation(11, 2, 14));
    pcKeyboard.put('"', new KeyboardLocation(11, 2, 14));

    pcKeyboard.put('z', new KeyboardLocation(1, 3, 16));
    pcKeyboard.put('x', new KeyboardLocation(2, 3, 15));
    pcKeyboard.put('c', new KeyboardLocation(3, 3, 14));
    pcKeyboard.put('v', new KeyboardLocation(4, 3, 13));
    pcKeyboard.put('b', new KeyboardLocation(5, 3, 12));
    pcKeyboard.put('n', new KeyboardLocation(6, 3, 11));
    pcKeyboard.put('m', new KeyboardLocation(7, 3, 11));
    pcKeyboard.put(',', new KeyboardLocation(8, 3, 12));
    pcKeyboard.put('<', new KeyboardLocation(8, 3, 12));
    pcKeyboard.put('.', new KeyboardLocation(9, 3, 13));
    pcKeyboard.put('>', new KeyboardLocation(9, 3, 13));
    pcKeyboard.put('/', new KeyboardLocation(10, 3, 14));
    pcKeyboard.put('?', new KeyboardLocation(10, 3, 14));
  }

}
