package com.rh.manage.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Dto.PointageDetailDTO;
import com.rh.manage.Dto.PointageEmployeAgregatDTO;
import com.rh.manage.Dto.StatistiquesDepartementDTO;
import com.rh.manage.Dto.StatistiquesPointageDTO;
import com.rh.manage.Service.VuePointageEmployeV2Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/pointages")
public class VuePointageEmployeV2Controller {
    
    private final VuePointageEmployeV2Service service;
    
    public VuePointageEmployeV2Controller(VuePointageEmployeV2Service service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<?> getAllPointages() {
        return ResponseEntity.ok(service.getAllPointages());
    }
    
    @GetMapping("/periode")
    public ResponseEntity<List<PointageDetailDTO>> getPointagesByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(service.getPointagesByPeriode(dateDebut, dateFin));
    }
    
    @GetMapping("/employe/{idEmploye}")
    public ResponseEntity<List<PointageDetailDTO>> getPointagesByEmploye(
            @PathVariable String idEmploye) {
        return ResponseEntity.ok(service.getPointagesByEmploye(idEmploye));
    }
    
    @GetMapping("/employe/{idEmploye}/periode")
    public ResponseEntity<List<PointageDetailDTO>> getPointagesByEmployeAndPeriode(
            @PathVariable String idEmploye,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(service.getPointagesByEmployeAndPeriode(idEmploye, dateDebut, dateFin));
    }
    
    @GetMapping("/departement/{idDepartement}")
    public ResponseEntity<List<PointageDetailDTO>> getPointagesByDepartement(
            @PathVariable String idDepartement) {
        return ResponseEntity.ok(service.getPointagesByDepartement(idDepartement));
    }
    
    @GetMapping("/departement/{idDepartement}/periode")
    public ResponseEntity<List<PointageDetailDTO>> getPointagesByDepartementAndPeriode(
            @PathVariable String idDepartement,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(service.getPointagesByDepartementAndPeriode(
                idDepartement, dateDebut, dateFin));
    }
    
    @GetMapping("/agregats")
    public ResponseEntity<List<PointageEmployeAgregatDTO>> getAgregats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(service.getAgregatsByPeriode(dateDebut, dateFin));
    }
    
    @GetMapping("/top/retardataires")
    public ResponseEntity<List<PointageEmployeAgregatDTO>> getTopRetardataires(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(service.getTopRetardataires(dateDebut, dateFin, limit));
    }
    
    @GetMapping("/top/travailleurs")
    public ResponseEntity<List<PointageEmployeAgregatDTO>> getTopTravailleurs(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(service.getTopTravailleurs(dateDebut, dateFin, limit));
    }
    
    @GetMapping("/top/heures-sup")
    public ResponseEntity<List<PointageEmployeAgregatDTO>> getTopHeuresSupplementaires(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(service.getTopHeuresSupplementaires(dateDebut, dateFin, limit));
    }
    
    @GetMapping("/statistiques")
    public ResponseEntity<StatistiquesPointageDTO> getStatistiques(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(service.getStatistiques(dateDebut, dateFin));
    }
    
    @GetMapping("/statistiques/departement")
    public ResponseEntity<List<StatistiquesDepartementDTO>> getStatistiquesDepartement(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(service.getStatistiquesDepartement(dateDebut, dateFin));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<PointageDetailDTO>> searchPointages(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(required = false) Integer minHeures,
            @RequestParam(required = false) Integer maxHeures,
            @RequestParam(required = false) Integer minRetard,
            @RequestParam(required = false) Integer maxRetard,
            @RequestParam(required = false) Integer minHeuresSup,
            @RequestParam(required = false) Integer maxHeuresSup) {
        
        return ResponseEntity.ok(service.searchPointages(
                matricule, nom, dateDebut, dateFin,
                minHeures, maxHeures, minRetard, maxRetard,
                minHeuresSup, maxHeuresSup));
    }
    
    // ==================== NOUVELLES MÉTHODES AVEC PAGINATION ====================
    
    /**
     * Récupère tous les pointages avec pagination
     */
    @GetMapping("/pagine")
    public ResponseEntity<Map<String, Object>> getAllPointagesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<PointageDetailDTO> pagePointages = service.getAllPointagesPaginated(page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("pointages", pagePointages.getContent());
        response.put("currentPage", pagePointages.getNumber());
        response.put("totalItems", pagePointages.getTotalElements());
        response.put("totalPages", pagePointages.getTotalPages());
        response.put("pageSize", pagePointages.getSize());
        response.put("isFirst", pagePointages.isFirst());
        response.put("isLast", pagePointages.isLast());
        response.put("hasNext", pagePointages.hasNext());
        response.put("hasPrevious", pagePointages.hasPrevious());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Filtre les pointages avec pagination (version simple)
     */
    @GetMapping("/filtre")
    public ResponseEntity<?> filterPointages(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String idDepartement,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
                try {
                    Page<PointageDetailDTO> pagePointages = service.filterPointages(
                            matricule, startDate, endDate, idDepartement, page, size);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("pointages", pagePointages.getContent());
                    response.put("currentPage", pagePointages.getNumber());
                    response.put("totalItems", pagePointages.getTotalElements());
                    response.put("totalPages", pagePointages.getTotalPages());
                    response.put("pageSize", pagePointages.getSize());
                    response.put("isFirst", pagePointages.isFirst());
                    response.put("isLast", pagePointages.isLast());
                    response.put("hasNext", pagePointages.hasNext());
                    response.put("hasPrevious", pagePointages.hasPrevious());
                    
                    // Ajouter les filtres appliqués pour information
                    Map<String, Object> filters = new HashMap<>();
                    filters.put("matricule", matricule);
                    filters.put("startDate", startDate);
                    filters.put("endDate", endDate);
                    filters.put("idDepartement", idDepartement);
                    response.put("appliedFilters", filters);
                    
                    return ResponseEntity.ok(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Erreur lors du filtre : " + e.getMessage());
                }
        
        
    }
    
    /**
     * Filtre les pointages avec pagination et tri avancé
     */
    @GetMapping("/filtre-avance")
    public ResponseEntity<Map<String, Object>> filterPointagesAdvanced(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String idDepartement,
            @RequestParam(required = false) String departementNom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "datePointage") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<PointageDetailDTO> pagePointages;
        
        if (departementNom != null && !departementNom.isEmpty()) {
            pagePointages = service.filterPointagesWithDepartementName(
                    matricule, startDate, endDate, idDepartement, departementNom, pageable);
        } else {
            pagePointages = service.filterPointagesWithSort(
                    matricule, startDate, endDate, idDepartement, pageable);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("pointages", pagePointages.getContent());
        response.put("currentPage", pagePointages.getNumber());
        response.put("totalItems", pagePointages.getTotalElements());
        response.put("totalPages", pagePointages.getTotalPages());
        response.put("pageSize", pagePointages.getSize());
        response.put("isFirst", pagePointages.isFirst());
        response.put("isLast", pagePointages.isLast());
        response.put("hasNext", pagePointages.hasNext());
        response.put("hasPrevious", pagePointages.hasPrevious());
        response.put("sortBy", sortBy);
        response.put("sortDirection", sortDirection);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Compte le nombre de pointages correspondant aux filtres
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countPointagesByFilters(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String idDepartement) {
        
        long count = service.countPointagesByFilters(matricule, startDate, endDate, idDepartement);
        
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        response.put("matricule", matricule);
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("idDepartement", idDepartement);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Export des pointages filtrés (sans pagination)
     */
    @GetMapping("/export")
    public ResponseEntity<List<PointageDetailDTO>> exportPointages(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String idDepartement) {
        
        Page<PointageDetailDTO> pagePointages = service.filterPointages(
                matricule, startDate, endDate, idDepartement, 0, Integer.MAX_VALUE);
        
        return ResponseEntity.ok(pagePointages.getContent());
    }
}