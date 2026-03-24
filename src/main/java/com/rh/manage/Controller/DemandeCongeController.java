package com.rh.manage.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.rh.manage.Model.DemandeConge;
import com.rh.manage.Model.Employe;
import com.rh.manage.Service.DemandeCongeService;
import com.rh.manage.Service.JwtService;
import com.rh.manage.Service.ManagerService;

import io.jsonwebtoken.Claims;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/demandes-conge")
// @CrossOrigin(origins = "*")

public class DemandeCongeController {

    private final DemandeCongeService service;
    @Autowired
    private JwtService jwtService;

    public DemandeCongeController(DemandeCongeService service) {
        this.service = service;
    } 

    @GetMapping("/mes-demandes")
    public ResponseEntity<?> getDemandeEmploye(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Claims claims = jwtService.validateToken(token);
            String idEmploye = claims.get("idEmploye", String.class);
            
            List<DemandeConge> demandes = service.getDemandesParEmploye(idEmploye).get();
            
            if (demandes != null && !demandes.isEmpty()) {
                return ResponseEntity.ok(demandes);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucune demande trouvée pour l'employé: " + idEmploye);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de la récupération des demandes : " + e.getMessage());
        }
    }

    @GetMapping("/employe/{idEmploye}")
    public ResponseEntity<?> getDemandesParEmploye(@PathVariable String idEmploye) { 
        try {
            List<DemandeConge> demandes = service.getDemandesParEmploye(idEmploye).get(); 
            if (demandes != null && !demandes.isEmpty()) {
                return ResponseEntity.ok(demandes); 
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucune demande trouvée pour l'employé: " + idEmploye);
            } 
        
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de la récupération des demandes par employé : " + e.getMessage());
        }
    }

    @PutMapping("/annuler/{id}")
    public ResponseEntity<?> annulerDemande(@PathVariable String id, @RequestBody DemandeConge demande) {
       try {
           DemandeConge demandeConge = service.annulerDemande(id, demande);
           return ResponseEntity.ok(demandeConge);
       }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de l\'annulation de congé : " + e.getMessage());
       }
    }

    // /mouvements-solde/employe/${idEmploye}/solde-actuel
    @GetMapping("/valide")
    public ResponseEntity<?> getDemandesValideesParManager() {
        try {
            // Récupérer toutes les demandes validées par le manager
            List<DemandeConge> lesDemandes = service.findAllDemandeValidatedByManager();
            return ResponseEntity.ok(lesDemandes);

        } catch (Exception e) {
            // Log l'erreur et renvoyer un message lisible
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de la récupération des demandes validées : " + e.getMessage());
        }
    } 

    // @GetMapping("/demandeEnAttente")
    // public ResponseEntity<?> getDemandeEnAttente() {
    //     return new String();
    // }
    

    @GetMapping("/totalDemandeAnneeEnCours")
    public ResponseEntity<?> getTotalDemandes() {
        try {
            // Récupérer le total des demandes pour l'année en cours
            Long totalDemandes = service.getTotalDemandesAnneeEnCours();
            
            // Structure de réponse JSON
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Total des demandes récupéré avec succès");
            response.put("data", Map.of(
                "totalDemandes", totalDemandes,
                "annee", LocalDate.now().getYear(),
                "periode", "Année en cours (" + LocalDate.now().getYear() + ")",
                "dateCalcul", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // En cas d'erreur
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Erreur lors du calcul des demandes");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/ByManager")
    // @PreAuthorize("hasRole('MANAGER')") 
    public ResponseEntity<?> getDemandeCongeParManager(Authentication authentication) {
        try {
            String userId = (String) authentication.getPrincipal();
            List<DemandeConge> lesDemandeConges = service.getDemandeCongesParManagerByUserId(userId);
            return ResponseEntity.ok(lesDemandeConges);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", 401,
                            "message", e.getMessage()
                    ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of(
                    "status", 500,
                    "message", "Erreur interne du serveur: " + e.getMessage()
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DemandeConge demande, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Claims claims = jwtService.validateToken(token);
            String idEmploye = claims.get("idEmploye", String.class);
            demande.setDecisionManager(0);
            DemandeConge savedDemande = service.enregistrer(demande, idEmploye);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDemande);
        } catch (IllegalArgumentException e) {
            // Erreur de validation
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur de validation: " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            // Erreur d'intégrité des données (contrainte BD)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur d'intégrité des données: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la création: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur: " + e.getMessage());
        }
    }

    // Endpoint pour récupérer les demandes d'un employé pour le calendrier
    @GetMapping("/employe/{employeId}/calendrier")
    public ResponseEntity<?> getDemandesCalendrierEmploye(
            @PathVariable String employeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        try {
            // 1. Récupérer les demandes selon les paramètres
            List<DemandeConge> demandes;
            demandes = service.getDemandesByEmployeAndPeriode(employeId, dateDebut, dateFin);
            // if (dateDebut != null && dateFin != null) {
            //     // Avec dates spécifiques
            //     demandes = service.getDemandesByEmployeAndPeriode(employeId, dateDebut, dateFin);
            // } else {
            //     // Sans dates - utiliser la méthode existante ou nouvelle
            //     demandes = service.findByEmployeId(employeId);
            // }
            
            // 2. Formater la réponse (même format que /ByManager)
            List<Map<String, Object>> demandesFormatees = demandes.stream()
                .map(demande -> {
                    Map<String, Object> demandeMap = new HashMap<>();
                    demandeMap.put("id", demande.getId());
                    demandeMap.put("dateDebut", demande.getDateDebut());
                    demandeMap.put("dateFin", demande.getDateFin());
                    // demandeMap.put("statut", demande.getStatut());
                    demandeMap.put("dateDemande", demande.getDateDemande());
                    demandeMap.put("nbJours", demande.getNbJours());
                    demandeMap.put("commentaire", demande.getCommentaire());
                    demandeMap.put("typeConge", demande.getTypeConge());
                    
                    // Informations de l'employé
                    if (demande.getEmploye() != null) {
                        Map<String, Object> employeInfo = new HashMap<>();
                        employeInfo.put("id", demande.getEmploye().getId());
                        employeInfo.put("nom", demande.getEmploye().getNom());
                        employeInfo.put("prenom", demande.getEmploye().getPrenom());
                        employeInfo.put("email", demande.getEmploye().getEmail());
                        demandeMap.put("employe", employeInfo);
                    }
                    
                    // Décision manager
                    if (demande.getDecisionManager() != null) {
                        demandeMap.put("decisionManager", demande.getDecisionManager());
                        demandeMap.put("commentaireManager", demande.getCommentaireManager());
                    }
                    
                    return demandeMap;
                })
                .collect(Collectors.toList());
            
            // 3. Construire la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("status", 200);
            response.put("message", "Demandes récupérées avec succès");
            response.put("demandes", demandesFormatees);
            response.put("count", demandesFormatees.size());
            
            // 4. Ajouter les paramètres de période s'ils ont été fournis
            if (dateDebut != null && dateFin != null) {
                response.put("periode", Map.of(
                    "dateDebut", dateDebut,
                    "dateFin", dateFin
                ));
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "status", 500,
                    "message", "Erreur serveur: " + e.getMessage()
                ));
        }
    }

    @PutMapping("/validate-rh/{id}")
    public ResponseEntity<?> DemandeValidationRH(
        @PathVariable String id,
        @RequestParam(required = true) int idMouvement,
        @RequestBody DemandeConge demande){
        try {
            demande.setId(id);
            DemandeConge demandeConge = service.validateRHWithInsertionMouvement(demande, idMouvement);
            // DemandeConge demandeConge = service.validerDemandeRH(demande);
            return ResponseEntity.ok(demandeConge); // 200 OK 

        } catch (RuntimeException e) {
            // Erreur métier (ex: demande non trouvée)
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            // Erreur interne (ex: problème de base de données)
            return ResponseEntity.status(500)
                    .body("Erreur interne lors de la validation de la demande : " + e.getMessage());
        }
    }

    @PutMapping("validate/{id}")
    public ResponseEntity<?> validateDemandeConge(@PathVariable String id) {
        try {
            DemandeConge updated = service.validerDemande(id);
            return ResponseEntity.ok(updated); // 200 OK

        } catch (RuntimeException e) {
            // Erreur métier (ex: demande non trouvée)
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            // Erreur interne (ex: problème de base de données)
            return ResponseEntity.status(500)
                    .body("Erreur interne lors de la validation de la demande : " + e.getMessage());
        }
    }

    @PutMapping("refuser/{id}")
    public ResponseEntity<?> refuserDemandeConge(@PathVariable String id) {
        try {
            DemandeConge updated = service.refuserDemande(id);
            return ResponseEntity.ok(updated); // 200 OK

        } catch (RuntimeException e) {
            // Erreur métier (ex: demande non trouvée)
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            // Erreur interne (ex: problème de base de données)
            return ResponseEntity.status(500)
                    .body("Erreur interne lors de la validation de la demande : " + e.getMessage());
        }
    }

    // Nouvelle méthode avec pagination
    // @GetMapping("/paginated")
    // public ResponseEntity<Map<String, Object>> getAllPaginated(
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "10") int size,
    //         @RequestParam(defaultValue = "dateDemande") String sortBy,
    //         @RequestParam(defaultValue = "desc") String direction,
    //         @RequestParam(required = false) Integer statut) {
        
    //     Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    //     Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
    //     Page<DemandeConge> pageResult;
        
    //     if (statut != null) {
    //         pageResult = service.findByStatut(statut, pageable);
    //     } else {
    //         pageResult = service.findAll(pageable);
    //     }
        
    //     Map<String, Object> response = new HashMap<>();
    //     response.put("demandes", pageResult.getContent());
    //     response.put("currentPage", pageResult.getNumber());
    //     response.put("totalItems", pageResult.getTotalElements());
    //     response.put("totalPages", pageResult.getTotalPages());
        
    //     return ResponseEntity.ok(response);
    // } 

    // Endpoint de filtrage avec pagination
    @GetMapping("/filtre")
    public ResponseEntity<Map<String, Object>> filtrerDemandes(
            @RequestParam(required = false) Integer statut,
            @RequestParam(required = false) String idEmploye,
            @RequestParam(required = false) String typeConge,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(defaultValue = "30jours") String periode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateDemande") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? 
            Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        Page<DemandeConge> pageResult = service.filtrerDemandes(
            statut, idEmploye, typeConge, dateDebut, dateFin, periode, pageable
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("demandes", pageResult.getContent());
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        
        return ResponseEntity.ok(response);
    }

    // Endpoint de filtrage sans pagination (pour compatibilité)
    // @GetMapping("/filtre/liste")
    // public ResponseEntity<List<DemandeConge>> filtrerDemandesListe(
    //         @RequestParam(required = false) Integer statut,
    //         @RequestParam(required = false) String idEmploye,
    //         @RequestParam(required = false) String typeConge,
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
    //         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
    //         @RequestParam(defaultValue = "30jours") String periode) {
        
    //     List<DemandeConge> demandes = service.filtrerDemandes(
    //         statut, idEmploye, typeConge, dateDebut, dateFin, periode
    //     );
        
    //     return ResponseEntity.ok(demandes);
    // }

    // Gardez l'ancienne méthode pour la compatibilité
    @GetMapping
    public ResponseEntity<List<DemandeConge>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandeConge> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DemandeConge> update(@PathVariable String id, @RequestBody DemandeConge demande) {
        demande.setId(id);
        return ResponseEntity.ok(service.save(demande));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Méthode pour les statistiques (optionnel)
    // @GetMapping("/stats")
    // public ResponseEntity<Map<String, Object>> getStats() {
    //     Map<String, Object> stats = new HashMap<>();
    //     stats.put("totalDemandes", service.count());
    //     stats.put("demandesEnAttente", service.countByStatut(0));
    //     stats.put("demandesApprouvees", service.countByStatut(1));
    //     stats.put("demandesRejetees", service.countByStatut(2));
    //     return ResponseEntity.ok(stats);
    // }
}