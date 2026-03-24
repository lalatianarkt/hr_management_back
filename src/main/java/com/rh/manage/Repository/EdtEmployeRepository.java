package com.rh.manage.Repository;

import com.rh.manage.Model.EdtEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EdtEmployeRepository extends JpaRepository<EdtEmploye, String> {
    
    // Trouver les EDT par statut
    List<EdtEmploye> findByStatut(Integer statut);
    
    // Trouver les EDT actifs
    List<EdtEmploye> findByStatutOrderByDateDuJourDesc(Integer statut);
    
    // Trouver les EDT par employé
    List<EdtEmploye> findByEmployeId(String employeId);
    
    // Trouver les EDT actifs par employé
    List<EdtEmploye> findByEmployeIdAndStatut(String employeId, Integer statut);
    
    // Trouver les EDT par horaire
    List<EdtEmploye> findByHoraireId(String horaireId);
    
    // Trouver les EDT actifs par horaire
    List<EdtEmploye> findByHoraireIdAndStatut(String horaireId, Integer statut);
    
    // Trouver les EDT par date
    List<EdtEmploye> findByDateDuJour(LocalDate dateDuJour);
    
    // Trouver les EDT actifs par date
    List<EdtEmploye> findByDateDuJourAndStatut(LocalDate dateDuJour, Integer statut);
    
    // Trouver les EDT par employé et date
    List<EdtEmploye> findByEmployeIdAndDateDuJour(String employeId, LocalDate dateDuJour);
    
    // Trouver les EDT actifs par employé et date
    EdtEmploye findByEmployeIdAndDateDuJourAndStatut(String employeId, LocalDate dateDuJour, Integer statut);
    
    // Trouver les EDT par type de shift
    List<EdtEmploye> findByIsShiftJour(Boolean isShiftJour);
    
    // Trouver les EDT actifs par type de shift
    List<EdtEmploye> findByIsShiftJourAndStatut(Boolean isShiftJour, Integer statut);
    
    // Trouver les EDT par plage de dates
    List<EdtEmploye> findByDateDuJourBetween(LocalDate startDate, LocalDate endDate);
    
    // Trouver les EDT actifs par plage de dates
    List<EdtEmploye> findByDateDuJourBetweenAndStatut(LocalDate startDate, LocalDate endDate, Integer statut);
    
    // Recherche par plage horaire
    @Query("SELECT e FROM EdtEmploye e WHERE e.heureDebut >= :heureDebut AND e.heureFin <= :heureFin AND e.statut = 1")
    List<EdtEmploye> findByPlageHoraire(@Param("heureDebut") LocalTime heureDebut, @Param("heureFin") LocalTime heureFin);
    
    // Vérifier si un EDT existe pour un employé à une date spécifique
    @Query("SELECT COUNT(e) > 0 FROM EdtEmploye e WHERE e.employe.id = :employeId AND e.dateDuJour = :date AND e.statut = 1")
    boolean existsByEmployeAndDate(@Param("employeId") String employeId, @Param("date") LocalDate date);
    
    // Récupérer avec jointure sur employé
    @Query("SELECT e FROM EdtEmploye e LEFT JOIN FETCH e.employe WHERE e.id = :id")
    Optional<EdtEmploye> findByIdWithEmploye(@Param("id") String id);
    
    // Récupérer avec jointure sur horaire
    @Query("SELECT e FROM EdtEmploye e LEFT JOIN FETCH e.horaire WHERE e.id = :id")
    Optional<EdtEmploye> findByIdWithHoraire(@Param("id") String id);
    
    // Trouver tous les EDT avec jointures
    @Query("SELECT e FROM EdtEmploye e LEFT JOIN FETCH e.employe LEFT JOIN FETCH e.horaire WHERE e.statut = :statut")
    List<EdtEmploye> findAllWithRelations(@Param("statut") Integer statut);
    
    // Compter les EDT actifs par employé
    @Query("SELECT COUNT(e) FROM EdtEmploye e WHERE e.employe.id = :employeId AND e.statut = 1")
    Long countEdtActifsByEmploye(@Param("employeId") String employeId);
}