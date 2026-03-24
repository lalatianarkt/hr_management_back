package com.rh.manage.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Dto.PointageEmployeAgregatDTO;
import com.rh.manage.Dto.StatistiquesDepartementDTO;
import com.rh.manage.Model.VuePointageEmployeV2;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VuePointageEmployeV2Repository extends JpaRepository<VuePointageEmployeV2, VuePointageEmployeV2.VuePointageEmployeV2Id> {
    
    // Recherche par période
    List<VuePointageEmployeV2> findByDatePointageBetweenOrderByDatePointageDesc(LocalDate dateDebut, LocalDate dateFin);
    
    // Recherche par employé
    List<VuePointageEmployeV2> findByIdEmployeOrderByDatePointageDesc(String idEmploye);
    
    // Recherche par matricule
    List<VuePointageEmployeV2> findByMatriculeOrderByDatePointageDesc(String matricule);
    
    // Recherche par nom (recherche textuelle)
    List<VuePointageEmployeV2> findByNomCompletContainingIgnoreCaseOrderByDatePointageDesc(String nom);
    
    // Recherche par département
    List<VuePointageEmployeV2> findByIdDepartementOrderByDatePointageDesc(String idDepartement);
    
    // Recherche par département et période
    List<VuePointageEmployeV2> findByIdDepartementAndDatePointageBetweenOrderByDatePointageDesc(
            String idDepartement, LocalDate dateDebut, LocalDate dateFin);
    
    // Agrégation par employé sur une période
    @Query("SELECT new com.rh.manage.Dto.PointageEmployeAgregatDTO(" +
           "v.idEmploye, v.matricule, v.nomComplet, v.idDepartement, v.departementNom, " +
           "SUM(v.dureeHeureTravailleeMinute), " +
           "SUM(v.dureeRetardMinute), " +
           "SUM(v.dureeHeureSupplementaire)) " +
           "FROM VuePointageEmployeV2 v " +
           "WHERE v.datePointage BETWEEN :dateDebut AND :dateFin " +
           "GROUP BY v.idEmploye, v.matricule, v.nomComplet, v.idDepartement, v.departementNom")
    List<PointageEmployeAgregatDTO> aggregateByEmployeBetweenDates(
            @Param("dateDebut") LocalDate dateDebut, 
            @Param("dateFin") LocalDate dateFin);
    
    // Top retardataires sur une période
    @Query("SELECT new com.rh.manage.Dto.PointageEmployeAgregatDTO(" +
           "v.idEmploye, v.matricule, v.nomComplet, v.idDepartement, v.departementNom, " +
           "SUM(v.dureeHeureTravailleeMinute), " +
           "SUM(v.dureeRetardMinute), " +
           "SUM(v.dureeHeureSupplementaire)) " +
           "FROM VuePointageEmployeV2 v " +
           "WHERE v.datePointage BETWEEN :dateDebut AND :dateFin " +
           "GROUP BY v.idEmploye, v.matricule, v.nomComplet, v.idDepartement, v.departementNom " +
           "ORDER BY SUM(v.dureeRetardMinute) DESC")
    List<PointageEmployeAgregatDTO> findTopRetardatairesBetweenDates(
            @Param("dateDebut") LocalDate dateDebut, 
            @Param("dateFin") LocalDate dateFin);
    
    // Statistiques par département
    @Query("SELECT new com.rh.manage.Dto.StatistiquesDepartementDTO(" +
           "v.idDepartement, v.departementNom, " +
           "COUNT(DISTINCT v.idEmploye), " +
           "SUM(v.dureeHeureTravailleeMinute), " +
           "SUM(v.dureeRetardMinute), " +
           "SUM(v.dureeHeureSupplementaire)) " +
           "FROM VuePointageEmployeV2 v " +
           "WHERE v.datePointage BETWEEN :dateDebut AND :dateFin " +
           "GROUP BY v.idDepartement, v.departementNom")
    List<StatistiquesDepartementDTO> getStatistiquesDepartementBetweenDates(
            @Param("dateDebut") LocalDate dateDebut, 
            @Param("dateFin") LocalDate dateFin);
    
    // Trouver par matricule
    Optional<VuePointageEmployeV2> findByMatricule(String matricule);

     @Query("SELECT v FROM VuePointageEmployeV2 v WHERE " +
       "(:matricule IS NULL OR v.matricule = :matricule) AND " +
       "(cast(:startDate as date) IS NULL OR v.datePointage >= :startDate) AND " +
       "(cast(:endDate as date) IS NULL OR v.datePointage <= :endDate) AND " +
       "(:idDepartement IS NULL OR v.idDepartement = :idDepartement)")
        Page<VuePointageEmployeV2> findByFilters(
                @Param("matricule") String matricule,
                @Param("startDate") LocalDate startDate,
                @Param("endDate") LocalDate endDate,
                @Param("idDepartement") String idDepartement,
                Pageable pageable);
    
    // Version alternative si vous voulez filtrer aussi par nom de département
    @Query("SELECT v FROM VuePointageEmployeV2 v WHERE " +
           "(:matricule IS NULL OR v.matricule = :matricule) AND " +
           "(:startDate IS NULL OR v.datePointage >= :startDate) AND " +
           "(:endDate IS NULL OR v.datePointage <= :endDate) AND " +
           "(:idDepartement IS NULL OR v.idDepartement = :idDepartement) AND " +
           "(:departementNom IS NULL OR v.departementNom = :departementNom)")
    Page<VuePointageEmployeV2> findByFiltersWithDepartementName(
            @Param("matricule") String matricule,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("idDepartement") String idDepartement,
            @Param("departementNom") String departementNom,
            Pageable pageable);
}