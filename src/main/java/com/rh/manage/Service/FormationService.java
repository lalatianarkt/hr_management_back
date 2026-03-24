package com.rh.manage.Service;

import com.rh.manage.Model.Formation;
import com.rh.manage.Repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FormationService {
    
    @Autowired
    private FormationRepository formationRepository;
    
    // === CRUD BASIQUE ===
    
    public Formation create(Formation formation) {
        // Vérifier si le nom existe déjà
        if (formationRepository.existsByNom(formation.getNom())) {
            throw new RuntimeException("Une formation avec le nom '" + formation.getNom() + "' existe déjà");
        }
        
        // Valider les données
        if (formation.getDuree() != null && formation.getDuree() < 0) {
            throw new RuntimeException("La durée ne peut pas être négative");
        }
        
        if (formation.getCout() != null && formation.getCout().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Le coût ne peut pas être négatif");
        }
        
        // Les dates seront gérées par @PrePersist
        return formationRepository.save(formation);
    }
    
    public List<Formation> getAll() {
        return formationRepository.findAllByOrderByNomAsc();
    }
    
    public Optional<Formation> getById(String id) {
        return formationRepository.findById(id);
    }
    
    public Formation update(String id, Formation formationDetails) {
        Formation formation = formationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'ID: " + id));
        
        // Vérifier si le nom a changé et s'il existe déjà (sauf pour cette formation)
        if (!formation.getNom().equals(formationDetails.getNom()) && 
            formationRepository.existsByNom(formationDetails.getNom())) {
            throw new RuntimeException("Une formation avec le nom '" + formationDetails.getNom() + "' existe déjà");
        }
        
        // Valider les données
        if (formationDetails.getDuree() != null && formationDetails.getDuree() < 0) {
            throw new RuntimeException("La durée ne peut pas être négative");
        }
        
        if (formationDetails.getCout() != null && formationDetails.getCout().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Le coût ne peut pas être négatif");
        }
        
        // Mettre à jour les champs modifiables
        formation.setNom(formationDetails.getNom());
        formation.setDescription(formationDetails.getDescription());
        formation.setDuree(formationDetails.getDuree());
        formation.setCout(formationDetails.getCout());
        formation.setPrerequis(formationDetails.getPrerequis());
        formation.setObjectifs(formationDetails.getObjectifs());
        
        // La date modified_at sera mise à jour automatiquement par @PreUpdate
        return formationRepository.save(formation);
    }
    
    public void deleteById(String id) {
        if (!formationRepository.existsById(id)) {
            throw new RuntimeException("Formation non trouvée avec l'ID: " + id);
        }
        formationRepository.deleteById(id);
    }
    
    // === MÉTHODES MÉTIER ===
    
    public boolean existsByNom(String nom) {
        return formationRepository.existsByNom(nom);
    }
    
    public Optional<Formation> getByNom(String nom) {
        return formationRepository.findByNom(nom);
    }
    
    public List<Formation> search(String searchTerm) {
        return formationRepository.search(searchTerm);
    }
    
    public List<Formation> getByDureeMin(Integer dureeMin) {
        return formationRepository.findByDureeGreaterThanEqual(dureeMin);
    }
    
    public List<Formation> getByDureeMax(Integer dureeMax) {
        return formationRepository.findByDureeLessThanEqual(dureeMax);
    }
    
    public List<Formation> getByCoutMax(BigDecimal coutMax) {
        return formationRepository.findByCoutLessThanEqual(coutMax);
    }
    
    public List<Formation> getAvecPrerequis() {
        return formationRepository.findByPrerequisIsNotNull();
    }
    
    public List<Formation> getSansPrerequis() {
        return formationRepository.findByPrerequisIsNull();
    }
    
    public long count() {
        return formationRepository.count();
    }
    
    // Statistiques
    public BigDecimal getCoutMoyen() {
        List<Formation> formations = formationRepository.findAll();
        if (formations.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal total = formations.stream()
            .map(f -> f.getCout() != null ? f.getCout() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return total.divide(BigDecimal.valueOf(formations.size()), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    public Double getDureeMoyenne() {
        List<Formation> formations = formationRepository.findAll();
        if (formations.isEmpty()) {
            return 0.0;
        }
        
        return formations.stream()
            .mapToInt(f -> f.getDuree() != null ? f.getDuree() : 0)
            .average()
            .orElse(0.0);
    }
}