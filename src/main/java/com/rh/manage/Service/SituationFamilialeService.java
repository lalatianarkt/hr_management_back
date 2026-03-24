package com.rh.manage.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

import com.rh.manage.Model.SituationFamiliale;
import com.rh.manage.Repository.SituationFamilialeRepository;

@Service
public class SituationFamilialeService {

    @Autowired
    private SituationFamilialeRepository situationFamilialeRepository;

    // Ajouter une situation
    public SituationFamiliale save(SituationFamiliale situationFamiliale) {
        return situationFamilialeRepository.save(situationFamiliale);
    }

    // Lister toutes les situations
    public List<SituationFamiliale> findAll() {
        return situationFamilialeRepository.findAll();
    }

    // Trouver une situation par ID
    public Optional<SituationFamiliale> findById(Integer id) {
        return situationFamilialeRepository.findById(id);
    }

    // Supprimer une situation
    public void deleteById(Integer id) {
        situationFamilialeRepository.deleteById(id);
    }

    // Modifier une situation existante
    public SituationFamiliale update(Integer id, SituationFamiliale updatedSituation) {
        return situationFamilialeRepository.findById(id)
                .map(existing -> {
                    existing.setType(updatedSituation.getType());
                    // existing.setModifiedAt(updatedSituation.getModifiedAt());
                    return situationFamilialeRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Situation familiale non trouvée avec id " + id));
    }
}
