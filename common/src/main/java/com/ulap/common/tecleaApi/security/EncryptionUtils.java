package com.ulap.util.tecleaApi.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

public class EncryptionUtils {

  /**
   * Generate a secret TripleDES encryption/decryption key
   */
  public static SecretKey generateKey() throws NoSuchAlgorithmException
  {
    // Get a key generator for Triple DES (a.k.a DESede)
    KeyGenerator keygen = KeyGenerator.getInstance(EncryptionConstants.cipherType);
    // Use it to generate a key
    return keygen.generateKey();
  }

  /**
   * Save the specified TripleDES SecretKey to the specified file
   */
  public static void writeKey(SecretKey key, File f) throws IOException,
                                                             NoSuchAlgorithmException, InvalidKeySpecException
  {
    // Convert the secret key to an array of bytes like this
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(EncryptionConstants.cipherType);
    DESedeKeySpec keyspec = (DESedeKeySpec) keyfactory.getKeySpec(key,
            DESedeKeySpec.class);
    byte[] rawkey = keyspec.getKey();

    // Write the raw key to the file
    FileOutputStream out = new FileOutputStream(f);
    out.write(rawkey);
    out.close();
  }

  /**
   * Read a TripleDES secret key from the specified file
   */
  public static SecretKey readKey(File f) throws IOException,
                                                  NoSuchAlgorithmException, InvalidKeyException,
                                                  InvalidKeySpecException
  {
    // Read the raw bytes from the keyfile
    DataInputStream in = new DataInputStream(new FileInputStream(f));
    byte[] rawkey = new byte[(int) f.length()];
    in.readFully(rawkey);
    in.close();

    // Convert the raw bytes to a secret key like this
    DESedeKeySpec keyspec = new DESedeKeySpec(rawkey);
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(EncryptionConstants.cipherType);
    SecretKey key = keyfactory.generateSecret(keyspec);
    return key;
  }


  public boolean isJceInstalled()
  {
    try
    {
      Cipher c = Cipher.getInstance(EncryptionConstants.cipherType);
      return true;
    } catch (Exception e)
    {
      // An exception here probably means the JCE provider hasn't
      // been permanently installed on this system by listing it
      // in the $JAVA_HOME/jre/lib/security/java.security file.
      // Therefore, we have to install the JCE provider explicitly.
      Provider sunjce = new com.sun.crypto.provider.SunJCE();
      Security.addProvider(sunjce);
      return false;
    }

  }
  /*
   * Copy pasted from BCrypt internals :(. Ideally a method
   * to exports parts would be public. We only care about rounds
   * currently.
   */
  protected static int getRounds(String salt)
  {
    char minor = (char) 0;
    int off = 0;

    if (salt.charAt(0) != '$' || salt.charAt(1) != '2')
      throw new IllegalArgumentException("Invalid salt version");
    if (salt.charAt(2) == '$')
      off = 3;
    else
    {
      minor = salt.charAt(2);
      if (minor != 'a' || salt.charAt(3) != '$')
        throw new IllegalArgumentException("Invalid salt revision");
      off = 4;
    }

    // Extract number of rounds
    if (salt.charAt(off + 2) > '$')
      throw new IllegalArgumentException("Missing salt rounds");
    return Integer.parseInt(salt.substring(off, off + 2));
  }


}
