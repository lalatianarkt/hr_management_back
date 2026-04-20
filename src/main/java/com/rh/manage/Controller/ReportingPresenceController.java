package com.rh.manage.Controller;

import com.rh.manage.Dto.ReportingPresenceDTO;
import com.rh.manage.Dto.ReportingPresenceFilterDTO;
import com.rh.manage.Dto.ReportingStatsDTO;
import com.rh.manage.Service.ReportingPresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reporting-presence")
public class ReportingPresenceController {
    
    private final ReportingPresenceService reportingPresenceService;
    
    public ReportingPresenceController(ReportingPresenceService reportingPresenceService) {
        this.reportingPresenceService = reportingPresenceService;
    }
    
    // ==================== ENDPOINTS DE RECHERCHE ====================
    
    // 1. Récupérer tout le reporting de présence
    @GetMapping
    public ResponseEntity<List<ReportingPresenceDTO>> getAllReportingPresence() {
        try {
            List<ReportingPresenceDTO> result = reportingPresenceService.getAllReportingPresence();
            if (result == null || result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 2. Rechercher avec filtres (POST)
    @PostMapping("/recherche")
    public ResponseEntity<List<ReportingPresenceDTO>> searchWithFilters(@RequestBody ReportingPresenceFilterDTO filter) {
        try {
            if (filter == null) {
                return ResponseEntity.badRequest().build();
            }
            
            List<ReportingPresenceDTO> result = reportingPresenceService.getReportingWithFilters(filter);
            if (result == null || result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 3. Rechercher avec filtres (GET avec paramètres)
    @GetMapping("/recherche")
    public ResponseEntity<List<ReportingPresenceDTO>> searchWithFiltersGet(
            @RequestParam(required = false) String departementId,
            @RequestParam(required = false) String nomComplet,
            @RequestParam(required = false) Integer minConges,
            @RequestParam(required = false) Integer maxConges,
            @RequestParam(required = false) String triPar,
            @RequestParam(required = false) String ordreTri) {
        
        try {
            ReportingPresenceFilterDTO filter = new ReportingPresenceFilterDTO();
            filter.setDepartementId(departementId);
            filter.setNomComplet(nomComplet);
            filter.setMinConges(minConges);
            filter.setMaxConges(maxConges);
            filter.setTriPar(triPar);
            filter.setOrdreTri(ordreTri);
            
            List<ReportingPresenceDTO> result = reportingPresenceService.getReportingWithFilters(filter);
            if (result == null || result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 4. Récupérer par matricule
    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<ReportingPresenceDTO> getByMatricule(@PathVariable String matricule) {
        try {
            if (matricule == null || matricule.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            ReportingPresenceDTO dto = reportingPresenceService.getByMatricule(matricule);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 5. Récupérer par département
    @GetMapping("/departement/{departementId}")
    public ResponseEntity<List<ReportingPresenceDTO>> getByDepartement(@PathVariable String departementId) {
        try {
            if (departementId == null || departementId.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            List<ReportingPresenceDTO> result = reportingPresenceService.getByDepartement(departementId);
            if (result == null || result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 6. Rechercher par nom
    @GetMapping("/recherche-nom")
    public ResponseEntity<List<ReportingPresenceDTO>> searchByNom(@RequestParam String nom) {
        try {
            if (nom == null || nom.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            List<ReportingPresenceDTO> result = reportingPresenceService.searchByNom(nom);
            if (result == null || result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 7. Top performers par heures travaillées
    @GetMapping("/top-performers")
    public ResponseEntity<List<ReportingPresenceDTO>> getTopPerformers(
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        try {
            List<ReportingPresenceDTO> result = reportingPresenceService.getTopPerformers(limit);
            if (result == null || result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // ==================== ENDPOINTS DE STATISTIQUES ====================
    
    // 8. Statistiques globales
    @GetMapping("/stats-globales")
    public ResponseEntity<ReportingStatsDTO> getGlobalStats() {
        try {
            ReportingStatsDTO stats = reportingPresenceService.getGlobalStats();
            if (stats == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 9. Statistiques formatées (texte)
    // @GetMapping("/stats-globales-formatees")
    // public ResponseEntity<String> getGlobalStatsFormatted() {
    //     try {
    //         String statsFormatted = reportingPresenceService.getGlobalStatsFormatted();
    //         if (statsFormatted == null || statsFormatted.isEmpty()) {
    //             return ResponseEntity.noContent().build();
    //         }
    //         return ResponseEntity.ok(statsFormatted);
    //     } catch (Exception e) {
    //         return ResponseEntity.internalServerError()
    //             .body("Erreur lors de la récupération des statistiques");
    //     }
    // }
    
    // 10. Statistiques par département
    @GetMapping("/stats-par-departement")
    public ResponseEntity<List<Object[]>> getStatsByDepartement() {
        try {
            List<Object[]> stats = reportingPresenceService.getStatsByDepartement();
            if (stats == null || stats.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 11. Employés avec plus de X jours de congés
    @GetMapping("/employes-conges-superieurs")
    public ResponseEntity<List<ReportingPresenceDTO>> getEmployesAvecCongesSuperieursA(
            @RequestParam(required = false, defaultValue = "10") Integer joursMinimum) {
        try {
            List<ReportingPresenceDTO> result = reportingPresenceService
                    .getEmployesAvecCongesSuperieursA(joursMinimum);
            if (result == null || result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // ==================== ENDPOINTS D'EXPORT ====================
    
    // 12. Exporter en CSV AVEC FILTRES (endpoint principal)
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToCsv(
            @RequestParam(required = false) String departementId,
            @RequestParam(required = false) String nomComplet,
            @RequestParam(required = false) Integer minConges,
            @RequestParam(required = false) Integer maxConges,
            @RequestParam(required = false) String triPar,
            @RequestParam(required = false) String ordreTri) {
        
        try {
            // Créer le filtre avec TOUS les paramètres
            ReportingPresenceFilterDTO filter = new ReportingPresenceFilterDTO();
            filter.setDepartementId(departementId);
            filter.setNomComplet(nomComplet);
            filter.setMinConges(minConges);
            filter.setMaxConges(maxConges);
            filter.setTriPar(triPar);
            filter.setOrdreTri(ordreTri);
            
            // Générer le CSV filtré
            byte[] csvBytes = reportingPresenceService.generateCsvExport(filter);
            
            // Générer un nom de fichier intelligent
            String filename = reportingPresenceService.generateExportFilename(filter);
            
            // Préparer les headers pour le téléchargement
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv; charset=utf-8"));
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(csvBytes.length);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 13. Exporter en CSV COMPLET (sans filtres)
    @GetMapping("/export/complet")
    public ResponseEntity<byte[]> exportAllCsv() {
        try {
            // Générer le CSV complet
            byte[] csvBytes = reportingPresenceService.generateCsvExportAll();
            
            // Nom de fichier
            String filename = "reporting_presence_complet_" + LocalDate.now() + ".csv";
            
            // Préparer les headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv; charset=utf-8"));
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(csvBytes.length);
            
            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 14. Exporter par département seulement
    @GetMapping("/export/departement/{departementId}")
    public ResponseEntity<byte[]> exportByDepartement(@PathVariable String departementId) {
        try {
            if (departementId == null || departementId.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Générer le CSV par département
            byte[] csvBytes = reportingPresenceService.generateCsvExportByDepartement(departementId);
            
            // Nom de fichier
            String deptSlug = departementId.toLowerCase().replaceAll("\\s+", "_");
            String filename = "reporting_presence_" + deptSlug + "_" + LocalDate.now() + ".csv";
            
            // Préparer les headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv; charset=utf-8"));
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(csvBytes.length);
            
            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // 15. Exporter avec filtres (POST - pour filtres complexes)
    @PostMapping("/export/filtres")
    public ResponseEntity<byte[]> exportWithFilters(@RequestBody ReportingPresenceFilterDTO filter) {
        try {
            if (filter == null) {
                return ResponseEntity.badRequest().build();
            }
            
            // Générer le CSV avec les filtres du body
            byte[] csvBytes = reportingPresenceService.generateCsvExport(filter);
            
            // Générer un nom de fichier intelligent
            String filename = reportingPresenceService.generateExportFilename(filter);
            
            // Préparer les headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv; charset=utf-8"));
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(csvBytes.length);
            
            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // ==================== ENDPOINTS DE TEST ====================
    
    // 16. Tester l'export (pour debug)
    @GetMapping("/export/test")
    public ResponseEntity<String> testExport() {
        try {
            // Créer un filtre test
            ReportingPresenceFilterDTO filter = new ReportingPresenceFilterDTO();
            filter.setDepartementId("Ressources Humaines");
            
            // Générer le CSV
            byte[] csvBytes = reportingPresenceService.generateCsvExport(filter);
            String csvContent = new String(csvBytes, StandardCharsets.UTF_8);
            
            // Retourner le contenu pour test
            return ResponseEntity.ok(
                "Test export réussi\nLignes: " + csvContent.split("\n").length + "\n\n" + csvContent
            );
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Erreur test export: " + e.getMessage());
        }
    }
    
    // 17. Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("API Reporting Présence est opérationnelle");
    }
    
    // ==================== MÉTHODES SUPPRIMÉES ====================
    // Les méthodes suivantes ont été supprimées car elles sont DOUBLONS :
    // 1. @GetMapping("/export-csv") → Remplacée par @GetMapping("/export")
    // 2. @GetMapping("/export") (ligne 253) → CONFLIT avec la nouvelle méthode
    // 3. private String generateCsv() → Déplacée dans le Service
    // 4. private String escapeCsv() → Déplacée dans le Service
}