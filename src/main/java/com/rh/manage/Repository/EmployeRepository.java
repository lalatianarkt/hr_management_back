package com.rh.manage.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.rh.manage.Model.Employe;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, String> {
    Optional<Employe> findByEmail(String email);

    // Récupérer tous les employés avec statut = 0
    List<Employe> findByStatut(int statut);
    
    // Récupérer un employé par ID avec statut = 0
    Optional<Employe> findByIdAndStatut(String id, int statut);

    // Alternative avec paramètre
    @Query("SELECT e FROM Employe e WHERE e.statut = :statut ORDER BY e.nom ASC, e.prenom ASC")
    List<Employe> findAllEmployeesByStatusSorted(int statut);

    // NOUVELLE : Version paginée
    @Query("SELECT e FROM Employe e WHERE e.statut = :statut ORDER BY e.nom, e.prenom")
    Page<Employe> findAllEmployeesByStatusSorted(
        @Param("statut") int statut, 
        Pageable pageable
    );

    // OU version plus simple sans paramètre de statut
    @Query("SELECT e FROM Employe e WHERE e.statut = 0 ORDER BY e.nom, e.prenom")
    Page<Employe> findAllActiveEmployeesSorted(Pageable pageable);

    // Nouvelle méthode - récupère tous les employés actifs sans pagination
    @Query("SELECT e FROM Employe e WHERE e.statut = 0 ORDER BY e.nom, e.prenom")
    List<Employe> findAllActiveEmployeesWithoutPagination();

    @Query("SELECT DISTINCT e FROM Employe e " +
       "JOIN InfosProfessionnelles ip ON e.id = ip.employe.id " +
       "LEFT JOIN ip.departement d " +
       "LEFT JOIN ip.typeContrat tc " +
       "WHERE e.statut = 0 " +
       "AND ip.statut = 0 " +  // IMPORTANT: seulement les infos professionnelles actives
       "AND (:matricule IS NULL OR :matricule = '' OR LOWER(ip.matricule) LIKE LOWER(CONCAT('%', :matricule, '%'))) " +
       "AND (:nom IS NULL OR :nom = '' OR LOWER(e.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) " +
       "AND (:prenom IS NULL OR :prenom = '' OR LOWER(e.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) " +
       "AND (:departementId IS NULL OR :departementId = '' OR d.id = :departementId) " +
       "AND (:typeContratId IS NULL OR :typeContratId = '' OR tc.id = :typeContratId) " +
       "AND (:statutId IS NULL OR ip.statut = :statutId)")
    Page<Employe> findWithFilters(
        @Param("matricule") String matricule,
        @Param("nom") String nom,
        @Param("prenom") String prenom,
        @Param("departementId") String departementId,
        @Param("typeContratId") String typeContratId,
        @Param("statutId") Long statutId,
        Pageable pageable
    );

    @Query("SELECT DISTINCT e FROM Employe e " +
        "JOIN InfosProfessionnelles ip ON e.id = ip.employe.id " +
        "LEFT JOIN ip.departement d " +
        "LEFT JOIN ip.typeContrat tc " +
        "WHERE e.statut = 0 " +
        "AND ip.statut = 0 " +
        "AND (:matricule IS NULL OR :matricule = '' OR LOWER(ip.matricule) LIKE LOWER(CONCAT('%', :matricule, '%'))) " +
        "AND (:nom IS NULL OR :nom = '' OR LOWER(e.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) " +
        "AND (:prenom IS NULL OR :prenom = '' OR LOWER(e.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) " +
        "AND (:departementId IS NULL OR :departementId = '' OR d.id = :departementId) " +
        "AND (:typeContratId IS NULL OR :typeContratId = '' OR tc.id = :typeContratId) " +
        "AND (:statutId IS NULL OR ip.statut = :statutId)")
    List<Employe> findWithFiltersWithoutPagination(
            @Param("matricule") String matricule,
            @Param("nom") String nom,
            @Param("prenom") String prenom,
            @Param("departementId") String departementId,
            @Param("typeContratId") String typeContratId,
            @Param("statutId") Long statutId
    );

}

