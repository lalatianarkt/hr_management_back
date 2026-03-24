package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.ReglementHoraireInterieur;
import com.rh.manage.Service.ReglementHoraireInterieurService;

import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reglements-horaires")
// @CrossOrigin(origins = "*")

public class ReglementHoraireInterieurController {
    
    @Autowired
    private ReglementHoraireInterieurService reglementHoraireInterieurService;
    
    // Réponse standardisée
    private ResponseEntity<Map<String, Object>> createResponse(Object data, String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", status == HttpStatus.OK || status == HttpStatus.CREATED);
        response.put("message", message);
        response.put("data", data);
        return new ResponseEntity<>(response, status);
    }
    
    // Récupérer tous les règlements
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllReglements() {
        try {
            List<ReglementHoraireInterieur> reglements = reglementHoraireInterieurService.getAllReglements();
            return createResponse(reglements, "Liste des règlements horaires récupérée", HttpStatus.OK);
        } catch (Exception e) {
            return createResponse(null, "Erreur lors de la récupération: " + e.getMessage(), 
                                 HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Récupérer un règlement par ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getReglementById(@PathVariable Long id) {
        try {
            ReglementHoraireInterieur reglement = reglementHoraireInterieurService.getReglementById(id);
            return createResponse(reglement, "Règlement horaire récupéré", HttpStatus.OK);
        } catch (RuntimeException e) {
            return createResponse(null, e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return createResponse(null, "Erreur lors de la récupération: " + e.getMessage(), 
                                 HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Récupérer le règlement actif (le plus récent)
    @GetMapping("/actuel")
    public ResponseEntity<Map<String, Object>> getCurrentReglement() {
        try {
            ReglementHoraireInterieur reglement = reglementHoraireInterieurService.getCurrentReglement();
            if (reglement == null) {
                return createResponse(null, "Aucun règlement horaire défini", HttpStatus.NOT_FOUND);
            }
            return createResponse(reglement, "Règlement horaire actuel récupéré", HttpStatus.OK);
        } catch (Exception e) {
            return createResponse(null, "Erreur lors de la récupération: " + e.getMessage(), 
                                 HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Créer un nouveau règlement
    @PostMapping
    public ResponseEntity<Map<String, Object>> createReglement(@RequestBody ReglementHoraireInterieur reglement) {
        try {
            ReglementHoraireInterieur newReglement = reglementHoraireInterieurService.createReglement(reglement);
            return createResponse(newReglement, "Règlement horaire créé avec succès", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return createResponse(null, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return createResponse(null, "Erreur lors de la création: " + e.getMessage(), 
                                 HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Mettre à jour un règlement existant
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateReglement(@PathVariable Long id, 
                                                                @RequestBody ReglementHoraireInterieur reglement) {
        try {
            ReglementHoraireInterieur updatedReglement = reglementHoraireInterieurService.updateReglement(id, reglement);
            return createResponse(updatedReglement, "Règlement horaire mis à jour avec succès", HttpStatus.OK);
        } catch (RuntimeException e) {
            return createResponse(null, e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return createResponse(null, "Erreur lors de la mise à jour: " + e.getMessage(), 
                                 HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Supprimer un règlement
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteReglement(@PathVariable Long id) {
        try {
            reglementHoraireInterieurService.deleteReglement(id);
            return createResponse(null, "Règlement horaire supprimé avec succès", HttpStatus.OK);
        } catch (RuntimeException e) {
            return createResponse(null, e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return createResponse(null, "Erreur lors de la suppression: " + e.getMessage(), 
                                 HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Vérifier si une heure est dans les heures de travail (version avec LocalTime)
    @PostMapping("/verifier-heure")
    public ResponseEntity<Map<String, Object>> verifierHeureTravail(@RequestBody Map<String, String> request) {
        try {
            String heureStr = request.get("heure");
            if (heureStr == null) {
                return createResponse(null, "Le paramètre 'heure' est requis", HttpStatus.BAD_REQUEST);
            }
            
            // Convertir la String en LocalTime
            LocalTime heure = LocalTime.parse(heureStr);
            boolean isWorkingHour = reglementHoraireInterieurService.isWithinWorkingHours(heure);
            
            Map<String, Object> result = new HashMap<>();
            result.put("heure", heureStr);
            result.put("estDansLesHeuresDeTravail", isWorkingHour);
            
            return createResponse(result, "Vérification effectuée", HttpStatus.OK);
        } catch (java.time.format.DateTimeParseException e) {
            return createResponse(null, "Format d'heure invalide. Utilisez HH:mm (ex: 08:30)", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return createResponse(null, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return createResponse(null, "Erreur lors de la vérification: " + e.getMessage(), 
                                 HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Endpoint pour calculer les heures automatiquement
    @PostMapping("/calculer-heures")
    public ResponseEntity<Map<String, Object>> calculerHeures(@RequestBody Map<String, String> request) {
        try {
            String heureEntree = request.get("heureEntree");
            String heureSortie = request.get("heureSortie");
            String pauseMinutes = request.get("pauseMinutes");
            
            if (heureEntree == null || heureSortie == null || pauseMinutes == null) {
                return createResponse(null, "Tous les paramètres sont requis", HttpStatus.BAD_REQUEST);
            }
            
            // Utiliser la méthode du service qui accepte les Strings
            ReglementHoraireInterieur result = reglementHoraireInterieurService.calculerHeuresFromStrings(
                heureEntree, heureSortie, pauseMinutes
            );
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("heureNormaleJournaliere", result.getHeureNormaleJournaliere());
            responseData.put("heureNormaleSemaine", result.getHeureNormaleSemaine());
            responseData.put("heureNormaleMois", result.getHeureNormaleMois());
            
            return createResponse(responseData, "Calcul effectué avec succès", HttpStatus.OK);
        } catch (RuntimeException e) {
            return createResponse(null, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return createResponse(null, "Erreur lors du calcul: " + e.getMessage(), 
                                 HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Nouveau endpoint: Calculer la durée journalière de travail
    @PostMapping("/calculer-duree-journaliere")
    public ResponseEntity<Map<String, Object>> calculerDureeJournaliere(@RequestBody Map<String, String> request) {
        try {
            String heureEntree = request.get("heureEntree");
            String heureSortie = request.get("heureSortie");
            String pauseMinutes = request.get("pauseMinutes");
            
            if (heureEntree == null || heureSortie == null) {
                return createResponse(null, "Les heures d'entrée et sortie sont requises", HttpStatus.BAD_REQUEST);
            }
            
            // Créer un objet temporaire
            ReglementHoraireInterieur temp = new ReglementHoraireInterieur();
            temp.setHeureMatEntree(LocalTime.parse(heureEntree));
            temp.setHeureApremSortie(LocalTime.parse(heureSortie));
            
            if (pauseMinutes != null) {
                temp.setDureeNormalePauseMinutes(new BigDecimal(pauseMinutes));
            }
            
            // Calculer la durée
            BigDecimal dureeJournaliere = reglementHoraireInterieurService.calculateDailyWorkingHours(temp);
            
            Map<String, Object> result = new HashMap<>();
            result.put("dureeJournaliere", dureeJournaliere);
            result.put("dureeJournaliereMinutes", dureeJournaliere.multiply(BigDecimal.valueOf(60)));
            
            return createResponse(result, "Durée journalière calculée", HttpStatus.OK);
        } catch (Exception e) {
            return createResponse(null, "Erreur lors du calcul: " + e.getMessage(), 
                                 HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Vérifier si un créneau horaire est valide
    @PostMapping("/verifier-creneau")
    public ResponseEntity<Map<String, Object>> verifierCreneau(@RequestBody Map<String, String> request) {
        try {
            String heureEntree = request.get("heureEntree");
            String heureSortie = request.get("heureSortie");
            
            if (heureEntree == null || heureSortie == null) {
                return createResponse(null, "Les heures d'entrée et sortie sont requises", HttpStatus.BAD_REQUEST);
            }
            
            LocalTime entree = LocalTime.parse(heureEntree);
            LocalTime sortie = LocalTime.parse(heureSortie);
            
            Map<String, Object> result = new HashMap<>();
            result.put("heureEntree", heureEntree);
            result.put("heureSortie", heureSortie);
            result.put("estValide", !sortie.isBefore(entree));
            result.put("dureeTotalMinutes", java.time.Duration.between(entree, sortie).toMinutes());
            
            if (sortie.isBefore(entree)) {
                result.put("message", "L'heure de sortie doit être après l'heure d'entrée");
            } else {
                result.put("message", "Créneau horaire valide");
            }
            
            return createResponse(result, "Vérification effectuée", HttpStatus.OK);
        } catch (Exception e) {
            return createResponse(null, "Erreur lors de la vérification: " + e.getMessage(), 
                                 HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}