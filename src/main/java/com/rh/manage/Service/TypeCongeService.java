package com.rh.manage.Service;

import org.springframework.stereotype.Service;

import com.rh.manage.Model.TypeConge;
import com.rh.manage.Repository.TypeCongeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TypeCongeService {

    private final TypeCongeRepository repository;

    public TypeCongeService(TypeCongeRepository repository) {
        this.repository = repository;
    }

    // CRUD méthodes

    public List<TypeConge> findAll() {
        return repository.findAll();
    }

    public Optional<TypeConge> findById(String id) {
        return repository.findById(id);
    }

    public TypeConge save(TypeConge typeConge) {
        return repository.save(typeConge);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
