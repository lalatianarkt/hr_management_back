package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.CategorieProfessionnelle;
import com.rh.manage.Repository.CategorieProfessionnelleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategorieProfessionnelleService {
    
    @Autowired
    private CategorieProfessionnelleRepository repository;
    
    // Créer une nouvelle catégorie
    public CategorieProfessionnelle create(CategorieProfessionnelle categorie) {
        if (categorie.getId() == null) {
            categorie.setId(java.util.UUID.randomUUID().toString());
        }
        return repository.save(categorie);
    }
    
    // Mettre à jour une catégorie
    public CategorieProfessionnelle update(String id, CategorieProfessionnelle categorieDetails) {
        CategorieProfessionnelle categorie = findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id: " + id));
        
        categorie.setCode(categorieDetails.getCode());
        categorie.setLibelle(categorieDetails.getLibelle());
        categorie.setDescription(categorieDetails.getDescription());
        categorie.setModifiedAt(LocalDateTime.now());
        
        return repository.save(categorie);
    }
    
    // Supprimer une catégorie
    public void delete(String id) {
        CategorieProfessionnelle categorie = findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id: " + id));
        repository.delete(categorie);
    }
    
    // Trouver par ID
    @Transactional(readOnly = true)
    public Optional<CategorieProfessionnelle> findById(String id) {
        return repository.findById(id);
    }
    
    // Trouver par code
    @Transactional(readOnly = true)
    public Optional<CategorieProfessionnelle> findByCode(String code) {
        return repository.findByCode(code);
    }
    
    // Lister toutes les catégories
    @Transactional(readOnly = true)
    public List<CategorieProfessionnelle> findAll() {
        return repository.findAll();
    }
    
    // Lister par ordre de création
    @Transactional(readOnly = true)
    public List<CategorieProfessionnelle> findAllOrderByCreatedAtDesc() {
        return repository.findAllByOrderByCreatedAtDesc();
    }
    
    // Rechercher par mot-clé
    @Transactional(readOnly = true)
    public List<CategorieProfessionnelle> search(String keyword) {
        return repository.searchByCodeOrLibelle(keyword);
    }
    
    // Vérifier si le code existe
    @Transactional(readOnly = true)
    public boolean codeExists(String code) {
        return repository.existsByCode(code);
    }
}
