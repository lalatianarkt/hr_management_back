package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.CategorieRub;
import com.rh.manage.Repository.CategorieRubRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategorieRubService {
    
    @Autowired
    private CategorieRubRepository categorieRubRepository;
    
    // Créer une nouvelle catégorie
    public CategorieRub create(CategorieRub categorieRub) {
        // Vérifier si l'ID existe déjà
        if (categorieRubRepository.existsById(categorieRub.getId())) {
            throw new RuntimeException("Une catégorie avec l'ID '" + categorieRub.getId() + "' existe déjà");
        }
        
        // Vérifier si le libellé existe déjà
        if (categorieRubRepository.existsByLibelleIgnoreCase(categorieRub.getLibelle())) {
            throw new RuntimeException("Une catégorie avec le libellé '" + categorieRub.getLibelle() + "' existe déjà");
        }
        
        // Définir les dates
        categorieRub.setCreatedAt(LocalDateTime.now());
        categorieRub.setModifiedAt(LocalDateTime.now());
        
        return categorieRubRepository.save(categorieRub);
    }
    
    // Mettre à jour une catégorie
    public CategorieRub update(String id, CategorieRub categorieRubDetails) {
        return categorieRubRepository.findById(id)
                .map(categorieRub -> {
                    // Ne pas modifier l'ID (clé primaire)
                    
                    // Vérifier si le nouveau libellé existe déjà (pour un autre ID)
                    if (!categorieRub.getLibelle().equalsIgnoreCase(categorieRubDetails.getLibelle())) {
                        Optional<CategorieRub> existing = categorieRubRepository
                                .findByLibelleIgnoreCase(categorieRubDetails.getLibelle());
                        if (existing.isPresent() && !existing.get().getId().equals(id)) {
                            throw new RuntimeException("Une catégorie avec le libellé '" + 
                                    categorieRubDetails.getLibelle() + "' existe déjà");
                        }
                    }
                    
                    categorieRub.setLibelle(categorieRubDetails.getLibelle());
                    categorieRub.setModifiedAt(LocalDateTime.now());
                    
                    return categorieRubRepository.save(categorieRub);
                })
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec id: " + id));
    }
    
    // Récupérer toutes les catégories
    @Transactional(readOnly = true)
    public List<CategorieRub> getAll() {
        return categorieRubRepository.findAllByOrderByLibelleAsc();
    }
    
    // Récupérer par ID
    @Transactional(readOnly = true)
    public Optional<CategorieRub> getById(String id) {
        return categorieRubRepository.findById(id);
    }
    
    // Récupérer par libellé
    @Transactional(readOnly = true)
    public Optional<CategorieRub> getByLibelle(String libelle) {
        return categorieRubRepository.findByLibelle(libelle);
    }
    
    // Supprimer une catégorie
    public void delete(String id) {
        // Vérifier si la catégorie est utilisée dans des rubriques
        if (isCategorieUsed(id)) {
            throw new RuntimeException("Impossible de supprimer cette catégorie car elle est utilisée dans des rubriques");
        }
        
        categorieRubRepository.deleteById(id);
    }
    
    // Vérifier si une catégorie est utilisée
    @Transactional(readOnly = true)
    public boolean isCategorieUsed(String id) {
        List<CategorieRub> usedCategories = categorieRubRepository.findUsedCategories();
        return usedCategories.stream().anyMatch(categorie -> categorie.getId().equals(id));
    }
    
    // Rechercher par mot-clé
    @Transactional(readOnly = true)
    public List<CategorieRub> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return categorieRubRepository.searchByKeyword(keyword.trim());
    }
    
    // Récupérer les catégories utilisées
    @Transactional(readOnly = true)
    public List<CategorieRub> getUsedCategories() {
        return categorieRubRepository.findUsedCategories();
    }
    
    // Vérifier l'existence
    @Transactional(readOnly = true)
    public boolean exists(String id) {
        return categorieRubRepository.existsById(id);
    }
    
    // Vérifier l'existence par libellé
    @Transactional(readOnly = true)
    public boolean existsByLibelle(String libelle) {
        return categorieRubRepository.existsByLibelleIgnoreCase(libelle);
    }
    
    // Compter le nombre de catégories
    @Transactional(readOnly = true)
    public long count() {
        return categorieRubRepository.count();
    }
    
    // Initialiser les données par défaut (pour Admin IT)
    public void initializeDefaultData() {
        // Vérifier si des données existent déjà
        if (categorieRubRepository.count() > 0) {
            throw new RuntimeException("Des catégories existent déjà. Initialisation annulée.");
        }
        
        // Créer les catégories par défaut
        List<CategorieRub> defaultCategories = List.of(
            new CategorieRub("SALAIRE", "Salaire"),
            new CategorieRub("HEURES_SUP", "Heures supplementaires"),
            new CategorieRub("PRIME", "Prime"),
            new CategorieRub("INDEMNITE", "Indemnite"),
            new CategorieRub("COTISATION", "Cotisation sociale"),
            new CategorieRub("IMPOT", "Impot"),
            new CategorieRub("RETENUE_DIV", "Retenue diverse"),
            new CategorieRub("TOTAL_INT", "Total intermediaire")
        );
        
        categorieRubRepository.saveAll(defaultCategories);
    }
    
    // Récupérer les statistiques d'utilisation
    @Transactional(readOnly = true)
    public List<Object[]> getUsageStats() {
        return categorieRubRepository.countRubriquesByCategorie();
    }
    
    // Récupérer les catégories avec statistiques détaillées
    @Transactional(readOnly = true)
    public List<Object[]> getCategoriesWithUsageStats() {
        return categorieRubRepository.getCategoriesWithUsageStats();
    }
    
    // Récupérer les catégories pour un type spécifique de rubrique
    @Transactional(readOnly = true)
    public List<CategorieRub> getCategoriesByType(String typeId) {
        // Cette méthode nécessite une jointure avec rubrique_paie
        // Implémentation simplifiée pour l'instant
        return getAll();
    }
}
