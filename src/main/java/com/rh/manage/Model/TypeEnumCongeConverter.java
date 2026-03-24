package com.rh.manage.Model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypeEnumCongeConverter implements AttributeConverter<TypeEnumConge, String> {

    @Override
    public String convertToDatabaseColumn(TypeEnumConge attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public TypeEnumConge convertToEntityAttribute(String dbData) {
        return dbData != null ? TypeEnumConge.valueOf(dbData) : null;
    }
}
