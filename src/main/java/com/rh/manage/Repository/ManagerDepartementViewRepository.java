package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.View.ManagerDepartementView;

import java.util.List;

@Repository
public interface ManagerDepartementViewRepository
        extends JpaRepository<ManagerDepartementView, String> {

    // Tous les managers actifs
    List<ManagerDepartementView> findByStatutManager(int statutManager);

    // Managers par département
    List<ManagerDepartementView> findByIdDepartement(String idDepartement);

    // Recherche par nom du manager
    List<ManagerDepartementView> findByNomManagerContainingIgnoreCase(String nom);

    List<ManagerDepartementView> findByStatutManagerAndStatutDepartement(int statutManager, int statutDepartement);
}

