// package com.rh.manage.Controller;

// import com.rh.manage.Model.Manager;
// import com.rh.manage.Model.Employe;
// import com.rh.manage.Model.ManagerEmploye;
// import com.rh.manage.Service.EmployeService;
// import com.rh.manage.Service.ManagerEmployeService;
// import com.rh.manage.Service.ManagerService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/manager-employe")
// // @CrossOrigin(origins = "*")

// public class ManagerEmployeController {

//     @Autowired
//     ManagerEmployeService service;

//     @Autowired
//     EmployeService employeService;

//     @Autowired
//     ManagerService managerService;

//     @GetMapping
//     public ResponseEntity<List<ManagerEmploye>> getAll() {
//         return ResponseEntity.ok(service.getAll());
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<ManagerEmploye> getById(@PathVariable int id) {
//         return service.getById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     // @GetMapping("/manager/{managerId}")
//     // public ResponseEntity<List<ManagerEmploye>> getByManager(@PathVariable String managerId) {
//     //     return ResponseEntity.ok(service.getByManager(managerId));
//     // }

//     // @GetMapping("/employe/{employeId}")
//     // public ResponseEntity<List<ManagerEmploye>> getByEmploye(@PathVariable String employeId) {
//     //     return ResponseEntity.ok(service.getByEmploye(employeId));
//     // }

//     @PostMapping
//     public ResponseEntity<?> create(@RequestBody ManagerEmploye managerEmploye) {
//         try {
//             // Vérifie et prépare les données avant l’insertion
//             ManagerEmploye data = service.createDataManagerEmploye(managerEmploye);
            
//             // Si tout est bon, on enregistre
//             ManagerEmploye saved = service.create(data);
            
//             // Retourne le résultat avec le statut 201 (Created)
//             return ResponseEntity.status(201).body(saved);

//         } catch (Exception e) {
//             // Retourne une erreur 400 (Bad Request) avec le message de l’exception
//             return ResponseEntity
//                     .status(400)
//                     .body(Map.of(
//                         "status", 400,
//                         "error", "Erreur lors de la création du lien Manager-Employé",
//                         "message", e.getMessage()
//                     ));
//         }
//     }


//     // @PutMapping("/{id}")
//     // public ResponseEntity<ManagerEmploye> update(@PathVariable Long id, @RequestBody ManagerEmploye updated) {
//     //     return ResponseEntity.ok(service.update(id, updated));
//     // }

//     // @DeleteMapping("/{id}")
//     // public ResponseEntity<Void> delete(@PathVariable Long id) {
//     //     service.delete(id);
//     //     return ResponseEntity.noContent().build();
//     // }
// }
