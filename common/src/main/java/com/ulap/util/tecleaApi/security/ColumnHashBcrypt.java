package com.ulap.util.tecleaApi.security;

import java.util.function.Function;

/**
 * This class is used for create hash for user account password
 * This is only used in case of user account password hash creation
 * Different class is used for password encryption for the resources.
 */
public class ColumnHashBcrypt implements Hashing {


  private final int logRounds;
  private static  ColumnHashBcrypt instance;

  /**
   * This method should be called to initialize the Jasypt encryption/decryption.
   */
  public static synchronized ColumnHashBcrypt getInstance(int rounds)
  {
    if (instance == null)
    {
      instance = new ColumnHashBcrypt(rounds);
    }
    return instance;
  }

  public ColumnHashBcrypt(int logRounds)
  {
    this.logRounds = logRounds;
  }

  public String hash(String password)
  {
    return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
  }

  public boolean verifyHash(String password, String hash)
  {
    return BCrypt.checkpw(password, hash);
  }

  public boolean verifyAndUpdateHash(String password, String hash, Function<String, Boolean> updateFunc)
  {
    if (BCrypt.checkpw(password, hash))
    {
      int rounds = EncryptionUtils.getRounds(hash);
      // It might be smart to only allow increasing the rounds.
      // If someone makes a mistake the ability to undo it would be nice though.
      if (rounds != logRounds)
      {
        //log.debug("Updating password from {} rounds to {}", rounds, logRounds);
        String newHash = hash(password);
        return updateFunc.apply(newHash);
      }
      return true;
    }
    return false;
  }


}
