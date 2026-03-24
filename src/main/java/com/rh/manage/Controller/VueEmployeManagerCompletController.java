package com.rh.manage.Controller;

import com.rh.manage.Dto.DepartementHierarchiqueDTO;
import com.rh.manage.Dto.ManagerHierarchiqueDTO;
import com.rh.manage.Dto.EmployeCompactDTO;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Service.InfosProfessionnellesService;
import com.rh.manage.Service.VueEmployeManagerCompletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hierarchie")
// @CrossOrigin(origins = "*")

public class VueEmployeManagerCompletController {
    
    private final VueEmployeManagerCompletService service;
    private final InfosProfessionnellesService infosProfessionnellesService;
    
    public VueEmployeManagerCompletController(
            VueEmployeManagerCompletService service,
            InfosProfessionnellesService infosProfessionnellesService) {
        this.service = service;
        this.infosProfessionnellesService = infosProfessionnellesService;
    }
    
    // 1. Endpoint principal pour l'organigramme compact
    @GetMapping("/organigramme-compact")
    public ResponseEntity<Map<String, Object>> getOrganigrammeCompact() {
        Map<String, Object> organigramme = service.getOrganigrammeCompact();
        return ResponseEntity.ok(organigramme);
    }
    
    // 2. Endpoint pour l'organigramme complet (original)
    // @GetMapping("/organigramme")
    // public ResponseEntity<Map<String, Object>> getOrganigrammeComplet() {
    //     Map<String, Object> organigramme = service.getOrganigrammeComplet();
    //     return ResponseEntity.ok(organigramme);
    // }
    
    // 3. Endpoint pour un département spécifique (version compacte)
    @GetMapping("/departement/{nom}")
    public ResponseEntity<Map<String, Object>> getDepartementCompact(@PathVariable String nom) {
        Map<String, Object> organigramme = service.getOrganigrammeCompact();
        @SuppressWarnings("unchecked")
        List<DepartementHierarchiqueDTO> hierarchie = (List<DepartementHierarchiqueDTO>) organigramme.get("hierarchie");
        
        if (hierarchie != null) {
            java.util.Optional<DepartementHierarchiqueDTO> departement = hierarchie.stream()
                .filter(dep -> dep.getNomDepartement().equalsIgnoreCase(nom))
                .findFirst();
            
            if (departement.isPresent()) {
                Map<String, Object> response = Map.of(
                    "status", "success",
                    "departement", departement.get(),
                    "timestamp", System.currentTimeMillis()
                );
                return ResponseEntity.ok(response);
            }
        }
        
        return ResponseEntity.ok(Map.of(
            "status", "not_found",
            "message", "Département non trouvé: " + nom
        ));
    }
    
    // 4. Endpoint pour un manager spécifique (version compacte)
    @GetMapping("/manager/{nomManager}")
    public ResponseEntity<Map<String, Object>> getManagerCompact(@PathVariable String nomManager) {
        Map<String, Object> organigramme = service.getOrganigrammeCompact();
        @SuppressWarnings("unchecked")
        List<DepartementHierarchiqueDTO> hierarchie = (List<DepartementHierarchiqueDTO>) organigramme.get("hierarchie");
        
        if (hierarchie != null) {
            for (DepartementHierarchiqueDTO departement : hierarchie) {
                java.util.Optional<ManagerHierarchiqueDTO> manager = departement.getManagers().stream()
                    .filter(m -> m.getNomComplet().equalsIgnoreCase(nomManager))
                    .findFirst();
                
                if (manager.isPresent()) {
                    // Compter les subordonnés directs
                    int nbSubordonnes = 0;
                    if (manager.get().getSubordonnesCompacts() != null) {
                        nbSubordonnes = manager.get().getSubordonnesCompacts().size();
                    } else if (manager.get().getSubordonnes() != null) {
                        nbSubordonnes = manager.get().getSubordonnes().size();
                    }
                    
                    Map<String, Object> response = Map.of(
                        "status", "success",
                        "manager", manager.get(),
                        "departement", departement.getNomDepartement(),
                        "nombreSubordonnes", nbSubordonnes,
                        "timestamp", System.currentTimeMillis()
                    );
                    return ResponseEntity.ok(response);
                }
            }
        }
        
        return ResponseEntity.ok(Map.of(
            "status", "not_found",
            "message", "Manager non trouvé: " + nomManager
        ));
    }
    
    // 5. Endpoint pour les subordonnés d'un manager
    @GetMapping("/manager/{nomManager}/subordonnes")
    public ResponseEntity<Map<String, Object>> getSubordonnesCompact(@PathVariable String nomManager) {
        Map<String, Object> organigramme = service.getOrganigrammeCompact();
        @SuppressWarnings("unchecked")
        List<DepartementHierarchiqueDTO> hierarchie = (List<DepartementHierarchiqueDTO>) organigramme.get("hierarchie");
        
        if (hierarchie != null) {
            for (DepartementHierarchiqueDTO departement : hierarchie) {
                java.util.Optional<ManagerHierarchiqueDTO> manager = departement.getManagers().stream()
                    .filter(m -> m.getNomComplet().equalsIgnoreCase(nomManager))
                    .findFirst();
                
                if (manager.isPresent()) {
                    // Récupérer les subordonnés compacts
                    List<EmployeCompactDTO> subordonnes = manager.get().getSubordonnesCompacts();
                    
                    Map<String, Object> response = Map.of(
                        "status", "success",
                        "manager", nomManager,
                        "departement", departement.getNomDepartement(),
                        "subordonnes", subordonnes != null ? subordonnes : List.of(),
                        "nombreSubordonnes", subordonnes != null ? subordonnes.size() : 0,
                        "timestamp", System.currentTimeMillis()
                    );
                    return ResponseEntity.ok(response);
                }
            }
        }
        
        return ResponseEntity.ok(Map.of(
            "status", "not_found",
            "message", "Manager non trouvé: " + nomManager,
            "subordonnes", List.of()
        ));
    }
    
    // 6. Endpoint pour les statistiques globales
    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Object>> getStatistiques() {
        Map<String, Object> organigramme = service.getOrganigrammeCompact();
        
        @SuppressWarnings("unchecked")
        Map<String, Object> statistiques = (Map<String, Object>) organigramme.get("statistiques");
        
        Map<String, Object> response = Map.of(
            "status", "success",
            "statistiques", statistiques != null ? statistiques : Map.of(),
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(response);
    }
    
    // 7. Endpoint pour les départements avec nombre de managers
    @GetMapping("/departements/summary")
    public ResponseEntity<Map<String, Object>> getDepartementsSummary() {
        Map<String, Object> organigramme = service.getOrganigrammeCompact();
        @SuppressWarnings("unchecked")
        List<DepartementHierarchiqueDTO> hierarchie = (List<DepartementHierarchiqueDTO>) organigramme.get("hierarchie");
        
        List<Map<String, Object>> summary = new java.util.ArrayList<>();
        
        if (hierarchie != null) {
            for (DepartementHierarchiqueDTO dep : hierarchie) {
                int totalEmployes = dep.getManagers().stream()
                    .mapToInt(m -> m.getNombreSubordonnes() != null ? m.getNombreSubordonnes() : 0)
                    .sum();
                
                Map<String, Object> depSummary = Map.of(
                    "nom", dep.getNomDepartement(),
                    "nombreManagers", dep.getManagers().size(),
                    "nombreEmployes", totalEmployes,
                    "total", dep.getManagers().size() + totalEmployes
                );
                summary.add(depSummary);
            }
        }
        
        Map<String, Object> response = Map.of(
            "status", "success",
            "departements", summary,
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(response);
    }
    
    // 8. Endpoint pour rechercher une personne
    @GetMapping("/recherche")
    public ResponseEntity<Map<String, Object>> recherche(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String departement) {
        
        Map<String, Object> organigramme = service.getOrganigrammeCompact();
        @SuppressWarnings("unchecked")
        List<DepartementHierarchiqueDTO> hierarchie = (List<DepartementHierarchiqueDTO>) organigramme.get("hierarchie");
        
        List<Map<String, Object>> resultats = new java.util.ArrayList<>();
        
        if (hierarchie != null && q != null && !q.trim().isEmpty()) {
            String searchTerm = q.toLowerCase().trim();
            
            for (DepartementHierarchiqueDTO dep : hierarchie) {
                // Filtrer par département si spécifié
                if (departement != null && !dep.getNomDepartement().equalsIgnoreCase(departement)) {
                    continue;
                }
                
                // Rechercher dans les managers
                for (ManagerHierarchiqueDTO manager : dep.getManagers()) {
                    boolean matchManager = false;
                    
                    // Recherche par nom
                    if (manager.getNomComplet().toLowerCase().contains(searchTerm)) {
                        matchManager = true;
                    }
                    
                    // Recherche par poste
                    if (manager.getNomPoste() != null && 
                        manager.getNomPoste().toLowerCase().contains(searchTerm)) {
                        matchManager = true;
                    }
                    
                    // Filtrer par type si spécifié
                    if (type != null && !type.equalsIgnoreCase("manager") && !type.equalsIgnoreCase("tous")) {
                        matchManager = false;
                    }
                    
                    if (matchManager) {
                        Map<String, Object> result = Map.of(
                            "type", "Manager",
                            "nom", manager.getNomComplet(),
                            "matricule", manager.getMatricule(),
                            "poste", manager.getNomPoste(),
                            "niveau", manager.getNomNiveau(),
                            "rang", manager.getRang(),
                            "departement", dep.getNomDepartement(),
                            "nombreSubordonnes", manager.getNombreSubordonnes()
                        );
                        resultats.add(result);
                    }
                    
                    // Rechercher dans les subordonnés
                    if (manager.getSubordonnesCompacts() != null) {
                        for (EmployeCompactDTO employe : manager.getSubordonnesCompacts()) {
                            boolean matchEmploye = false;
                            
                            // Recherche par nom
                            if (employe.getNomComplet().toLowerCase().contains(searchTerm) ||
                                (employe.getNomAbrege() != null && 
                                 employe.getNomAbrege().toLowerCase().contains(searchTerm))) {
                                matchEmploye = true;
                            }
                            
                            // Recherche par poste
                            if (employe.getNomPoste() != null && 
                                employe.getNomPoste().toLowerCase().contains(searchTerm)) {
                                matchEmploye = true;
                            }
                            
                            // Filtrer par type si spécifié
                            if (type != null && !type.equalsIgnoreCase("employe") && !type.equalsIgnoreCase("tous")) {
                                matchEmploye = false;
                            }
                            
                            if (matchEmploye) {
                                Map<String, Object> result = Map.of(
                                    "type", "Employé",
                                    "nom", employe.getNomComplet(),
                                    "nomAbrege", employe.getNomAbrege(),
                                    "matricule", employe.getMatricule(),
                                    "poste", employe.getNomPoste(),
                                    "niveau", employe.getNomNiveau(),
                                    "rang", employe.getRang(),
                                    "departement", dep.getNomDepartement(),
                                    "manager", manager.getNomComplet()
                                );
                                resultats.add(result);
                            }
                        }
                    }
                }
            }
        }
        
        Map<String, Object> response = Map.of(
            "status", "success",
            "query", q != null ? q : "",
            "nombreResultats", resultats.size(),
            "resultats", resultats,
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(response);
    }
    
    // 9. Endpoint pour exporter les données compactes
    @GetMapping("/export/compact")
    public ResponseEntity<Map<String, Object>> exportCompact() {
        Map<String, Object> organigramme = service.getOrganigrammeCompact();
        
        Map<String, Object> response = Map.of(
            "status", "success",
            "data", organigramme,
            "format", "compact",
            "timestamp", System.currentTimeMillis(),
            "version", "1.0"
        );
        
        return ResponseEntity.ok(response);
    }
    
    // 10. Endpoint pour vérifier la santé
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        try {
            Map<String, Object> organigramme = service.getOrganigrammeCompact();
            String status = (String) organigramme.get("status");
            
            Map<String, Object> health = Map.of(
                "status", "UP",
                "service", "organigramme-compact",
                "apiStatus", status,
                "timestamp", System.currentTimeMillis(),
                "version", "1.0"
            );
            
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            Map<String, Object> health = Map.of(
                "status", "DOWN",
                "error", e.getMessage(),
                "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(503).body(health);
        }
    }
    
    // 11. Endpoint pour obtenir la liste des départements
    @GetMapping("/departements")
    public ResponseEntity<Map<String, Object>> getAllDepartements() {
        Map<String, Object> organigramme = service.getOrganigrammeCompact();
        @SuppressWarnings("unchecked")
        List<DepartementHierarchiqueDTO> hierarchie = (List<DepartementHierarchiqueDTO>) organigramme.get("hierarchie");
        
        List<String> departements = new java.util.ArrayList<>();
        
        if (hierarchie != null) {
            departements = hierarchie.stream()
                .map(DepartementHierarchiqueDTO::getNomDepartement)
                .collect(java.util.stream.Collectors.toList());
        }
        
        Map<String, Object> response = Map.of(
            "status", "success",
            "departements", departements,
            "nombreDepartements", departements.size(),
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(response);
    }
    
    // 12. Endpoint pour obtenir les top managers (plus de subordonnés)
    @GetMapping("/top-managers")
    public ResponseEntity<Map<String, Object>> getTopManagers(
            @RequestParam(defaultValue = "10") int limit) {
        
        Map<String, Object> organigramme = service.getOrganigrammeCompact();
        @SuppressWarnings("unchecked")
        List<DepartementHierarchiqueDTO> hierarchie = (List<DepartementHierarchiqueDTO>) organigramme.get("hierarchie");
        
        List<Map<String, Object>> topManagers = new java.util.ArrayList<>();
        
        if (hierarchie != null) {
            // Collecter tous les managers
            List<ManagerHierarchiqueDTO> tousManagers = hierarchie.stream()
                .flatMap(dep -> dep.getManagers().stream())
                .collect(java.util.stream.Collectors.toList());
            
            // Trier par nombre de subordonnés (décroissant)
            tousManagers.sort((m1, m2) -> {
                Integer sub1 = m1.getNombreSubordonnes() != null ? m1.getNombreSubordonnes() : 0;
                Integer sub2 = m2.getNombreSubordonnes() != null ? m2.getNombreSubordonnes() : 0;
                return sub2.compareTo(sub1);
            });
            
            // Limiter et formater les résultats
            for (int i = 0; i < Math.min(limit, tousManagers.size()); i++) {
                ManagerHierarchiqueDTO manager = tousManagers.get(i);
                
                // Trouver le département
                String departement = hierarchie.stream()
                    .filter(dep -> dep.getManagers().contains(manager))
                    .map(DepartementHierarchiqueDTO::getNomDepartement)
                    .findFirst()
                    .orElse("Inconnu");
                
                Map<String, Object> managerInfo = Map.of(
                    "rang", i + 1,
                    "nom", manager.getNomComplet(),
                    "poste", manager.getNomPoste(),
                    "niveau", manager.getNomNiveau(),
                    "rangHierarchique", manager.getRang(),
                    "departement", departement,
                    "nombreSubordonnes", manager.getNombreSubordonnes()
                );
                
                topManagers.add(managerInfo);
            }
        }
        
        Map<String, Object> response = Map.of(
            "status", "success",
            "limit", limit,
            "topManagers", topManagers,
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(response);
    }
    
    // 13. Endpoint pour debug
    @GetMapping("/debug")
    public ResponseEntity<Map<String, Object>> getDebugInfo() {
        try {
            Map<String, Object> debugInfo = service.getOrganigrammeCompact();
            
            // Ajouter des informations supplémentaires
            Map<String, Object> response = new java.util.HashMap<>(debugInfo);
            response.put("debug", true);
            response.put("javaVersion", System.getProperty("java.version"));
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = Map.of(
                "status", "error",
                "message", e.getMessage(),
                "stackTrace", e.getStackTrace()
            );
            return ResponseEntity.status(500).body(error);
        }
    }
    
    // 14. Endpoint pour obtenir les données brutes
    // @GetMapping("/raw-data")
    // public ResponseEntity<Map<String, Object>> getRawData() {
    //     try {
    //         List<com.rh.manage.View.VueEmployeManagerComplet> rawData = 
    //             service.getDonneesBrutesVue();
            
    //         List<Map<String, Object>> formattedData = rawData.stream()
    //             .map(emp -> Map.of(
    //                 "matricule", emp.getEmployeMatricule(),
    //                 "nomComplet", emp.getNomComplet(),
    //                 "prenom", emp.getEmployePrenom(),
    //                 "nom", emp.getEmployeNom(),
    //                 "poste", emp.getNomPoste(),
    //                 "niveau", emp.getNomNiveau(),
    //                 "rang", emp.getRang(),
    //                 "departement", emp.getNomDepartement(),
    //                 "manager", emp.getNomCompletManager(),
    //                 "posteManager", emp.getNomPosteManager(),
    //                 "niveauManager", emp.getNomNiveauManager(),
    //                 "rangManager", emp.getRangPosteManager()
    //             ))
    //             .collect(java.util.stream.Collectors.toList());
            
    //         Map<String, Object> response = Map.of(
    //             "status", "success",
    //             "nombreLignes", formattedData.size(),
    //             "data", formattedData,
    //             "timestamp", System.currentTimeMillis()
    //         );
            
    //         return ResponseEntity.ok(response);
    //     } catch (Exception e) {
    //         return ResponseEntity.ok(Map.of(
    //             "status", "error",
    //             "message", e.getMessage()
    //         ));
    //     }
    // }
    
    // 15. Endpoint pour obtenir les managers par département
    // @GetMapping("/departement/{nom}/managers")
    // public ResponseEntity<Map<String, Object>> getManagersByDepartement(@PathVariable String nom) {
    //     Map<String, Object> organigramme = service.getOrganigrammeCompact();
    //     @SuppressWarnings("unchecked")
    //     List<DepartementHierarchiqueDTO> hierarchie = (List<DepartementHierarchiqueDTO>) organigramme.get("hierarchie");
        
    //     if (hierarchie != null) {
    //         java.util.Optional<DepartementHierarchiqueDTO> departement = hierarchie.stream()
    //             .filter(dep -> dep.getNomDepartement().equalsIgnoreCase(nom))
    //             .findFirst();
            
    //         if (departement.isPresent()) {
    //             List<Map<String, Object>> managersInfo = departement.get().getManagers().stream()
    //                 .map(manager -> Map.of(
    //                     "nom", manager.getNomComplet(),
    //                     "matricule", manager.getMatricule(),
    //                     "poste", manager.getNomPoste(),
    //                     "niveau", manager.getNomNiveau(),
    //                     "rang", manager.getRang(),
    //                     "nombreSubordonnes", manager.getNombreSubordonnes()
    //                 ))
    //                 .collect(java.util.stream.Collectors.toList());
                
    //             Map<String, Object> response = Map.of(
    //                 "status", "success",
    //                 "departement", nom,
    //                 "nombreManagers", managersInfo.size(),
    //                 "managers", managersInfo,
    //                 "timestamp", System.currentTimeMillis()
    //             );
                
    //             return ResponseEntity.ok(response);
    //         }
    //     }
        
    //     return ResponseEntity.ok(Map.of(
    //         "status", "not_found",
    //         "message", "Département non trouvé: " + nom,
    //         "managers", List.of()
    //     ));
    // }
    @GetMapping("/manager/matricule/{matricule}")
    public ResponseEntity<Map<String, Object>> getManagerCompactByMatricule(@PathVariable String matricule) {
        Map<String, Object> organigramme = service.getOrganigrammeCompact();
        @SuppressWarnings("unchecked")
        List<DepartementHierarchiqueDTO> hierarchie = (List<DepartementHierarchiqueDTO>) organigramme.get("hierarchie");
        InfosProfessionnelles infosPro = infosProfessionnellesService.findByMatricule(matricule).orElse(null);

        if (infosPro == null || infosPro.getEmploye() == null) {
            return ResponseEntity.ok(Map.of(
                "status", "not_found",
                "message", "Aucun employe actif trouve pour le matricule: " + matricule
            ));
        }

        String nom = infosPro.getEmploye().getNom() != null ? infosPro.getEmploye().getNom().trim() : "";
        String prenom = infosPro.getEmploye().getPrenom() != null ? infosPro.getEmploye().getPrenom().trim() : "";
        String nomPrenom = (nom + " " + prenom).trim();
        String prenomNom = (prenom + " " + nom).trim();

        if (hierarchie != null) {
            for (DepartementHierarchiqueDTO departement : hierarchie) {
                java.util.Optional<ManagerHierarchiqueDTO> manager = departement.getManagers().stream()
                    .filter(m -> matchesManagerIdentity(m, matricule, nomPrenom, prenomNom))
                    .findFirst();

                if (manager.isPresent()) {
                    int nbSubordonnes = 0;
                    if (manager.get().getSubordonnesCompacts() != null) {
                        nbSubordonnes = manager.get().getSubordonnesCompacts().size();
                    } else if (manager.get().getSubordonnes() != null) {
                        nbSubordonnes = manager.get().getSubordonnes().size();
                    }

                    Map<String, Object> response = Map.of(
                        "status", "success",
                        "manager", manager.get(),
                        "departement", departement.getNomDepartement(),
                        "nombreSubordonnes", nbSubordonnes,
                        "timestamp", System.currentTimeMillis()
                    );
                    return ResponseEntity.ok(response);
                }
            }
        }

        return ResponseEntity.ok(Map.of(
            "status", "not_found",
            "message", "Manager non trouve pour le matricule: " + matricule
        ));
    }

    private boolean matchesManagerIdentity(
            ManagerHierarchiqueDTO manager,
            String matricule,
            String nomPrenom,
            String prenomNom) {
        if (manager == null) {
            return false;
        }

        if (manager.getMatricule() != null && manager.getMatricule().equalsIgnoreCase(matricule)) {
            return true;
        }

        String nomManager = normalize(manager.getNomComplet());
        return !nomManager.isEmpty() && (
            nomManager.equals(normalize(nomPrenom)) ||
            nomManager.equals(normalize(prenomNom))
        );
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().replaceAll("\\s+", " ").toLowerCase();
    }
}
