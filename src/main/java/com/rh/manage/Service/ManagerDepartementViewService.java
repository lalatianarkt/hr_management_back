package com.rh.manage.Service;

import org.springframework.stereotype.Service;

import com.rh.manage.Repository.ManagerDepartementViewRepository;
import com.rh.manage.View.ManagerDepartementView;

import java.util.List;

@Service
public class ManagerDepartementViewService {

    private final ManagerDepartementViewRepository repository;

    public ManagerDepartementViewService(ManagerDepartementViewRepository repository) {
        this.repository = repository;
    }


    // Tous les managers + départements
    public List<ManagerDepartementView> getAll() {
        return repository.findAll();
    }

    // Managers actifs uniquement
    public List<ManagerDepartementView> getManagersActifs() {
        return repository.findByStatutManager(0); // 0 = actif
    }

    // Managers par département
    public List<ManagerDepartementView> getByDepartement(String idDepartement) {
        return repository.findByIdDepartement(idDepartement);
    }

    // Recherche par nom
    public List<ManagerDepartementView> searchByNom(String nom) {
        return repository.findByNomManagerContainingIgnoreCase(nom);
    }

    public List<ManagerDepartementView> findActifDepWithActifManager(){
        return repository.findByStatutManagerAndStatutDepartement(0, 0);
    }
}

