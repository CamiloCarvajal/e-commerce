package com.camilo.ecommerce.infraestructure.driven_adapters.crypto;

import com.camilo.ecommerce.application.port.Crypto;

import java.util.HexFormat;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Component
public class CryptoRepository implements Crypto {

    // Always return the same encrypt for a text (A hash)
    private final String HASH_SALT;

    // Encrypt / Decrypt
    private final TextEncryptor textEncryptor;

    // For check the passwords
    private final BCryptPasswordEncoder passwordEncoder;

    public CryptoRepository(
    @Value("${spring.encryption.password}") String encryptionPassword,
    @Value("${spring.encryption.salt}") String salt) {

        // Convert string to byte array using a specific charset (e.g., UTF-8)
        byte[] bytes = salt.getBytes(StandardCharsets.UTF_8);

        // Format the byte array to a hexadecimal string
        String hexSalt = HexFormat.of().formatHex(bytes);

        this.HASH_SALT = salt;
        // Configure encrypter
        this.textEncryptor = Encryptors.delux(encryptionPassword, hexSalt);
        // Configure encoder for passwords
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /** TEXT  **/

    /**
     * Hash a text. It returns the same hashed value for the same text.
     * @param text Text to hash.
     * @return Text hashed.
     * @throws Error Error generating salt.
     *
     */
    @Override
    public String hash(String text) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Add a value static for salt
            String textWithSalt = text + HASH_SALT;
            byte[] hashBytes = digest.digest(textWithSalt.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
            throw new Error("Error generating hash", e);
        }
    }

    // Encode a text
    @Override
    public String encode(String text) {
        return textEncryptor.encrypt(text);
    }

    // Decode a text
    @Override
    public String decode(String text) {
        return textEncryptor.decrypt(text);
    }


    /** PASSWORD **/

    // Encode the password (Different password any time)
    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // Verify if a password match with the stored password
    @Override
    public boolean validatePassword(String passwordRaw, String passwordEncoded) {
        return passwordEncoder.matches(passwordRaw, passwordEncoded);
    }

}