package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.Abreviation;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbreviationRepository extends JpaRepository<Abreviation, Long> {
    
    // Recherche par abréviation exacte
    Optional<Abreviation> findByAbreviation(String abreviation);
    
    // Recherche par libellé (insensible à la casse)
    List<Abreviation> findByLibelleContainingIgnoreCase(String libelle);
    
    // Recherche par abréviation (insensible à la casse)
    List<Abreviation> findByAbreviationContainingIgnoreCase(String abreviation);
    
    // Recherche par libellé ou abréviation
    @Query("SELECT a FROM Abreviation a WHERE LOWER(a.libelle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.abreviation) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Abreviation> searchByKeyword(@Param("keyword") String keyword);
    
    // Vérifier si une abréviation existe déjà (pour la création)
    boolean existsByAbreviation(String abreviation);
    
    // Récupérer toutes les abréviations triées par libellé
    List<Abreviation> findAllByOrderByLibelleAsc();
    
    // Récupérer toutes les abréviations triées par abréviation
    List<Abreviation> findAllByOrderByAbreviationAsc();
}
