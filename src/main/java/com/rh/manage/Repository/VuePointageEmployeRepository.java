package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rh.manage.View.VuePointageEmploye;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VuePointageEmployeRepository extends JpaRepository<VuePointageEmploye, String> {
    // Récupérer les pointages entre deux dates
    List<VuePointageEmploye> findByDatePointageBetweenOrderByDatePointageDesc(LocalDate dateDebut, LocalDate dateFin);
    
    // Trouver par matricule
    Optional<VuePointageEmploye> findByMatricule(String matricule);
    
    // Trouver par nom (recherche partielle)
    List<VuePointageEmploye> findByNomCompletContainingIgnoreCase(String nom);
    
    // Trier par heures travaillées (descendant)
    List<VuePointageEmploye> findAllByOrderByTotalHeureTravailleeDesc();
    
    // Trier par retard (descendant) - top retardataires
    List<VuePointageEmploye> findAllByOrderByTotalRetardDesc();
    
    // Trier par heures supplémentaires (descendant)
    List<VuePointageEmploye> findAllByOrderByTotalHeureSupDesc();
    
    // Trouver les employés avec retard supérieur à X minutes
    @Query("SELECT v FROM VuePointageEmploye v WHERE v.totalRetard > :minutes")
    List<VuePointageEmploye> findEmployesAvecRetardSuperieurA(Integer minutes);
    
    // Trouver les employés avec heures sup supérieures à X minutes
    @Query("SELECT v FROM VuePointageEmploye v WHERE v.totalHeureSup > :minutes")
    List<VuePointageEmploye> findEmployesAvecHeuresSupSuperieuresA(Integer minutes);
    
    // Calculer les statistiques globales
    @Query("SELECT COUNT(v), " +
           "COALESCE(SUM(v.totalHeureTravaillee), 0), " +
           "COALESCE(SUM(v.totalRetard), 0), " +
           "COALESCE(SUM(v.totalHeureSup), 0) " +
           "FROM VuePointageEmploye v")
    Object[] getStatistiquesGlobales();
    
    // Trouver les employés avec le meilleur temps effectif (temps travaillé - retard)
    @Query("SELECT v FROM VuePointageEmploye v " +
           "WHERE v.totalHeureTravaillee IS NOT NULL " +
           "ORDER BY (v.totalHeureTravaillee - COALESCE(v.totalRetard, 0)) DESC")
    List<VuePointageEmploye> findTopByTempsEffectif();
    
    // Recherche avancée avec critères multiples
    @Query("SELECT v FROM VuePointageEmploye v WHERE " +
           "(:matricule IS NULL OR v.matricule LIKE %:matricule%) AND " +
           "(:nom IS NULL OR v.nomComplet LIKE %:nom%) AND " +
           "(:minHeures IS NULL OR v.totalHeureTravaillee >= :minHeures) AND " +
           "(:maxHeures IS NULL OR v.totalHeureTravaillee <= :maxHeures)")
    List<VuePointageEmploye> rechercheAvancee(String matricule, String nom, 
                                            Integer minHeures, Integer maxHeures);
}