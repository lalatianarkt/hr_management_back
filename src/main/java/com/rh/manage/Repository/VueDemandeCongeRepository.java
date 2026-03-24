package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.VueDemandeConge;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VueDemandeCongeRepository extends JpaRepository<VueDemandeConge, String> {
    
    // Trouver toutes les demandes d'un employé par ID
    List<VueDemandeConge> findByIdEmploye(String idEmploye);
    
    // Trouver par matricule
    List<VueDemandeConge> findByMatricule(String matricule);
    
    // Trouver par nom complet d'employé
    List<VueDemandeConge> findByNomCompletEmployeContainingIgnoreCase(String nomComplet);
    
    // Trouver par nom d'employé
    List<VueDemandeConge> findByNomEmployeContainingIgnoreCase(String nom);
    
    // Trouver par prénom d'employé
    List<VueDemandeConge> findByPrenomEmployeContainingIgnoreCase(String prenom);
    
    // Trouver par département (ID)
    List<VueDemandeConge> findByIdDepartement(String idDepartement);
    
    // Trouver par nom de département
    List<VueDemandeConge> findByNomDepartementContainingIgnoreCase(String departement);
    
    // Trouver par manager (ID)
    List<VueDemandeConge> findByIdManager(String idManager);
    
    // Trouver par statut (libellé de décision)
    List<VueDemandeConge> findByDecisionManagerLibelle(String decisionLibelle);
    
    // Trouver par période
    List<VueDemandeConge> findByDateDebutBetween(LocalDate dateDebut, LocalDate dateFin);
    
    // Trouver les demandes en attente
    @Query("SELECT v FROM VueDemandeConge v WHERE v.decisionManagerLibelle = 'en attente'")
    List<VueDemandeConge> findDemandesEnAttente();
    
    // Trouver les demandes validées
    @Query("SELECT v FROM VueDemandeConge v WHERE v.decisionManagerLibelle = 'validé par le manager'")
    List<VueDemandeConge> findDemandesValidees();
    
    // Trouver les demandes refusées
    @Query("SELECT v FROM VueDemandeConge v WHERE v.decisionManagerLibelle = 'refusé par le manager'")
    List<VueDemandeConge> findDemandesRefusees();
    
    // Trouver les demandes annulées par le demandeur
    @Query("SELECT v FROM VueDemandeConge v WHERE v.decisionManagerLibelle = 'annulé par le demandeur'")
    List<VueDemandeConge> findDemandesAnnuleesParDemandeur();
    
    // Trouver les demandes annulées par le RH
    @Query("SELECT v FROM VueDemandeConge v WHERE v.decisionManagerLibelle = 'annulé par le RH'")
    List<VueDemandeConge> findDemandesAnnuleesParRH();
    
    // Recherche multicritère améliorée
    @Query("SELECT v FROM VueDemandeConge v WHERE " +
           "(:idEmploye IS NULL OR v.idEmploye = :idEmploye) AND " +
           "(:matricule IS NULL OR v.matricule = :matricule) AND " +
           "(:nomEmploye IS NULL OR LOWER(v.nomEmploye) LIKE LOWER(CONCAT('%', :nomEmploye, '%'))) AND " +
           "(:prenomEmploye IS NULL OR LOWER(v.prenomEmploye) LIKE LOWER(CONCAT('%', :prenomEmploye, '%'))) AND " +
           "(:idDepartement IS NULL OR v.idDepartement = :idDepartement) AND " +
           "(:idManager IS NULL OR v.idManager = :idManager) AND " +
           "(:statut IS NULL OR LOWER(v.decisionManagerLibelle) LIKE LOWER(CONCAT('%', :statut, '%'))) AND " +
           "(:dateDebut IS NULL OR v.dateDebut >= :dateDebut) AND " +
           "(:dateFin IS NULL OR v.dateFin <= :dateFin)")
    List<VueDemandeConge> rechercherDemandesAvance(
            @Param("idEmploye") String idEmploye,
            @Param("matricule") String matricule,
            @Param("nomEmploye") String nomEmploye,
            @Param("prenomEmploye") String prenomEmploye,
            @Param("idDepartement") String idDepartement,
            @Param("idManager") String idManager,
            @Param("statut") String statut,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);
    
    // Statistiques par département (avec ID)
    @Query("SELECT v.idDepartement, v.nomDepartement, COUNT(v) FROM VueDemandeConge v GROUP BY v.idDepartement, v.nomDepartement")
    List<Object[]> countByDepartementDetail();
    
    // Statistiques par statut
    @Query("SELECT v.decisionManagerLibelle, COUNT(v) FROM VueDemandeConge v GROUP BY v.decisionManagerLibelle")
    List<Object[]> countByStatut();
    
    // Statistiques par manager
    @Query("SELECT v.idManager, v.nomCompletManager, COUNT(v) FROM VueDemandeConge v GROUP BY v.idManager, v.nomCompletManager")
    List<Object[]> countByManager();
    
    // Trouver les demandes d'un manager par ID
    List<VueDemandeConge> findByIdManagerAndDecisionManagerLibelle(String idManager, String statut);
    
    // Trouver les demandes d'un département par ID
    List<VueDemandeConge> findByIdDepartementAndDecisionManagerLibelle(String idDepartement, String statut);
    
    // Recherche par matricule ou nom
    @Query("SELECT v FROM VueDemandeConge v WHERE " +
           "LOWER(v.matricule) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(v.nomCompletEmploye) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<VueDemandeConge> searchByMatriculeOrName(@Param("search") String search);
    
    // Obtenir les demandes d'un employé avec filtres
    @Query("SELECT v FROM VueDemandeConge v WHERE v.idEmploye = :idEmploye AND " +
           "(:statut IS NULL OR v.decisionManagerLibelle = :statut) AND " +
           "(:annee IS NULL OR YEAR(v.dateDebut) = :annee)")
    List<VueDemandeConge> findByEmployeWithFilters(
            @Param("idEmploye") String idEmploye,
            @Param("statut") String statut,
            @Param("annee") Integer annee);
}
