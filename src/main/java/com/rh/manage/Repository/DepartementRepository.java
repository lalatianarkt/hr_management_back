package com.rh.manage.Repository;

import com.rh.manage.Model.Departement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, String> {
    // Vous pouvez ajouter ici des méthodes personnalisées si besoin
    Departement findByNom(String nom);
    List<Departement> findByStatut(int statut);
    
    // Departement findById(long> id);
}
