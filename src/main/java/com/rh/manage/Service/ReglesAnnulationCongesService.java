package com.rh.manage.Service;

import org.springframework.stereotype.Service;

import com.rh.manage.Model.ReglesAnnulationConges;
import com.rh.manage.Repository.ReglesAnnulationCongesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReglesAnnulationCongesService {

    private final ReglesAnnulationCongesRepository repository;

    public ReglesAnnulationCongesService(ReglesAnnulationCongesRepository repository) {
        this.repository = repository;
    }

    public List<ReglesAnnulationConges> getAllRegles() {
        return repository.findAll();
    }

    public Optional<ReglesAnnulationConges> getRegleById(String id) {
        return repository.findById(id);
    }

    public List<ReglesAnnulationConges> getActives() {
        return repository.findByActif(true);
    }

    public ReglesAnnulationConges saveRegle(ReglesAnnulationConges regle) {
        return repository.save(regle);
    }

    public void deleteRegle(String id) {
        repository.deleteById(id);
    }
}

