package com.rh.manage.Service;

import com.rh.manage.Model.SessionFormation;
import com.rh.manage.Repository.SessionFormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SessionFormationService {
    
    @Autowired
    private SessionFormationRepository sessionFormationRepository;
    
    // Récupérer toutes les sessions
    public List<SessionFormation> getAllSessions() {
        return sessionFormationRepository.findAll();
    }
    
    // Récupérer toutes les sessions avec détails
    public List<SessionFormation> getAllSessionsWithDetails() {
        return sessionFormationRepository.findAllWithDetails();
    }
    
    // Récupérer une session par son ID
    public Optional<SessionFormation> getSessionById(String id) {
        return sessionFormationRepository.findById(id);
    }
    
    // Récupérer une session par son ID avec détails
    @Transactional(readOnly = true)
    public Optional<SessionFormation> getSessionByIdWithDetails(String id) {
        Optional<SessionFormation> session = sessionFormationRepository.findById(id);
        if (session.isPresent()) {
            // Force l'initialisation des relations LAZY
            SessionFormation s = session.get();
            if (s.getFormation() != null) {
                s.getFormation().getNom(); // Force le chargement
            }
            if (s.getFormateur() != null) {
                s.getFormateur().getNom(); // Force le chargement
            }
        }
        return session;
    }
    
    // Créer une nouvelle session
    public SessionFormation createSession(SessionFormation session) {
        // Les callbacks @PrePersist gèrent l'ID et created_at automatiquement
        
        // Validation des données
        validateSession(session);
        
        return sessionFormationRepository.save(session);
    }
    
    // Mettre à jour une session existante
    public SessionFormation updateSession(String id, SessionFormation sessionDetails) {
        Optional<SessionFormation> optionalSession = sessionFormationRepository.findById(id);
        
        if (optionalSession.isPresent()) {
            SessionFormation session = optionalSession.get();
            
            // Mise à jour des champs
            session.setDateDebut(sessionDetails.getDateDebut());
            session.setDateFin(sessionDetails.getDateFin());
            session.setLieu(sessionDetails.getLieu());
            session.setStatut(sessionDetails.getStatut());
            session.setPlaceMax(sessionDetails.getPlaceMax());
            
            // Mise à jour des relations
            if (sessionDetails.getFormation() != null) {
                session.setFormation(sessionDetails.getFormation());
            }
            if (sessionDetails.getFormateur() != null) {
                session.setFormateur(sessionDetails.getFormateur());
            }
            
            // Validation
            validateSession(session);
            
            // @PreUpdate gère modified_at automatiquement
            return sessionFormationRepository.save(session);
        } else {
            throw new RuntimeException("Session de formation non trouvée avec l'id: " + id);
        }
    }
    
    // Supprimer une session
    public void deleteSession(String id) {
        if (sessionFormationRepository.existsById(id)) {
            sessionFormationRepository.deleteById(id);
        } else {
            throw new RuntimeException("Session de formation non trouvée avec l'id: " + id);
        }
    }
    
    // Validation des données de session
    private void validateSession(SessionFormation session) {
        if (session.getDateDebut() == null) {
            throw new IllegalArgumentException("La date de début est obligatoire");
        }
        
        if (session.getStatut() == null) {
            throw new IllegalArgumentException("Le statut est obligatoire");
        }
        
        if (session.getStatut() < 1 || session.getStatut() > 4) {
            throw new IllegalArgumentException("Le statut doit être entre 1 et 4");
        }
        
        // Validation des dates
        if (session.getDateFin() != null) {
            try {
                // Si dateFin est au format date, on peut valider
                LocalDate dateFin = LocalDate.parse(session.getDateFin());
                if (session.getDateDebut().isAfter(dateFin)) {
                    throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin");
                }
            } catch (Exception e) {
                // Si dateFin n'est pas au format date (texte libre), on ne valide pas la comparaison
            }
        }
        
        if (session.getPlaceMax() != null && session.getPlaceMax() < 0) {
            throw new IllegalArgumentException("Le nombre de places ne peut pas être négatif");
        }
    }
    
    // Rechercher par formation
    public List<SessionFormation> getSessionsByFormation(String formationId) {
        return sessionFormationRepository.findByFormationId(formationId);
    }
    
    // Rechercher par formateur
    public List<SessionFormation> getSessionsByFormateur(String formateurId) {
        return sessionFormationRepository.findByFormateurId(formateurId);
    }
    
    // Rechercher par statut
    public List<SessionFormation> getSessionsByStatut(Integer statut) {
        return sessionFormationRepository.findByStatut(statut);
    }
    
    // Rechercher par lieu
    public List<SessionFormation> searchSessionsByLieu(String lieu) {
        return sessionFormationRepository.findByLieuContainingIgnoreCase(lieu);
    }
    
    // Rechercher par nom de formation
    public List<SessionFormation> searchSessionsByNomFormation(String nomFormation) {
        return sessionFormationRepository.findByNomFormationContaining(nomFormation);
    }
    
    // Rechercher par nom de formateur
    public List<SessionFormation> searchSessionsByNomFormateur(String nomFormateur) {
        return sessionFormationRepository.findByNomFormateurContaining(nomFormateur);
    }
    
    // Rechercher les sessions à venir
    public List<SessionFormation> getSessionsAVenir() {
        return sessionFormationRepository.findByDateDebutAfter(LocalDate.now());
    }
    
    // Rechercher les sessions en cours
    public List<SessionFormation> getSessionsEnCours() {
        return sessionFormationRepository.findSessionsEnCours(LocalDate.now());
    }
    
    // Rechercher les sessions passées
    public List<SessionFormation> getSessionsPassees() {
        return sessionFormationRepository.findByDateDebutBefore(LocalDate.now());
    }
    
    // Rechercher les sessions avec places disponibles
    public List<SessionFormation> getSessionsWithAvailablePlaces() {
        return sessionFormationRepository.findSessionsWithAvailablePlaces();
    }
    
    // Vérifier la disponibilité d'un formateur
    public boolean isFormateurDisponible(String formateurId, LocalDate dateDebut, String dateFin) {
        return !sessionFormationRepository.isFormateurOccupe(formateurId, dateDebut, dateFin);
    }
    
    // Changer le statut d'une session
    public SessionFormation changeStatut(String id, Integer nouveauStatut) {
        Optional<SessionFormation> optionalSession = sessionFormationRepository.findById(id);
        
        if (optionalSession.isPresent()) {
            SessionFormation session = optionalSession.get();
            
            if (nouveauStatut < 1 || nouveauStatut > 4) {
                throw new IllegalArgumentException("Le statut doit être entre 1 et 4");
            }
            
            session.setStatut(nouveauStatut);
            // @PreUpdate gère modified_at automatiquement
            return sessionFormationRepository.save(session);
        } else {
            throw new RuntimeException("Session de formation non trouvée avec l'id: " + id);
        }
    }
    
    // Vérifier l'existence d'une session
    public boolean sessionExists(String id) {
        return sessionFormationRepository.existsById(id);
    }
    
    // Compter le nombre total de sessions
    public long countSessions() {
        return sessionFormationRepository.count();
    }
    
    // Récupérer les statistiques des sessions
    public List<Object[]> getStatistiquesSessions() {
        return sessionFormationRepository.countSessionsByStatut();
    }
    
    // Récupérer les statistiques mensuelles
    public List<Object[]> getStatistiquesMensuelles() {
        return sessionFormationRepository.findMonthlyStatistics();
    }
    
    // Rechercher les sessions récentes
    public List<SessionFormation> getSessionsRecentess(int jours) {
        LocalDateTime dateLimite = LocalDateTime.now().minusDays(jours);
        return sessionFormationRepository.findByCreatedAtAfter(dateLimite);
    }
}