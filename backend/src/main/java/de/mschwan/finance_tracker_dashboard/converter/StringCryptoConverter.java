package de.mschwan.finance_tracker_dashboard.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
@Converter
public class StringCryptoConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES";
    private static byte[] KEY;

    @Value("${app.security.encryption.secret}")
    public void setKey(String secret) {
        KEY = secret.getBytes();
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, ALGORITHM));
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY, ALGORITHM));
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }
}