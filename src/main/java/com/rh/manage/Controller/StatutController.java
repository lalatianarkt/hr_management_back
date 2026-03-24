package com.rh.manage.Controller;

import com.rh.manage.Service.StatutClotureService;
import com.rh.manage.Service.StatutService;
import com.rh.manage.Service.TokenService;
import com.rh.manage.Service.TokenService.TokenException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statuts")
public class StatutController {
    
    @Autowired
    private StatutService statutService;

    @Autowired
    private StatutClotureService statutClotureService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/RH/statut")
    public ResponseEntity<?> getAllStatutsRH(@RequestHeader("Authorization") String authHeader){
        try {
            Map<Integer, String> statutsMap = statutService.getAllStatutsMouvementRH();
            List<Map<String, Object>> statutsList = new ArrayList<>();
            tokenService.validateToken(authHeader);
            for (Map.Entry<Integer, String> entry : statutsMap.entrySet()) {
                Map<String, Object> statut = new HashMap<>();
                statut.put("id", entry.getKey());
                statut.put("libelle", entry.getValue());
                statutsList.add(statut);
            } 
            return ResponseEntity.ok(statutsList);
        } catch(TokenException e){
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
             return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    /**
     * Récupère tous les statuts sous forme de liste
     * GET /api/statuts
     */
    @GetMapping
    public ResponseEntity<?> getAllStatuts(@RequestHeader("Authorization") String authHeader) {
        try {
            Map<Integer, String> statutsMap = statutService.getAllStatuts();
            List<Map<String, Object>> statutsList = new ArrayList<>();
            tokenService.validateToken(authHeader);
            for (Map.Entry<Integer, String> entry : statutsMap.entrySet()) {
                Map<String, Object> statut = new HashMap<>();
                statut.put("id", entry.getKey());
                statut.put("libelle", entry.getValue());
                statut.put("estActif", statutService.estActif(entry.getKey()));
                statutsList.add(statut);
            }  
            return ResponseEntity.ok(statutsList);
        } catch(TokenException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }  catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/paie")
    public ResponseEntity<List<Map<String, Object>>> getAllStatutsPaie() {
        try {
            Map<Integer, String> statutsMap = statutClotureService.getAllStatuts();
            List<Map<String, Object>> statutsList = new ArrayList<>();
            
            for (Map.Entry<Integer, String> entry : statutsMap.entrySet()) {
                Map<String, Object> statut = new HashMap<>();
                statut.put("id", entry.getKey());
                statut.put("libelle", entry.getValue());
                statut.put("estActif", statutService.estActif(entry.getKey()));
                statutsList.add(statut);
            }
            
            return ResponseEntity.ok(statutsList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    /**
     * Récupère un statut par son ID
     * GET /api/statuts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStatutById(@PathVariable Integer id) {
        try {
            Map<Integer, String> statutsMap = statutService.getAllStatuts();
            
            if (!statutsMap.containsKey(id)) {
                return ResponseEntity.notFound().build();
            }
            
            Map<String, Object> statut = new HashMap<>();
            statut.put("id", id);
            statut.put("libelle", statutsMap.get(id));
            statut.put("estActif", statutService.estActif(id));
            statut.put("estInactif", statutService.estInactif(id));
            
            return ResponseEntity.ok(statut);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    /**
     * Récupère le libellé d'un statut par son ID
     * GET /api/statuts/libelle/{id}
     */
    @GetMapping("/libelle/{id}")
    public ResponseEntity<Map<String, String>> getLibelleStatut(@PathVariable Integer id) {
        try {
            String libelle = statutService.getLibelleStatut(id);
            Map<String, String> response = new HashMap<>();
            response.put("libelle", libelle);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    /**
     * Récupère l'ID d'un statut par son libellé
     * GET /api/statuts/id?libelle={libelle}
     */
    @GetMapping("/id")
    public ResponseEntity<Map<String, Integer>> getIdStatut(@RequestParam String libelle) {
        try {
            int id = statutService.getIdStatut(libelle);
            Map<String, Integer> response = new HashMap<>();
            response.put("id", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    /**
     * Vérifie si un statut est actif
     * GET /api/statuts/{id}/est-actif
     */
    @GetMapping("/{id}/est-actif")
    public ResponseEntity<Map<String, Boolean>> estStatutActif(@PathVariable Integer id) {
        try {
            boolean estActif = statutService.estActif(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("estActif", estActif);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    /**
     * Vérifie si un statut est inactif
     * GET /api/statuts/{id}/est-inactif
     */
    @GetMapping("/{id}/est-inactif")
    public ResponseEntity<Map<String, Boolean>> estStatutInactif(@PathVariable Integer id) {
        try {
            boolean estInactif = statutService.estInactif(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("estInactif", estInactif);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    /**
     * Récupère seulement les statuts actifs
     * GET /api/statuts/actifs
     */
    @GetMapping("/actifs")
    public ResponseEntity<List<Map<String, Object>>> getStatutsActifs() {
        try {
            Map<Integer, String> statutsMap = statutService.getAllStatuts();
            List<Map<String, Object>> statutsActifs = new ArrayList<>();
            
            for (Map.Entry<Integer, String> entry : statutsMap.entrySet()) {
                if (statutService.estActif(entry.getKey())) {
                    Map<String, Object> statut = new HashMap<>();
                    statut.put("id", entry.getKey());
                    statut.put("libelle", entry.getValue());
                    statutsActifs.add(statut);
                }
            }
            
            return ResponseEntity.ok(statutsActifs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    /**
     * Récupère seulement les statuts inactifs
     * GET /api/statuts/inactifs
     */
    @GetMapping("/inactifs")
    public ResponseEntity<List<Map<String, Object>>> getStatutsInactifs() {
        try {
            Map<Integer, String> statutsMap = statutService.getAllStatuts();
            List<Map<String, Object>> statutsInactifs = new ArrayList<>();
            
            for (Map.Entry<Integer, String> entry : statutsMap.entrySet()) {
                if (statutService.estInactif(entry.getKey())) {
                    Map<String, Object> statut = new HashMap<>();
                    statut.put("id", entry.getKey());
                    statut.put("libelle", entry.getValue());
                    statutsInactifs.add(statut);
                }
            }
            
            return ResponseEntity.ok(statutsInactifs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    /**
     * Récupère les statuts sous forme de Map simple
     * GET /api/statuts/map
     */
    @GetMapping("/map")
    public ResponseEntity<Map<Integer, String>> getStatutsMap() {
        try {
            Map<Integer, String> statutsMap = statutService.getAllStatuts();
            return ResponseEntity.ok(statutsMap);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    /**
     * Récupère les statuts disponibles pour les listes d'employés actifs
     * GET /api/statuts/pour-liste-employes
     */
    @GetMapping("/pour-liste-employes")
    public ResponseEntity<List<Map<String, Object>>> getStatutsPourListeEmployes() {
        try {
            Map<Integer, String> statutsMap = statutService.getAllStatuts();
            List<Map<String, Object>> statutsList = new ArrayList<>();
            
            for (Map.Entry<Integer, String> entry : statutsMap.entrySet()) {
                // Exclure "Archivé" (ID 2) pour les listes actives
                if (entry.getKey() != 2) { // Archivé = ID 2 dans votre configuration
                    Map<String, Object> statut = new HashMap<>();
                    statut.put("id", entry.getKey());
                    statut.put("libelle", entry.getValue());
                    statutsList.add(statut);
                }
            }
            
            return ResponseEntity.ok(statutsList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}