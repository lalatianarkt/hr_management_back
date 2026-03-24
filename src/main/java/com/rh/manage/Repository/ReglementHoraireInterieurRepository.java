package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.ReglementHoraireInterieur;

import java.time.LocalTime;
import java.util.List;


@Repository
public interface ReglementHoraireInterieurRepository extends JpaRepository<ReglementHoraireInterieur, Long> {
    
    // Recherche par plage horaire d'entrée
    ReglementHoraireInterieur findByHeureMatEntree(LocalTime heureMatEntree);
    
    // Vérifier s'il existe déjà un règlement avec ces heures
    boolean existsByHeureMatEntreeAndHeureApremSortie(LocalTime heureMatEntree, LocalTime heureApremSortie);
    
    // Trouver le dernier règlement créé
    ReglementHoraireInterieur findFirstByOrderByCreatedAtDesc();

    ReglementHoraireInterieur findByStatut(int statut);
}