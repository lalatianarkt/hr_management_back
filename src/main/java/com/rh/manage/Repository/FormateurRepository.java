package com.rh.manage.Repository;

import com.rh.manage.Model.Formateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormateurRepository extends JpaRepository<Formateur, String> {
    
    // Trouver un formateur par email
    Optional<Formateur> findByEmail(String email);
    
    // Trouver des formateurs par spécialité
    List<Formateur> findBySpecialite(String specialite);
    
    // Trouver des formateurs par type
    List<Formateur> findByType(Integer type);
    
    // Trouver des formateurs par nom (avec LIKE)
    List<Formateur> findByNomContainingIgnoreCase(String nom);
    
    // Trouver des formateurs par id employé
    List<Formateur> findByIdEmploye(String idEmploye);
    
    // Requête personnalisée pour compter les formateurs par spécialité
    @Query("SELECT f.specialite, COUNT(f) FROM Formateur f GROUP BY f.specialite")
    List<Object[]> countFormateursBySpecialite();
    
    // Requête pour trouver les formateurs créés après une certaine date
    @Query("SELECT f FROM Formateur f WHERE f.createdAt > :date")
    List<Formateur> findFormateursCreatedAfter(@Param("date") java.time.LocalDateTime date);
}
