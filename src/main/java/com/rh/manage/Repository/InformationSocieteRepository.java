package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.InformationSociete;

import java.util.Optional;

@Repository
public interface InformationSocieteRepository extends JpaRepository<InformationSociete, Integer> {
    
    // Trouver par nom de company
    Optional<InformationSociete> findByNomCompany(String nomCompany);
    
    // Vérifier l'existence par nom
    boolean existsByNomCompany(String nomCompany);
    
    // Trouver par nom contenant (recherche)
    Optional<InformationSociete> findByNomCompanyContainingIgnoreCase(String nomCompany);
    
    // Trouver la première société (pour système mono-société)
    Optional<InformationSociete> findFirstByOrderByIdAsc();
}
