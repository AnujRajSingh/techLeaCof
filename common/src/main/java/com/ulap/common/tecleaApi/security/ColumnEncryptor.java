package com.ulap.util.tecleaApi.security;

/**
 * Encryption/decryption using Jasypt.
 */
public abstract class  ColumnEncryptor implements Encryption{

   /**
   * This method should be called to initialize the Jasypt encryption/decryption.
   */

   public static  synchronized ColumnEncryptorJasypt getInstance()
   {
     ColumnEncryptorJasypt instance=null;
     if (instance == null)
     {
       instance = new ColumnEncryptorJasypt();
     }
     return instance;
   }

  public abstract byte[] encrypt(byte[] message);


  public abstract String encrypt(String plaintext);


  public abstract byte[] decrypt(byte[] encryptedMessage);


  public abstract String decrypt(String ciphertext);


 }
