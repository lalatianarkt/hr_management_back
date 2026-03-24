package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.TypeDemande;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeDemandeRepository extends JpaRepository<TypeDemande, String> {
    
    // Trouver par type (nom)
    Optional<TypeDemande> findByType(String type);
    
    // Trouver par type contenant (recherche)
    List<TypeDemande> findByTypeContaining(String type);
    
    // Trouver par type (ignore case)
    Optional<TypeDemande> findByTypeIgnoreCase(String type);
    
    // Vérifier l'existence d'un type
    boolean existsByType(String type);
    
    // Vérifier l'existence d'un type (excluant un ID)
    boolean existsByTypeAndIdNot(String type, String id);
    
    // Trouver tous triés par type
    List<TypeDemande> findAllByOrderByTypeAsc();
}
