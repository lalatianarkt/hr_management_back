package com.rh.manage.Service;

import com.rh.manage.Model.PosteEmploye;
import com.rh.manage.Repository.PosteEmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PosteEmployeService {

    @Autowired
    private PosteEmployeRepository repository;

    // Créer ou mettre à jour un PosteEmploye
    public PosteEmploye save(PosteEmploye pe) {
        return repository.save(pe);
    }

    // Récupérer par id
    public Optional<PosteEmploye> getById(String id) {
        return repository.findById(id);
    }

    // Récupérer tous
    public List<PosteEmploye> getAll() {
        return repository.findAll();
    }

    // Récupérer tous les postes d'un employé
    public List<PosteEmploye> getByEmployeId(String employeId) {
        return repository.findByEmployeId(employeId);
    }

    // Récupérer tous les employés d'un poste
    public List<PosteEmploye> getByPosteId(String posteId) {
        return repository.findByPosteId(posteId);
    }

    // Supprimer un PosteEmploye
    public void delete(String id) {
        repository.deleteById(id);
    }
}
