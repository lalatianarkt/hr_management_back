package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.ReglesAnnulationConges;

import java.util.List;

@Repository
public interface ReglesAnnulationCongesRepository extends JpaRepository<ReglesAnnulationConges, String> {
    List<ReglesAnnulationConges> findByActif(Boolean actif);
}

