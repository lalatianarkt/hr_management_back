package com.rh.manage.Repository;

import com.rh.manage.Model.DemandeAbsence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeAbsenceRepository extends JpaRepository<DemandeAbsence, String> {
    
    // Trouver par employé
    List<DemandeAbsence> findByEmployeId(String employeId);
    
    // Trouver par employé et période
    List<DemandeAbsence> findByEmployeIdAndDateHeureAbsenceDebutBetween(
            String employeId, LocalDateTime debut, LocalDateTime fin);
    
    // Trouver par statut
    List<DemandeAbsence> findByStatut(Integer statut);
    
    // Trouver par employé et statut
    List<DemandeAbsence> findByEmployeIdAndStatut(String employeId, Integer statut);
    
    // Vérifier les chevauchements d'absences
    @Query("SELECT d FROM DemandeAbsence d WHERE " +
           "d.employe.id = :employeId AND " +
           "d.statut IN (0, 1) AND " + // En attente ou approuvée
           "(:debut < d.dateHeureAbsenceFin AND :fin > d.dateHeureAbsenceDebut)")
    List<DemandeAbsence> findOverlappingAbsences(
            @Param("employeId") String employeId,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin);
    
    // Exclure la demande actuelle lors de la vérification de chevauchement (pour update)
    @Query("SELECT d FROM DemandeAbsence d WHERE " +
           "d.id != :demandeId AND " +
           "d.employe.id = :employeId AND " +
           "d.statut IN (0, 1) AND " +
           "(:debut < d.dateHeureAbsenceFin AND :fin > d.dateHeureAbsenceDebut)")
    List<DemandeAbsence> findOverlappingAbsencesExcludingCurrent(
            @Param("demandeId") String demandeId,
            @Param("employeId") String employeId,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin);
    
    // Trouver les demandes pour un manager (employés sous sa responsabilité)
    // @Query("SELECT d FROM DemandeAbsence d " +
    //        "JOIN d.employe e " +
    //        "WHERE e.manager.id = :managerId " +
    //        "ORDER BY d.createdAt DESC")
    // List<DemandeAbsence> findByManagerId(@Param("managerId") String managerId);
    
    // Statistiques par mois
    @Query("SELECT COUNT(d), d.statut FROM DemandeAbsence d " +
           "WHERE YEAR(d.createdAt) = :annee AND MONTH(d.createdAt) = :mois " +
           "GROUP BY d.statut")
    List<Object[]> getStatsByMonth(@Param("mois") int mois, @Param("annee") int annee);
    
    // Vérifier si une demande existe pour un employé à une période donnée
    boolean existsByEmployeIdAndDateHeureAbsenceDebutBetweenAndStatutIn(
            String employeId, 
            LocalDateTime debut, 
            LocalDateTime fin, 
            List<Integer> statuts);
    
    // Trouver les demandes en attente d'approbation
    @Query("SELECT d FROM DemandeAbsence d WHERE d.statut = 0 ORDER BY d.createdAt ASC")
    List<DemandeAbsence> findPendingDemandes();
    
    // Trouver par période
    List<DemandeAbsence> findByDateHeureAbsenceDebutBetween(LocalDateTime debut, LocalDateTime fin);
    
    // Compter les demandes par employé et année
    @Query("SELECT COUNT(d) FROM DemandeAbsence d " +
           "WHERE d.employe.id = :employeId AND YEAR(d.createdAt) = :annee")
    Long countByEmployeAndYear(@Param("employeId") String employeId, @Param("annee") int annee);
}