package com.rh.manage.Repository;

import com.rh.manage.Model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, String> {
    
    // Trouver par nom (exact)
    Optional<Categorie> findByNom(String nom);
    
    // Trouver par nom (contient)
    List<Categorie> findByNomContainingIgnoreCase(String nom);
    
    // Vérifier si un nom existe déjà (pour éviter les doublons)
    boolean existsByNom(String nom);
    
    // Trouver par couleur
    List<Categorie> findByCouleur(String couleur);
    
    // Compter le nombre de catégories
    long count();
    
    // Trouver les catégories avec description non nulle
    List<Categorie> findByDescriptionIsNotNull();
    
    // Recherche personnalisée par nom ou description
    @Query("SELECT c FROM Categorie c WHERE " +
           "LOWER(c.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Categorie> searchByNomOrDescription(@Param("searchTerm") String searchTerm);
    
    // Trier par nom
    List<Categorie> findAllByOrderByNomAsc();
}