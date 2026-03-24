package com.rh.manage.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.RegleGestionConges;

@Repository
public interface RegleGestionCongesRepository extends JpaRepository<RegleGestionConges, Integer> {
    // Récupérer la dernière règle active (statut = 0) selon la date de création
    Optional<RegleGestionConges> findFirstByStatutOrderByCreatedAtDesc(int statut);
}

