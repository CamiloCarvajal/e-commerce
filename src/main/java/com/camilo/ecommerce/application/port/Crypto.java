package com.camilo.ecommerce.application.port;

public interface Crypto {

    String hash(String text);
    String encode(String text);
    String decode(String text);
    String encodePassword(String text);
    boolean validatePassword(String passwordRaw, String passwordEncoded);

}
