package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.CalendrierFerie;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendrierFerieRepository extends JpaRepository<CalendrierFerie, Long> {
    
    // Trouver par date
    List<CalendrierFerie> findByDateFerie(LocalDate dateFerie);
    
    // Trouver par libellé (contient)
    List<CalendrierFerie> findByLibelleContainingIgnoreCase(String libelle);
    
    // Trouver les jours fériés actifs
    List<CalendrierFerie> findByEstActifTrue();
    
    // Trouver par plage de dates
    List<CalendrierFerie> findByDateFerieBetween(LocalDate startDate, LocalDate endDate);
    
    // Vérifier si une date est fériée
    boolean existsByDateFerieAndEstActifTrue(LocalDate dateFerie);
    
    // Requête personnalisée pour trouver par année
    @Query("SELECT c FROM CalendrierFerie c WHERE YEAR(c.dateFerie) = :year AND c.estActif = true")
    List<CalendrierFerie> findFeriesByYear(@Param("year") int year);
    
    // Trouver les jours fériés futurs actifs
    @Query("SELECT c FROM CalendrierFerie c WHERE c.dateFerie >= CURRENT_DATE AND c.estActif = true ORDER BY c.dateFerie ASC")
    List<CalendrierFerie> findUpcomingFeries();
}
