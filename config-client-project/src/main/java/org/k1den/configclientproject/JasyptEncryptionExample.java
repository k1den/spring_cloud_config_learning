package org.k1den.configclientproject;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

public class JasyptEncryptionExample {

    public static void main(String[] args) {
        // Создаем encryptor с теми же параметрами, что и в вашем приложении
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("client1"); // Пароль для шифрования (должен совпадать с jasypt.encryptor.password)
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setIvGenerator(new RandomIvGenerator());

        // Пароль, который нужно зашифровать
        String rawPassword = "client1"; // Замените на реальный пароль для config server
        String encryptedPassword = encryptor.encrypt(rawPassword);

        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encrypted password: ENC(" + encryptedPassword + ")");

        // Для проверки - дешифруем обратно
        String decryptedPassword = encryptor.decrypt(encryptedPassword);
        System.out.println("Decrypted password: " + decryptedPassword);
    }
}