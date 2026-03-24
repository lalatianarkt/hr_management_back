package com.rh.manage.Repository;

import com.rh.manage.Model.Sexe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SexeRepository extends JpaRepository<Sexe, Integer> {
    
    // Récupérer tous les sexes ordonnés par sexe
    List<Sexe> findAllByOrderBySexe();
    
    // Trouver un sexe par son code
    Sexe findByCode(String code);
    
    // Vérifier si un code existe
    boolean existsByCode(String code);
    
    // Trouver par le champ sexe
    List<Sexe> findBySexeContainingIgnoreCase(String sexe);
}