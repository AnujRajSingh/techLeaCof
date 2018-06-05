package com.ulap.util.tecleaApi.security;

import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class ColumnEncryptorJasypt extends ColumnEncryptor implements Encryption {


  /**
   * This method should be called to initialize the Jasypt encryption/decryption.
   */

  private final String sk1 = "050e020a05";
  private static ColumnEncryptorJasypt instance;
  private StandardPBEStringEncryptor stringEncryptor;
  private StandardPBEByteEncryptor byteEncryptor;
  private final String sk2 = "0a01030d070b";
  private final String sk3 = "010f0c080f";

  protected ColumnEncryptorJasypt()
  {
    byteEncryptor = new StandardPBEByteEncryptor();
    byteEncryptor.setPassword(getSecretKey());

    stringEncryptor = new StandardPBEStringEncryptor();
    stringEncryptor.setPassword(getSecretKey());
  }

  /**
   * This method should be called to initialize the Jasypt encryption/decryption.
   */
  public static  synchronized ColumnEncryptorJasypt getInstance()
  {
    if (instance == null)
    {
      instance = new ColumnEncryptorJasypt();
    }
    return instance;
  }


  public byte[] encrypt(byte[] message)
  {
    return byteEncryptor.encrypt(message);
  }

  public String encrypt(String plaintext)
  {
    return stringEncryptor.encrypt(plaintext);
  }

  public byte[] decrypt(byte[] encryptedMessage)
  {
    return byteEncryptor.decrypt(encryptedMessage);
  }

  public String decrypt(String ciphertext)
  {
    return stringEncryptor.decrypt(ciphertext);
  }

  private String getSecretKey()
  {
    return sk2 + sk1 + sk3;
  }


}
