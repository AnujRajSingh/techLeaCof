/*
 * Copyright: Copyright (c) 2006
 * Company: Group1 Software, Inc.
 * All Rights Reserved
 *
 * This software and documentation is the confidential and proprietary
 * information of Group1 Software, Inc. ("Confidential Information").
 *
 */

package com.ulap.util.tecleaApi.Algorithm;

import java.io.UnsupportedEncodingException;


public abstract class AbstractHashingMatchAlgorithm implements HashAlgorithm, MatchAlgorithm
{
  protected AbstractHashingMatchAlgorithm()
  {
  }

  public final double compare(String suspect, String candidate) throws UnsupportedEncodingException
  {
    return getHashCodeMatchAlgorithm().compare(getHashCode(suspect), getHashCode(candidate));
  }

  protected abstract MatchAlgorithm getHashCodeMatchAlgorithm();
}
