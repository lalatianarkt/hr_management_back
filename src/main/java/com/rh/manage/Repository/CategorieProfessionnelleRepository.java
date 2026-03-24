package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.CategorieProfessionnelle;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieProfessionnelleRepository extends JpaRepository<CategorieProfessionnelle, String> {
    
    // Recherche par code
    Optional<CategorieProfessionnelle> findByCode(String code);
    
    // Recherche par libellé contenant un mot-clé
    List<CategorieProfessionnelle> findByLibelleContainingIgnoreCase(String keyword);
    
    // Recherche par code ou libellé
    @Query("SELECT c FROM CategorieProfessionnelle c WHERE " +
           "LOWER(c.code) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.libelle) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<CategorieProfessionnelle> searchByCodeOrLibelle(String searchTerm);
    
    // Vérifier si un code existe déjà
    boolean existsByCode(String code);
    
    // Trier par date de création
    List<CategorieProfessionnelle> findAllByOrderByCreatedAtDesc();
}
