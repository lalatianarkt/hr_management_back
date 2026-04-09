package com.rh.manage.Controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Dto.AbsenceCongeDTO;
import com.rh.manage.Dto.AbsenceCongeFilterDTO;
import com.rh.manage.Dto.StatistiquesAbsenceDTO;
import com.rh.manage.Service.AbsenceCongeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/absences-conges")
public class AbsenceCongeController {
    
    private final AbsenceCongeService absenceCongeService;

    public AbsenceCongeController(AbsenceCongeService absenceCongeService) {
        this.absenceCongeService = absenceCongeService;
    }

    @GetMapping
    public ResponseEntity<List<AbsenceCongeDTO>> getAll() {
        return ResponseEntity.ok(absenceCongeService.getAllAbsencesConges());
    }
    
    @PostMapping("/recherche")
    public ResponseEntity<List<AbsenceCongeDTO>> searchWithFilters(
            @RequestBody AbsenceCongeFilterDTO filter) {
        return ResponseEntity.ok(absenceCongeService.getAbsencesCongesWithFilters(filter));
    }

    @GetMapping("/employe/{idEmp}")
    public ResponseEntity<?> getAbsenceByEmploye(@PathVariable String idEmp){
        return ResponseEntity.ok(absenceCongeService.getAbsenceByEmploye(idEmp));
    } 
    
    // @GetMapping("/employe/{employeId}")
    // public ResponseEntity<List<AbsenceCongeDTO>> getByEmploye(
    //         @PathVariable String employeId) {
    //     return ResponseEntity.ok(absenceCongeService.getByEmploye(employeId));
    // }
    
    @GetMapping("/periode")
    public ResponseEntity<List<AbsenceCongeDTO>> getByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(absenceCongeService.getByPeriode(dateDebut, dateFin));
    }
    
    @GetMapping("/departement/{departementId}")
    public ResponseEntity<List<AbsenceCongeDTO>> getByDepartement(
            @PathVariable String departementId) {
        return ResponseEntity.ok(absenceCongeService.getByDepartement(departementId));
    }
    
    @GetMapping("/statistiques/globales")
    public ResponseEntity<StatistiquesAbsenceDTO> getStatistiquesGlobales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        if (dateDebut != null && dateFin != null) {
            return ResponseEntity.ok(absenceCongeService.getStatistiquesGlobales(dateDebut, dateFin));
        }
        return ResponseEntity.ok(absenceCongeService.getStatistiquesGlobales());
    }
    
    @GetMapping("/statistiques/periodiques")
    public ResponseEntity<StatistiquesAbsenceDTO> getStatistiquesParPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(absenceCongeService.getStatistiquesParPeriode(dateDebut, dateFin));
    }
    
    @GetMapping("/statistiques/employe/{employeId}")
    public ResponseEntity<StatistiquesAbsenceDTO> getStatistiquesParEmploye(
            @PathVariable String employeId) {
        return ResponseEntity.ok(absenceCongeService.getStatistiquesParEmploye(employeId));
    }
    
    @GetMapping("/export")
    public ResponseEntity<String> exportToCsv(
            @RequestParam(required = false) String employeId,
            @RequestParam(required = false) String departementId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        // AbsenceCongeFilterDTO filter = AbsenceCongeFilterDTO.builder()
        //         .employeId(employeId)
        //         .departementId(departementId)
        //         .dateDebut(dateDebut)
        //         .dateFin(dateFin)
        //         .build();
        AbsenceCongeFilterDTO filter = new AbsenceCongeFilterDTO(employeId, departementId, dateDebut, dateFin);
        
        List<AbsenceCongeDTO> data = absenceCongeService.getAbsencesCongesWithFilters(filter);
        String csv = convertToCsv(data);
        
        return ResponseEntity.ok()
                .header("Content-Type", "text/csv")
                .header("Content-Disposition", "attachment; filename=absences-conges.csv")
                .body(csv);
    }
    
    private String convertToCsv(List<AbsenceCongeDTO> data) {
        StringBuilder csv = new StringBuilder();
        csv.append("Date;Employé ID;Nom complet;Matricule;Département;Poste;Type absence\n");
        
        for (AbsenceCongeDTO dto : data) {
            csv.append(dto.getDateAbsence()).append(";")
               .append(dto.getEmployeId()).append(";")
               .append(dto.getNomComplet()).append(";")
               .append(dto.getMatricule()).append(";")
               .append(dto.getDepartementNom()).append(";")
               .append(dto.getNomPoste()).append(";")
               .append(dto.getTypeAbsence()).append("\n");
        }
        
        return csv.toString();
    }
}
