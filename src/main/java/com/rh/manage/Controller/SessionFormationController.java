package com.rh.manage.Controller;

import com.rh.manage.Model.SessionFormation;
import com.rh.manage.Service.SessionFormationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions-formation")
// @CrossOrigin(origins = "*")

public class SessionFormationController {
    
    @Autowired
    private SessionFormationService sessionFormationService;
    
    // GET - Récupérer toutes les sessions
    @GetMapping
    public ResponseEntity<List<SessionFormation>> getAllSessions() {
        try {
            List<SessionFormation> sessions = sessionFormationService.getAllSessionsWithDetails();
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Récupérer une session par son ID
    @GetMapping("/{id}")
    public ResponseEntity<SessionFormation> getSessionById(@PathVariable String id) {
        try {
            Optional<SessionFormation> session = sessionFormationService.getSessionByIdWithDetails(id);
            return session.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // POST - Créer une nouvelle session
    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody SessionFormation session) {
        try {
            System.out.println("=== CRÉATION SESSION ===");
            System.out.println("Session reçue: " + session);
            System.out.println("Formation: " + (session.getFormation() != null ? session.getFormation().getId() : "null"));
            System.out.println("Formateur: " + (session.getFormateur() != null ? session.getFormateur().getId() : "null"));
            
            SessionFormation nouvelleSession = sessionFormationService.createSession(session);
            System.out.println("✅ Session créée: " + nouvelleSession.getId());
            
            return new ResponseEntity<>(nouvelleSession, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erreur validation: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("❌ Erreur interne: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors de la création de la session: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // PUT - Mettre à jour une session existante
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSession(@PathVariable String id, @RequestBody SessionFormation session) {
        try {
            SessionFormation sessionMaj = sessionFormationService.updateSession(id, session);
            return new ResponseEntity<>(sessionMaj, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la modification de la session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // DELETE - Supprimer une session
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteSession(@PathVariable String id) {
        try {
            sessionFormationService.deleteSession(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Rechercher par formation
    @GetMapping("/formation/{formationId}")
    public ResponseEntity<List<SessionFormation>> getSessionsByFormation(@PathVariable String formationId) {
        try {
            List<SessionFormation> sessions = sessionFormationService.getSessionsByFormation(formationId);
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Rechercher par formateur
    @GetMapping("/formateur/{formateurId}")
    public ResponseEntity<List<SessionFormation>> getSessionsByFormateur(@PathVariable String formateurId) {
        try {
            List<SessionFormation> sessions = sessionFormationService.getSessionsByFormateur(formateurId);
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Rechercher par statut
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<SessionFormation>> getSessionsByStatut(@PathVariable Integer statut) {
        try {
            List<SessionFormation> sessions = sessionFormationService.getSessionsByStatut(statut);
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Rechercher par lieu
    @GetMapping("/recherche/lieu")
    public ResponseEntity<List<SessionFormation>> searchSessionsByLieu(@RequestParam String lieu) {
        try {
            List<SessionFormation> sessions = sessionFormationService.searchSessionsByLieu(lieu);
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Rechercher par nom de formation
    @GetMapping("/recherche/formation")
    public ResponseEntity<List<SessionFormation>> searchSessionsByNomFormation(@RequestParam String nom) {
        try {
            List<SessionFormation> sessions = sessionFormationService.searchSessionsByNomFormation(nom);
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Rechercher par nom de formateur
    @GetMapping("/recherche/formateur")
    public ResponseEntity<List<SessionFormation>> searchSessionsByNomFormateur(@RequestParam String nom) {
        try {
            List<SessionFormation> sessions = sessionFormationService.searchSessionsByNomFormateur(nom);
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Sessions à venir
    @GetMapping("/a-venir")
    public ResponseEntity<List<SessionFormation>> getSessionsAVenir() {
        try {
            List<SessionFormation> sessions = sessionFormationService.getSessionsAVenir();
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Sessions en cours
    @GetMapping("/en-cours")
    public ResponseEntity<List<SessionFormation>> getSessionsEnCours() {
        try {
            List<SessionFormation> sessions = sessionFormationService.getSessionsEnCours();
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Sessions passées
    @GetMapping("/passees")
    public ResponseEntity<List<SessionFormation>> getSessionsPassees() {
        try {
            List<SessionFormation> sessions = sessionFormationService.getSessionsPassees();
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Sessions avec places disponibles
    @GetMapping("/places-disponibles")
    public ResponseEntity<List<SessionFormation>> getSessionsWithAvailablePlaces() {
        try {
            List<SessionFormation> sessions = sessionFormationService.getSessionsWithAvailablePlaces();
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Vérifier disponibilité formateur
    @GetMapping("/disponibilite-formateur")
    public ResponseEntity<Boolean> checkFormateurDisponibility(
            @RequestParam String formateurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam String dateFin) {
        try {
            boolean disponible = sessionFormationService.isFormateurDisponible(formateurId, dateDebut, dateFin);
            return new ResponseEntity<>(disponible, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // PATCH - Changer le statut d'une session
    @PatchMapping("/{id}/statut")
    public ResponseEntity<SessionFormation> changeStatut(@PathVariable String id, @RequestParam Integer statut) {
        try {
            SessionFormation session = sessionFormationService.changeStatut(id, statut);
            return new ResponseEntity<>(session, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }  catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Compter le nombre de sessions
    @GetMapping("/count")
    public ResponseEntity<Long> countSessions() {
        try {
            long count = sessionFormationService.countSessions();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Statistiques des sessions
    @GetMapping("/statistiques")
    public ResponseEntity<List<Object[]>> getStatistiquesSessions() {
        try {
            List<Object[]> statistiques = sessionFormationService.getStatistiquesSessions();
            return new ResponseEntity<>(statistiques, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Sessions récentes
    @GetMapping("/recentes")
    public ResponseEntity<List<SessionFormation>> getSessionsRecentess(@RequestParam(defaultValue = "7") int jours) {
        try {
            List<SessionFormation> sessions = sessionFormationService.getSessionsRecentess(jours);
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}