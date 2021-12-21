package ew.quilt.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AES {

    public static byte[] encrypt(SecretKey secretKey, byte[] iv, String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] byteCipher = cipher.doFinal(message.getBytes("UTF-8"));
        return byteCipher;
    }

    public static byte[] decrypt(SecretKey secretKey, byte[] cipherText, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] decrypted = cipher.doFinal(cipherText);
        return decrypted;
    }

    public static void encryptFile(SecretKey secretKey, byte[] iv, File plainFile, File encryptedFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte buffer[] = new byte[4096];
        try (InputStream in = new FileInputStream(plainFile)) {
            try (OutputStream out = new FileOutputStream(encryptedFile)) {
                int readByte = in.read(buffer);
                while (readByte > 0) {
                    byte[] cipherByte = cipher.update(buffer, 0, readByte);
                    out.write(cipherByte);
                    readByte = in.read(buffer);
                }
                cipher.doFinal();
            }
        }
    }

    public static void decryptFile(SecretKey secretKey, byte[] iv, File cipherTextFile, File decryptedFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte buffer[] = new byte[4096];
        try (InputStream in = new FileInputStream(cipherTextFile)) {
            try (OutputStream out = new FileOutputStream(decryptedFile)) {
                int readByte = in.read(buffer);
                while (readByte > 0) {
                    byte[] decryptedByte = cipher.update(buffer, 0, readByte);
                    out.write(decryptedByte);
                    readByte = in.read(buffer);
                }
                cipher.doFinal();
            }
        }
    }
}
