package com.rh.manage.Service;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class FormuleIdGenerator {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public String generateNextId(String prefix) {
        // Récupérer le dernier ID depuis la base
        Query query = entityManager.createQuery(
            "SELECT MAX(f.id) FROM Formule f WHERE f.id LIKE 'FO-%'"
        );
        String lastId = (String) query.getSingleResult();
        
        if (lastId == null) {
            return prefix + "0001";
        }
        
        // Extraire le nombre et incrémenter
        String numberStr = lastId.substring(prefix.length());
        try {
            int number = Integer.parseInt(numberStr);
            number++;
            return prefix + String.format("%04d", number);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Format d'ID invalide: " + lastId);
        }
    }
}

