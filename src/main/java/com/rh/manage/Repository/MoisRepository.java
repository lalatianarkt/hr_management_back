package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.Mois;

import java.util.Optional;

@Repository
public interface MoisRepository extends JpaRepository<Mois, Integer> {
    
    // Trouver par libellé
    Optional<Mois> findByLibelle(String libelle);
    
    // Vérifier l'existence par libellé
    boolean existsByLibelle(String libelle);
    
    // Vérifier l'existence par libellé (excluant un ID)
    boolean existsByLibelleAndIdNot(String libelle, Integer id);
    
    // Trouver par libellé contenant (recherche)
    Optional<Mois> findByLibelleContainingIgnoreCase(String libelle);
    
    // Trouver tous triés par ordre des mois (si vous avez un champ ordre, sinon par ID)
    // Par défaut, retourne par ID croissant
}
