package com.rh.manage.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.ServiceUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.rh.manage.Dto.CongeParTypeDTO;
import com.rh.manage.Dto.StatCongeDTO;
import com.rh.manage.Model.DemandeConge;
import com.rh.manage.Model.VueStatistiquesEmployeDemandes;
import com.rh.manage.Service.CongeParTypeService;
import com.rh.manage.Service.DemandeCongeService;
import com.rh.manage.Service.StatCongeService;
import com.rh.manage.Service.TypeCongeService;
import com.rh.manage.Service.VueStatistiquesEmployeDemandesService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statConge")
public class StatCongeController {
    @Autowired
    DemandeCongeService demandeCongeService;

    @Autowired 
    StatCongeService statCongeService;

    @Autowired
    VueStatistiquesEmployeDemandesService vueStatistiquesEmployeDemandesService;

    @Autowired
    CongeParTypeService congeParTypeService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getStatistiqueDashboard() {
        try {
            // Récupérer toutes les données (pourrait être en parallèle)
            StatCongeDTO stat = statCongeService.getStatistiqueConge();
            List<VueStatistiquesEmployeDemandes> les_top_emp = vueStatistiquesEmployeDemandesService.getTopEmployesByDemandes(5);
            List<CongeParTypeDTO> les_stat_conge_par_type = congeParTypeService.getStatTypeConge();
            List<DemandeConge> les_demandes_recentes = demandeCongeService.getDemandesRecentest(5);
            
            // Vérifier que les données de base sont présentes
            if (stat == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Statistiques générales non disponibles");
            }
            
            // Construire la réponse
            Map<String, Object> response = new LinkedHashMap<>();
            
            // Données principales
            response.put("success", true);
            response.put("message", "Données du dashboard récupérées avec succès");
            response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            // Données du dashboard
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("statistiquesGenerales", stat);
            
            if (les_top_emp != null && !les_top_emp.isEmpty()) {
                data.put("topEmployes", les_top_emp);
            } else {
                data.put("topEmployes", Collections.emptyList());
            }
            
            if (les_stat_conge_par_type != null && !les_stat_conge_par_type.isEmpty()) {
                data.put("repartitionParType", les_stat_conge_par_type);
            } else {
                data.put("repartitionParType", Collections.emptyList());
            }
            
            if (les_demandes_recentes != null && !les_demandes_recentes.isEmpty()) {
                data.put("demandesRecentess", les_demandes_recentes);
            } else {
                data.put("demandesRecentess", Collections.emptyList());
            }
            
            response.put("data", data);
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Log l'erreur
            // logger.error("Erreur lors de la récupération du dashboard", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Erreur lors du chargement du dashboard");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    
}
