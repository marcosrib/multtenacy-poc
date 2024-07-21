package com.example.demo;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class Cryptography {

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";


    private Cryptography() {
    }

    // Gera uma nova chave
    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE);
        return encodeKey(keyGen.generateKey());
    }

    // Converte a chave para String Base64
    public static String encodeKey(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // Converte a String Base64 de volta para SecretKey
    public static SecretKey decodeKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    // Criptografa um texto
    public static String encrypt(String plainText, String keyString) throws Exception {
        SecretKey key = decodeKey(keyString);
        IvParameterSpec iv = generateIv();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        byte[] ivAndEncryptedBytes = new byte[iv.getIV().length + encryptedBytes.length];
        System.arraycopy(iv.getIV(), 0, ivAndEncryptedBytes, 0, iv.getIV().length);
        System.arraycopy(encryptedBytes, 0, ivAndEncryptedBytes, iv.getIV().length, encryptedBytes.length);
        return Base64.getEncoder().encodeToString(ivAndEncryptedBytes);
    }

    // Descriptografa um texto
    public static String decrypt(String encryptedText, String keyString) throws Exception {
        SecretKey key = decodeKey(keyString);
        byte[] ivAndEncryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] ivBytes = new byte[16];
        byte[] encryptedBytes = new byte[ivAndEncryptedBytes.length - 16];
        System.arraycopy(ivAndEncryptedBytes, 0, ivBytes, 0, ivBytes.length);
        System.arraycopy(ivAndEncryptedBytes, 16, encryptedBytes, 0, encryptedBytes.length);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
