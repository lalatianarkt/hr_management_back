package com.rh.manage.Service;

import com.rh.manage.Model.VueStatistiquesEmployeDemandes;
import com.rh.manage.Repository.VueStatistiquesEmployeDemandesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class VueStatistiquesEmployeDemandesService {
    
    @Autowired
    private VueStatistiquesEmployeDemandesRepository repository;
    
    /**
     * Récupère toutes les statistiques
     */
    public List<VueStatistiquesEmployeDemandes> getAllStatistiques() {
        return repository.findAll();
    }
    
    /**
     * Récupère les statistiques d'un employé spécifique
     */
    public Optional<VueStatistiquesEmployeDemandes> getStatistiquesByEmployeId(String employeId) {
        return repository.findByEmployeId(employeId);
    }
    
    /**
     * Récupère les top N employés avec le plus de demandes
     */
    public List<VueStatistiquesEmployeDemandes> getTopEmployesByDemandes(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("nombreDemandes").descending());
        Page<VueStatistiquesEmployeDemandes> page = repository.findTopByNombreDemandes(pageable);
        return page.getContent();
    }
    
    /**
     * Récupère les top 5 employés avec le plus de demandes
     */
    public List<VueStatistiquesEmployeDemandes> getTop5EmployesByDemandes() {
        return getTopEmployesByDemandes(5);
    }
    
    /**
     * Récupère les top N employés avec le plus de jours demandés
     */
    public List<VueStatistiquesEmployeDemandes> getTopEmployesByJoursDemandes(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("totalJoursDemandes").descending());
        Page<VueStatistiquesEmployeDemandes> page = repository.findTopByTotalJoursDemandes(pageable);
        return page.getContent();
    }
    
    /**
     * Récupère les employés avec des demandes en attente
     */
    public List<VueStatistiquesEmployeDemandes> getEmployesAvecDemandesEnAttente() {
        return repository.findByDemandesEnAttenteGreaterThan(0L);
    }
    
    /**
     * Récupère les employés avec un taux d'approbation élevé (> 80%)
     */
    public List<VueStatistiquesEmployeDemandes> getEmployesAvecTauxApprobationEleve() {
        return repository.findByTauxApprobationGreaterThan(80.0);
    }
    
    /**
     * Recherche d'employés par nom ou prénom
     */
    public List<VueStatistiquesEmployeDemandes> searchEmployes(String searchTerm) {
        return repository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(searchTerm, searchTerm);
    }
    
    /**
     * Récupère les statistiques globales
     */
    public Map<String, Object> getStatistiquesGlobales() {
        Object[] result = repository.getStatistiquesGlobales();
        
        if (result == null || result[0] == null) {
            return Collections.emptyMap();
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEmployes", result[0]);
        stats.put("totalDemandes", result[1]);
        stats.put("totalJours", result[2]);
        stats.put("moyenneGlobaleJours", result[3]);
        stats.put("tauxApprobationMoyen", result[4]);
        
        return stats;
    }
    
    /**
     * Récupère les employés avec pagination
     */
    public Page<VueStatistiquesEmployeDemandes> getEmployesWithPagination(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return repository.findAll(pageable);
    }
    
    /**
     * Récupère les employés avec filtres
     */
    public List<VueStatistiquesEmployeDemandes> getEmployesWithFilters(Long minDemandes, Long maxDemandes, 
                                                                       Double minTauxApprobation) {
        List<VueStatistiquesEmployeDemandes> result = new ArrayList<>();
        
        if (minDemandes != null) {
            result = repository.findByNombreDemandesGreaterThan(minDemandes);
        }
        
        if (maxDemandes != null) {
            List<VueStatistiquesEmployeDemandes> filtered = repository.findByNombreDemandesLessThan(maxDemandes);
            if (result.isEmpty()) {
                result = filtered;
            } else {
                result.retainAll(filtered);
            }
        }
        
        if (minTauxApprobation != null) {
            List<VueStatistiquesEmployeDemandes> filtered = repository.findByTauxApprobationGreaterThan(minTauxApprobation);
            if (result.isEmpty()) {
                result = filtered;
            } else {
                result.retainAll(filtered);
            }
        }
        
        return result;
    }
}