package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.VueDemandeConge;
import com.rh.manage.Service.VueDemandeCongeService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vue-demandes-conge")
public class VueDemandeCongeController {
    @Autowired
    private VueDemandeCongeService vueDemandeCongeService;
    
    // Récupérer toutes les demandes
    @GetMapping
    public ResponseEntity<List<VueDemandeConge>> getAllDemandes() {
        List<VueDemandeConge> demandes = vueDemandeCongeService.getAllDemandes();
        return ResponseEntity.ok(demandes);
    }
    
    // Recherche multicritère avancée
    @GetMapping("/recherche-avancee")
    public ResponseEntity<List<VueDemandeConge>> rechercherDemandesAvance(
            @RequestParam(required = false) String idEmploye,
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String nomEmploye,
            @RequestParam(required = false) String prenomEmploye,
            @RequestParam(required = false) String idDepartement,
            @RequestParam(required = false) String idManager,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        List<VueDemandeConge> resultats = vueDemandeCongeService.rechercherDemandesAvance(
            idEmploye, matricule, nomEmploye, prenomEmploye, 
            idDepartement, idManager, statut, dateDebut, dateFin);
        
        return ResponseEntity.ok(resultats);
    }
    
    // Obtenir les demandes d'un employé par ID
    @GetMapping("/employe/{idEmploye}")
    public ResponseEntity<List<VueDemandeConge>> getByIdEmploye(@PathVariable String idEmploye) {
        List<VueDemandeConge> demandes = vueDemandeCongeService.getByIdEmploye(idEmploye);
        return ResponseEntity.ok(demandes);
    }
    
    // Obtenir les demandes par matricule
    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<List<VueDemandeConge>> getByMatricule(@PathVariable String matricule) {
        List<VueDemandeConge> demandes = vueDemandeCongeService.getByMatricule(matricule);
        return ResponseEntity.ok(demandes);
    }
    
    // Obtenir les demandes en attente
    @GetMapping("/en-attente")
    public ResponseEntity<List<VueDemandeConge>> getDemandesEnAttente() {
        List<VueDemandeConge> demandes = vueDemandeCongeService.getDemandesEnAttente();
        return ResponseEntity.ok(demandes);
    }
    
    // Obtenir les demandes validées
    @GetMapping("/validees")
    public ResponseEntity<List<VueDemandeConge>> getDemandesValidees() {
        List<VueDemandeConge> demandes = vueDemandeCongeService.getDemandesValidees();
        return ResponseEntity.ok(demandes);
    }
    
    // Obtenir les demandes refusées
    @GetMapping("/refusees")
    public ResponseEntity<List<VueDemandeConge>> getDemandesRefusees() {
        List<VueDemandeConge> demandes = vueDemandeCongeService.getDemandesRefusees();
        return ResponseEntity.ok(demandes);
    }
    
    // Obtenir les demandes annulées par le demandeur
    @GetMapping("/annulees-demandeur")
    public ResponseEntity<List<VueDemandeConge>> getDemandesAnnuleesParDemandeur() {
        List<VueDemandeConge> demandes = vueDemandeCongeService.getDemandesAnnuleesParDemandeur();
        return ResponseEntity.ok(demandes);
    }
    
    // Obtenir les demandes annulées par le RH
    @GetMapping("/annulees-rh")
    public ResponseEntity<List<VueDemandeConge>> getDemandesAnnuleesParRH() {
        List<VueDemandeConge> demandes = vueDemandeCongeService.getDemandesAnnuleesParRH();
        return ResponseEntity.ok(demandes);
    }
    
    // Obtenir les demandes d'un département (ID)
    @GetMapping("/departement/{idDepartement}")
    public ResponseEntity<List<VueDemandeConge>> getByIdDepartement(@PathVariable String idDepartement) {
        List<VueDemandeConge> demandes = vueDemandeCongeService.getByIdDepartement(idDepartement);
        return ResponseEntity.ok(demandes);
    }
    
    // Obtenir les demandes d'un manager (ID)
    @GetMapping("/manager/{idManager}")
    public ResponseEntity<List<VueDemandeConge>> getByIdManager(@PathVariable String idManager) {
        List<VueDemandeConge> demandes = vueDemandeCongeService.getByIdManager(idManager);
        return ResponseEntity.ok(demandes);
    }
    
    // Obtenir les demandes d'un manager avec statut spécifique
    @GetMapping("/manager/{idManager}/statut/{statut}")
    public ResponseEntity<List<VueDemandeConge>> getByIdManagerAndStatut(
            @PathVariable String idManager,
            @PathVariable String statut) {
        List<VueDemandeConge> demandes = vueDemandeCongeService.getByIdManagerAndStatut(idManager, statut);
        return ResponseEntity.ok(demandes);
    }
    
    // Statistiques détaillées
    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Object>> getStatistiques() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("parDepartement", vueDemandeCongeService.getStatsByDepartementDetail());
        stats.put("parStatut", vueDemandeCongeService.getStatsByStatut());
        stats.put("parManager", vueDemandeCongeService.getStatsByManager());
        stats.put("totalDemandes", vueDemandeCongeService.getAllDemandes().size());
        stats.put("enAttente", vueDemandeCongeService.getDemandesEnAttente().size());
        stats.put("validees", vueDemandeCongeService.getDemandesValidees().size());
        stats.put("refusees", vueDemandeCongeService.getDemandesRefusees().size());
        stats.put("annuleesDemandeur", vueDemandeCongeService.getDemandesAnnuleesParDemandeur().size());
        stats.put("annuleesRH", vueDemandeCongeService.getDemandesAnnuleesParRH().size());
        
        return ResponseEntity.ok(stats);
    }
    
    // Recherche par matricule ou nom
    @GetMapping("/recherche-rapide")
    public ResponseEntity<List<VueDemandeConge>> rechercheRapide(@RequestParam String search) {
        List<VueDemandeConge> resultats = vueDemandeCongeService.searchByMatriculeOrName(search);
        return ResponseEntity.ok(resultats);
    }
    
    // Obtenir les demandes d'un employé avec filtres
    @GetMapping("/employe/{idEmploye}/filtres")
    public ResponseEntity<List<VueDemandeConge>> getByEmployeWithFilters(
            @PathVariable String idEmploye,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) Integer annee) {
        
        List<VueDemandeConge> demandes = vueDemandeCongeService.getByEmployeWithFilters(idEmploye, statut, annee);
        return ResponseEntity.ok(demandes);
    }
    
    // Filtrer par période
    @GetMapping("/periode")
    public ResponseEntity<List<VueDemandeConge>> getByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        List<VueDemandeConge> demandes = vueDemandeCongeService.getByPeriode(dateDebut, dateFin);
        return ResponseEntity.ok(demandes);
    }
    
    // Détail d'une demande (par ID)
    @GetMapping("/{id}")
    public ResponseEntity<VueDemandeConge> getById(@PathVariable String id) {
        VueDemandeConge demande = vueDemandeCongeService.getById(id);
        if (demande != null) {
            return ResponseEntity.ok(demande);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Statistiques pour un employé
    @GetMapping("/statistiques/employe/{idEmploye}")
    public ResponseEntity<Map<String, Object>> getStatistiquesEmploye(@PathVariable String idEmploye) {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalDemandes", vueDemandeCongeService.getByIdEmploye(idEmploye).size());
        stats.put("parStatut", vueDemandeCongeService.countByStatutForEmploye(idEmploye));
        
        return ResponseEntity.ok(stats);
    }
    
    // Statistiques pour un département
    @GetMapping("/statistiques/departement/{idDepartement}")
    public ResponseEntity<Map<String, Object>> getStatistiquesDepartement(@PathVariable String idDepartement) {
        Map<String, Object> stats = new HashMap<>();
        
        List<VueDemandeConge> demandes = vueDemandeCongeService.getByIdDepartement(idDepartement);
        stats.put("totalDemandes", demandes.size());
        stats.put("parStatut", vueDemandeCongeService.countByStatutForDepartement(idDepartement));
        
        return ResponseEntity.ok(stats);
    }
}
