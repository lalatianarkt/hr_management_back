package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rh.manage.Model.SituationFamiliale;

@Repository
public interface SituationFamilialeRepository extends JpaRepository<SituationFamiliale, Integer> {
    // Tu peux ajouter des méthodes personnalisées ici, par exemple :
    // Optional<SituationFamiliale> findByType(String type);
}
