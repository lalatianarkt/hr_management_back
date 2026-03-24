package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.NiveauHierarchique;

@Repository
public interface NiveauHierarchiqueRepository extends JpaRepository<NiveauHierarchique, String> {
    // JpaRepository fournit déjà les méthodes CRUD de base
}

