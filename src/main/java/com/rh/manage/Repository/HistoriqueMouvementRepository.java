package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.HistoriqueMouvement;

@Repository
public interface HistoriqueMouvementRepository extends JpaRepository<HistoriqueMouvement, String> {

}

