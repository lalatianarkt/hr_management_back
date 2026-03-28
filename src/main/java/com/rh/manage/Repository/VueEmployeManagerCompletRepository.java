package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rh.manage.View.VueEmployeManagerComplet;

import java.util.List;

@Repository
public interface VueEmployeManagerCompletRepository extends JpaRepository<VueEmployeManagerComplet, String> {
    
    // Trouver par matricule d'employé
    VueEmployeManagerComplet findByEmployeMatricule(String matricule);
    
    // Trouver tous les employés d'un manager spécifique
    @Query("SELECT v FROM VueEmployeManagerComplet v WHERE v.nomCompletManager = :nomManager OR v.nomPosteManager = :posteManager")
    List<VueEmployeManagerComplet> findByManager(String nomManager, String posteManager);
    
    // Trouver par département
    List<VueEmployeManagerComplet> findByNomDepartement(String departement);
    
    // Trouver par niveau hiérarchique
    List<VueEmployeManagerComplet> findByNomNiveau(String niveau);
    
    // Trouver par poste
    List<VueEmployeManagerComplet> findByNomPoste(String poste);
    
    // Trouver les employés sans manager
    // @Query("SELECT v FROM VueEmployeManagerComplet v WHERE v.nomCompletManager IS NULL OR v.nomCompletManager = ''")
    // List<VueEmployeManagerComplet> findEmployesSansManager();
    
    // Hiérarchie par manager (regroupement)
    // @Query("SELECT v.nomCompletManager, v.nomPosteManager, v.rangPosteManager, COUNT(v) as nbSubordonnes " +
    //        "FROM VueEmployeManagerComplet v " +
    //        "WHERE v.nomCompletManager IS NOT NULL " +
    //        "GROUP BY v.nomCompletManager, v.nomPosteManager, v.rangPosteManager " +
    //        "ORDER BY v.rangPosteManager DESC")
    // List<Object[]> findHierarchieParManager();
    
    // // Trouver par écart de rang
    // @Query("SELECT v FROM VueEmployeManagerComplet v WHERE (v.rangPosteManager - v.rang) >= :ecartMin")
    // List<VueEmployeManagerComplet> findByEcartRangMinimum(Integer ecartMin);
}
