package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.View.AbsenceCongeView;
import com.rh.manage.View.AbsenceCongeViewId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AbsenceCongeRepository extends JpaRepository<AbsenceCongeView, AbsenceCongeViewId> {
    
    // Recherche par employé
    List<AbsenceCongeView> findByEmployeId(String employeId);
    
    // Recherche par type d'absence
    List<AbsenceCongeView> findByTypeAbsence(AbsenceCongeView.TypeAbsence type);
    
    // Recherche par période
    List<AbsenceCongeView> findByDateAbsenceBetween(LocalDate dateDebut, LocalDate dateFin);
    
    // Recherche par département
    List<AbsenceCongeView> findByIdDepartement(String idDepartement);
    
    // Recherche combinée
    @Query("SELECT a FROM AbsenceCongeView a WHERE " +
           "(:employeId IS NULL OR a.employeId = :employeId) AND " +
           "(:departementId IS NULL OR a.idDepartement = :departementId) AND " +
           "(:typeAbsence IS NULL OR a.typeAbsence = :typeAbsence) AND " +
           "a.dateAbsence >= COALESCE(:dateDebut, a.dateAbsence) AND " +
           "a.dateAbsence <= COALESCE(:dateFin, a.dateAbsence)")
    List<AbsenceCongeView> findWithFilters(
            @Param("employeId") String employeId,
            @Param("departementId") String departementId,
            @Param("typeAbsence") AbsenceCongeView.TypeAbsence typeAbsence,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);
    
    // Statistiques par employé
    @Query("SELECT COUNT(a) FROM AbsenceCongeView a WHERE a.employeId = :employeId AND a.typeAbsence = 'ABSENCE'")
    Long countAbsencesByEmployeId(@Param("employeId") String employeId);
    
    @Query("SELECT COUNT(a) FROM AbsenceCongeView a WHERE a.employeId = :employeId AND a.typeAbsence = 'CONGE'")
    Long countCongesByEmployeId(@Param("employeId") String employeId);
    
    // Statistiques globales
    @Query("SELECT COUNT(a) FROM AbsenceCongeView a WHERE a.typeAbsence = 'absence'")
    Long countTotalAbsences();
    
    @Query("SELECT COUNT(a) FROM AbsenceCongeView a WHERE a.typeAbsence = 'conge'")
    Long countTotalConges();
    
    // Statistiques par période
    @Query("SELECT COUNT(a) FROM AbsenceCongeView a WHERE " +
           "a.dateAbsence BETWEEN :dateDebut AND :dateFin AND " +
           "a.typeAbsence = :typeAbsence")
    Long countByPeriodAndType(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin,
            @Param("typeAbsence") AbsenceCongeView.TypeAbsence typeAbsence);

    Optional<List<AbsenceCongeView>> findByEmployeIdAndIdDepartement(String employeId, String idDepartement);
}
