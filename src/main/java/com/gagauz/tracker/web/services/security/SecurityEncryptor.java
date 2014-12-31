package com.gagauz.tracker.web.services.security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;

public class SecurityEncryptor {
    private static final String ALGORITHM = "AES";
    private final Cipher encrypt;
    private final Cipher decrypt;

    public SecurityEncryptor(@Inject @Value("${" + SymbolConstants.HMAC_PASSPHRASE + "}") String passphrase)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException {
        Key key = new SecretKeySpec(passphrase.getBytes(), ALGORITHM);
        encrypt = Cipher.getInstance(ALGORITHM);
        encrypt.init(Cipher.ENCRYPT_MODE, key);

        decrypt = Cipher.getInstance(ALGORITHM);
        decrypt.init(Cipher.DECRYPT_MODE, key);
    }

    public String encrypt(String valueToEnc) {
        byte[] encValue;
        try {
            encValue = encrypt.doFinal(valueToEnc.getBytes());
            return Base64.encodeBase64String(encValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String encryptedValue) {
        byte[] decordedValue = Base64.decodeBase64(encryptedValue);
        byte[] decValue;
        try {
            decValue = decrypt.doFinal(decordedValue);
            return new String(decValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
