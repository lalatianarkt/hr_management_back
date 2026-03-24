package com.rh.manage.Service;

import com.rh.manage.Model.Sexe;
import com.rh.manage.Repository.SexeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SexeService {
    
    @Autowired
    private SexeRepository sexeRepository;
    
    // Récupérer tous les sexes ordonnés par sexe
    public List<Sexe> getAllSexes() {
        return sexeRepository.findAllByOrderBySexe();
    }
    
    // Récupérer tous les sexes (sans ordre spécifique)
    public List<Sexe> getAllSexesSimple() {
        return sexeRepository.findAll();
    }
    
    // Récupérer un sexe par son ID
    public Sexe getSexeById(Integer id) {
        return sexeRepository.findById(id).orElse(null);
    }
    
    // Récupérer un sexe par son code
    public Sexe getSexeByCode(String code) {
        return sexeRepository.findByCode(code);
    }
    
    // Vérifier si un code existe
    public boolean codeExists(String code) {
        return sexeRepository.existsByCode(code);
    }
    
    // Compter le nombre de sexes
    public long countSexes() {
        return sexeRepository.count();
    }
    
    // Rechercher par nom de sexe
    public List<Sexe> searchBySexe(String sexe) {
        return sexeRepository.findBySexeContainingIgnoreCase(sexe);
    }
}