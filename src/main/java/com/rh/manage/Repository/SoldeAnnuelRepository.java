package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.SoldeAnnuel;

import java.util.List;
import java.util.Optional;

@Repository
public interface SoldeAnnuelRepository extends JpaRepository<SoldeAnnuel, String> {
    List<SoldeAnnuel> findByEmploye(Employe employe);
    List<SoldeAnnuel> findByAnneeAndEmploye(Integer annee, Employe employe);
    // Nouvelle méthode pour récupérer par année
    List<SoldeAnnuel> findByAnnee(Integer annee);
    
    // Méthode pour récupérer par employé ET année
    Optional<SoldeAnnuel> findByEmployeAndAnnee(Employe employe, Integer annee);
    
    // Méthode avec tri par année décroissante
    @Query("SELECT s FROM SoldeAnnuel s WHERE s.annee = :annee ORDER BY s.employe.id ASC")
    List<SoldeAnnuel> findByAnneeOrderByEmploye(@Param("annee") Integer annee);
    
    // Méthode pour récupérer les années disponibles
    @Query("SELECT DISTINCT s.annee FROM SoldeAnnuel s ORDER BY s.annee DESC")
    List<Integer> findDistinctAnnees();
}
