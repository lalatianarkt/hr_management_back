package com.rh.manage.Repository;

import com.rh.manage.Model.Pointage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointageRepository extends JpaRepository<Pointage, String> {

    /**
     * Récupère TOUS les pointages d'aujourd'hui (par défaut)
     * Utilisée quand l'utilisateur n'a pas appliqué de filtres
     */
    @Query("SELECT p FROM Pointage p LEFT JOIN FETCH p.employe WHERE p.datePointage = CURRENT_DATE")
    Page<Pointage> findAllPointagesAujourdhui(Pageable pageable);
    
    // Trouver par employé
    List<Pointage> findByEmployeId(String employeId);
    
    // Trouver par date
    List<Pointage> findByDatePointage(LocalDate date);

    // Modifiez la méthode existante pour gérer les dates null
    @Query("SELECT p FROM Pointage p WHERE " +
        "(:startDate IS NULL OR p.datePointage >= :startDate) AND " +
        "(:endDate IS NULL OR p.datePointage <= :endDate)")
    Page<Pointage> findByDatePointageBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    // Ajoutez cette méthode pour les pointages du jour par défaut
    // @Query("SELECT p FROM Pointage p WHERE p.datePointage = CURRENT_DATE")
    // Page<Pointage> findPointagesDuJour(Pageable pageable);

    // Pagination avec filtres multiples
    @Query("SELECT p FROM Pointage p LEFT JOIN FETCH p.employe e WHERE " +
       "(:employeId IS NULL OR e.id = :employeId) AND " +
       "(:startDate IS NULL OR p.datePointage >= :startDate) AND " +
       "(:endDate IS NULL OR p.datePointage <= :endDate) AND " +
       "(:statut IS NULL OR p.statutPointage = :statut)")
    Page<Pointage> findByFilters(
        @Param("employeId") String employeId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("statut") Integer statut,
        Pageable pageable);

    @Query("SELECT p FROM Pointage p LEFT JOIN FETCH p.employe WHERE p.datePointage = CURRENT_DATE")
    Page<Pointage> findPointagesDuJour(Pageable pageable);

    @Query("SELECT p FROM Pointage p LEFT JOIN FETCH p.employe e WHERE e.id = :employeId AND p.datePointage = :date")
    List<Pointage> findByEmployeIdAndDatePointage(@Param("employeId") String employeId, @Param("date") LocalDate date);

    @Query("SELECT p FROM Pointage p LEFT JOIN FETCH p.employe WHERE p.datePointage BETWEEN :startDate AND :endDate")
    List<Pointage> findByDatePointageBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Trouver par employé et période
    List<Pointage> findByEmployeIdAndDatePointageBetween(String employeId, LocalDate startDate, LocalDate endDate);
    
    // Trouver par statut
    List<Pointage> findByStatutPointage(Integer statut);
    
    // Vérifier si un pointage existe pour un employé à une date
    boolean existsByEmployeIdAndDatePointage(String employeId, LocalDate date);
    
    // Trouver les pointages avec retard
    @Query("SELECT p FROM Pointage p WHERE p.dureeRetardMinute > 0")
    List<Pointage> findPointagesAvecRetard();
    
    // Calculer le total d'heures travaillées par employé sur une période
    @Query("SELECT SUM(p.dureeHeureTravailleeMinute) FROM Pointage p WHERE p.employe.id = :employeId AND p.datePointage BETWEEN :startDate AND :endDate")
    Double sumHeuresTravailleesByEmployeAndPeriod(
            @Param("employeId") String employeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    // Trouver les pointages du jour
    @Query("SELECT p FROM Pointage p WHERE p.datePointage = CURRENT_DATE")
    List<Pointage> findPointagesDuJour();
    
    // Trouver les pointages à valider
    List<Pointage> findByStatutPointageOrderByDatePointageAsc(Integer statut);
}