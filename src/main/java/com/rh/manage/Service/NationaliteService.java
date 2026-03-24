package com.rh.manage.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Model.Nationalite;
import com.rh.manage.Repository.NationaliteRepository;

@Service
public class NationaliteService {

    @Autowired
    private NationaliteRepository nationaliteRepository;

    // ✅ Récupérer toutes les nationalités
    public List<Nationalite> getAllNationalites() {
        return nationaliteRepository.findAll();
    }

    // ✅ Enregistrer une nationalité
    public Nationalite save(Nationalite nationalite){
        return nationaliteRepository.save(nationalite);
    }

    // ✅ Récupérer une nationalité par ID
    public Optional<Nationalite> getNationaliteById(int id) {
        return nationaliteRepository.findById(id);
    }
}
