package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.TypeTempsTravail;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeTempsTravailRepository extends JpaRepository<TypeTempsTravail, Long> {
    
    // Recherche par temps de travail exact
    Optional<TypeTempsTravail> findByTempsTravail(String tempsTravail);
    
    // Recherche par temps de travail contenant un mot-clé (insensible à la casse)
    List<TypeTempsTravail> findByTempsTravailContainingIgnoreCase(String keyword);
    
    // Vérifier si un temps de travail existe déjà
    boolean existsByTempsTravail(String tempsTravail);
    
    // Trier par temps de travail
    List<TypeTempsTravail> findAllByOrderByTempsTravailAsc();
    
    // Trier par date de création
    List<TypeTempsTravail> findAllByOrderByCreatedAtDesc();
    
    // Compter le nombre de types
    long count();
}
