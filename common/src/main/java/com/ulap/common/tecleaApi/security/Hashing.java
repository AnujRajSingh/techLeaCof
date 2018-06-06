package com.ulap.util.tecleaApi.security;

import java.util.function.Function;

public interface Hashing {

  public String hash(String text);
  public boolean verifyHash(String password, String hash);
  public boolean verifyAndUpdateHash(String password, String hash, Function<String, Boolean> updateFunc);

}
