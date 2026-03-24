package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Model.Formateur;
import com.rh.manage.Repository.FormateurRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FormateurService {
    
    @Autowired
    private FormateurRepository formateurRepository;
    
    // Récupérer tous les formateurs
    public List<Formateur> getAllFormateurs() {
        return formateurRepository.findAll();
    }
    
    // Récupérer un formateur par son ID
    public Optional<Formateur> getFormateurById(String id) {
        return formateurRepository.findById(id);
    }
    
    // Créer un nouveau formateur
    public Formateur createFormateur(Formateur formateur) {
        formateur.setCreatedAt(LocalDateTime.now());
        return formateurRepository.save(formateur);
    }
    
    // Mettre à jour un formateur existant
    public Formateur updateFormateur(String id, Formateur formateurDetails) {
        Optional<Formateur> optionalFormateur = formateurRepository.findById(id);
        
        if (optionalFormateur.isPresent()) {
            Formateur formateur = optionalFormateur.get();
            
            formateur.setNom(formateurDetails.getNom());
            formateur.setEmail(formateurDetails.getEmail());
            formateur.setTelephone(formateurDetails.getTelephone());
            formateur.setSpecialite(formateurDetails.getSpecialite());
            formateur.setType(formateurDetails.getType());
            formateur.setModifiedAt(LocalDateTime.now());
            formateur.setIdEmploye(formateurDetails.getIdEmploye());
            
            return formateurRepository.save(formateur);
        } else {
            throw new RuntimeException("Formateur non trouvé avec l'id: " + id);
        }
    }
    
    // Supprimer un formateur
    public void deleteFormateur(String id) {
        if (formateurRepository.existsById(id)) {
            formateurRepository.deleteById(id);
        } else {
            throw new RuntimeException("Formateur non trouvé avec l'id: " + id);
        }
    }
    
    // Rechercher par email
    public Optional<Formateur> getFormateurByEmail(String email) {
        return formateurRepository.findByEmail(email);
    }
    
    // Rechercher par spécialité
    public List<Formateur> getFormateursBySpecialite(String specialite) {
        return formateurRepository.findBySpecialite(specialite);
    }
    
    // Rechercher par type
    public List<Formateur> getFormateursByType(Integer type) {
        return formateurRepository.findByType(type);
    }
    
    // Rechercher par nom (recherche partielle)
    public List<Formateur> searchFormateursByNom(String nom) {
        return formateurRepository.findByNomContainingIgnoreCase(nom);
    }
    
    // Vérifier l'existence d'un formateur
    public boolean formateurExists(String id) {
        return formateurRepository.existsById(id);
    }
    
    // Compter le nombre total de formateurs
    public long countFormateurs() {
        return formateurRepository.count();
    }
}
