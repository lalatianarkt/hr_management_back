package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.JourTravail;
import com.rh.manage.Repository.JourTravailRepository;

import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;

@Service
public class JourTravailService {
    
    @Autowired
    private JourTravailRepository jourTravailRepository;
    
    // CRUD Operations
    
    @Transactional(readOnly = true)
    public List<JourTravail> getAllJoursTravail() {
        return jourTravailRepository.findAllByOrderByCodeJourAsc();
    }
    
    @Transactional(readOnly = true)
    public JourTravail getJourTravailById(Long id) {
        return jourTravailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Jour de travail non trouvé avec l'ID: " + id));
    }
    
    @Transactional(readOnly = true)
    public JourTravail getJourTravailByCode(Integer codeJour) {
        return jourTravailRepository.findByCodeJour(codeJour)
                .orElseThrow(() -> new EntityNotFoundException("Jour de travail non trouvé avec le code: " + codeJour));
    }
    
    @Transactional
    public JourTravail createJourTravail(JourTravail jourTravail) {
        // Validation du code jour (1-7)
        if (jourTravail.getCodeJour() < 1 || jourTravail.getCodeJour() > 7) {
            throw new IllegalArgumentException("Le code jour doit être entre 1 et 7");
        }
        
        // Validation du statut (0-4)
        if (jourTravail.getStatut() < 0 || jourTravail.getStatut() > 4) {
            throw new IllegalArgumentException("Le statut doit être entre 0 et 4");
        }
        
        // Validation de est_weekend (0 ou 1)
        if (jourTravail.getEstWeekend() != 0 && jourTravail.getEstWeekend() != 1) {
            throw new IllegalArgumentException("est_weekend doit être 0 ou 1");
        }
        
        // Vérifier si le code jour existe déjà
        if (jourTravailRepository.existsByCodeJour(jourTravail.getCodeJour())) {
            throw new IllegalArgumentException("Un jour de travail avec le code " + jourTravail.getCodeJour() + " existe déjà");
        }
        
        // Vérifier si le nom existe déjà
        if (jourTravailRepository.existsByNomJour(jourTravail.getNomJour())) {
            throw new IllegalArgumentException("Un jour de travail avec le nom '" + jourTravail.getNomJour() + "' existe déjà");
        }
        
        return jourTravailRepository.save(jourTravail);
    }
    
    @Transactional
    public JourTravail updateJourTravail(Long id, JourTravail jourTravailDetails) {
        JourTravail jourTravail = getJourTravailById(id);
        
        // Mettre à jour les champs
        if (jourTravailDetails.getNomJour() != null) {
            // Vérifier que le nouveau nom n'existe pas déjà
            Optional<JourTravail> existing = jourTravailRepository.findByNomJour(jourTravailDetails.getNomJour());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new IllegalArgumentException("Un autre jour avec ce nom existe déjà");
            }
            jourTravail.setNomJour(jourTravailDetails.getNomJour());
        }
        
        if (jourTravailDetails.getCodeJour() != null) {
            // Vérifier que le nouveau code n'existe pas déjà
            Optional<JourTravail> existing = jourTravailRepository.findByCodeJour(jourTravailDetails.getCodeJour());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new IllegalArgumentException("Un autre jour avec ce code existe déjà");
            }
            jourTravail.setCodeJour(jourTravailDetails.getCodeJour());
        }
        
        if (jourTravailDetails.getStatut() != null) {
            if (jourTravailDetails.getStatut() < 0 || jourTravailDetails.getStatut() > 4) {
                throw new IllegalArgumentException("Le statut doit être entre 0 et 4");
            }
            jourTravail.setStatut(jourTravailDetails.getStatut());
        }
        
        if (jourTravailDetails.getEstWeekend() != null) {
            if (jourTravailDetails.getEstWeekend() != 0 && jourTravailDetails.getEstWeekend() != 1) {
                throw new IllegalArgumentException("est_weekend doit être 0 ou 1");
            }
            jourTravail.setEstWeekend(jourTravailDetails.getEstWeekend());
        }
        
        return jourTravailRepository.save(jourTravail);
    }
    
    @Transactional
    public void deleteJourTravail(Long id) {
        if (!jourTravailRepository.existsById(id)) {
            throw new EntityNotFoundException("Jour de travail non trouvé avec l'ID: " + id);
        }
        jourTravailRepository.deleteById(id);
    }
    
    // Méthodes métier
    
    @Transactional(readOnly = true)
    public List<JourTravail> getJoursActifs() {
        return jourTravailRepository.findByStatut(1);
    }
    
    @Transactional(readOnly = true)
    public List<JourTravail> getJoursInactifs() {
        return jourTravailRepository.findByStatut(0);
    }
    
    @Transactional(readOnly = true)
    public List<JourTravail> getWeekends() {
        return jourTravailRepository.findByEstWeekend(1);
    }
    
    @Transactional(readOnly = true)
    public List<JourTravail> getJoursOuvrables() {
        return jourTravailRepository.findByStatutAndEstWeekend(1, 0);
    }
    
    @Transactional
    public void initialiserJoursSemaine() {
        // Vérifier si la table est vide
        if (jourTravailRepository.count() == 0) {
            // Créer les 7 jours de la semaine
            String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
            
            for (int i = 0; i < jours.length; i++) {
                int code = i + 1;
                int estWeekend = (code == 6 || code == 7) ? 1 : 0;
                int statut = (estWeekend == 1) ? 0 : 1; // Weekends inactifs par défaut
                
                JourTravail jour = new JourTravail();
                jour.setNomJour(jours[i]);
                jour.setCodeJour(code);
                jour.setStatut(statut);
                jour.setEstWeekend(estWeekend);
                
                jourTravailRepository.save(jour);
            }
        }
    }
    
    @Transactional(readOnly = true)
    public boolean isJourOuvrable(Integer codeJour) {
        Optional<JourTravail> jour = jourTravailRepository.findByCodeJour(codeJour);
        return jour.isPresent() && jour.get().getStatut() == 1 && jour.get().getEstWeekend() == 0;
    }

    public JourTravail getJourByNom(String nom){
        return jourTravailRepository.findByNomJour(nom).get();
    }
}
