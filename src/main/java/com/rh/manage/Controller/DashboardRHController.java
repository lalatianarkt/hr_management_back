package com.rh.manage.Controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rh.manage.Dto.AlerteDTO;
import com.rh.manage.Dto.DashboardRHDTO;
import com.rh.manage.Dto.KPIDTO;
import com.rh.manage.Service.DashboardRHService;
import com.rh.manage.Service.TokenService;
import com.rh.manage.Service.TokenService.TokenException;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardRHController {

    @Autowired
    private TokenService tokenService;

    @Autowired 
    private DashboardRHService dashboardRHService;

    // @GetMapping
    // public ResponseEntity<?> getDashboard() {
    //     try { 
    //         // tokenService.validateToken(authHeader);
    //         DashboardRHDTO dashboard = dashboardRHService.getDashboardRH();
    //         return ResponseEntity.ok(dashboard);
    //     }  
    //     catch (Exception e) {
    //         e.printStackTrace();
    //         System.out.println("error : " + e.getMessage());
    //         // Retourner une erreur HTTP simple
    //         Map<String, Object> errorResponse = new HashMap<>();
    //         errorResponse.put("timestamp", java.time.LocalDateTime.now());
    //         errorResponse.put("status", 500);
    //         errorResponse.put("error", "Internal Server Error");
    //         errorResponse.put("message", "Erreur lors de la génération du dashboard: " + e.getMessage());
    //         errorResponse.put("path", "/api/dashboard-rh");
            
    //         return ResponseEntity.status(500).body(errorResponse);
    //     }
    // }

    @GetMapping("/periode")
    public ResponseEntity<?> getDashboardParPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin
        ) {
        try {
            if (dateDebut == null || dateFin == null) {
                return ResponseEntity.badRequest()
                    .body(creerDashboardErreur("Les dates de début et de fin sont obligatoires"));
            }
            
            if (dateDebut.isAfter(dateFin)) {
                return ResponseEntity.badRequest()
                    .body(creerDashboardErreur("La date de début doit être antérieure à la date de fin"));
            }
            DashboardRHDTO dashboard = dashboardRHService.getDashboardRHByPeriode(dateDebut, dateFin);
            return ResponseEntity.ok(dashboard);
            
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            return ResponseEntity.internalServerError()
                .body(creerDashboardErreur("Erreur lors de la génération du dashboard: " + e.getMessage()));
        }
    }

    /**
     * Crée un dashboard d'erreur pour les cas d'échec
     */
    private DashboardRHDTO creerDashboardErreur(String message) {
        DashboardRHDTO dashboard = new DashboardRHDTO();
        dashboard.setDateGeneration(java.time.LocalDate.now());
        dashboard.setPeriode("Erreur");
        
        // Créer un KPI d'erreur
        KPIDTO kpiErreur = new KPIDTO();
        kpiErreur.setTitre("Erreur");
        kpiErreur.setValeur(0);
        kpiErreur.setUnite("");
        kpiErreur.setCouleur("danger");
        kpiErreur.setIcone("warning");
        kpiErreur.setDescription(message);
        
        dashboard.setEffectifTotal(kpiErreur);
        
        // Ajouter une alerte
        AlerteDTO alerte = new AlerteDTO();
        alerte.setType("system");
        alerte.setTitre("Erreur système");
        alerte.setMessage(message);
        alerte.setNiveau("eleve");
        alerte.setActionRecommandee("Contacter l'administrateur système");
        alerte.setDateDetection(java.time.LocalDate.now());
        
        dashboard.setAlertes(java.util.Collections.singletonList(alerte));
        
        return dashboard;
    }
}
