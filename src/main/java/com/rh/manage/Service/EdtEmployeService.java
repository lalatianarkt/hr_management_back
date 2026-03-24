package com.rh.manage.Service;

import com.rh.manage.Model.EdtEmploye;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.HoraireJournalier;
import com.rh.manage.Model.JourTravail;
import com.rh.manage.Repository.EdtEmployeRepository;
import com.rh.manage.Repository.EmployeRepository;
import com.rh.manage.Repository.HoraireJournalierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EdtEmployeService {
    
    @Autowired
    private EdtEmployeRepository edtRepository;
    
    @Autowired
    private EmployeRepository employeRepository;
    
    @Autowired
    private HoraireJournalierRepository horaireRepository;

    @Autowired
    private DateService dateService;

    @Autowired
    private JourTravailService jourTravailService;
    
    // Créer un nouvel EDT
    public EdtEmploye createEdt(EdtEmploye edt) {
        String nom_jour = dateService.extractNomJourByDate(edt.getDateDuJour());
        JourTravail jour_travail = jourTravailService.getJourByNom(nom_jour);
        // edt.Set

        // Validation des données obligatoires
        if (edt.getDateDuJour() == null) {
            throw new IllegalArgumentException("La date du jour est obligatoire");
        }
        
        // Validation de l'employé
        if (edt.getEmploye() == null || edt.getEmploye().getId() == null) {
            throw new IllegalArgumentException("L'employé est obligatoire");
        }
        
        Employe employe = employeRepository.findById(edt.getEmploye().getId())
                .orElseThrow(() -> new IllegalArgumentException("Employé non trouvé avec ID: " + edt.getEmploye().getId()));
        edt.setEmploye(employe);
        
        // Validation de l'horaire (optionnel)
        if (edt.getHoraire() != null && edt.getHoraire().getId() != null) {
            HoraireJournalier horaire = horaireRepository.findById(edt.getHoraire().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Horaire non trouvé avec ID: " + edt.getHoraire().getId()));
            edt.setHoraire(horaire);
        }
        
        // Vérifier si un EDT existe déjà pour cet employé à cette date
        boolean exists = edtRepository.existsByEmployeAndDate(edt.getEmploye().getId(), edt.getDateDuJour());
        if (exists) {
            throw new IllegalArgumentException("Un EDT existe déjà pour cet employé à cette date");
        }
        
        // Vérifier que l'heure de fin est après l'heure de début si les deux sont fournies
        if (edt.getHeureDebut() != null && edt.getHeureFin() != null) {
            if (!edt.getHeureFin().isAfter(edt.getHeureDebut())) {
                throw new IllegalArgumentException("L'heure de fin doit être après l'heure de début");
            }
        }
        
        // Définir des valeurs par défaut si non fournies
        if (edt.getStatut() == null) {
            edt.setStatut(1); // Actif par défaut
        }
        
        if (edt.getIsShiftJour() == null) {
            edt.setIsShiftJour(true); // Shift jour par défaut
        }
        
        // Sauvegarde
        return edtRepository.save(edt);
    }
    
    // Mettre à jour un EDT
    public EdtEmploye updateEdt(String id, EdtEmploye edtDetails) {
        EdtEmploye edt = edtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EDT non trouvé avec ID: " + id));
        
        // Mise à jour des champs
        if (edtDetails.getDateDuJour() != null) {
            edt.setDateDuJour(edtDetails.getDateDuJour());
        }
        
        if (edtDetails.getHeureDebut() != null) {
            edt.setHeureDebut(edtDetails.getHeureDebut());
        }
        
        if (edtDetails.getHeureFin() != null) {
            edt.setHeureFin(edtDetails.getHeureFin());
        }
        
        // Vérifier que l'heure de fin est après l'heure de début si les deux sont fournies
        if (edt.getHeureDebut() != null && edt.getHeureFin() != null) {
            if (!edt.getHeureFin().isAfter(edt.getHeureDebut())) {
                throw new IllegalArgumentException("L'heure de fin doit être après l'heure de début");
            }
        }
        
        if (edtDetails.getStatut() != null) {
            edt.setStatut(edtDetails.getStatut());
        }
        
        if (edtDetails.getIsShiftJour() != null) {
            edt.setIsShiftJour(edtDetails.getIsShiftJour());
        }
        
        if (edtDetails.getEmploye() != null && edtDetails.getEmploye().getId() != null) {
            Employe employe = employeRepository.findById(edtDetails.getEmploye().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Employé non trouvé"));
            edt.setEmploye(employe);
        }
        
        if (edtDetails.getHoraire() != null && edtDetails.getHoraire().getId() != null) {
            HoraireJournalier horaire = horaireRepository.findById(edtDetails.getHoraire().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Horaire non trouvé"));
            edt.setHoraire(horaire);
        }
        
        // Le modifiedAt sera mis à jour automatiquement par @PreUpdate
        return edtRepository.save(edt);
    }
    
    // Récupérer un EDT par ID
    @Transactional(readOnly = true)
    public EdtEmploye getEdtById(String id) {
        return edtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EDT non trouvé avec ID: " + id));
    }
    
    // Récupérer un EDT par ID avec jointures
    @Transactional(readOnly = true)
    public EdtEmploye getEdtByIdWithEmploye(String id) {
        return edtRepository.findByIdWithEmploye(id)
                .orElseThrow(() -> new RuntimeException("EDT non trouvé avec ID: " + id));
    }
    
    // Récupérer tous les EDT
    @Transactional(readOnly = true)
    public List<EdtEmploye> getAllEdt() {
        return edtRepository.findAll();
    }
    
    // Récupérer les EDT actifs
    @Transactional(readOnly = true)
    public List<EdtEmploye> getEdtActifs() {
        return edtRepository.findByStatut(1);
    }
    
    // Récupérer les EDT par employé
    @Transactional(readOnly = true)
    public List<EdtEmploye> getEdtByEmploye(String employeId) {
        return edtRepository.findByEmployeId(employeId);
    }
    
    // Récupérer les EDT actifs par employé
    @Transactional(readOnly = true)
    public List<EdtEmploye> getEdtActifsByEmploye(String employeId) {
        return edtRepository.findByEmployeIdAndStatut(employeId, 1);
    }
    
    // Récupérer les EDT par date
    @Transactional(readOnly = true)
    public List<EdtEmploye> getEdtByDate(LocalDate date) {
        return edtRepository.findByDateDuJour(date);
    }
    
    // Récupérer les EDT actifs par date
    @Transactional(readOnly = true)
    public List<EdtEmploye> getEdtActifsByDate(LocalDate date) {
        return edtRepository.findByDateDuJourAndStatut(date, 1);
    }
    
    // Récupérer les EDT par employé et date
    @Transactional(readOnly = true)
    public List<EdtEmploye> getEdtByEmployeAndDate(String employeId, LocalDate date) {
        return edtRepository.findByEmployeIdAndDateDuJour(employeId, date);
    }
    
    // Récupérer l'EDT actif par employé et date
    @Transactional(readOnly = true)
    public EdtEmploye getEdtActifByEmployeAndDate(String employeId, LocalDate date) {
        return edtRepository.findByEmployeIdAndDateDuJourAndStatut(employeId, date, 1);
    }
    
    // Récupérer les EDT par plage de dates
    @Transactional(readOnly = true)
    public List<EdtEmploye> getEdtByDateRange(LocalDate startDate, LocalDate endDate) {
        return edtRepository.findByDateDuJourBetween(startDate, endDate);
    }
    
    // Récupérer les EDT actifs par plage de dates
    @Transactional(readOnly = true)
    public List<EdtEmploye> getEdtActifsByDateRange(LocalDate startDate, LocalDate endDate) {
        return edtRepository.findByDateDuJourBetweenAndStatut(startDate, endDate, 1);
    }
    
    // Récupérer les EDT par type de shift
    @Transactional(readOnly = true)
    public List<EdtEmploye> getEdtByShiftType(Boolean isShiftJour) {
        return edtRepository.findByIsShiftJour(isShiftJour);
    }
    
    // Récupérer les EDT actifs par type de shift
    @Transactional(readOnly = true)
    public List<EdtEmploye> getEdtActifsByShiftType(Boolean isShiftJour) {
        return edtRepository.findByIsShiftJourAndStatut(isShiftJour, 1);
    }
    
    // Supprimer un EDT (changement de statut)
    public void deleteEdt(String id) {
        EdtEmploye edt = edtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EDT non trouvé avec ID: " + id));
        
        // Soft delete: changement de statut
        edt.setStatut(0);
        edtRepository.save(edt);
    }
    
    // Supprimer définitivement un EDT
    public void deleteEdtPermanently(String id) {
        if (!edtRepository.existsById(id)) {
            throw new RuntimeException("EDT non trouvé avec ID: " + id);
        }
        
        edtRepository.deleteById(id);
    }
    
    // Vérifier si un EDT existe
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return edtRepository.existsById(id);
    }
    
    // Vérifier si un EDT actif existe pour un employé à une date
    @Transactional(readOnly = true)
    public boolean existsEdtActifForEmployeAndDate(String employeId, LocalDate date) {
        return edtRepository.existsByEmployeAndDate(employeId, date);
    }
    
    // Compter les EDT actifs par employé
    @Transactional(readOnly = true)
    public long countEdtActifsByEmploye(String employeId) {
        Long count = edtRepository.countEdtActifsByEmploye(employeId);
        return count != null ? count : 0;
    }
}