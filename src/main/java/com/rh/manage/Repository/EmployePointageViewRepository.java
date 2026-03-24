package com.rh.manage.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.View.EmployePointageView;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployePointageViewRepository extends JpaRepository<EmployePointageView, String> {
    
    // Trouver par matricule
    Optional<EmployePointageView> findByMatricule(String matricule);
    
    // Trouver par nom ou prénom (recherche insensible à la casse)
    List<EmployePointageView> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
    
    // Trouver par nom complet
    @Query("SELECT e FROM EmployePointageView e WHERE LOWER(e.nomComplet) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<EmployePointageView> findByNomCompletContaining(@Param("search") String search);
    
    // Trouver par département
    List<EmployePointageView> findByDepartement(String departement);
    
    // Trouver par poste
    List<EmployePointageView> findByPoste(String poste);
    
    // Trouver tous les employés actifs
    @Query("SELECT e FROM EmployePointageView e WHERE e.employeStatut = 0 AND e.infoProStatut = 0")
    List<EmployePointageView> findAllActifs();
    
    // Trouver par département avec pagination
    Page<EmployePointageView> findByDepartement(String departement, Pageable pageable);
    
    // Recherche avancée avec filtres multiples
    @Query("SELECT e FROM EmployePointageView e WHERE " +
           "(:matricule IS NULL OR e.matricule LIKE %:matricule%) AND " +
           "(:nom IS NULL OR LOWER(e.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:prenom IS NULL OR LOWER(e.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) AND " +
           "(:departement IS NULL OR e.departement = :departement) AND " +
           "(:poste IS NULL OR e.poste = :poste)")
    List<EmployePointageView> findByFilters(
            @Param("matricule") String matricule,
            @Param("nom") String nom,
            @Param("prenom") String prenom,
            @Param("departement") String departement,
            @Param("poste") String poste);
    
    // Compter par département
    @Query("SELECT e.departement, COUNT(e) FROM EmployePointageView e GROUP BY e.departement ORDER BY e.departement")
    List<Object[]> countByDepartement();
    
    // Compter par poste
    @Query("SELECT e.poste, COUNT(e) FROM EmployePointageView e GROUP BY e.poste ORDER BY e.poste")
    List<Object[]> countByPoste();
    
    // Vérifier si un matricule existe
    boolean existsByMatricule(String matricule);
    
    // Trouver par liste d'IDs
    List<EmployePointageView> findByIdIn(List<String> ids);
}
