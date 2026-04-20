package com.rh.manage.Controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
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
import com.rh.manage.Dto.TendanceMensuelleDTO;
import com.rh.manage.Service.DashboardRHService;
import com.rh.manage.Service.JwtService;
import com.rh.manage.Service.TokenService;
import com.rh.manage.Service.TokenService.TokenException;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardRHController {

    @Autowired
    private TokenService tokenService;

    @Autowired 
    private DashboardRHService dashboardRHService;

    @Autowired
    private JwtService jwtService;

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

    @GetMapping("/taux-presence")
    public ResponseEntity<?> getTauxPresence(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        try {
            if (dateDebut == null || dateFin == null) {
                return ResponseEntity.badRequest()
                        .body(creerDashboardErreur("Les dates de dÃ©but et de fin sont obligatoires"));
            }
            if (dateDebut.isAfter(dateFin)) {
                return ResponseEntity.badRequest()
                        .body(creerDashboardErreur("La date de dÃ©but doit Ãªtre antÃ©rieure Ã  la date de fin"));
            }

            double taux = dashboardRHService.getTauxPresence(dateDebut, dateFin);

            Map<String, Object> response = new HashMap<>();
            response.put("dateDebut", dateDebut);
            response.put("dateFin", dateFin);
            response.put("tauxPresence", taux);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(creerDashboardErreur("Erreur lors du calcul du taux de prÃ©sence: " + e.getMessage()));
        }
    }

    @GetMapping("/salaireBrut")
    public ResponseEntity<?> getSalaireBrut(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        try {
            if (dateDebut == null || dateFin == null) {
                return ResponseEntity.badRequest()
                        .body(creerDashboardErreur("Les dates de dÃ©but et de fin sont obligatoires"));
            }
            if (dateDebut.isAfter(dateFin)) {
                return ResponseEntity.badRequest()
                        .body(creerDashboardErreur("La date de dÃ©but doit Ãªtre antÃ©rieure Ã  la date de fin"));
            }
            double salaireBrut = dashboardRHService.calculerSalaireBrut(dateDebut, dateFin);
            return ResponseEntity.ok(salaireBrut);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(creerDashboardErreur("Erreur lors du calcul de la masse salariale: " + e.getMessage()));
        }
    }
    

    @GetMapping("/masse-salariale")
    public ResponseEntity<?> getMasseSalariale(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        try {
            if (dateDebut == null || dateFin == null) {
                return ResponseEntity.badRequest()
                        .body(creerDashboardErreur("Les dates de dÃ©but et de fin sont obligatoires"));
            }
            if (dateDebut.isAfter(dateFin)) {
                return ResponseEntity.badRequest()
                        .body(creerDashboardErreur("La date de dÃ©but doit Ãªtre antÃ©rieure Ã  la date de fin"));
            }

            double masse = dashboardRHService.getMasseSalariale(dateDebut, dateFin);

            Map<String, Object> response = new HashMap<>();
            response.put("dateDebut", dateDebut);
            response.put("dateFin", dateFin);
            response.put("masseSalariale", masse);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(creerDashboardErreur("Erreur lors du calcul de la masse salariale: " + e.getMessage()));
        }
    }

    @GetMapping("/manager")
    public ResponseEntity<?> getDashboardManagerParPeriode0(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
        @RequestHeader("Authorization") String authHeader){
        try { 
            String token = authHeader.substring(7);
            Claims claims = jwtService.validateToken(token);
            return ResponseEntity.ok(dashboardRHService.getDashboardManagerParPeriodeParManager(dateDebut, dateFin, claims));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            return ResponseEntity.internalServerError()
                .body(creerDashboardErreur("Erreur lors de la génération du dashboard: " + e.getMessage()));
        }
    }
    

    @GetMapping("/tendances")
    public ResponseEntity<?> getTendancesParPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin
        ) {
        try {
            if (dateDebut == null || dateFin == null) {
                return ResponseEntity.badRequest()
                    .body(creerDashboardErreur("Les dates de dÃ©but et de fin sont obligatoires"));
            }

            if (dateDebut.isAfter(dateFin)) {
                return ResponseEntity.badRequest()
                    .body(creerDashboardErreur("La date de dÃ©but doit Ãªtre antÃ©rieure Ã  la date de fin"));
            }

            List<TendanceMensuelleDTO> tendances = dashboardRHService.getTendancesParPeriode(dateDebut, dateFin);
            return ResponseEntity.ok(tendances);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            return ResponseEntity.internalServerError()
                .body(creerDashboardErreur("Erreur lors du calcul des tendances: " + e.getMessage()));
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
