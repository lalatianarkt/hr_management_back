package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Model.Poste;
import com.rh.manage.Repository.PosteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PosteService {

    @Autowired
    private PosteRepository posteRepository;

    // Récupérer tous les postes ordonnés par nom
    public List<Poste> getAllPostes() {
        return posteRepository.findAllByOrderByNom();
    }

    // Récupérer un poste par son ID
    public Optional<Poste> getPosteById(String id) {
        return posteRepository.findById(id);
    }

    // Récupérer un poste par son nom
    public Optional<Poste> getPosteByNom(String nom) {
        return posteRepository.findByNom(nom);
    }

    // Créer un nouveau poste
    public Poste createPoste(Poste poste) {
        // Vérifier si le poste existe déjà
        if (posteRepository.existsByNom(poste.getNom())) {
            throw new RuntimeException("Un poste avec le nom '" + poste.getNom() + "' existe déjà");
        }
        
        // Définir la date de création
        poste.setCreatedAt(LocalDateTime.now());
        
        return posteRepository.save(poste);
    }

    // Mettre à jour un poste existant
    public Poste updatePoste(String id, Poste posteDetails) {
        Optional<Poste> posteOptional = posteRepository.findById(id);
        
        if (posteOptional.isPresent()) {
            Poste poste = posteOptional.get();
            
            // Vérifier si le nouveau nom n'est pas déjà utilisé par un autre poste
            if (!poste.getNom().equals(posteDetails.getNom()) && 
                posteRepository.existsByNom(posteDetails.getNom())) {
                throw new RuntimeException("Un poste avec le nom '" + posteDetails.getNom() + "' existe déjà");
            }
            
            // Mettre à jour les champs
            poste.setNom(posteDetails.getNom());
            poste.setDescription(posteDetails.getDescription());
            poste.setModifiedAt(LocalDateTime.now());
            
            return posteRepository.save(poste);
        } else {
            throw new RuntimeException("Poste non trouvé avec l'ID: " + id);
        }
    }

    // Supprimer un poste
    public void deletePoste(String id) {
        Optional<Poste> poste = posteRepository.findById(id);
        
        if (poste.isPresent()) {
            // Vérifier s'il y a des employés associés à ce poste
            // (vous devrez implémenter cette vérification selon votre modèle)
            posteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Poste non trouvé avec l'ID: " + id);
        }
    }

    // Rechercher des postes
    public List<Poste> searchPostes(String searchTerm) {
        return posteRepository.searchPostes(searchTerm);
    }

    // Rechercher par nom
    public List<Poste> searchByNom(String nom) {
        return posteRepository.findByNomContainingIgnoreCase(nom);
    }

    // Rechercher par description
    public List<Poste> searchByDescription(String description) {
        return posteRepository.findByDescriptionContainingIgnoreCase(description);
    }

    // Vérifier si un poste existe
    public boolean posteExists(String nom) {
        return posteRepository.existsByNom(nom);
    }

    // Compter le nombre de postes
    public long countPostes() {
        return posteRepository.count();
    }

    // Récupérer les postes récents
    public List<Poste> getRecentPostes() {
        return posteRepository.findTop10ByOrderByCreatedAtDesc();
    }

    // Récupérer les postes créés après une date
    public List<Poste> getPostesAfterDate(LocalDateTime date) {
        return posteRepository.findByCreatedAtAfter(date);
    }

    public List<Poste> getPostesByDepartementId(String departementId) {
        // Vérifier d'abord si le département existe (optionnel)
        if (departementId == null || departementId.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID du département ne peut pas être null ou vide");
        }
        
        return posteRepository.findByDepartementId(departementId);
    }
    
    public boolean departementHasPostes(String departementId) {
        return posteRepository.existsByDepartementId(departementId);
    }

    public List<Poste> findByDepartementId(String departementId){
       return posteRepository.findByDepartementId(departementId);
    }

    public List<Poste> getPostesByIdNiveau(String idNiveau) {
        return posteRepository.findByNiveauHierarchique_Id(idNiveau);
    }
}
