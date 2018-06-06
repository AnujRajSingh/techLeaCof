package com.ulap.util.tecleaApi.Algorithm;

import java.io.UnsupportedEncodingException;


public interface MatchAlgorithm
{
  String getName();

  double compare(String suspect, String candidate) throws UnsupportedEncodingException;
}
