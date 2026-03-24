package com.rh.manage.Service;

import com.rh.manage.Model.TypeUser;
import com.rh.manage.Repository.TypeUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeUserService {

    @Autowired
    private TypeUserRepository typeUserRepository;

    public TypeUser getByType(String type){
        return typeUserRepository.findByType(type);
    }

    // Récupérer tous les types d'utilisateurs
    public List<TypeUser> getAllTypeUsers() {
        return typeUserRepository.findAll();
    }

    // Récupérer un type d'utilisateur par ID
    public Optional<TypeUser> getTypeUserById(Integer id) {
        return typeUserRepository.findById(id);
    }

    // Créer un nouveau type d'utilisateur
    public TypeUser createTypeUser(TypeUser typeUser) {
        return typeUserRepository.save(typeUser);
    }

    // Mettre à jour un type d'utilisateur existant
    public TypeUser updateTypeUser(Integer id, TypeUser typeUserDetails) {
        TypeUser typeUser = typeUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TypeUser introuvable avec id : " + id));
        typeUser.setType(typeUserDetails.getType());
        return typeUserRepository.save(typeUser);
    }

    // Supprimer un type d'utilisateur
    public void deleteTypeUser(Integer id) {
        TypeUser typeUser = typeUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TypeUser introuvable avec id : " + id));
        typeUserRepository.delete(typeUser);
    }
}
