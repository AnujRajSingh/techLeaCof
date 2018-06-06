package com.ulap.util.tecleaApi.security;

import com.google.common.base.Throwables;
import com.google.common.io.BaseEncoding;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Advanced symmetric Encryption implementation, This implementation uses key stored in the
 * java keystore and save in the trusted store of JRE
 */
public class ColumnEncryptorAes extends ColumnEncryptor implements Encryption {

  private SecretKeySpec secretKeySpec;
  private Cipher cipher;
  private IvParameterSpec iv;
  private static ColumnEncryptorAes instance;
  private static String secretKeyFile;
  private static String keystoreName;
  private static String keyStorepassword;
  private static String keypassword;
  private static String alias;


  /**
   * This method should be called to initialize the Jasypt encryption/decryption.
   */
  public static synchronized ColumnEncryptorAes getInstance(String secretKeyFile, String keystoreName, String keyStorepassword, String keypassword, String alias)
  {

    if (instance == null)
    {
      instance = new ColumnEncryptorAes( secretKeyFile,  keystoreName,  keyStorepassword,  keypassword,  alias);
    }
    return instance;
  }


  /**
   * Create AESCipher based on existing {@link Key}
   */
  public ColumnEncryptorAes(String secretKeyFile, String keystoreName, String keyStorepassword, String keypassword, String alias)
  {
    this.secretKeyFile = secretKeyFile;
    this.keystoreName = keystoreName;
    this.keyStorepassword = keyStorepassword;
    this.keypassword = keypassword;
    this.alias = alias;

  }

  /**
   * Create AESCipher based on existing {@link Key}
   */
  private ColumnEncryptorAes()
  {
    this(getSecretKey().getEncoded());
  }

  /**
   * Create AESCipher based on existing {@link Key} and Initial Vector (iv) in bytes
   *
   * @param key Key
   */

  private ColumnEncryptorAes(Key key, byte[] iv)
  {
    this(key.getEncoded(), iv);
  }

  /**
   * <p>Create AESCipher using a byte[] array as a key</p>
   * <p/>
   * <p><strong>NOTE:</strong> Uses an Initial Vector of 16 0x0 bytes.</p>
   */
  private ColumnEncryptorAes(byte[] key)
  {
    this(key, EncryptionConstants.INITIAL_IV);
  }

  private ColumnEncryptorAes(byte[] key, byte[] iv)
  {
    try
    {
      this.secretKeySpec = new SecretKeySpec(key, EncryptionConstants.SECRET_KEYALGO);
      this.iv = new IvParameterSpec(iv);
      this.cipher = Cipher.getInstance(EncryptionConstants.ALGORITHM_AES256);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e)
    {
      throw Throwables.propagate(e);
    }
  }

  /**
   * Takes message and encrypts with Key
   *
   * @param message String
   * @return String Base64 encoded
   */
  @Override
  public String encrypt(String message)
  {
    try
    {
      Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);

      byte[] encryptedTextBytes = cipher.doFinal(message.getBytes("UTF-8"));

      return BaseEncoding.base64().encode(encryptedTextBytes);
    } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeyException | InvalidAlgorithmParameterException e)
    {
      throw Throwables.propagate(e);
    }
  }

  /**
   * Takes Base64 encoded String and decodes with provided key
   *
   * @param message String encoded with Base64
   * @return String
   */
  @Override
  public String decrypt(String message)
  {
    try
    {
      Cipher cipher = getCipher(Cipher.DECRYPT_MODE);

      byte[] encryptedTextBytes = BaseEncoding.base64().decode(message);
      byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

      return new String(decryptedTextBytes);
    } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e)
    {
      throw Throwables.propagate(e);
    }
  }

  /**
   * Takes message and encrypts with Key
   *
   * @param message String
   * @return String Base64 encoded
   */
  @Override
  public byte[] encrypt(byte[] message)
  {
    try
    {
      Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);

      byte[] encryptedTextBytes = cipher.doFinal(message);

      return encryptedTextBytes;
    } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e)
    {
      throw Throwables.propagate(e);
    }
  }

  /**
   * Takes Base64 encoded String and decodes with provided key
   *
   * @param message String encoded with Base64
   * @return String
   */
  @Override
  public byte[] decrypt(byte[] message)
  {
    try
    {
      Cipher cipher = getCipher(Cipher.DECRYPT_MODE);

      byte[] decryptedTextBytes = cipher.doFinal(message);

      return decryptedTextBytes;
    } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e)
    {
      throw Throwables.propagate(e);
    }
  }

  /**
   * Get IV in Base64 Encoded String
   *
   * @return String Base64 Encoded
   */
  public String getIV()
  {
    return BaseEncoding.base64().encode(iv.getIV());
  }


  private Cipher getCipher(int encryptMode) throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    cipher.init(encryptMode, getSecretKeySpec(), iv);
    return cipher;
  }

  private SecretKeySpec getSecretKeySpec()
  {
    return secretKeySpec;
  }

  /**
   * private key retireval
   *
   * @return
   */
  private Key getPrivateKey()
  {
    try
    {
      final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
      ks.load(new FileInputStream(getKeystoreName()), getKeyStorepassword().toCharArray());
      Key key = ks.getKey(getAlias(), getKeypassword().toCharArray());

      return key;
    } catch (NoSuchAlgorithmException | IOException | CertificateException | KeyStoreException | UnrecoverableKeyException e)
    {
      throw Throwables.propagate(e);

    }

  }

  /**
   * Used to get the public key
   *
   * @return
   * @throws NoSuchPaddingException
   * @throws NoSuchAlgorithmException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   * @throws IOException
   * @throws KeyStoreException
   * @throws CertificateException
   * @throws UnrecoverableKeyException
   * @throws InvalidKeyException
   * @throws InvalidAlgorithmParameterException
   */
  private static PublicKey getPublicKey() throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, IOException, KeyStoreException, CertificateException, UnrecoverableKeyException, InvalidKeyException, InvalidAlgorithmParameterException
  {
    FileInputStream publicKeyFile = new FileInputStream(
            getKeystoreName());
    KeyStore publicKeyStore = KeyStore.getInstance(KeyStore
                                                           .getDefaultType());
    publicKeyStore.load(publicKeyFile, getKeypassword().toCharArray());
    sun.security.x509.X509CertImpl publicCertificate = (sun.security.x509.X509CertImpl) publicKeyStore
                                                                                                .getCertificate(getAlias());
    PublicKey publicKey = publicCertificate.getPublicKey();
    return publicKey;
  }

  /**
   * @return
   */
  private static SecretKeySpec getSecretKey()
  {
    try
    {
      FileInputStream in = new FileInputStream(getSecretKeyFile());
      Cipher cipher = Cipher.getInstance(EncryptionConstants.ALGORITHM_RSA);
      cipher.init(Cipher.DECRYPT_MODE, getPublicKey());
      byte[] b = new byte[EncryptionConstants.AES_TYPE];
      in.read(b);
      byte[] keyb = cipher.doFinal(b);
      SecretKeySpec skey = new SecretKeySpec(keyb, EncryptionConstants.SECRET_KEYALGO);
      return skey;
    } catch (Exception e)
    {

    }
    return null;
  }


  /**
   * Creating Sysmetric key
   *
   * @param privateKey
   * @throws NoSuchPaddingException
   * @throws NoSuchAlgorithmException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   * @throws IOException
   * @throws KeyStoreException
   * @throws CertificateException
   * @throws UnrecoverableKeyException
   * @throws InvalidKeyException
   */
  private void createAesKey(Key privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, IOException, KeyStoreException, CertificateException, UnrecoverableKeyException, InvalidKeyException
  {

    KeyGenerator kgen = KeyGenerator.getInstance(EncryptionConstants.SECRET_KEYALGO);
    kgen.init(EncryptionConstants.AES_TYPE);
    SecretKey skey = kgen.generateKey();

    FileOutputStream out = new FileOutputStream(getSecretKeyFile());
    Cipher cipher = Cipher.getInstance(EncryptionConstants.ALGORITHM_RSA);
    cipher.init(Cipher.ENCRYPT_MODE, privateKey);
    byte[] b = cipher.doFinal(skey.getEncoded());
    out.write(b);
    out.close();

  }


  public static String getAlgorithmAes256()
  {
    return EncryptionConstants.ALGORITHM_AES256;
  }

  public static String getSecretKeyFile()
  {
    return secretKeyFile;
  }

  public static String getKeystoreName()
  {
    return keystoreName;
  }

  public static String getKeyStorepassword()
  {
    return keyStorepassword;
  }

  public static String getKeypassword()
  {
    return keypassword;
  }

  public static String getAlias()
  {
    return alias;
  }

}

enum KeyEncoding {

  BASE64, BASE32, HEX

}