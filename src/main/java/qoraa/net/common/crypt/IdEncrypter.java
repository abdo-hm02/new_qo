package qoraa.net.common.crypt;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for encrypting and decrypting entity IDs.
 * Uses AES encryption with a standardized format to secure database IDs
 * when exposed through the API.
 *
 * The encryption process:
 * 1. Formats the ID using a standard pattern (Q{id}I)
 * 2. Encrypts the formatted string using AES-256
 * 3. Returns URL-safe Base64 encoded string
 */

@Slf4j
public final class IdEncrypter {
    private static final String ID_FORMAT = "Q%dI";
    private static final Pattern ID_PATTERN = Pattern.compile("Q(\\d+)I");
    private static final IdEncrypter INSTANCE = new IdEncrypter();
    private final Encrypter encrypter;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes AES encryption with a 256-bit key.
     */

    private IdEncrypter() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey secretKey = keyGenerator.generateKey();
            this.encrypter = new AESEncrypter(secretKey);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("Failed to initialize encryption", e);
        }
    }

    /**
     * Encrypts a numeric ID into a secure ticket string.
     * @param id The database ID to encrypt
     * @return Encrypted and encoded ticket string
     */

    public static String encryptGenericId(Long id) {
        return INSTANCE.encrypt(id);
    }

    /**
     * Decrypts a ticket string back to its original numeric ID.
     * @param encryptedId The encrypted ticket string
     * @return Original database ID
     * @throws EncryptionException if decryption fails or format is invalid
     */

    public static Long decryptGenericId(String encryptedId) {
        return INSTANCE.decrypt(encryptedId);
    }

    private String encrypt(Long id) {
        String formattedId = String.format(ID_FORMAT, id);
        return encrypter.encrypt(formattedId);
    }

    private Long decrypt(String encryptedId) {
        String decrypted = encrypter.decrypt(encryptedId);
        Matcher matcher = ID_PATTERN.matcher(decrypted);
        if (!matcher.matches()) {
            throw new EncryptionException("Invalid ID format");
        }
        return Long.parseLong(matcher.group(1));
    }
}
