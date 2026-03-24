package com.rh.manage.Service;

import com.rh.manage.Model.HoraireJournalier;
import com.rh.manage.Model.JourTravail;
import com.rh.manage.Repository.HoraireJournalierRepository;
import com.rh.manage.Repository.JourTravailRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class HoraireJournalierService {

    HoraireJournalierRepository horaireRepository;
    JourTravailRepository jourTravailRepository;

    // Créer un nouvel horaire
    public HoraireJournalier createHoraire(HoraireJournalier horaire) {
        // log.info("Création d'un nouvel horaire journalier");
        
        // Validation des données obligatoires
        if (horaire.getHeureDebut() == null || horaire.getHeureFin() == null) {
            throw new IllegalArgumentException("Les heures de début et de fin sont obligatoires");
        }
        
        // Vérifier que l'heure de fin est après l'heure de début
        if (!horaire.getHeureFin().isAfter(horaire.getHeureDebut())) {
            throw new IllegalArgumentException("L'heure de fin doit être après l'heure de début");
        }
        
        // Validation du jour de travail
        if (horaire.getJourTravail() != null && horaire.getJourTravail().getId() != null) {
            JourTravail jourTravail = jourTravailRepository.findById(horaire.getJourTravail().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Jour de travail non trouvé avec ID: " + horaire.getJourTravail().getId()));
            horaire.setJourTravail(jourTravail);
        }
        
        // Définir des valeurs par défaut si non fournies
        if (horaire.getStatut() == null) {
            horaire.setStatut(1); // Actif par défaut
        }
        
        if (horaire.getIsShiftJour() == null) {
            horaire.setIsShiftJour(true); // Shift jour par défaut
        }
        
        if (horaire.getDureePauseMinutes() == null) {
            horaire.setDureePauseMinutes(60); // 1 heure par défaut
        }
        
        if (horaire.getDureeJournaliereAttendue() == null) {
            // Calculer automatiquement à partir des heures
            long dureeMinutes = horaire.calculerDureePresenceMinutes();
            horaire.setDureeJournaliereAttendue((int) dureeMinutes);
        }
        
        // Validation des durées
        if (horaire.getDureePauseMinutes() < 0) {
            throw new IllegalArgumentException("La durée de pause ne peut pas être négative");
        }
        
        if (horaire.getDureeJournaliereAttendue() <= 0) {
            throw new IllegalArgumentException("La durée journalière attendue doit être positive");
        }
        
        if (horaire.getDureePauseMinutes() > horaire.getDureeJournaliereAttendue()) {
            throw new IllegalArgumentException("La durée de pause ne peut pas dépasser la durée journalière attendue");
        }
        
        // Sauvegarde
        HoraireJournalier savedHoraire = horaireRepository.save(horaire);
        // log.info("Horaire créé avec ID: {}", savedHoraire.getId());
        
        return savedHoraire;
    }

    // Mettre à jour un horaire
    public HoraireJournalier updateHoraire(String id, HoraireJournalier horaireDetails) {
        // log.info("Mise à jour de l'horaire avec ID: {}", id);
        
        HoraireJournalier horaire = horaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horaire non trouvé avec ID: " + id));
        
        // Mise à jour des champs
        if (horaireDetails.getHeureDebut() != null) {
            horaire.setHeureDebut(horaireDetails.getHeureDebut());
        }
        
        if (horaireDetails.getHeureFin() != null) {
            horaire.setHeureFin(horaireDetails.getHeureFin());
        }
        
        // Vérifier que l'heure de fin est après l'heure de début
        if (horaire.getHeureDebut() != null && horaire.getHeureFin() != null) {
            if (!horaire.getHeureFin().isAfter(horaire.getHeureDebut())) {
                throw new IllegalArgumentException("L'heure de fin doit être après l'heure de début");
            }
        }
        
        if (horaireDetails.getDureePauseMinutes() != null) {
            horaire.setDureePauseMinutes(horaireDetails.getDureePauseMinutes());
        }
        
        if (horaireDetails.getDureeJournaliereAttendue() != null) {
            horaire.setDureeJournaliereAttendue(horaireDetails.getDureeJournaliereAttendue());
        }
        
        if (horaireDetails.getStatut() != null) {
            horaire.setStatut(horaireDetails.getStatut());
        }
        
        if (horaireDetails.getIsShiftJour() != null) {
            horaire.setIsShiftJour(horaireDetails.getIsShiftJour());
        }
        
        if (horaireDetails.getJourTravail() != null && horaireDetails.getJourTravail().getId() != null) {
            JourTravail jourTravail = jourTravailRepository.findById(horaireDetails.getJourTravail().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Jour de travail non trouvé"));
            horaire.setJourTravail(jourTravail);
        }
        
        // Validation des durées
        if (horaire.getDureePauseMinutes() != null && horaire.getDureePauseMinutes() < 0) {
            throw new IllegalArgumentException("La durée de pause ne peut pas être négative");
        }
        
        if (horaire.getDureeJournaliereAttendue() != null && horaire.getDureeJournaliereAttendue() <= 0) {
            throw new IllegalArgumentException("La durée journalière attendue doit être positive");
        }
        
        if (horaire.getDureePauseMinutes() != null && horaire.getDureeJournaliereAttendue() != null) {
            if (horaire.getDureePauseMinutes() > horaire.getDureeJournaliereAttendue()) {
                throw new IllegalArgumentException("La durée de pause ne peut pas dépasser la durée journalière attendue");
            }
        }
        
        // Le modifiedAt sera mis à jour automatiquement par @PreUpdate
        HoraireJournalier updatedHoraire = horaireRepository.save(horaire);
        // log.info("Horaire mis à jour avec ID: {}", id);
        
        return updatedHoraire;
    }

    // Récupérer un horaire par ID
    @Transactional(readOnly = true)
    public HoraireJournalier getHoraireById(String id) {
        // log.debug("Récupération de l'horaire avec ID: {}", id);
        
        return horaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horaire non trouvé avec ID: " + id));
    }

    // Récupérer tous les horaires
    @Transactional(readOnly = true)
    public List<HoraireJournalier> getAllHoraires() {
        // log.debug("Récupération de tous les horaires");
        return horaireRepository.findAll();
    }

    // Récupérer les horaires actifs
    @Transactional(readOnly = true)
    public List<HoraireJournalier> getHorairesActifs() {
        // log.debug("Récupération des horaires actifs");
        return horaireRepository.findByStatut(1);
    }

    // Récupérer les horaires par jour de travail
    @Transactional(readOnly = true)
    public List<HoraireJournalier> getHorairesByJourTravail(Integer jourTravailId) {
        // log.debug("Récupération des horaires pour le jour de travail ID: {}", jourTravailId);
        return horaireRepository.findByJourTravailId(jourTravailId);
    }

    // Récupérer les horaires actifs par jour de travail
    @Transactional(readOnly = true)
    public List<HoraireJournalier> getHorairesActifsByJourTravail(Integer jourTravailId) {
        // log.debug("Récupération des horaires actifs pour le jour de travail ID: {}", jourTravailId);
        return horaireRepository.findByJourTravailIdAndStatut(jourTravailId, 1);
    }

    // Récupérer les horaires par type de shift
    @Transactional(readOnly = true)
    public List<HoraireJournalier> getHorairesByShiftType(Boolean isShiftJour) {
        // log.debug("Récupération des horaires pour shift jour: {}", isShiftJour);
        return horaireRepository.findByIsShiftJour(isShiftJour);
    }

    // Récupérer les horaires actifs par type de shift
    @Transactional(readOnly = true)
    public List<HoraireJournalier> getHorairesActifsByShiftType(Boolean isShiftJour) {
        // log.debug("Récupération des horaires actifs pour shift jour: {}", isShiftJour);
        return horaireRepository.findByIsShiftJourAndStatut(isShiftJour, 1);
    }

    // Supprimer un horaire (changement de statut)
    public void deleteHoraire(String id) {
        // log.info("Suppression (désactivation) de l'horaire avec ID: {}", id);
        
        HoraireJournalier horaire = horaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horaire non trouvé avec ID: " + id));
        
        // Soft delete: changement de statut
        horaire.setStatut(0);
        horaireRepository.save(horaire);
        
        // log.info("Horaire désactivé avec ID: {}", id);
    }

    // Supprimer définitivement un horaire
    public void deleteHorairePermanently(String id) {
        // log.warn("Suppression permanente de l'horaire avec ID: {}", id);
        
        if (!horaireRepository.existsById(id)) {
            throw new RuntimeException("Horaire non trouvé avec ID: " + id);
        }
        
        horaireRepository.deleteById(id);
        // log.info("Horaire supprimé définitivement avec ID: {}", id);
    }

    // Vérifier si un horaire existe
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return horaireRepository.existsById(id);
    }

}