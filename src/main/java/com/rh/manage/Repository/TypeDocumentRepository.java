package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.TypeDocument;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeDocumentRepository extends JpaRepository<TypeDocument, String> {
    // Trouver tous les types de documents ordonnés par intitulé
    List<TypeDocument> findAllByOrderByIntitule();
    
    Optional<TypeDocument> findByIntitule(String intitule);
    
    // Trouver par intitulé contenant une chaîne (insensible à la casse)
    List<TypeDocument> findByIntituleContainingIgnoreCase(String intitule);
    
    // Vérifier si un type existe par intitulé
    boolean existsByIntitule(String intitule);
    
    // Vérifier si un type existe par id
    boolean existsById(String id);
    
    // Trouver tous les types triés par intitulé
    List<TypeDocument> findAllByOrderByIntituleAsc();
}
