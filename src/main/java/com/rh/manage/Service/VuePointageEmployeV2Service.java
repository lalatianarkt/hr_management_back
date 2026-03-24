package com.rh.manage.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rh.manage.Dto.PointageDetailDTO;
import com.rh.manage.Dto.PointageEmployeAgregatDTO;
import com.rh.manage.Dto.PointageEmployeDTO;
import com.rh.manage.Dto.StatistiquesDepartementDTO;
import com.rh.manage.Dto.StatistiquesPointageDTO;
import com.rh.manage.Model.VuePointageEmployeV2;
import com.rh.manage.Repository.VuePointageEmployeV2Repository;
import com.rh.manage.View.VuePointageEmploye;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VuePointageEmployeV2Service {
    
    private final VuePointageEmployeV2Repository repository;
    
    public VuePointageEmployeV2Service(VuePointageEmployeV2Repository repository) {
        this.repository = repository;
    }
    
    // Récupérer tous les pointages détaillés
    public List<VuePointageEmployeV2> getAllPointages() {
        return repository.findAll();
    }
    
    // Récupérer les pointages par période
    public List<PointageDetailDTO> getPointagesByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return repository.findByDatePointageBetweenOrderByDatePointageDesc(dateDebut, dateFin)
                .stream()
                .map(PointageDetailDTO::new)
                .collect(Collectors.toList());
    }
    
    // Récupérer les pointages d'un employé
    public List<PointageDetailDTO> getPointagesByEmploye(String idEmploye) {
        return repository.findByIdEmployeOrderByDatePointageDesc(idEmploye)
                .stream()
                .map(PointageDetailDTO::new)
                .collect(Collectors.toList());
    }
    
    // Récupérer les pointages d'un employé sur une période
    public List<PointageDetailDTO> getPointagesByEmployeAndPeriode(String idEmploye, 
                                                                  LocalDate dateDebut, 
                                                                  LocalDate dateFin) {
        return repository.findByIdEmployeOrderByDatePointageDesc(idEmploye)
                .stream()
                .filter(p -> !p.getDatePointage().isBefore(dateDebut) && !p.getDatePointage().isAfter(dateFin))
                .map(PointageDetailDTO::new)
                .collect(Collectors.toList());
    }
    
    // Récupérer les pointages par département
    public List<PointageDetailDTO> getPointagesByDepartement(String idDepartement) {
        return repository.findByIdDepartementOrderByDatePointageDesc(idDepartement)
                .stream()
                .map(PointageDetailDTO::new)
                .collect(Collectors.toList());
    }
    
    // Récupérer les pointages par département et période
    public List<PointageDetailDTO> getPointagesByDepartementAndPeriode(String idDepartement,
                                                                       LocalDate dateDebut,
                                                                       LocalDate dateFin) {
        return repository.findByIdDepartementAndDatePointageBetweenOrderByDatePointageDesc(
                idDepartement, dateDebut, dateFin)
                .stream()
                .map(PointageDetailDTO::new)
                .collect(Collectors.toList());
    }
    
    // Obtenir les agrégats par employé sur une période
    public List<PointageEmployeAgregatDTO> getAgregatsByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return repository.aggregateByEmployeBetweenDates(dateDebut, dateFin);
    }
    
    // Obtenir le top des retardataires sur une période
    public List<PointageEmployeAgregatDTO> getTopRetardataires(LocalDate dateDebut, 
                                                               LocalDate dateFin, 
                                                               int limit) {
        return repository.findTopRetardatairesBetweenDates(dateDebut, dateFin)
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    // Obtenir le top des travailleurs sur une période
    public List<PointageEmployeAgregatDTO> getTopTravailleurs(LocalDate dateDebut, 
                                                              LocalDate dateFin, 
                                                              int limit) {
        return repository.aggregateByEmployeBetweenDates(dateDebut, dateFin)
                .stream()
                .sorted((a, b) -> b.getTotalHeureTravaillee().compareTo(a.getTotalHeureTravaillee()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    // Obtenir le top des heures supplémentaires sur une période
    public List<PointageEmployeAgregatDTO> getTopHeuresSupplementaires(LocalDate dateDebut, 
                                                                       LocalDate dateFin, 
                                                                       int limit) {
        return repository.aggregateByEmployeBetweenDates(dateDebut, dateFin)
                .stream()
                .sorted((a, b) -> b.getTotalHeureSup().compareTo(a.getTotalHeureSup()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Obtenir les statistiques globales sur une période
    public StatistiquesPointageDTO getStatistiques(LocalDate dateDebut, LocalDate dateFin) {
        StatistiquesPointageDTO stats = new StatistiquesPointageDTO();
        
        List<PointageEmployeAgregatDTO> agregats = getAgregatsByPeriode(dateDebut, dateFin);
        
        if (agregats.isEmpty()) {
            return stats;
        }
        
        long nombreEmployes = agregats.size();
        double totalHeures = agregats.stream()
                .mapToDouble(PointageEmployeAgregatDTO::getTotalHeureTravailleeEnHeures)
                .sum();
        double totalRetard = agregats.stream()
                .mapToDouble(PointageEmployeAgregatDTO::getTotalRetardEnHeures)
                .sum();
        double totalHeuresSup = agregats.stream()
                .mapToDouble(PointageEmployeAgregatDTO::getTotalHeureSupEnHeures)
                .sum();
        
        stats.setNombreEmployes(nombreEmployes);
        stats.setTotalHeuresTravaillees(Math.round(totalHeures * 100.0) / 100.0);
        stats.setTotalRetard(Math.round(totalRetard * 100.0) / 100.0);
        stats.setTotalHeuresSupplementaires(Math.round(totalHeuresSup * 100.0) / 100.0);
        stats.setMoyenneHeuresParEmploye(Math.round((totalHeures / nombreEmployes) * 100.0) / 100.0);
        stats.setMoyenneRetardParEmploye(Math.round((totalRetard / nombreEmployes) * 100.0) / 100.0);
        stats.setMoyenneHeuresSupParEmploye(Math.round((totalHeuresSup / nombreEmployes) * 100.0) / 100.0);
        
        stats.setTopRetardataires(getTopRetardataires(dateDebut, dateFin, 5));
        stats.setTopTravailleurs(getTopTravailleurs(dateDebut, dateFin, 5));
        
        double tauxRetardMoyen = agregats.stream()
                .mapToDouble(PointageEmployeAgregatDTO::getPourcentageRetard)
                .average()
                .orElse(0.0);
        double tauxHeuresSupMoyen = agregats.stream()
                .mapToDouble(PointageEmployeAgregatDTO::getPourcentageHeureSup)
                .average()
                .orElse(0.0);
        
        stats.setTauxRetardMoyen(Math.round(tauxRetardMoyen * 100.0) / 100.0);
        stats.setTauxHeuresSupMoyen(Math.round(tauxHeuresSupMoyen * 100.0) / 100.0);
        
        return stats;
    }
    
    // Obtenir les statistiques par département sur une période
    public List<StatistiquesDepartementDTO> getStatistiquesDepartement(LocalDate dateDebut, 
                                                                       LocalDate dateFin) {
        return repository.getStatistiquesDepartementBetweenDates(dateDebut, dateFin);
    }
    
    // Recherche par critères multiples
    public List<PointageDetailDTO> searchPointages(String matricule, String nom,
                                                   LocalDate dateDebut, LocalDate dateFin,
                                                   Integer minHeures, Integer maxHeures,
                                                   Integer minRetard, Integer maxRetard,
                                                   Integer minHeuresSup, Integer maxHeuresSup) {
        
        List<VuePointageEmployeV2> resultats = repository.findByDatePointageBetweenOrderByDatePointageDesc(
                dateDebut != null ? dateDebut : LocalDate.of(1900, 1, 1),
                dateFin != null ? dateFin : LocalDate.now()
        );
        
        return resultats.stream()
                .filter(v -> matricule == null || v.getMatricule().toLowerCase().contains(matricule.toLowerCase()))
                .filter(v -> nom == null || v.getNomComplet().toLowerCase().contains(nom.toLowerCase()))
                .filter(v -> minHeures == null || (v.getDureeHeureTravailleeMinute() != null && 
                        v.getDureeHeureTravailleeMinute() >= minHeures))
                .filter(v -> maxHeures == null || (v.getDureeHeureTravailleeMinute() != null && 
                        v.getDureeHeureTravailleeMinute() <= maxHeures))
                .filter(v -> minRetard == null || (v.getDureeRetardMinute() != null && 
                        v.getDureeRetardMinute() >= minRetard))
                .filter(v -> maxRetard == null || (v.getDureeRetardMinute() != null && 
                        v.getDureeRetardMinute() <= maxRetard))
                .filter(v -> minHeuresSup == null || (v.getDureeHeureSupplementaire() != null && 
                        v.getDureeHeureSupplementaire() >= minHeuresSup))
                .filter(v -> maxHeuresSup == null || (v.getDureeHeureSupplementaire() != null && 
                        v.getDureeHeureSupplementaire() <= maxHeuresSup))
                .map(PointageDetailDTO::new)
                .collect(Collectors.toList());
    }
    
    // NOUVELLE MÉTHODE : Filtrer les pointages avec pagination
    public Page<PointageDetailDTO> filterPointages(
            String matricule,
            LocalDate startDate,
            LocalDate endDate,
            String idDepartement,
            int page,
            int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        Page<VuePointageEmployeV2> pointagesPage = repository.findByFilters(
                matricule,
                startDate,
                endDate,
                idDepartement,
                pageable
        );
        
        return pointagesPage.map(PointageDetailDTO::new);
    }
    
    // NOUVELLE MÉTHODE : Filtrer les pointages avec pagination et tri
    public Page<PointageDetailDTO> filterPointagesWithSort(
            String matricule,
            LocalDate startDate,
            LocalDate endDate,
            String idDepartement,
            Pageable pageable) {
        
        Page<VuePointageEmployeV2> pointagesPage = repository.findByFilters(
                matricule,
                startDate,
                endDate,
                idDepartement,
                pageable
        );
        
        return pointagesPage.map(PointageDetailDTO::new);
    }
    
    // NOUVELLE MÉTHODE : Filtrer les pointages avec nom du département
    public Page<PointageDetailDTO> filterPointagesWithDepartementName(
            String matricule,
            LocalDate startDate,
            LocalDate endDate,
            String idDepartement,
            String departementNom,
            Pageable pageable) {
        
        Page<VuePointageEmployeV2> pointagesPage = repository.findByFiltersWithDepartementName(
                matricule,
                startDate,
                endDate,
                idDepartement,
                departementNom,
                pageable
        );
        
        return pointagesPage.map(PointageDetailDTO::new);
    }
    
    // NOUVELLE MÉTHODE : Version simplifiée avec valeurs par défaut
    public Page<PointageDetailDTO> filterPointagesSimple(
            String matricule,
            LocalDate startDate,
            LocalDate endDate,
            String idDepartement,
            int page,
            int size) {
        
        return filterPointages(matricule, startDate, endDate, idDepartement, page, size);
    }
    
    // NOUVELLE MÉTHODE : Obtenir tous les pointages avec pagination
    public Page<PointageDetailDTO> getAllPointagesPaginated(int page, int size) {
        return filterPointages(null, null, null, null, page, size);
    }
    
    // NOUVELLE MÉTHODE : Compter le nombre total de pointages correspondant aux filtres
    public long countPointagesByFilters(
            String matricule,
            LocalDate startDate,
            LocalDate endDate,
            String idDepartement) {
        
        return repository.findByFilters(
                matricule,
                startDate,
                endDate,
                idDepartement,
                Pageable.unpaged()
        ).getTotalElements();
    }
}

