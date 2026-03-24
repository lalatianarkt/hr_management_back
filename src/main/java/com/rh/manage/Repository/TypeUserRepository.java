package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.TypeUser;

@Repository
public interface TypeUserRepository extends JpaRepository<TypeUser, Integer> {
    // Vous pouvez ajouter des méthodes personnalisées si nécessaire
    TypeUser findByType(String type);
}
