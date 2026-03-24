package com.rh.manage.Service;

import org.springframework.stereotype.Service;

import com.rh.manage.Model.TypeMouvement;
import com.rh.manage.Repository.TypeMouvementRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TypeMouvementService {

    private final TypeMouvementRepository repository;

    public TypeMouvementService(TypeMouvementRepository repository) {
        this.repository = repository;
    }

    public List<TypeMouvement> getAll() {
        return repository.findAll();
    }

    public Optional<TypeMouvement> getById(String id) {
        return repository.findById(id);
    }

    public TypeMouvement save(TypeMouvement typeMouvement) {
        return repository.save(typeMouvement);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}

