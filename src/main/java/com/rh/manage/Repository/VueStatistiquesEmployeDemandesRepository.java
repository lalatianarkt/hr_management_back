package com.rh.manage.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.VueStatistiquesEmployeDemandes;

@Repository
public interface VueStatistiquesEmployeDemandesRepository extends JpaRepository<VueStatistiquesEmployeDemandes, String> {
    
    /**
     * Trouve les statistiques d'un employé spécifique
     */
    Optional<VueStatistiquesEmployeDemandes> findByEmployeId(String employeId);
    
    /**
     * Trouve les statistiques par matricule
     */
    Optional<VueStatistiquesEmployeDemandes> findByMatricule(String matricule);
    
    /**
     * Trouve les statistiques par email
     */
    Optional<VueStatistiquesEmployeDemandes> findByEmail(String email);
    
    /**
     * Trouve par nom ou prénom (recherche insensible à la casse)
     */
    List<VueStatistiquesEmployeDemandes> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
    
    /**
     * Trouve les employés avec plus de X demandes
     */
    List<VueStatistiquesEmployeDemandes> findByNombreDemandesGreaterThan(Long minDemandes);
    
    /**
     * Trouve les employés avec moins de X demandes
     */
    List<VueStatistiquesEmployeDemandes> findByNombreDemandesLessThan(Long maxDemandes);
    
    /**
     * Trouve les employés avec une moyenne de jours supérieure à X
     */
    List<VueStatistiquesEmployeDemandes> findByMoyenneJoursDemandeGreaterThan(Double minMoyenne);
    
    /**
     * Trouve les employés avec un taux d'approbation supérieur à X
     */
    List<VueStatistiquesEmployeDemandes> findByTauxApprobationGreaterThan(Double minTaux);
    
    /**
     * Trouve les employés avec des demandes en attente
     */
    List<VueStatistiquesEmployeDemandes> findByDemandesEnAttenteGreaterThan(Long minEnAttente);
    
    /**
     * Trouve les top N employés avec le plus de demandes
     */
    @Query("SELECT v FROM VueStatistiquesEmployeDemandes v ORDER BY v.nombreDemandes DESC")
    Page<VueStatistiquesEmployeDemandes> findTopByNombreDemandes(Pageable pageable);
    
    /**
     * Trouve les top N employés avec le plus de jours demandés
     */
    @Query("SELECT v FROM VueStatistiquesEmployeDemandes v ORDER BY v.totalJoursDemandes DESC NULLS LAST")
    Page<VueStatistiquesEmployeDemandes> findTopByTotalJoursDemandes(Pageable pageable);
    
    /**
     * Trouve les employés avec le meilleur taux d'approbation
     */
    @Query("SELECT v FROM VueStatistiquesEmployeDemandes v WHERE v.tauxApprobation > 0 ORDER BY v.tauxApprobation DESC")
    Page<VueStatistiquesEmployeDemandes> findTopByTauxApprobation(Pageable pageable);
    
    /**
     * Trouve les employés avec le plus de demandes en attente
     */
    @Query("SELECT v FROM VueStatistiquesEmployeDemandes v WHERE v.demandesEnAttente > 0 ORDER BY v.demandesEnAttente DESC")
    Page<VueStatistiquesEmployeDemandes> findTopByDemandesEnAttente(Pageable pageable);
    
    /**
     * Statistiques globales (agrégation)
     */
    @Query("SELECT " +
           "COUNT(v) as totalEmployes, " +
           "SUM(v.nombreDemandes) as totalDemandes, " +
           "SUM(v.totalJoursDemandes) as totalJours, " +
           "AVG(v.moyenneJoursDemande) as moyenneGlobale, " +
           "AVG(v.tauxApprobation) as tauxApprobationMoyen " +
           "FROM VueStatistiquesEmployeDemandes v")
    Object[] getStatistiquesGlobales();
}

