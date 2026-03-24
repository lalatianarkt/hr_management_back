package com.rh.manage.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.InfosAdministratives;

@Repository
public interface InfosAdministrativesRepository extends JpaRepository<InfosAdministratives, String> {

    // Vérifie si un CIN existe déjà
    boolean existsByCin(String cin);

    // Cherche une InfosAdministratives par CIN
    Optional<InfosAdministratives> findByCin(String cin);
}
