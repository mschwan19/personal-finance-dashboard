package de.mschwan.finance_tracker_dashboard.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter
public class BigDecimalCryptoConverter implements AttributeConverter<BigDecimal, String> {

    private final StringCryptoConverter stringConverter = new StringCryptoConverter();

    @Override
    public String convertToDatabaseColumn(BigDecimal attribute) {
        if (attribute == null) return null;
        return stringConverter.convertToDatabaseColumn(attribute.toString());
    }

    @Override
    public BigDecimal convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String decryptedStr = stringConverter.convertToEntityAttribute(dbData);
        return new BigDecimal(decryptedStr);
    }
}