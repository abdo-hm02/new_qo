package qoraa.net.common.crypt;

import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Implementation of the Encrypter interface using AES encryption in CBC mode.
 * Provides secure encryption and decryption of strings using:
 * - AES-256 encryption
 * - CBC mode with random IV
 * - PKCS5 padding
 * - URL-safe Base64 encoding
 */

@Getter
public class AESEncrypter implements Encrypter {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private final SecretKey secretKey;
    private final String algorithm;

    /**
     * Creates a new AESEncrypter with the provided secret key.
     * @param secretKey The AES secret key for encryption/decryption
     */

    public AESEncrypter(SecretKey secretKey) {
        this.secretKey = secretKey;
        this.algorithm = ALGORITHM;
    }

    /**
     * Encrypts a string value using AES encryption.
     * The process:
     * 1. Generates a random IV
     * 2. Encrypts the data using AES/CBC/PKCS5Padding
     * 3. Combines IV and encrypted data
     * 4. Encodes using URL-safe Base64
     *
     * @param value The string to encrypt
     * @return URL-safe Base64 encoded encrypted string
     * @throws EncryptionException if encryption fails
     */

    @Override
    public String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            byte[] iv = generateIV();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(combined);
        } catch (Exception e) {
            throw new EncryptionException("Encryption failed", e);
        }
    }

    /**
     * Decrypts an encrypted string value.
     * The process:
     * 1. Decodes from URL-safe Base64
     * 2. Extracts IV and encrypted data
     * 3. Decrypts using AES/CBC/PKCS5Padding
     *
     * @param encrypted The encrypted string to decrypt
     * @return Original decrypted string
     * @throws EncryptionException if decryption fails
     */

    @Override
    public String decrypt(String encrypted) {
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(encrypted);
            byte[] iv = new byte[16];
            byte[] content = new byte[decoded.length - 16];
            System.arraycopy(decoded, 0, iv, 0, 16);
            System.arraycopy(decoded, 16, content, 0, content.length);

            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] decrypted = cipher.doFinal(content);
            return new String(decrypted);
        } catch (Exception e) {
            throw new EncryptionException("Decryption failed", e);
        }
    }

    /**
     * Generates a random 16-byte Initialization Vector (IV).
     * @return Random IV bytes
     */

    private byte[] generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}
