package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Model.NiveauHierarchique;
import com.rh.manage.Repository.NiveauHierarchiqueRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NiveauHierarchiqueService {

    @Autowired
    private NiveauHierarchiqueRepository repository;

    // Récupérer tous les niveaux
    public List<NiveauHierarchique> getAll() {
        return repository.findAll();
    }

    // Récupérer par ID
    public Optional<NiveauHierarchique> getById(String id) {
        return repository.findById(id);
    }

    // Ajouter ou mettre à jour
    public NiveauHierarchique save(NiveauHierarchique niveau) {
        return repository.save(niveau);
    }

    // Supprimer
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}

