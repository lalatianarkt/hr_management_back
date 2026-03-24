// package com.rh.manage.Controller;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.rh.manage.Model.DemandeMouvement;
// import com.rh.manage.Service.DemandeMouvementService;

// import java.util.List;

// @RestController
// @RequestMapping("/api/demande-mouvements")
// public class DemandeMouvementController {

//     private final DemandeMouvementService service;

//     public DemandeMouvementController(DemandeMouvementService service) {
//         this.service = service;
//     }

//     @GetMapping
//     public List<DemandeMouvement> getAll() {
//         return service.getAll();
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<DemandeMouvement> getById(@PathVariable String id) {
//         return service.getById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     @PostMapping
//     public DemandeMouvement create(@RequestBody DemandeMouvement demande) {
//         return service.save(demande);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<DemandeMouvement> update(@PathVariable String id, @RequestBody DemandeMouvement demande) {
//         return service.getById(id)
//                 .map(existing -> {
//                     existing.setMotif(demande.getMotif());
//                     existing.setStatut(demande.getStatut());
//                     existing.setDateDemande(demande.getDateDemande());
//                     existing.setDateValidation(demande.getDateValidation());
//                     existing.setCommentaire(demande.getCommentaire());
//                     existing.setEmployeDemandeur(demande.getEmployeDemandeur());
//                     existing.setTypeMouvement(demande.getTypeMouvement());
//                     DemandeMouvement updated = service.save(existing);
//                     return ResponseEntity.ok(updated);
//                 })
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> delete(@PathVariable String id) {
//         if (service.getById(id).isPresent()) {
//             service.deleteById(id);
//             return ResponseEntity.noContent().build();
//         }
//         return ResponseEntity.notFound().build();
//     }
// }

