package com.rh.manage.Repository;

import com.rh.manage.Model.Departement;
import com.rh.manage.Model.DepartementManager;
import com.rh.manage.Model.Manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartementManagerRepository extends JpaRepository<DepartementManager, String> {

    // ✅ TROUVER TOUS LES MANAGERS ACTIFS PAR DÉPARTEMENT
    @Query("SELECT dm FROM DepartementManager dm WHERE dm.departement.id = :departementId AND dm.dateFin IS NULL")
    List<DepartementManager> findManagersActifsByDepartement(@Param("departementId") String departementId);

     // ✅ TROUVER TOUS LES DÉPARTEMENTS AVEC MANAGERS ACTIFS
    @Query("SELECT DISTINCT dm.departement FROM DepartementManager dm WHERE dm.dateFin IS NULL")
    List<Departement> findDepartementsAvecManagersActifs();
    
    // Trouver par département
    List<DepartementManager> findByDepartementId(String departementId);
    
    // Trouver par manager
    List<DepartementManager> findByManagerId(String managerId);
    
    // Trouver la gestion actuelle d'un département
    Optional<DepartementManager> findByDepartementIdAndDateFinIsNull(String departementId);
    
    // Trouver les gestions actuelles d'un manager
    List<DepartementManager> findByManagerIdAndDateFinIsNull(String managerId);
    
    // Trouver toutes les gestions actives
    List<DepartementManager> findByDateFinIsNull();
    
    // Trouver les gestions dans une période
    List<DepartementManager> findByDateDebutBetween(LocalDate startDate, LocalDate endDate);
    
    // Vérifier si un département a un manager actuel
    boolean existsByDepartementIdAndDateFinIsNull(String departementId);
    
    // Vérifier si un manager gère actuellement un département
    boolean existsByManagerIdAndDepartementIdAndDateFinIsNull(String managerId, String departementId);
    
    // Compter le nombre de départements gérés par un manager
    @Query("SELECT COUNT(dm) FROM DepartementManager dm WHERE dm.manager.id = :managerId AND dm.dateFin IS NULL")
    Long countActiveDepartementsByManager(@Param("managerId") String managerId);
    
    // Trouver l'historique complet d'un département
    List<DepartementManager> findByDepartementIdOrderByDateDebutDesc(String departementId);

    // ✅ TROUVER LA RELATION ACTIVE POUR UN MANAGER
    @Query("SELECT dm FROM DepartementManager dm WHERE dm.manager = :manager AND dm.dateFin IS NULL")
    Optional<DepartementManager> findByManagerAndDateFinIsNull(@Param("manager") Manager manager);
    
    // ✅ VARIANTE AVEC L'ID DU MANAGER
    // @Query("SELECT dm FROM DepartementManager dm WHERE dm.manager.id = :managerId AND dm.dateFin IS NULL")
    // Optional<DepartementManager> findByManagerIdAndDateFinIsNull(@Param("managerId") String managerId);
    
    // ✅ TROUVER TOUTES LES RELATIONS ACTIVES POUR UN MANAGER (au cas où il y en aurait plusieurs)
    @Query("SELECT dm FROM DepartementManager dm WHERE dm.manager = :manager AND dm.dateFin IS NULL")
    List<DepartementManager> findAllByManagerAndDateFinIsNull(@Param("manager") Manager manager);
    
    // ✅ TROUVER LE DÉPARTEMENT ACTUEL D'UN MANAGER
    @Query("SELECT dm.departement FROM DepartementManager dm WHERE dm.manager = :manager AND dm.dateFin IS NULL")
    Optional<Departement> findCurrentDepartementByManager(@Param("manager") Manager manager);
}
