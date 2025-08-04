package org.k1den.configclientproject;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

public class JasyptEncryptionExample {

    public static void main(String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("client1");
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setIvGenerator(new RandomIvGenerator());

        String rawPassword = "client1";
        String encryptedPassword = encryptor.encrypt(rawPassword);

        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encrypted password: ENC(" + encryptedPassword + ")");

        String decryptedPassword = encryptor.decrypt(encryptedPassword);
        System.out.println("Decrypted password: " + decryptedPassword);
    }
}