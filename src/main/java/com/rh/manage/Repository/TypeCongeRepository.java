package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.TypeConge;

@Repository
public interface TypeCongeRepository extends JpaRepository<TypeConge, String> {
    // JpaRepository fournit déjà les méthodes CRUD de base
}

