package qoraa.net.common.crypt;

public interface Encrypter {
    String encrypt(String value);
    String decrypt(String encrypted);
}
