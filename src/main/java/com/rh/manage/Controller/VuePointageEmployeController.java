// package com.rh.manage.Controller;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.rh.manage.Dto.PointageEmployeDTO;
// import com.rh.manage.Dto.StatistiquesPointageDTO;
// import com.rh.manage.Service.VuePointageEmployeService;

// import java.time.LocalDate;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;

// @RestController
// @RequestMapping("/api/pointage")
// // @CrossOrigin(origins = "*")

// public class VuePointageEmployeController {
    
//     private final VuePointageEmployeService service;
    
//     public VuePointageEmployeController(VuePointageEmployeService service) {
//         this.service = service;
//     }
    
//     // 1. Récupérer tous les pointages
//     @GetMapping
//     public ResponseEntity<List<PointageEmployeDTO>> getAllPointages() {
//         List<PointageEmployeDTO> pointages = service.getAllPointages();
//         return ResponseEntity.ok(pointages);
//     }
    
//     // 2. Récupérer par ID
//     @GetMapping("/{id}")
//     public ResponseEntity<PointageEmployeDTO> getPointageById(@PathVariable String id) {
//         Optional<PointageEmployeDTO> pointage = service.getById(id);
//         return pointage.map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }
    
//     // 3. Récupérer par matricule
//     @GetMapping("/matricule/{matricule}")
//     public ResponseEntity<PointageEmployeDTO> getPointageByMatricule(@PathVariable String matricule) {
//         Optional<PointageEmployeDTO> pointage = service.getByMatricule(matricule);
//         return pointage.map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }
    
//     // 4. Rechercher par nom
//     @GetMapping("/search")
//     public ResponseEntity<List<PointageEmployeDTO>> searchByNom(@RequestParam String nom) {
//         List<PointageEmployeDTO> resultats = service.searchByNom(nom);
//         return ResponseEntity.ok(resultats);
//     }
    
//     // 5. Statistiques globales
//     @GetMapping("/statistiques")
//     public ResponseEntity<StatistiquesPointageDTO> getStatistiques(LocalDate dateDebut, LocalDate dateFin) {
//         StatistiquesPointageDTO stats = service.getStatistiques(dateDebut, dateFin);
//         return ResponseEntity.ok(stats);
//     }
    
//     // 6. Top retardataires
//     @GetMapping("/top/retardataires")
//     public ResponseEntity<List<PointageEmployeDTO>> getTopRetardataires(
//             @RequestParam(defaultValue = "10") int limit) {
//         List<PointageEmployeDTO> topRetardataires = service.getTopRetardataires(limit);
//         return ResponseEntity.ok(topRetardataires);
//     }
    
//     // 7. Top travailleurs
//     @GetMapping("/top/travailleurs")
//     public ResponseEntity<List<PointageEmployeDTO>> getTopTravailleurs(
//             @RequestParam(defaultValue = "10") int limit) {
//         List<PointageEmployeDTO> topTravailleurs = service.getTopTravailleurs(limit);
//         return ResponseEntity.ok(topTravailleurs);
//     }
    
//     // 8. Top heures supplémentaires
//     @GetMapping("/top/heures-sup")
//     public ResponseEntity<List<PointageEmployeDTO>> getTopHeuresSup(
//             @RequestParam(defaultValue = "10") int limit) {
//         List<PointageEmployeDTO> topHeuresSup = service.getTopHeuresSupplementaires(limit);
//         return ResponseEntity.ok(topHeuresSup);
//     }
    
//     // 9. Filtrer les pointages
//     @GetMapping("/filter")
//     public ResponseEntity<List<PointageEmployeDTO>> filterPointages(
//             @RequestParam(required = false) String matricule,
//             @RequestParam(required = false) String nom,
//             @RequestParam(required = false) Integer minHeures,
//             @RequestParam(required = false) Integer maxHeures,
//             @RequestParam(required = false) Integer minRetard,
//             @RequestParam(required = false) Integer maxRetard,
//             @RequestParam(required = false) Integer minHeuresSup,
//             @RequestParam(required = false) Integer maxHeuresSup) {
        
//         List<PointageEmployeDTO> resultats = service.filterPointages(
//             matricule, nom, minHeures, maxHeures, 
//             minRetard, maxRetard, minHeuresSup, maxHeuresSup
//         );
        
//         return ResponseEntity.ok(resultats);
//     }
    
//     // 10. Générer un rapport complet
//     // @GetMapping("/rapport")
//     // public ResponseEntity<Map<String, Object>> generateRapport() {
//     //     Map<String, Object> rapport = service.generateRapport();
//     //     return ResponseEntity.ok(rapport);
//     // }
    
//     // 11. Export des données (format simplifié)
//     // @GetMapping("/export")
//     // public ResponseEntity<List<Map<String, Object>>> exportPointages() {
//     //     List<PointageEmployeDTO> pointages = service.getAllPointages();
        
//     //     List<Map<String, Object>> export = pointages.stream()
//     //         .map(p -> Map.of(
//     //             "id", p.getIdEmploye(),
//     //             "matricule", p.getMatricule(),
//     //             "nomComplet", p.getNomComplet(),
//     //             "heuresTravaillees", p.getTotalHeureTravailleeHeures(),
//     //             "retard", p.getTotalRetardHeures(),
//     //             "heuresSupplementaires", p.getTotalHeureSupHeures(),
//     //             "tempsEffectif", p.getTempsEffectifHeures(),
//     //             "pourcentageRetard", p.getPourcentageRetard(),
//     //             "pourcentageHeuresSup", p.getPourcentageHeureSup()
//     //         ))
//     //         .collect(java.util.stream.Collectors.toList());
        
//     //     return ResponseEntity.ok(export);
//     // }
    
//     // 12. Vérification de santé
//     @GetMapping("/health")
//     public ResponseEntity<Map<String, Object>> healthCheck() {
//         try {
//             List<PointageEmployeDTO> pointages = service.getAllPointages();
            
//             Map<String, Object> health = Map.of(
//                 "status", "UP",
//                 "service", "pointage-employe",
//                 "nombreEmployes", pointages.size(),
//                 "timestamp", System.currentTimeMillis()
//             );
            
//             return ResponseEntity.ok(health);
//         } catch (Exception e) {
//             Map<String, Object> health = Map.of(
//                 "status", "DOWN",
//                 "error", e.getMessage(),
//                 "timestamp", System.currentTimeMillis()
//             );
//             return ResponseEntity.status(503).body(health);
//         }
//     }
    
//     // 13. Obtenir la distribution des heures travaillées
//     @GetMapping("/distribution/heures")
//     public ResponseEntity<Map<String, Long>> getDistributionHeures() {
//         List<PointageEmployeDTO> pointages = service.getAllPointages();
        
//         Map<String, Long> distribution = new java.util.HashMap<>();
//         distribution.put("0-50h", pointages.stream()
//             .filter(p -> p.getTotalHeureTravailleeHeures() < 50).count());
//         distribution.put("50-100h", pointages.stream()
//             .filter(p -> p.getTotalHeureTravailleeHeures() >= 50 && p.getTotalHeureTravailleeHeures() < 100).count());
//         distribution.put("100-150h", pointages.stream()
//             .filter(p -> p.getTotalHeureTravailleeHeures() >= 100 && p.getTotalHeureTravailleeHeures() < 150).count());
//         distribution.put("150-200h", pointages.stream()
//             .filter(p -> p.getTotalHeureTravailleeHeures() >= 150 && p.getTotalHeureTravailleeHeures() < 200).count());
//         distribution.put("200h+", pointages.stream()
//             .filter(p -> p.getTotalHeureTravailleeHeures() >= 200).count());
        
//         return ResponseEntity.ok(distribution);
//     }
    
//     // 14. Alertes retard sévère (> 5 heures)
//     @GetMapping("/alertes/retard")
//     public ResponseEntity<List<PointageEmployeDTO>> getAlertesRetard() {
//         List<PointageEmployeDTO> alertes = service.getTopRetardataires(20)
//             .stream()
//             .filter(p -> p.getTotalRetardHeures() > 5)
//             .collect(java.util.stream.Collectors.toList());
        
//         return ResponseEntity.ok(alertes);
//     }
    
//     // 15. Alertes heures sup importantes (> 20 heures)
//     @GetMapping("/alertes/heures-sup")
//     public ResponseEntity<List<PointageEmployeDTO>> getAlertesHeuresSup() {
//         List<PointageEmployeDTO> alertes = service.getTopHeuresSupplementaires(20)
//             .stream()
//             .filter(p -> p.getTotalHeureSupHeures() > 20)
//             .collect(java.util.stream.Collectors.toList());
        
//         return ResponseEntity.ok(alertes);
//     }
// }
