package com.rh.manage.Controller;

import com.rh.manage.Model.EdtEmploye;
import com.rh.manage.Service.EdtEmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/edt-employes")
public class EdtEmployeController {
    
    @Autowired
    private EdtEmployeService edtService;
    
    @PostMapping
    public ResponseEntity<?> createEdt(@RequestBody EdtEmploye edt) {
        try {
            
            EdtEmploye createdEdt = edtService.createEdt(edt);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEdt);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la création de l'EDT: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getEdtById(@PathVariable String id) {
        try {
            EdtEmploye edt = edtService.getEdtById(id);
            return ResponseEntity.ok(edt);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<List<EdtEmploye>> getAllEdt() {
        List<EdtEmploye> edtList = edtService.getAllEdt();
        return ResponseEntity.ok(edtList);
    }
    
    @GetMapping("/actifs")
    public ResponseEntity<List<EdtEmploye>> getEdtActifs() {
        List<EdtEmploye> edtActifs = edtService.getEdtActifs();
        return ResponseEntity.ok(edtActifs);
    }
    
    @GetMapping("/employe/{employeId}")
    public ResponseEntity<?> getEdtByEmploye(@PathVariable String employeId) {
        try {
            List<EdtEmploye> edtList = edtService.getEdtByEmploye(employeId);
            return ResponseEntity.ok(edtList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }
    
    @GetMapping("/actifs/employe/{employeId}")
    public ResponseEntity<?> getEdtActifsByEmploye(@PathVariable String employeId) {
        try {
            List<EdtEmploye> edtList = edtService.getEdtActifsByEmploye(employeId);
            return ResponseEntity.ok(edtList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getEdtByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            List<EdtEmploye> edtList = edtService.getEdtByDate(date);
            return ResponseEntity.ok(edtList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }
    
    @GetMapping("/actifs/date/{date}")
    public ResponseEntity<?> getEdtActifsByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            List<EdtEmploye> edtList = edtService.getEdtActifsByDate(date);
            return ResponseEntity.ok(edtList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }
    
    @GetMapping("/employe/{employeId}/date/{date}")
    public ResponseEntity<?> getEdtByEmployeAndDate(
            @PathVariable String employeId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            List<EdtEmploye> edtList = edtService.getEdtByEmployeAndDate(employeId, date);
            return ResponseEntity.ok(edtList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }
    
    @GetMapping("/actifs/employe/{employeId}/date/{date}")
    public ResponseEntity<?> getEdtActifByEmployeAndDate(
            @PathVariable String employeId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            EdtEmploye edt = edtService.getEdtActifByEmployeAndDate(employeId, date);
            if (edt == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Aucun EDT actif trouvé pour cet employé à cette date"));
            }
            return ResponseEntity.ok(edt);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }
    
    @GetMapping("/periode")
    public ResponseEntity<?> getEdtByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            List<EdtEmploye> edtList = edtService.getEdtByDateRange(startDate, endDate);
            return ResponseEntity.ok(edtList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }
    
    @GetMapping("/actifs/periode")
    public ResponseEntity<?> getEdtActifsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            List<EdtEmploye> edtList = edtService.getEdtActifsByDateRange(startDate, endDate);
            return ResponseEntity.ok(edtList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }
    
    @GetMapping("/shift/{isShiftJour}")
    public ResponseEntity<?> getEdtByShiftType(@PathVariable Boolean isShiftJour) {
        try {
            List<EdtEmploye> edtList = edtService.getEdtByShiftType(isShiftJour);
            return ResponseEntity.ok(edtList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }
    
    @GetMapping("/actifs/shift/{isShiftJour}")
    public ResponseEntity<?> getEdtActifsByShiftType(@PathVariable Boolean isShiftJour) {
        try {
            List<EdtEmploye> edtList = edtService.getEdtActifsByShiftType(isShiftJour);
            return ResponseEntity.ok(edtList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEdt(@PathVariable String id, @RequestBody EdtEmploye edtDetails) {
        try {
            EdtEmploye updatedEdt = edtService.updateEdt(id, edtDetails);
            return ResponseEntity.ok(updatedEdt);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la mise à jour: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEdt(@PathVariable String id) {
        try {
            edtService.deleteEdt(id);
            return ResponseEntity.ok(Map.of("message", "EDT désactivé avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la suppression: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/permanent/{id}")
    public ResponseEntity<?> deleteEdtPermanently(@PathVariable String id) {
        try {
            edtService.deleteEdtPermanently(id);
            return ResponseEntity.ok(Map.of("message", "EDT supprimé définitivement avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la suppression définitive: " + e.getMessage()));
        }
    }
    
    @GetMapping("/exists/{id}")
    public ResponseEntity<Map<String, Boolean>> checkEdtExists(@PathVariable String id) {
        boolean exists = edtService.existsById(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/exists/actif/employe/{employeId}/date/{date}")
    public ResponseEntity<Map<String, Boolean>> checkEdtActifForEmployeAndDate(
            @PathVariable String employeId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        boolean exists = edtService.existsEdtActifForEmployeAndDate(employeId, date);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/count/actifs/employe/{employeId}")
    public ResponseEntity<Map<String, Long>> countEdtActifsByEmploye(@PathVariable String employeId) {
        long count = edtService.countEdtActifsByEmploye(employeId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}