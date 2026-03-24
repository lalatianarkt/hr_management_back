package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.RubriqueType;
import com.rh.manage.Repository.RubriqueTypeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RubriqueTypeService {
    
    @Autowired
    private RubriqueTypeRepository rubriqueTypeRepository;
    
    // Créer un nouveau type
    public RubriqueType create(RubriqueType rubriqueType) {
        // Vérifier si l'ID existe déjà
        if (rubriqueTypeRepository.existsById(rubriqueType.getId())) {
            throw new RuntimeException("Un type avec l'ID '" + rubriqueType.getId() + "' existe déjà");
        }
        
        // Vérifier si le libellé existe déjà
        if (rubriqueTypeRepository.existsByLibelleIgnoreCase(rubriqueType.getLibelle())) {
            throw new RuntimeException("Un type avec le libellé '" + rubriqueType.getLibelle() + "' existe déjà");
        }
        
        // Définir les dates
        rubriqueType.setCreatedAt(LocalDateTime.now());
        rubriqueType.setModifiedAt(LocalDateTime.now());
        
        return rubriqueTypeRepository.save(rubriqueType);
    }
    
    // Mettre à jour un type
    public RubriqueType update(String id, RubriqueType rubriqueTypeDetails) {
        return rubriqueTypeRepository.findById(id)
                .map(rubriqueType -> {
                    // Ne pas modifier l'ID (clé primaire)
                    
                    // Vérifier si le nouveau libellé existe déjà (pour un autre ID)
                    if (!rubriqueType.getLibelle().equalsIgnoreCase(rubriqueTypeDetails.getLibelle())) {
                        Optional<RubriqueType> existing = rubriqueTypeRepository
                                .findByLibelleIgnoreCase(rubriqueTypeDetails.getLibelle());
                        if (existing.isPresent() && !existing.get().getId().equals(id)) {
                            throw new RuntimeException("Un type avec le libellé '" + 
                                    rubriqueTypeDetails.getLibelle() + "' existe déjà");
                        }
                    }
                    
                    rubriqueType.setLibelle(rubriqueTypeDetails.getLibelle());
                    rubriqueType.setDescription(rubriqueTypeDetails.getDescription());
                    rubriqueType.setModifiedAt(LocalDateTime.now());
                    
                    return rubriqueTypeRepository.save(rubriqueType);
                })
                .orElseThrow(() -> new RuntimeException("Type de rubrique non trouvé avec id: " + id));
    }
    
    // Récupérer tous les types
    @Transactional(readOnly = true)
    public List<RubriqueType> getAll() {
        return rubriqueTypeRepository.findAllByOrderByIdAsc();
    }
    
    // Récupérer par ID
    @Transactional(readOnly = true)
    public Optional<RubriqueType> getById(String id) {
        return rubriqueTypeRepository.findById(id);
    }
    
    // Récupérer par libellé
    @Transactional(readOnly = true)
    public Optional<RubriqueType> getByLibelle(String libelle) {
        return rubriqueTypeRepository.findByLibelle(libelle);
    }
    
    // Supprimer un type
    public void delete(String id) {
        // Vérifier si le type est utilisé dans des rubriques
        if (isTypeUsed(id)) {
            throw new RuntimeException("Impossible de supprimer ce type car il est utilisé dans des rubriques");
        }
        
        rubriqueTypeRepository.deleteById(id);
    }
    
    // Vérifier si un type est utilisé
    @Transactional(readOnly = true)
    public boolean isTypeUsed(String id) {
        // Cette méthode vérifie si le type est utilisé dans la table rubrique_paie
        // Implémentation dépendante de votre structure
        // Pour l'instant, retourne false (à implémenter)
        return false;
    }
    
    // Rechercher par mot-clé
    @Transactional(readOnly = true)
    public List<RubriqueType> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return rubriqueTypeRepository.searchByKeyword(keyword.trim());
    }
    
    // Récupérer les types actuellement utilisés
    @Transactional(readOnly = true)
    public List<RubriqueType> getUsedTypes() {
        return rubriqueTypeRepository.findUsedTypes();
    }
    
    // Vérifier l'existence
    @Transactional(readOnly = true)
    public boolean exists(String id) {
        return rubriqueTypeRepository.existsById(id);
    }
    
    // Vérifier l'existence par libellé
    @Transactional(readOnly = true)
    public boolean existsByLibelle(String libelle) {
        return rubriqueTypeRepository.existsByLibelleIgnoreCase(libelle);
    }
    
    // Compter le nombre de types
    @Transactional(readOnly = true)
    public long count() {
        return rubriqueTypeRepository.count();
    }
    
    // Initialiser les données par défaut (pour Admin IT)
    public void initializeDefaultData() {
        // Vérifier si des données existent déjà
        if (rubriqueTypeRepository.count() > 0) {
            throw new RuntimeException("Des types de rubriques existent déjà. Initialisation annulée.");
        }
        
        // Créer les types par défaut
        List<RubriqueType> defaultTypes = List.of(
            new RubriqueType("GAIN", "Gain", "Element qui augmente le salaire"),
            new RubriqueType("RETENUE", "Retenue", "Element qui diminue le salaire"),
            new RubriqueType("TOTAL", "Total", "Total intermediaire ou final du bulletin"),
            new RubriqueType("EXCEPTIONNEL", "Exceptionnel", "Element occasionnel ou exceptionnel")
        );
        
        rubriqueTypeRepository.saveAll(defaultTypes);
    }
}
