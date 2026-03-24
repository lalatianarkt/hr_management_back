// package com.rh.manage.Controller;

// import com.rh.manage.Model.DepartementManager;
// import com.rh.manage.Service.DepartementManagerService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDate;
// import java.util.List;

// @RestController
// @RequestMapping("/api/departement-manager")
// public class DepartementManagerController {

//     @Autowired
//     private DepartementManagerService departementManagerService;

//     // === CRUD BASIQUE ===

//     @PostMapping
//     public ResponseEntity<?> createDepartementManager(@RequestBody DepartementManager departementManager) {
//         try {
//             DepartementManager savedGestion = departementManagerService.save(departementManager);
//             return ResponseEntity.status(HttpStatus.CREATED).body(savedGestion);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                     .body("Erreur lors de la création: " + e.getMessage());
//         }
//     }

//     @GetMapping
//     public ResponseEntity<List<DepartementManager>> getAllDepartementManagers() {
//         List<DepartementManager> gestions = departementManagerService.findAll();
//         return ResponseEntity.ok(gestions);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<?> getDepartementManagerById(@PathVariable String id) {
//         try {
//             DepartementManager gestion = departementManagerService.findById(id);
//             return ResponseEntity.ok(gestion);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body("Gestion département-manager non trouvée pour l'ID: " + id);
//         }
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<?> updateDepartementManager(@PathVariable String id, @RequestBody DepartementManager departementManager) {
//         try {
//             DepartementManager updatedGestion = departementManagerService.update(id, departementManager);
//             return ResponseEntity.ok(updatedGestion);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                     .body("Erreur lors de la mise à jour: " + e.getMessage());
//         }
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<?> deleteDepartementManager(@PathVariable String id) {
//         try {
//             departementManagerService.deleteById(id);
//             return ResponseEntity.ok("Gestion département-manager supprimée avec succès");
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body("Erreur lors de la suppression: " + e.getMessage());
//         }
//     }

//     // === MÉTHODES MÉTIER ===

//     @GetMapping("/departement/{departementId}")
//     public ResponseEntity<?> getGestionByDepartement(@PathVariable String departementId) {
//         try {
//             List<DepartementManager> gestions = departementManagerService.findByDepartementId(departementId);
//             return ResponseEntity.ok(gestions);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body("Aucune gestion trouvée pour le département ID: " + departementId);
//         }
//     }

//     @GetMapping("/manager/{managerId}")
//     public ResponseEntity<?> getGestionByManager(@PathVariable String managerId) {
//         try {
//             List<DepartementManager> gestions = departementManagerService.findByManagerId(managerId);
//             return ResponseEntity.ok(gestions);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body("Aucune gestion trouvée pour le manager ID: " + managerId);
//         }
//     }

//     @GetMapping("/departement/{departementId}/manager-actuel")
//     public ResponseEntity<?> getCurrentManagerByDepartement(@PathVariable String departementId) {
//         try {
//             DepartementManager gestion = departementManagerService.findCurrentManagerByDepartementId(departementId);
//             return ResponseEntity.ok(gestion);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body("Aucun manager actuel trouvé pour le département ID: " + departementId);
//         }
//     }

//     @GetMapping("/manager/{managerId}/departements-actuels")
//     public ResponseEntity<?> getCurrentDepartementsByManager(@PathVariable String managerId) {
//         try {
//             List<DepartementManager> gestions = departementManagerService.findCurrentDepartementsByManagerId(managerId);
//             return ResponseEntity.ok(gestions);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body("Aucun département actuel trouvé pour le manager ID: " + managerId);
//         }
//     }

//     @PostMapping("/affecter")
//     public ResponseEntity<?> assignManagerToDepartement(
//             @RequestParam String managerId,
//             @RequestParam String departementId,
//             @RequestParam String dateDebut) {
        
//         try {
//             LocalDate debut = LocalDate.parse(dateDebut);
//             DepartementManager nouvelleAffectation = departementManagerService.assignManagerToDepartement(managerId, departementId, debut);
//             return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleAffectation);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                     .body("Erreur lors de l'affectation: " + e.getMessage());
//         }
//     }

//     @PutMapping("/{id}/desaffecter")
//     public ResponseEntity<?> unassignManagerFromDepartement(
//             @PathVariable String id,
//             @RequestParam String dateFin) {
        
//         try {
//             LocalDate fin = LocalDate.parse(dateFin);
//             departementManagerService.unassignManagerFromDepartement(id, fin);
//             return ResponseEntity.ok("Manager désaffecté du département avec succès");
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                     .body("Erreur lors de la désaffectation: " + e.getMessage());
//         }
//     }

//     @GetMapping("/departement/{departementId}/est-gere")
//     public ResponseEntity<?> checkIfDepartementIsManaged(@PathVariable String departementId) {
//         try {
//             boolean isManaged = departementManagerService.isDepartementManaged(departementId);
//             return ResponseEntity.ok(isManaged);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                     .body("Erreur lors de la vérification: " + e.getMessage());
//         }
//     }

//     @GetMapping("/verifier-affectation")
//     public ResponseEntity<?> checkIfManagerManagesDepartement(
//             @RequestParam String managerId,
//             @RequestParam String departementId) {
        
//         try {
//             boolean isManaging = departementManagerService.isManagerManagingDepartement(managerId, departementId);
//             return ResponseEntity.ok(isManaging);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                     .body("Erreur lors de la vérification: " + e.getMessage());
//         }
//     }

//     @GetMapping("/manager/{managerId}/statistiques")
//     public ResponseEntity<?> getManagerStats(@PathVariable String managerId) {
//         try {
//             Long nbDepartements = departementManagerService.countActiveDepartementsByManager(managerId);
//             return ResponseEntity.ok("Nombre de départements gérés: " + nbDepartements);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body("Erreur lors du calcul des statistiques: " + e.getMessage());
//         }
//     }

//     @GetMapping("/departement/{departementId}/historique")
//     public ResponseEntity<?> getDepartementHistory(@PathVariable String departementId) {
//         try {
//             List<DepartementManager> historique = departementManagerService.getDepartementHistory(departementId);
//             return ResponseEntity.ok(historique);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body("Erreur lors de la récupération de l'historique: " + e.getMessage());
//         }
//     }
// }