package com.ulap.util.tecleaApi.security;

import com.google.common.base.Throwables;
import com.google.common.io.BaseEncoding;

import javax.crypto.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

/**
 * This algorithm is for countries where restriction of using AES.
 * This uses fixed String key and proceed using keystore
 */
public class ColumnEncryptorTripleDes extends ColumnEncryptor implements Encryption {



  private String keystoreName;
  private String keystorePassword;
  private String alias;
  private String keyPassword;
  private String keyfileStr;
  private static ColumnEncryptorTripleDes instance;

  private ColumnEncryptorTripleDes()
  {
  }

  /**
   * Constructor with configuration
   *
   * @param keyfileStr
   * @param keystoreName
   * @param keyStorepassword
   * @param keypassword
   * @param alias
   */
  public ColumnEncryptorTripleDes(String keyfileStr, String keystoreName, String keyStorepassword, String keypassword, String alias)
  {
    this.keystoreName = keystoreName;
    this.keystorePassword = keyStorepassword;
    this.keyPassword = keypassword;
    this.alias = alias;
    this.keyfileStr = keyfileStr;

  }


  /**
   * This method should be called to initialize the Jasypt encryption/decryption.
   */
  public static synchronized ColumnEncryptorTripleDes getInstance(String key)
  {
    if (instance == null)
    {
      instance = new ColumnEncryptorTripleDes();
    }
    return instance;
  }



  /**
   * Use the specified TripleDES key to encrypt bytes from the input stream
   * and write them to the output stream. This method uses CipherOutputStream
   * to perform the encryption and write bytes at the same time.
   */
  public void encrypt(SecretKey key, InputStream in, OutputStream out)
          throws NoSuchAlgorithmException, InvalidKeyException,
                         NoSuchPaddingException, IOException
  {
    // Create and initialize the encryption engine
    Cipher cipher = Cipher.getInstance(EncryptionConstants.cipherType);
    cipher.init(Cipher.ENCRYPT_MODE, key);

    // Create a special output stream to do the work for us
    CipherOutputStream cos = new CipherOutputStream(out, cipher);

    // Read from the input and write to the encrypting output stream
    byte[] buffer = new byte[EncryptionConstants.byteLength];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1)
    {
      cos.write(buffer, 0, bytesRead);
    }
    cos.close();

    // For extra security, don't leave any plaintext hanging around memory.
    java.util.Arrays.fill(buffer, (byte) 0);
  }

  /**
   * Use the specified TripleDES key to decrypt bytes ready from the input
   * stream and write them to the output stream. This method uses uses Cipher
   * directly to show how it can be done without CipherInputStream and
   * CipherOutputStream.
   */
  public void decrypt(SecretKey key, InputStream in, OutputStream out)
          throws NoSuchAlgorithmException, InvalidKeyException, IOException,
                         IllegalBlockSizeException, NoSuchPaddingException,
                         BadPaddingException
  {
    // Create and initialize the decryption engine
    Cipher cipher = Cipher.getInstance(EncryptionConstants.cipherType);
    cipher.init(Cipher.DECRYPT_MODE, key);

    // Read bytes, decrypt, and write them out.
    byte[] buffer = new byte[EncryptionConstants.byteLength];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1)
    {
      out.write(cipher.update(buffer, 0, bytesRead));
    }

    // Write out the final bunch of decrypted bytes
    out.write(cipher.doFinal());
    out.flush();
  }

  /**
   * Encrypt array of byte
   * @param message
   * @return byte[]
   */
  @Override
  public byte[] encrypt(byte[] message)
  {
    try
    {
      File keyFile = null;
      try
      {
        keyFile = new File(keyfileStr);
      } catch (Exception e)
      {
        throw Throwables.propagate(e);
      }
      if (keyFile != null)
      {
        // Create and initialize the encryption engine
        Cipher cipher = Cipher.getInstance(EncryptionConstants.cipherType);
        cipher.init(Cipher.ENCRYPT_MODE, EncryptionUtils.readKey(keyFile));
        byte[] encryptedTextBytes = cipher.doFinal(message);
        return encryptedTextBytes;
      } else
      {
        throw new InvalidKeySpecException();
      }
    } catch (InvalidKeySpecException | IOException | NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e)
    {
      throw Throwables.propagate(e);

    }

  }
  /**
   * Encrypt plain text
   * @param plaintext
   * @return String
   */
  @Override
  public String encrypt(String plaintext)
  {
    try
    {
      File keyFile = null;
      try
      {
        keyFile = new File(keyfileStr);
      } catch (Exception e)
      {
        throw Throwables.propagate(e);
      }
      if (keyFile != null)
      {
        // Create and initialize the encryption engine
        Cipher cipher = Cipher.getInstance(EncryptionConstants.cipherType);
        cipher.init(Cipher.ENCRYPT_MODE, EncryptionUtils.readKey(keyFile));
        byte[] encryptedTextBytes = cipher.doFinal(plaintext.getBytes("UTF-8"));
        return BaseEncoding.base64().encode(encryptedTextBytes);
      } else
      {
        throw new InvalidKeySpecException(EncryptionConstants.KEY_NOT_FOUND);
      }
    } catch (InvalidKeySpecException | IOException | NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e)
    {
      throw Throwables.propagate(e);

    }
  }

  /**
   * decrypt Array of Byte
   * @param encryptedMessage
   * @return array of Byte
   */
  @Override
  public byte[] decrypt(byte[] encryptedMessage)
  {
    try
    {
      File keyFile = null;
      try
      {
        keyFile = new File(keyfileStr);
      } catch (Exception e)
      {
        throw Throwables.propagate(e);
      }
      if (keyFile != null)
      {
        Cipher cipher = Cipher.getInstance(EncryptionConstants.cipherType);
        keyFile = new File(keyfileStr);
        cipher.init(Cipher.DECRYPT_MODE, EncryptionUtils.readKey(keyFile));

        byte[] decryptedTextBytes = cipher.doFinal(encryptedMessage);

        return decryptedTextBytes;
      } else
      {
        throw new InvalidKeySpecException(EncryptionConstants.KEY_NOT_FOUND);
      }
    } catch (InvalidKeySpecException | IOException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e)
    {
      throw Throwables.propagate(e);
    }
  }

  /**
   * decrypt Array of Byte
   * @param ciphertext
   * @return String
   */
  @Override
  public String decrypt(String ciphertext)
  {
    try
    {
      File keyFile = null;
      try
      {
        keyFile = new File(keyfileStr);
      } catch (Exception e)
      {
        throw Throwables.propagate(e);
      }
      if (keyFile != null)
      {
        Cipher cipher = Cipher.getInstance(EncryptionConstants.cipherType);
        cipher.init(Cipher.DECRYPT_MODE, EncryptionUtils.readKey(keyFile));

        byte[] encryptedTextBytes = BaseEncoding.base64().decode(ciphertext);
        byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

        return new String(decryptedTextBytes);
      } else
      {
        throw new InvalidKeySpecException(EncryptionConstants.KEY_NOT_FOUND);
      }
    } catch (InvalidKeySpecException | IOException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e)
    {
      throw Throwables.propagate(e);
    }
  }

  /**
   * Private key from keystore
   *
   * @return
   */
  private Key getPrivateKey()
  {
    try
    {
      KeyStore ks = KeyStore.getInstance(EncryptionConstants.keyStoreType);
      ks.load(new FileInputStream(getKeystoreName()), getKeystorePassword().toCharArray());
      Key key = ks.getKey(getAlias(), getKeyPassword().toCharArray());
      return key;
    } catch (NoSuchAlgorithmException | IOException | CertificateException | KeyStoreException | UnrecoverableKeyException e)
    {
      throw Throwables.propagate(e);

    }

  }

  public String getKeystoreName()
  {
    return keystoreName;
  }

  public String getKeystorePassword()
  {
    return keystorePassword;
  }

  public String getAlias()
  {
    return alias;
  }

  public String getKeyPassword()
  {
    return keyPassword;
  }

  public String getKeyfileStr()
  {
    return keyfileStr;
  }
}
