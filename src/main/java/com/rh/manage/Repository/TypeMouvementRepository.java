package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.TypeMouvement;

@Repository
public interface TypeMouvementRepository extends JpaRepository<TypeMouvement, String> {
    // Ici, vous pouvez ajouter des méthodes custom si besoin
}
