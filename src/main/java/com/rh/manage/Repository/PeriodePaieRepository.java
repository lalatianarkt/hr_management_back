package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.PeriodePaie;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodePaieRepository extends JpaRepository<PeriodePaie, String> {
    
    // Recherche par statut
    Optional<PeriodePaie> findByStatut(Integer statut);
    
    // Recherche par date de début
    List<PeriodePaie> findByDateDebut(LocalDate dateDebut);
    
    // Recherche par date de fin
    List<PeriodePaie> findByDateFin(LocalDate dateFin);
    
    // Recherche des périodes entre deux dates
    @Query("SELECT p FROM PeriodePaie p WHERE p.dateDebut >= :startDate AND p.dateFin <= :endDate")
    List<PeriodePaie> findBetweenDates(@Param("startDate") LocalDate startDate, 
                                       @Param("endDate") LocalDate endDate);
    
    // Recherche des périodes actives (statut = 1 par exemple)
    @Query("SELECT p FROM PeriodePaie p WHERE p.statut = 1")
    List<PeriodePaie> findActivePeriods();
    
    // Vérification si une période existe déjà pour une plage de dates
    @Query("SELECT COUNT(p) > 0 FROM PeriodePaie p WHERE " +
       "(p.dateDebut <= :endDate AND p.dateFin >= :startDate)")
       boolean existsByDateRange(@Param("startDate") LocalDate startDate, 
                          @Param("endDate") LocalDate endDate);
}