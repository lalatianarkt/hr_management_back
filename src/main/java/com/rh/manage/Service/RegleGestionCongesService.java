package com.rh.manage.Service;

import org.springframework.stereotype.Service;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.RegleGestionConges;
import com.rh.manage.Repository.RegleGestionCongesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RegleGestionCongesService {

    private final RegleGestionCongesRepository repository;

    public RegleGestionCongesService(RegleGestionCongesRepository repository) {
        this.repository = repository;
    }

    public RegleGestionConges create(RegleGestionConges regle) {
        // statut = 0 est déjà défini dans l'entité lors de @PrePersist
        return repository.save(regle);
    }

    public List<RegleGestionConges> getAll() {
        return repository.findAll();
    } 

    public RegleGestionConges getDerniereRegleGestionCongesParStatutActive(){
        return repository.findFirstByStatutOrderByCreatedAtDesc(0).get();
    }

    public Optional<RegleGestionConges> getById(int id) {
        return repository.findById(id);
    }

    public RegleGestionConges update(int id, RegleGestionConges regle) {
        regle.setId(id);
        return repository.save(regle);
    }

    public RegleGestionConges softDelete(int id) {
        return repository.findById(id).map(r -> {
            r.setStatut(1); // statut = 1 pour soft delete
            return repository.save(r);
        }).orElseThrow(() -> new RuntimeException("Règle non trouvée"));
    }

}

