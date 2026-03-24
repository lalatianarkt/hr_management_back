package com.rh.manage.Service;

import com.rh.manage.Model.Competence;
import com.rh.manage.Model.Categorie;
import com.rh.manage.Repository.CompetenceRepository;
import com.rh.manage.Repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompetenceService {
    
    @Autowired
    private CompetenceRepository competenceRepository;
    
    @Autowired
    private CategorieRepository categorieRepository;
    
    // === CRUD BASIQUE ===
    
    public Competence create(Competence competence) {
        // Vérifier si le nom existe déjà
        if (competenceRepository.existsByNom(competence.getNom())) {
            throw new RuntimeException("Une compétence avec le nom '" + competence.getNom() + "' existe déjà");
        }
        
        // Vérifier que la catégorie existe
        if (competence.getCategorie() != null && competence.getCategorie().getId() != null) {
            Categorie categorie = categorieRepository.findById(competence.getCategorie().getId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + competence.getCategorie().getId()));
            competence.setCategorie(categorie);
        }
        
        // Définir les dates
        if (competence.getCreatedAt() == null) {
            competence.setCreatedAt(LocalDateTime.now());
        }
        
        return competenceRepository.save(competence);
    }
    
    public List<Competence> getAll() {
        return competenceRepository.findAllByOrderByNomAsc();
    }
    
    public Optional<Competence> getById(String id) {
        return competenceRepository.findById(id);
    }
    
    public Competence update(String id, Competence competenceDetails) {
        Competence competence = competenceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Compétence non trouvée avec l'ID: " + id));
        
        // Vérifier si le nom a changé et s'il existe déjà
        if (!competence.getNom().equals(competenceDetails.getNom()) && 
            competenceRepository.existsByNom(competenceDetails.getNom())) {
            throw new RuntimeException("Une compétence avec le nom '" + competenceDetails.getNom() + "' existe déjà");
        }
        
        // Mettre à jour les champs
        competence.setNom(competenceDetails.getNom());
        competence.setDescription(competenceDetails.getDescription());
        
        // Mettre à jour la catégorie si fournie
        if (competenceDetails.getCategorie() != null && competenceDetails.getCategorie().getId() != null) {
            Categorie categorie = categorieRepository.findById(competenceDetails.getCategorie().getId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + competenceDetails.getCategorie().getId()));
            competence.setCategorie(categorie);
        } else {
            competence.setCategorie(null);
        }
        
        competence.setModifiedAt(LocalDateTime.now());
        
        return competenceRepository.save(competence);
    }
    
    public void deleteById(String id) {
        if (!competenceRepository.existsById(id)) {
            throw new RuntimeException("Compétence non trouvée avec l'ID: " + id);
        }
        competenceRepository.deleteById(id);
    }
    
    // === MÉTHODES MÉTIER ===
    
    public boolean existsByNom(String nom) {
        return competenceRepository.existsByNom(nom);
    }
    
    public Optional<Competence> getByNom(String nom) {
        return competenceRepository.findByNom(nom);
    }
    
    public List<Competence> getByCategorie(String categorieId) {
        return competenceRepository.findByCategorieId(categorieId);
    }
    
    public List<Competence> search(String searchTerm) {
        return competenceRepository.searchByNomOrDescription(searchTerm);
    }
    
    public List<Competence> searchByCategorie(String searchTerm, String categorieId) {
        return competenceRepository.searchByNomOrDescriptionAndCategorie(searchTerm, categorieId);
    }
    
    public List<Competence> getWithoutCategorie() {
        return competenceRepository.findByCategorieIsNull();
    }
    
    public long count() {
        return competenceRepository.count();
    }
    
    public long countByCategorie(String categorieId) {
        return competenceRepository.countByCategorieId(categorieId);
    }
    
    // Initialiser des compétences par défaut
    public void initializeDefaultCompetences() {
        // Cette méthode peut être utilisée pour créer des compétences de base
        // selon les besoins de l'application
    }
}