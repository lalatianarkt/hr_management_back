package com.rh.manage.Service;

import com.rh.manage.Model.Departement;
import com.rh.manage.Repository.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;

    public Departement updateDepartement(Departement departement){
        Departement dep = getDepartementById(departement.getId()).get();
        departement.setCreatedAt(dep.getCreatedAt());
        departement.setModifiedAt(dep.getModifiedAt()); 
        return departementRepository.save(departement);
    }

    // ✅ Créer ou mettre à jour un département
    public Departement saveDepartement(Departement departement) {
        return departementRepository.save(departement);
    }

    // ✅ Récupérer tous les départements
    public List<Departement> getAllDepartements() {
        return departementRepository.findAll();
    }

    // ✅ Récupérer un département par son ID
    public Optional<Departement> getDepartementById(String id) {
        return departementRepository.findById(id);
    }

    // ✅ Supprimer un département
    public void deleteDepartement(String id) {
        departementRepository.deleteById(id);
    }

    // ✅ Récupérer un département par son nom
    public Departement getDepartementByNom(String nom) {
        return departementRepository.findByNom(nom);
    }

    public List<Departement> getDepartementActif(){
        return departementRepository.findByStatut(0);
    } 
}

