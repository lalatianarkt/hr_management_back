package com.rh.manage.Service;

import org.springframework.stereotype.Service;

import com.rh.manage.Model.TypeDocument;
import com.rh.manage.Repository.TypeDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TypeDocumentService {
    
    @Autowired
    private TypeDocumentRepository typeDocumentRepository;
    
    // Créer un nouveau type de document
    public TypeDocument createTypeDocument(TypeDocument typeDocument) {
        // Vérifier si le type existe déjà
        if (typeDocumentRepository.existsByIntitule(typeDocument.getIntitule())) {
            throw new RuntimeException("Un type de document avec cet intitulé existe déjà");
        }
        
        // Générer un ID si non fourni
        if (typeDocument.getId() == null || typeDocument.getId().trim().isEmpty()) {
            String generatedId = "TYPEDOC-" + System.currentTimeMillis();
            typeDocument.setId(generatedId);
        }
        
        return typeDocumentRepository.save(typeDocument);
    }
    
    // Récupérer tous les types de documents
    public List<TypeDocument> getAllTypeDocuments() {
        return typeDocumentRepository.findAllByOrderByIntituleAsc();
    }
    
    // Récupérer un type par son ID
    public Optional<TypeDocument> getTypeDocumentById(String id) {
        return typeDocumentRepository.findById(id);
    }
    
    // Récupérer un type par son intitulé
    public Optional<TypeDocument> getTypeDocumentByIntitule(String intitule) {
        return typeDocumentRepository.findByIntitule(intitule);
    }
    
    // Rechercher des types par intitulé (recherche partielle)
    public List<TypeDocument> searchTypeDocuments(String keyword) {
        return typeDocumentRepository.findByIntituleContainingIgnoreCase(keyword);
    }
    
    // Mettre à jour un type de document
    public TypeDocument updateTypeDocument(String id, TypeDocument updatedTypeDocument) {
        return typeDocumentRepository.findById(id)
            .map(existingType -> {
                // Vérifier si le nouvel intitulé n'existe pas déjà (sauf pour le même document)
                if (!existingType.getIntitule().equals(updatedTypeDocument.getIntitule()) 
                    && typeDocumentRepository.existsByIntitule(updatedTypeDocument.getIntitule())) {
                    throw new RuntimeException("Un type de document avec cet intitulé existe déjà");
                }
                
                existingType.setIntitule(updatedTypeDocument.getIntitule());
                return typeDocumentRepository.save(existingType);
            })
            .orElseThrow(() -> new RuntimeException("Type de document non trouvé avec l'ID: " + id));
    }
    
    // Supprimer un type de document
    public void deleteTypeDocument(String id) {
        // Vérifier si le type existe
        if (!typeDocumentRepository.existsById(id)) {
            throw new RuntimeException("Type de document non trouvé avec l'ID: " + id);
        }
        
        // Vérifier si des documents utilisent ce type (à implémenter si nécessaire)
        // TypeDocument typeDocument = typeDocumentRepository.findById(id).get();
        // if (typeDocument.getDocuments() != null && !typeDocument.getDocuments().isEmpty()) {
        //     throw new RuntimeException("Impossible de supprimer ce type car il est utilisé par des documents");
        // }
        
        typeDocumentRepository.deleteById(id);
    }
    
    // Vérifier si un type existe
    public boolean typeDocumentExists(String id) {
        return typeDocumentRepository.existsById(id);
    }
    
    // Compter le nombre de types de documents
    public long countTypeDocuments() {
        return typeDocumentRepository.count();
    }
}
