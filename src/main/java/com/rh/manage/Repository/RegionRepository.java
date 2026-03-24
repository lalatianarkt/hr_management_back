package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.Region;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, String> {
    
    // Recherche par nom exact
    Optional<Region> findByNom(String nom);
    
    // Recherche par nom contenant un mot-clé (insensible à la casse)
    List<Region> findByNomContainingIgnoreCase(String keyword);
    
    // Vérifier si un nom existe déjà
    boolean existsByNom(String nom);
    
    // Trier par nom
    List<Region> findAllByOrderByNomAsc();
    
    // Trier par date de création
    List<Region> findAllByOrderByCreatedAtDesc();
    
    // Recherche avec pagination
    @Query("SELECT r FROM Region r WHERE LOWER(r.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Region> searchRegions(String searchTerm);
    
    // Compter le nombre de régions
    long count();
}
