package com.rh.manage.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rh.manage.View.PaieCompleteView;

@Repository
public interface PaieCompleteViewRepository  extends JpaRepository<PaieCompleteView, Long>{
    List<PaieCompleteView> findByIdEmploye(Long idEmploye);

    // Exemple : récupérer par période
    List<PaieCompleteView> findByAnneePaieAndMoisPaie(Integer annee, Integer mois);

    List<PaieCompleteView> findByDepartementAndDateDebutPeriode(String departement, LocalDate dateDebutPeriode);

    // @Query("SELECT p FROM vue_paie_complete p WHERE p.categorieSalaire = :categorie")
    // List<PaieCompleteView> findByCategorieSalaire(String categorie);
}
