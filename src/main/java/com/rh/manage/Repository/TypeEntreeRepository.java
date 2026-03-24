package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.TypeEntree;

@Repository
public interface TypeEntreeRepository extends JpaRepository<TypeEntree, String> {
    
    boolean existsByLibelle(String libelle);
    
    TypeEntree findByLibelle(String libelle);
}
