package com.ulap.util.tecleaApi.Algorithm;

import java.util.Set;


public interface KeyboardLayout
{
  String getName();

  KeyboardLocation getKeyboardLocation(char charObj);

  Set<Character> getCharacters();
}
