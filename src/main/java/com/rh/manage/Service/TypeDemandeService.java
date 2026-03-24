package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.TypeDemande;
import com.rh.manage.Repository.TypeDemandeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TypeDemandeService {
    
    @Autowired
    private TypeDemandeRepository typeDemandeRepository;
    
    /**
     * Créer un nouveau type de demande
     */
    @Transactional
    public TypeDemande createTypeDemande(TypeDemande typeDemande) {
        // Validation
        validateTypeDemande(typeDemande);
        
        // Vérifier l'unicité
        if (typeDemandeRepository.existsByType(typeDemande.getType())) {
            throw new IllegalArgumentException("Un type de demande avec ce nom existe déjà");
        }
        
        // Générer ID si non fourni
        if (typeDemande.getId() == null || typeDemande.getId().trim().isEmpty()) {
            typeDemande.setId(UUID.randomUUID().toString());
        }
        
        // Dates
        LocalDateTime now = LocalDateTime.now();
        typeDemande.setCreatedAt(now);
        typeDemande.setModifiedAt(now);
        
        return typeDemandeRepository.save(typeDemande);
    }
    
    /**
     * Mettre à jour un type de demande
     */
    @Transactional
    public TypeDemande updateTypeDemande(String id, TypeDemande typeDemandeDetails) {
        TypeDemande typeDemande = getTypeDemandeById(id);
        
        // Validation
        validateTypeDemande(typeDemandeDetails);
        
        // Vérifier l'unicité (excluant l'ID courant)
        if (typeDemandeRepository.existsByTypeAndIdNot(typeDemandeDetails.getType(), id)) {
            throw new IllegalArgumentException("Un autre type de demande avec ce nom existe déjà");
        }
        
        // Mettre à jour les champs
        typeDemande.setType(typeDemandeDetails.getType());
        typeDemande.setModifiedAt(LocalDateTime.now());
        
        return typeDemandeRepository.save(typeDemande);
    }
    
    /**
     * Supprimer un type de demande
     */
    @Transactional
    public void deleteTypeDemande(String id) {
        if (!typeDemandeRepository.existsById(id)) {
            throw new RuntimeException("Type de demande non trouvé avec ID: " + id);
        }
        typeDemandeRepository.deleteById(id);
    }
    
    /**
     * Obtenir un type de demande par son ID
     */
    public TypeDemande getTypeDemandeById(String id) {
        return typeDemandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type de demande non trouvé avec ID: " + id));
    }
    
    /**
     * Obtenir un type de demande par son nom
     */
    public TypeDemande getTypeDemandeByType(String type) {
        return typeDemandeRepository.findByType(type)
                .orElseThrow(() -> new RuntimeException("Type de demande non trouvé avec type: " + type));
    }
    
    /**
     * Obtenir tous les types de demande
     */
    public List<TypeDemande> getAllTypeDemandes() {
        return typeDemandeRepository.findAllByOrderByTypeAsc();
    }
    
    /**
     * Rechercher des types de demande par nom
     */
    public List<TypeDemande> searchByType(String searchTerm) {
        return typeDemandeRepository.findByTypeContaining(searchTerm);
    }
    
    /**
     * Valider les données d'un type de demande
     */
    private void validateTypeDemande(TypeDemande typeDemande) {
        if (typeDemande.getType() == null || typeDemande.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Le type est obligatoire");
        }
        
        if (typeDemande.getType().length() > 150) {
            throw new IllegalArgumentException("Le type ne peut pas dépasser 150 caractères");
        }
    }
}
