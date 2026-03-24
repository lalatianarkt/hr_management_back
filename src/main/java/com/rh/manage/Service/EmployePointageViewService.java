package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rh.manage.Repository.EmployePointageViewRepository;
import com.rh.manage.View.EmployePointageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployePointageViewService {
    
    @Autowired
    private EmployePointageViewRepository employePointageViewRepository;
    
    /**
     * Récupérer tous les employés (actifs seulement)
     */
    public List<EmployePointageView> getAllEmployesActifs() {
        return employePointageViewRepository.findAllActifs();
    }
    
    /**
     * Récupérer un employé par son ID
     */
    public Optional<EmployePointageView> getEmployeById(String id) {
        return employePointageViewRepository.findById(id);
    }
    
    /**
     * Récupérer un employé par son matricule
     */
    public Optional<EmployePointageView> getEmployeByMatricule(String matricule) {
        return employePointageViewRepository.findByMatricule(matricule);
    }
    
    /**
     * Rechercher des employés par nom ou prénom
     */
    public List<EmployePointageView> searchEmployes(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllEmployesActifs();
        }
        
        String search = searchTerm.trim();
        return employePointageViewRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(search, search);
    }
    
    /**
     * Rechercher avec filtres multiples
     */
    public List<EmployePointageView> searchWithFilters(String matricule, String nom, String prenom, String departement, String poste) {
        return employePointageViewRepository.findByFilters(
            matricule, nom, prenom, departement, poste
        ).stream()
         .filter(e -> e.isActif())
         .collect(Collectors.toList());
    }
    
    /**
     * Récupérer les employés par département
     */
    public List<EmployePointageView> getEmployesByDepartement(String departement) {
        return employePointageViewRepository.findByDepartement(departement)
                .stream()
                .filter(e -> e.isActif())
                .collect(Collectors.toList());
    }
    
    /**
     * Récupérer les employés par poste
     */
    public List<EmployePointageView> getEmployesByPoste(String poste) {
        return employePointageViewRepository.findByPoste(poste)
                .stream()
                .filter(e -> e.isActif())
                .collect(Collectors.toList());
    }
    
    /**
     * Récupérer avec pagination
     */
    public Page<EmployePointageView> getAllEmployesActifs(Pageable pageable) {
        return employePointageViewRepository.findAll(pageable);
    }
    
    /**
     * Récupérer les statistiques par département
     */
    public Map<String, Long> getStatsByDepartement() {
        List<Object[]> stats = employePointageViewRepository.countByDepartement();
        Map<String, Long> result = new HashMap<>();
        
        for (Object[] stat : stats) {
            String departement = (String) stat[0];
            Long count = (Long) stat[1];
            result.put(departement, count);
        }
        
        return result;
    }
    
    /**
     * Récupérer les statistiques par poste
     */
    public Map<String, Long> getStatsByPoste() {
        List<Object[]> stats = employePointageViewRepository.countByPoste();
        Map<String, Long> result = new HashMap<>();
        
        for (Object[] stat : stats) {
            String poste = (String) stat[0];
            Long count = (Long) stat[1];
            result.put(poste, count);
        }
        
        return result;
    }
    
    /**
     * Récupérer tous les départements distincts
     */
    public List<String> getAllDepartements() {
        return employePointageViewRepository.findAll()
                .stream()
                .filter(e -> e.isActif())
                .map(EmployePointageView::getDepartement)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    /**
     * Récupérer tous les postes distincts
     */
    public List<String> getAllPostes() {
        return employePointageViewRepository.findAll()
                .stream()
                .filter(e -> e.isActif())
                .map(EmployePointageView::getPoste)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    /**
     * Récupérer les employés pour le dropdown (format pour select)
     */
    // public List<Map<String, Object>> getEmployesForDropdown() {
    //     return getAllEmployesActifs()
    //             .stream()
    //             .map(e -> Map.of(
    //                 "id", e.getId(),
    //                 "matricule", e.getMatricule(),
    //                 "nom", e.getNom(),
    //                 "prenom", e.getPrenom(),
    //                 "nomComplet", e.getNomComplet(),
    //                 "affichageComplet", e.getAffichageComplet(),
    //                 "departement", e.getDepartement(),
    //                 "poste", e.getPoste(),
    //                 "display", e.getAffichageComplet()
    //             ))
    //             .collect(Collectors.toList());
    // }
    
    /**
     * Vérifier si un matricule existe
     */
    public boolean matriculeExists(String matricule) {
        return employePointageViewRepository.existsByMatricule(matricule);
    }
    
    /**
     * Récupérer le nombre total d'employés actifs
     */
    public long countEmployesActifs() {
        return getAllEmployesActifs().size();
    }
}
