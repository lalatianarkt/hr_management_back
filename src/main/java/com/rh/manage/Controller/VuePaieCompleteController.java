package com.rh.manage.Controller;

import com.rh.manage.Model.Departement;
import com.rh.manage.Model.VuePaieComplete;
import com.rh.manage.Service.DepartementService;
import com.rh.manage.Service.VuePaieCompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.relation.RelationNotFoundException;

@RestController
@RequestMapping("/api/vue-paie-complete")
public class VuePaieCompleteController {
    @Autowired
    private VuePaieCompleteService service;

    @Autowired
    private DepartementService departementService;

    // @GetMapping("/departement/{departement}/pagine")
    // public ResponseEntity<?> getPaiesPaginees(
    //         @PathVariable String departement,
    //         @RequestParam(required = false) Integer statutCloture,
    //         @RequestParam(required = false) String matricule,
    //         @RequestParam(required = false) String search,
    //         @RequestParam(required = false) LocalDate dateDebut,
    //         @RequestParam(required = false) LocalDate dateFin,
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "10") int size,
    //         @RequestParam(defaultValue = "nomComplet") String sortBy,
    //         @RequestParam(defaultValue = "asc") String direction) {
        
    //     try {
    //         System.out.println("depart : " + departement);
    //         String nom_dept = departementService.getDepartementById(departement).get().getNom();
    //         System.out.println("nom_dept : " + nom_dept);
    //         System.out.println("ato e");
    //         System.out.println("departement : "+ nom_dept);
    //         System.out.println("statut : " + statutCloture);
    //         System.out.println("page : " + page);
    //         System.out.println("size : " + size);
    //         System.out.println("sortBy : " + sortBy);
    //         System.out.println("asc : " + direction);
    //         Map<String, Object> result = service.findByDepartementAndStatutClotureWithPagination(
    //         nom_dept, statutCloture, matricule, search, 
    //         dateDebut, dateFin,
    //         page, size, sortBy, direction);

    //         // Map<String, Object> result = service
    //         //     .findByDepartementAndStatutClotureWithPagination(
    //         //         nom_dept, statutCloture, matricule, search,
    //         //         page, size, sortBy, direction
    //         //     );
    //         return ResponseEntity.ok(result);
    //     } catch (IllegalArgumentException e) {
    //         e.printStackTrace();
    //         System.out.println("errrr : " + e.getMessage());
    //         Map<String, String> error = new HashMap<>();
    //         error.put("error", "Paramètre invalide");
    //         error.put("message", e.getMessage());
    //         return ResponseEntity.badRequest().body(error); 
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         System.out.println("ato e erreur");
    //         System.out.println("errr : " + e.getMessage());
    //         Map<String, String> error = new HashMap<>();
    //         error.put("error", "Erreur serveur");
    //         error.put("message", e.getMessage());
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    //     }
    // }

    @GetMapping("/departement/{departement}/pagine")
public ResponseEntity<?> getPaiesPaginees(
        @PathVariable String departement,
        @RequestParam(required = false) Integer statutCloture,
        @RequestParam(required = false) String matricule,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String dateDebut,    // Changé de LocalDate à String
        @RequestParam(required = false) String dateFin,      // Changé de LocalDate à String
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "nomComplet") String sortBy,
        @RequestParam(defaultValue = "asc") String direction) {
    
    try {
        System.out.println("depart : " + departement);
        String nom_dept = departementService.getDepartementById(departement)
                .orElseThrow(() -> new IllegalArgumentException("Département non trouvé"))
                .getNom();
        System.out.println("nom_dept : " + nom_dept);
        System.out.println("ato e");
        System.out.println("departement : " + nom_dept);
        System.out.println("statut : " + statutCloture);
        System.out.println("dateDebut : " + dateDebut);
        System.out.println("dateFin : " + dateFin);
        System.out.println("page : " + page);
        System.out.println("size : " + size);
        System.out.println("sortBy : " + sortBy);
        System.out.println("direction : " + direction);
        
        // Validation optionnelle des formats de date
        if (dateDebut != null && !dateDebut.isEmpty()) {
            try {
                LocalDate.parse(dateDebut);
            } catch (Exception e) {
                throw new IllegalArgumentException("Format de date début invalide. Utilisez YYYY-MM-DD");
            }
        }
        
        if (dateFin != null && !dateFin.isEmpty()) {
            try {
                LocalDate.parse(dateFin);
            } catch (Exception e) {
                throw new IllegalArgumentException("Format de date fin invalide. Utilisez YYYY-MM-DD");
            }
        }
        
        Map<String, Object> result = service.findByDepartementAndStatutClotureWithPagination(
            nom_dept, statutCloture, matricule, search, 
            dateDebut, dateFin,
            page, size, sortBy, direction);

        return ResponseEntity.ok(result);
        
    } catch (IllegalArgumentException e) {
        e.printStackTrace();
        System.err.println("Erreur paramètre: " + e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Paramètre invalide");
        error.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(error); 
        
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Erreur serveur: " + e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erreur serveur");
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

    @GetMapping("/departement/{idDepartement}/bulletin-paie")
    public ResponseEntity<?> getPaieParDepartement(@PathVariable String idDepartement) {
        // System.out.println("idDep : " + idDepartement);
        // return ResponseEntity.ok(null);
        try {

            System.out.println("tafa e : " + idDepartement);
            // 1. Récupérer le département
            Departement departement = departementService.getDepartementById(idDepartement)
                .orElseThrow(() -> new RelationNotFoundException("Département non trouvé avec l'ID: " + idDepartement));
            
            // 2. Récupérer les paies du département
            List<VuePaieComplete> les_vue_paies_completes = service.findByDepartement(departement.getNom());
            System.out.println("tsy tonga ve " + les_vue_paies_completes.size());
            
            // 3. Retourner la réponse
            if (les_vue_paies_completes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                    Map.of("message", "Aucune paie trouvée pour le département: " + departement.getNom())
                );
            }
            
            return ResponseEntity.ok(les_vue_paies_completes);
            
        } catch (ResourceAccessException e) {
            System.out.println("erreur nenareo+++++++++" + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            System.out.println("erreur de débutant : " + e.getMessage() );
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("error", "Erreur lors de la récupération des paies par département")
            );
        }
    }
    
    // Récupérer toutes les données
    @GetMapping
    public ResponseEntity<List<VuePaieComplete>> getAll() {
        try {
            List<VuePaieComplete> result = service.getAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Récupérer par ID employé
    @GetMapping("/employe/{idEmploye}")
    public ResponseEntity<List<VuePaieComplete>> getByIdEmploye(@PathVariable String idEmploye) {
        try {
            List<VuePaieComplete> result = service.getByIdEmploye(idEmploye);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Récupérer par matricule
    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<List<VuePaieComplete>> getByMatricule(@PathVariable String matricule) {
        try {
            List<VuePaieComplete> result = service.getByMatricule(matricule);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Récupérer par département
    @GetMapping("/departement/{departement}")
    public ResponseEntity<List<VuePaieComplete>> getByDepartement(@PathVariable String departement) {
        try {
            List<VuePaieComplete> result = service.getByDepartement(departement);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Récupérer par statut de clôture
    @GetMapping("/statut/{statutCloture}")
    public ResponseEntity<List<VuePaieComplete>> getByStatutCloture(@PathVariable Integer statutCloture) {
        try {
            List<VuePaieComplete> result = service.getByStatutCloture(statutCloture);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Récupérer par période
    @GetMapping("/periode")
    public ResponseEntity<List<VuePaieComplete>> getByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        try {
            List<VuePaieComplete> result = service.getByPeriode(dateDebut, dateFin);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Récupérer par mois et année
    @GetMapping("/mois-annee")
    public ResponseEntity<List<VuePaieComplete>> getByMoisAnnee(
            @RequestParam Integer mois,
            @RequestParam Integer annee) {
        try {
            List<VuePaieComplete> result = service.getByMoisAnnee(mois, annee);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Recherche avec critères multiples
    @GetMapping("/recherche")
    public ResponseEntity<List<VuePaieComplete>> searchByCriteria(
            @RequestParam(required = false) String departement,
            @RequestParam(required = false) Integer statutCloture,
            @RequestParam(required = false) String categorieSalaire,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        try {
            List<VuePaieComplete> result = service.searchByCriteria(
                departement, statutCloture, categorieSalaire, dateDebut, dateFin);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Statistiques par département
    @GetMapping("/stats/departement")
    public ResponseEntity<Map<String, Object>> getStatsDepartement() {
        try {
            Map<String, Object> result = service.getStatsDepartement();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Statistiques par mois/année
    @GetMapping("/stats/mois-annee")
    public ResponseEntity<Map<String, Object>> getStatsMoisAnnee() {
        try {
            Map<String, Object> result = service.getStatsMoisAnnee();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Totaux globaux pour une période
    @GetMapping("/totaux")
    public ResponseEntity<Map<String, BigDecimal>> getTotauxGlobaux(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        try {
            Map<String, BigDecimal> result = service.getTotauxGlobaux(dateDebut, dateFin);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Périodes distinctes disponibles
    @GetMapping("/periodes-distinctes")
    public ResponseEntity<List<LocalDate>> getPeriodesDistinctes() {
        try {
            List<LocalDate> result = service.getPeriodesDistinctes();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Départements distincts
    @GetMapping("/departements-distincts")
    public ResponseEntity<List<String>> getDepartementsDistincts() {
        try {
            List<String> result = service.getDepartementsDistincts();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Catégories de salaire distinctes
    @GetMapping("/categories-salaire-distinctes")
    public ResponseEntity<List<String>> getCategoriesSalaireDistinctes() {
        try {
            List<String> result = service.getCategoriesSalaireDistinctes();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/bulletin/{idPaie}")
    public ResponseEntity<?> getParPaie(@PathVariable String idPaie) {
        try {
            VuePaieComplete vuePaieComplete = service.getByIdPaie(idPaie);
            return ResponseEntity.ok(vuePaieComplete);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
    // Recherche avancée
    @GetMapping("/recherche-avancee")
    public ResponseEntity<List<VuePaieComplete>> searchAvancee(
            @RequestParam(required = false) String searchTerm) {
        try {
            List<VuePaieComplete> result = service.searchAvancee(searchTerm);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
