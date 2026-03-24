package com.rh.manage.converter;

import com.rh.manage.Model.EtatCivil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EtatCivilConverter implements AttributeConverter<EtatCivil, String> {
    
    @Override
    public String convertToDatabaseColumn(EtatCivil etatCivil) {
        if (etatCivil == null) {
            return null;
        }
        // ✅ Retourne la valeur ENUM comme string PostgreSQL
        return etatCivil.name();
    }
    
    @Override
    public EtatCivil convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return EtatCivil.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            return EtatCivil.CELIBATAIRE;
        }
    }
}
