package com.rh.manage.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rh.manage.Dto.EmployePaieDTO;
import com.rh.manage.Service.EmployePaieService;
@RestController
@RequestMapping("/api/employes-paie")
public class EmployePaieController {
    @Autowired
    EmployePaieService employePaieService;

    @GetMapping
    public ResponseEntity<List<EmployePaieDTO>> getAllEmployesPaie() {
        List<EmployePaieDTO> resultats = employePaieService.getAllEmpwithPaie();
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/employes-paie/etat/{etat}")
    public ResponseEntity<List<EmployePaieDTO>> getEmployesByEtat(@PathVariable int etat) {
        List<EmployePaieDTO> resultats = employePaieService.getEmployesByEtat(etat);
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/employes-paie/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("inactif", employePaieService.countByEtat(0));
        stats.put("enAttente", employePaieService.countByEtat(1));
        stats.put("actif", employePaieService.countByEtat(2));
        return ResponseEntity.ok(stats);
    }
}
