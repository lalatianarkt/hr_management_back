package com.rh.manage.Controller;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.rh.manage.Model.Pointage;
import com.rh.manage.Service.PointageService;
import com.rh.manage.Service.PointageValidationService;
import com.rh.manage.Utils.ValidationResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pointages")
public class PointageController {

    @Autowired
    PointageService pointageService;

    @Autowired
    PointageValidationService pointageValidationService;

    @GetMapping("/paginated/aujourdhui")
    public ResponseEntity<Map<String, Object>> getPointagesAujourdhui(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "dateHeureArrivee") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        try {
            Page<Pointage> pointagesPage = pointageService.getPointagesAujourdhuiPaginated(
                    page, size, sortBy, direction);
            
            Map<String, Object> response = pointageService.createPagedResponse(pointagesPage);
            response.put("date", LocalDate.now());
            response.put("isToday", true);
            response.put("mode", "default");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Erreur lors de la récupération des pointages du jour", 
                        "details", e.getMessage()
                    ));
        }
    }

    /**
     * Récupère les pointages paginés avec filtres
     */
    @GetMapping("/paginated/filtres")
    public ResponseEntity<Map<String, Object>> getPointagesByFiltersPaginated(
            @RequestParam(required = false) String employeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "datePointage") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        try { 
            Page<Pointage> pointagesPage = pointageService.getPointagesActivesByFiltersPaginated(
                    employeId, startDate, endDate, page, size, sortBy, direction);
            
            Map<String, Object> response = pointageService.createPagedResponse(pointagesPage);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la récupération des pointages filtrés", 
                                 "details", e.getMessage()));
        }
    }

    /**
     * Récupère les pointages paginés par période (dates optionnelles)
     * Par défaut: retourne les pointages d'aujourd'hui
     */
    @GetMapping("/paginated/periode")
    public ResponseEntity<Map<String, Object>> getPointagesByPeriodPaginated(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "datePointage") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        try {
            // Si les dates ne sont pas fournies, utiliser aujourd'hui
            if (startDate == null && endDate == null) {
                LocalDate aujourdhui = LocalDate.now();
                startDate = aujourdhui;
                endDate = aujourdhui;
            }
            
            Page<Pointage> pointagesPage = pointageService.getPointagesByPeriodPaginated(
                    startDate, endDate, page, size, sortBy, direction);
            
            Map<String, Object> response = pointageService.createPagedResponse(pointagesPage);
            
            // Ajouter des métadonnées sur la période
            response.put("periodStart", startDate);
            response.put("periodEnd", endDate);
            response.put("isToday", startDate.equals(LocalDate.now()) && endDate.equals(LocalDate.now()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Erreur lors de la récupération des pointages", 
                        "details", e.getMessage(),
                        "timestamp", LocalDateTime.now()
                    ));
        }
    }

    /**
     * Récupère les pointages paginés du jour (alias plus simple)
     */
    // @GetMapping("/paginated/aujourdhui")
    // public ResponseEntity<Map<String, Object>> getPointagesAujourdhuiPaginated(
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "20") int size,
    //         @RequestParam(defaultValue = "dateHeureArrivee") String sortBy,
    //         @RequestParam(defaultValue = "asc") String direction) {
        
    //     try {
    //         LocalDate aujourdhui = LocalDate.now();
            
    //         Page<Pointage> pointagesPage = pointageService.getPointagesByPeriodPaginated(
    //                 aujourdhui, aujourdhui, page, size, sortBy, direction);
            
    //         Map<String, Object> response = pointageService.createPagedResponse(pointagesPage);
    //         response.put("date", aujourdhui);
    //         response.put("isToday", true);
            
    //         return ResponseEntity.ok(response);
            
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body(Map.of(
    //                     "error", "Erreur lors de la récupération des pointages du jour", 
    //                     "details", e.getMessage()
    //                 ));
    //     }
    // }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importPointage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new LinkedHashMap<>();
        
        try {
            System.out.println("=== DÉBUT IMPORT POINTAGE ===");
            System.out.println("Nom du fichier: " + file.getOriginalFilename());
            System.out.println("Taille du fichier: " + file.getSize() + " bytes");
            System.out.println("Type MIME: " + file.getContentType());
            
            // Vérifier le type de fichier
            String contentType = file.getContentType();
            if (!pointageValidationService.isValidExcelFile(contentType, file.getOriginalFilename())) {
                System.out.println("❌ Format de fichier non supporté");
                response.put("success", false);
                response.put("error", "Format de fichier non supporté. Utilisez .xlsx, .xls ou .csv");
                response.put("fileName", file.getOriginalFilename());
                response.put("fileSize", file.getSize());
                response.put("contentType", contentType);
                return ResponseEntity.badRequest().body(response);
            }
            
            // Traiter le fichier Excel
            Workbook workbook;
            try {
                workbook = WorkbookFactory.create(file.getInputStream());
            } catch (Exception e) {
                System.err.println("❌ Erreur lors de l'ouverture du fichier Excel: " + e.getMessage());
                response.put("success", false);
                response.put("error", "Impossible d'ouvrir le fichier Excel. Vérifiez le format.");
                response.put("details", e.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
            
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                System.err.println("❌ Aucune feuille trouvée dans le fichier");
                response.put("success", false);
                response.put("error", "Aucune feuille trouvée dans le fichier Excel");
                return ResponseEntity.badRequest().body(response);
            }
            
            System.out.println("Feuille traitée: " + sheet.getSheetName());
            System.out.println("Nombre total de lignes: " + (sheet.getLastRowNum() + 1));
            
            // Importer et valider les données
            ValidationResult validationResult = pointageValidationService.importDataFromSheet(sheet);
            
            // Préparer la réponse
            response = validationResult.toResponse();
            
            // Ajouter des informations sur le fichier
            response.put("fileName", file.getOriginalFilename());
            response.put("fileSize", file.getSize());
            
            // Si des erreurs sont présentes, retourner un 400 avec les détails
            if (validationResult.hasErrors()) {
                response.put("message", "Import terminé avec des erreurs. Corrigez les données avant de réimporter.");
                System.out.println("⚠️ Import terminé avec " + validationResult.getErrorCount() + " erreurs");
                
                // Vous pouvez choisir de retourner 200 ou 400 selon votre logique métier
                // Ici, on retourne 200 avec les erreurs pour que le frontend puisse les afficher
                return ResponseEntity.ok(response);
                
            } else {
                response.put("message", "Import réussi. Toutes les données sont valides.");
                System.out.println("✅ Import réussi. " + validationResult.getValidCount() + " données valides.");
                return ResponseEntity.ok(response);
            }
        } 
        // catch (IOException e) {
        //     System.err.println("❌ Erreur d'IO lors de la lecture du fichier: " + e.getMessage());
        //     response.put("success", false);
        //     response.put("error", "Erreur de lecture du fichier");
        //     response.put("details", e.getMessage());
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            
        // } 
        catch (Exception e) {
            System.err.println("❌ Erreur inattendue lors de l'import: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Erreur lors du traitement du fichier");
            response.put("details", e.getMessage());
            response.put("exceptionType", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            
        } finally {
            System.out.println("=== FIN IMPORT POINTAGE ===");
        }
    }

    // @PostMapping
    // public ResponseEntity<Pointage> createPointage(@RequestBody Pointage pointage) {
    //     Pointage created = pointageService.

    //     return ResponseEntity.status(HttpStatus.CREATED).body(created);
    // }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pointage> getPointage(@PathVariable String id) {
        Pointage pointage = pointageService.getPointageById(id);
        return ResponseEntity.ok(pointage);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Pointage> updatePointage(
            @PathVariable String id, 
            @RequestBody Pointage pointageDetails) {
        Pointage updated = pointageService.updatePointage(id, pointageDetails);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePointage(@PathVariable String id) {
        pointageService.deletePointage(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/employe/{employeId}")
    public ResponseEntity<List<Pointage>> getPointagesByEmploye(@PathVariable String employeId) {
        List<Pointage> pointages = pointageService.getPointagesByEmploye(employeId);
        return ResponseEntity.ok(pointages);
    }
    
    @GetMapping("/periode")
    public ResponseEntity<List<Pointage>> getPointagesByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Pointage> pointages = pointageService.getPointagesByPeriod(startDate, endDate);
        return ResponseEntity.ok(pointages);
    }
    
    @GetMapping("/employe/{employeId}/periode")
    public ResponseEntity<List<Pointage>> getPointagesByEmployeAndPeriod(
            @PathVariable String employeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Pointage> pointages = pointageService.getPointagesByEmployeAndPeriod(employeId, startDate, endDate);
        return ResponseEntity.ok(pointages);
    }
    
    @PostMapping("/{id}/valider")
    public ResponseEntity<Pointage> validerPointage(@PathVariable String id) {
        Pointage pointage = pointageService.validerPointage(id);
        return ResponseEntity.ok(pointage);
    }
    
    @PostMapping("/{id}/payer")
    public ResponseEntity<Pointage> marquerPaye(@PathVariable String id) {
        Pointage pointage = pointageService.marquerPaye(id);
        return ResponseEntity.ok(pointage);
    }
    
    @GetMapping("/employe/{employeId}/total-heures")
    public ResponseEntity<Map<String, Object>> getTotalHeuresTravaillees(
            @PathVariable String employeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Double total = pointageService.getTotalHeuresTravaillees(employeId, startDate, endDate);
        
        return ResponseEntity.ok(Map.of(
                "employeId", employeId,
                "startDate", startDate,
                "endDate", endDate,
                "totalHeuresMinutes", total,
                "totalHeuresDecimal", total != null ? total / 60.0 : 0.0
        ));
    }
    
    @GetMapping("/jour")
    public ResponseEntity<List<Pointage>> getPointagesDuJour() {
        // Cette méthode nécessiterait une implémentation spécifique dans le service
        // Pour l'instant, retourne les pointages d'aujourd'hui
        LocalDate aujourdhui = LocalDate.now();
        List<Pointage> pointages = pointageService.getPointagesByPeriod(aujourdhui, aujourdhui);
        return ResponseEntity.ok(pointages);
    }
}
