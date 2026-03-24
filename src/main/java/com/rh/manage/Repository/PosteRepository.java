package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.Poste;

import java.util.List;
import java.util.Optional;

@Repository
public interface PosteRepository extends JpaRepository<Poste, String> {
    
    // Trouver tous les postes ordonnés par nom
    List<Poste> findAllByOrderByNom();
    
    // Trouver un poste par son nom exact
    Optional<Poste> findByNom(String nom);
    
    // Rechercher des postes par nom (recherche partielle insensible à la casse)
    List<Poste> findByNomContainingIgnoreCase(String nom);
    
    // Rechercher des postes par description (recherche partielle)
    List<Poste> findByDescriptionContainingIgnoreCase(String description);
    
    // Vérifier si un poste existe par nom
    boolean existsByNom(String nom);
    
    // Compter le nombre de postes
    long count();
    
    // Trouver les postes créés après une certaine date
    List<Poste> findByCreatedAtAfter(java.time.LocalDateTime date);
    
    // Recherche avancée avec plusieurs critères
    @Query("SELECT p FROM Poste p WHERE " +
           "LOWER(p.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Poste> searchPostes(@Param("searchTerm") String searchTerm);
    
    // Trouver les postes les plus récents
    List<Poste> findTop10ByOrderByCreatedAtDesc();

    // Méthode 1: Utilisant la relation directe
    List<Poste> findByDepartementId(String departementId);
    
    // Méthode 2: Avec requête JPQL personnalisée
    @Query("SELECT p FROM Poste p WHERE p.departement.id = :departementId")
    List<Poste> findPostesByDepartementId(@Param("departementId") String departementId);
    
    // Méthode 3: Vérifier si le département existe et a des postes
    @Query("SELECT COUNT(p) > 0 FROM Poste p WHERE p.departement.id = :departementId")
    boolean existsByDepartementId(@Param("departementId") String departementId);

    List<Poste> findByNiveauHierarchique_Id(String idNiveau);
}
