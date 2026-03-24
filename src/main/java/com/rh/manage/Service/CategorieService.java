package com.rh.manage.Service;

import com.rh.manage.Model.Categorie;
import com.rh.manage.Repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategorieService {
    
    @Autowired
    private CategorieRepository categorieRepository;
    
    // === CRUD BASIQUE ===
    
    public Categorie create(Categorie categorie) {
        // Générer un ID si non fourni
        if (categorie.getId() == null || categorie.getId().isEmpty()) {
            categorie.setId("CAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        
        // Vérifier si le nom existe déjà
        if (categorieRepository.existsByNom(categorie.getNom())) {
            throw new RuntimeException("Une catégorie avec le nom '" + categorie.getNom() + "' existe déjà");
        }
        
        // Définir les dates
        categorie.setCreatedAt(LocalDateTime.now());
        
        return categorieRepository.save(categorie);
    }
    
    public List<Categorie> getAll() {
        return categorieRepository.findAllByOrderByNomAsc();
    }
    
    public Optional<Categorie> getById(String id) {
        return categorieRepository.findById(id);
    }
    
    public Categorie update(String id, Categorie categorieDetails) {
        Categorie categorie = categorieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + id));
        
        // Vérifier si le nom a changé et s'il existe déjà
        if (!categorie.getNom().equals(categorieDetails.getNom()) && 
            categorieRepository.existsByNom(categorieDetails.getNom())) {
            throw new RuntimeException("Une catégorie avec le nom '" + categorieDetails.getNom() + "' existe déjà");
        }
        
        // Mettre à jour les champs
        categorie.setNom(categorieDetails.getNom());
        categorie.setDescription(categorieDetails.getDescription());
        categorie.setCouleur(categorieDetails.getCouleur());
        categorie.setModifiedAt(LocalDateTime.now());
        
        return categorieRepository.save(categorie);
    }
    
    public void deleteById(String id) {
        if (!categorieRepository.existsById(id)) {
            throw new RuntimeException("Catégorie non trouvée avec l'ID: " + id);
        }
        categorieRepository.deleteById(id);
    }
    
    // === MÉTHODES MÉTIER ===
    
    public boolean existsByNom(String nom) {
        return categorieRepository.existsByNom(nom);
    }
    
    public Optional<Categorie> getByNom(String nom) {
        return categorieRepository.findByNom(nom);
    }
    
    public List<Categorie> search(String searchTerm) {
        return categorieRepository.searchByNomOrDescription(searchTerm);
    }
    
    public List<Categorie> getByCouleur(String couleur) {
        return categorieRepository.findByCouleur(couleur);
    }
    
    public long count() {
        return categorieRepository.count();
    }
    
    // Vérifier et créer des catégories par défaut
    public void initializeDefaultCategories() {
        String[][] defaultCategories = {
            {"Développement", "Compétences techniques de développement", "#1890ff"},
            {"Frontend", "Compétences interface utilisateur", "#52c41a"},
            {"Base de données", "Compétences gestion de données", "#faad14"},
            {"Bureautique", "Logiciels bureautiques", "#722ed1"},
            {"Métier", "Compétences spécifiques au métier", "#fa541c"},
            {"Soft Skills", "Compétences comportementales", "#13c2c2"},
            {"Management", "Compétences en management d'équipe", "#eb2f96"}
        };
        
        for (String[] categoryData : defaultCategories) {
            if (!existsByNom(categoryData[0])) {
                Categorie categorie = new Categorie();
                categorie.setNom(categoryData[0]);
                categorie.setDescription(categoryData[1]);
                categorie.setCouleur(categoryData[2]);
                create(categorie);
            }
        }
    }
}