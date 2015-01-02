package com.gagauz.tracker.web.services.security;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class SecurityEncryptor {
    private static final String ALGORITHM = "AES";
    private final Cipher encrypt;
    private final Cipher decrypt;
    private static final String CH = "latin1";

    public SecurityEncryptor(String passphrase) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), "sdfadsfds".getBytes(CH), 65536, 128);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey key = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
            encrypt = Cipher.getInstance(ALGORITHM);
            encrypt.init(Cipher.ENCRYPT_MODE, key);

            decrypt = Cipher.getInstance(ALGORITHM);
            decrypt.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String valueToEnc) {
        try {
            byte[] encValue = encrypt.doFinal(valueToEnc.getBytes(CH));
            System.out.println(Base64.encodeBase64String(encValue));
            return Base64.encodeBase64String(encValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String encryptedValue) {
        try {
            byte[] decordedValue = Base64.decodeBase64(encryptedValue.getBytes(CH));
            byte[] decValue;
            decValue = decrypt.doFinal(decordedValue);
            return new String(decValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
