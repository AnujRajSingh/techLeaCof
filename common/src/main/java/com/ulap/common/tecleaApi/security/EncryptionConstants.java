package com.ulap.util.tecleaApi.security;

public class EncryptionConstants {

  //Constants
  public static final String ALGORITHM_AES256 = "AES/CBC/PKCS5Padding";
  public static final String SECRET_KEYALGO = "AES";
  public static final String ALGORITHM_RSA = "RSA/ECB/PKCS1Padding";
  public static final int AES_TYPE = 256;
  public static final byte[] INITIAL_IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  //Algorithm Specific Constants
  public final static String cipherType = "DESede";
  public final static String keyStoreType = "jks";
  public final static int byteLength = 2048;
  public final static String KEY_NOT_FOUND = "Not Able to find valid Key";
}
