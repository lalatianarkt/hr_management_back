package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Model.VueDemandeConge;
import com.rh.manage.Repository.VueDemandeCongeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VueDemandeCongeService {
    
    @Autowired
    private VueDemandeCongeRepository vueDemandeCongeRepository;
    
    // Récupérer toutes les demandes
    public List<VueDemandeConge> getAllDemandes() {
        return vueDemandeCongeRepository.findAll();
    }
    
    // Recherche multicritère avancée
    public List<VueDemandeConge> rechercherDemandesAvance(
            String idEmploye,
            String matricule,
            String nomEmploye,
            String prenomEmploye,
            String idDepartement,
            String idManager,
            String statut,
            LocalDate dateDebut,
            LocalDate dateFin) {
        
        return vueDemandeCongeRepository.rechercherDemandesAvance(
            idEmploye, matricule, nomEmploye, prenomEmploye, 
            idDepartement, idManager, statut, dateDebut, dateFin);
    }
    
    // Obtenir les demandes d'un employé par ID
    public List<VueDemandeConge> getByIdEmploye(String idEmploye) {
        return vueDemandeCongeRepository.findByIdEmploye(idEmploye);
    }
    
    // Obtenir les demandes par matricule
    public List<VueDemandeConge> getByMatricule(String matricule) {
        return vueDemandeCongeRepository.findByMatricule(matricule);
    }
    
    // Obtenir les demandes en attente
    public List<VueDemandeConge> getDemandesEnAttente() {
        return vueDemandeCongeRepository.findDemandesEnAttente();
    }
    
    // Obtenir les demandes validées
    public List<VueDemandeConge> getDemandesValidees() {
        return vueDemandeCongeRepository.findDemandesValidees();
    }
    
    // Obtenir les demandes refusées
    public List<VueDemandeConge> getDemandesRefusees() {
        return vueDemandeCongeRepository.findDemandesRefusees();
    }
    
    // Obtenir les demandes annulées par le demandeur
    public List<VueDemandeConge> getDemandesAnnuleesParDemandeur() {
        return vueDemandeCongeRepository.findDemandesAnnuleesParDemandeur();
    }
    
    // Obtenir les demandes annulées par le RH
    public List<VueDemandeConge> getDemandesAnnuleesParRH() {
        return vueDemandeCongeRepository.findDemandesAnnuleesParRH();
    }
    
    // Recherche par département (ID)
    public List<VueDemandeConge> getByIdDepartement(String idDepartement) {
        return vueDemandeCongeRepository.findByIdDepartement(idDepartement);
    }
    
    // Recherche par manager (ID)
    public List<VueDemandeConge> getByIdManager(String idManager) {
        return vueDemandeCongeRepository.findByIdManager(idManager);
    }
    
    // Obtenir les demandes d'un manager avec statut spécifique
    public List<VueDemandeConge> getByIdManagerAndStatut(String idManager, String statut) {
        return vueDemandeCongeRepository.findByIdManagerAndDecisionManagerLibelle(idManager, statut);
    }
    
    // Statistiques par département détaillées
    public List<Map<String, Object>> getStatsByDepartementDetail() {
        List<Object[]> results = vueDemandeCongeRepository.countByDepartementDetail();
        return results.stream()
            .map(obj -> Map.of(
                "idDepartement", obj[0],
                "nomDepartement", obj[1],
                "nombreDemandes", obj[2]
            ))
            .collect(Collectors.toList());
    }
    
    // Statistiques par statut
    public Map<String, Long> getStatsByStatut() {
        List<Object[]> results = vueDemandeCongeRepository.countByStatut();
        return results.stream()
            .collect(Collectors.toMap(
                obj -> (String) obj[0],
                obj -> (Long) obj[1]
            ));
    }
    
    // Statistiques par manager
    public List<Map<String, Object>> getStatsByManager() {
        List<Object[]> results = vueDemandeCongeRepository.countByManager();
        return results.stream()
            .map(obj -> Map.of(
                "idManager", obj[0],
                "nomManager", obj[1],
                "nombreDemandes", obj[2]
            ))
            .collect(Collectors.toList());
    }
    
    // Recherche par matricule ou nom
    public List<VueDemandeConge> searchByMatriculeOrName(String search) {
        return vueDemandeCongeRepository.searchByMatriculeOrName(search);
    }
    
    // Obtenir les demandes d'un employé avec filtres
    public List<VueDemandeConge> getByEmployeWithFilters(String idEmploye, String statut, Integer annee) {
        return vueDemandeCongeRepository.findByEmployeWithFilters(idEmploye, statut, annee);
    }
    
    // Filtrer par période
    public List<VueDemandeConge> getByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return vueDemandeCongeRepository.findByDateDebutBetween(dateDebut, dateFin);
    }
    
    // Obtenir une demande par ID
    public VueDemandeConge getById(String id) {
        return vueDemandeCongeRepository.findById(id).orElse(null);
    }
    
    // Compter les demandes par statut pour un employé
    public Map<String, Long> countByStatutForEmploye(String idEmploye) {
        List<VueDemandeConge> demandes = vueDemandeCongeRepository.findByIdEmploye(idEmploye);
        return demandes.stream()
            .collect(Collectors.groupingBy(
                VueDemandeConge::getDecisionManagerLibelle,
                Collectors.counting()
            ));
    }
    
    // Compter les demandes par statut pour un département
    public Map<String, Long> countByStatutForDepartement(String idDepartement) {
        List<VueDemandeConge> demandes = vueDemandeCongeRepository.findByIdDepartement(idDepartement);
        return demandes.stream()
            .collect(Collectors.groupingBy(
                VueDemandeConge::getDecisionManagerLibelle,
                Collectors.counting()
            ));
    }
}
