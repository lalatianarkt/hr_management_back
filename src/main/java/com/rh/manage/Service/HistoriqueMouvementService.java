package com.rh.manage.Service;

import org.springframework.stereotype.Service;

import com.rh.manage.Model.HistoriqueMouvement;
import com.rh.manage.Repository.HistoriqueMouvementRepository;

import java.util.List;
import java.util.Optional;

@Service
public class HistoriqueMouvementService {

    private final HistoriqueMouvementRepository repository;

    public HistoriqueMouvementService(HistoriqueMouvementRepository repository) {
        this.repository = repository;
    }

    public List<HistoriqueMouvement> getAll() {
        return repository.findAll();
    }

    public Optional<HistoriqueMouvement> getById(String id) {
        return repository.findById(id);
    }

    public HistoriqueMouvement save(HistoriqueMouvement historique) {
        return repository.save(historique);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}


