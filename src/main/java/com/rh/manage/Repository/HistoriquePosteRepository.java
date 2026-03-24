package com.rh.manage.Repository;

import com.rh.manage.Model.HistoriquePoste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoriquePosteRepository extends JpaRepository<HistoriquePoste, Long> {
    
    // Trouver tous les historiques d'un employé
    List<HistoriquePoste> findByEmployeIdOrderByDateDebutDesc(String employeId);
    
    // Trouver les historiques par poste
    List<HistoriquePoste> findByPosteId(String posteId);
    
    // Trouver l'historique actuel (sans date_fin)
    Optional<HistoriquePoste> findByEmployeIdAndDateFinIsNull(String employeId);
    
    // Trouver les historiques dans une période
    List<HistoriquePoste> findByDateDebutBetween(LocalDate startDate, LocalDate endDate);
    
    // Trouver les historiques par employé et poste
    List<HistoriquePoste> findByEmployeIdAndPosteId(String employeId, String posteId);
    
    // Statistique : nombre de changements par employé
    @Query("SELECT COUNT(h) FROM HistoriquePoste h WHERE h.employe.id = :employeId")
    Long countByEmployeId(@Param("employeId") String employeId);
    
    // Historiques avec date_fin nulle (postes actuels)
    List<HistoriquePoste> findByDateFinIsNull();
    
    // Vérifier si un employé a déjà eu un poste
    boolean existsByEmployeIdAndPosteId(String employeId, String posteId);
}