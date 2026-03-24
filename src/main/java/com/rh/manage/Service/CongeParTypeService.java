package com.rh.manage.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rh.manage.Model.TypeConge;
import com.rh.manage.Dto.CongeParTypeDTO;


@Service
public class CongeParTypeService {

    @Autowired
    TypeCongeService typeCongeService;

    @Autowired
    DemandeCongeService demandeCongeService;

    public List<CongeParTypeDTO> getStatTypeConge() {
        List<CongeParTypeDTO> les_stats = new ArrayList<>();
        List<TypeConge> les_types_conges = typeCongeService.findAll();
        Long total = demandeCongeService.getTotalDemandesAnneeEnCours();
        
        // Palette de couleurs prédéfinie (assez large pour couvrir plusieurs types)
        String[] paletteCouleurs = {
            "#4CAF50", "#2196F3", "#FF9800", "#E91E63", "#9C27B0",
            "#795548", "#607D8B", "#009688", "#FF5722", "#3F51B5",
            "#00BCD4", "#8BC34A", "#FFC107", "#673AB7", "#CDDC39"
        };
        
        int indexCouleur = 0;
        
        for (TypeConge typeConge : les_types_conges) {
            CongeParTypeDTO congeParTypeDTO = new CongeParTypeDTO();
            Long count = demandeCongeService.countDemandeCongeParType(typeConge);
            Double pourcentage = calculPourcentageParType(count, total);

            congeParTypeDTO.setTypeConge(typeConge);
            congeParTypeDTO.setCount(count);
            congeParTypeDTO.setPourcentage(pourcentage);
            
            // Assigner une couleur de la palette (cyclique)
            congeParTypeDTO.setColor(paletteCouleurs[indexCouleur % paletteCouleurs.length]);
            indexCouleur++;
            
            les_stats.add(congeParTypeDTO);
        }
        
        // Trier par pourcentage décroissant
        les_stats.sort((a, b) -> Double.compare(b.getPourcentage(), a.getPourcentage()));
        
        return les_stats;
    }

    public Double calculPourcentageParType(Long countType, Long countTotal) {
        if (countType == null || countTotal == null || countTotal == 0L) {
            return 0.0;
        }
        
        double pourcentage = (countType.doubleValue() / countTotal.doubleValue()) * 100.0;
        
        // Arrondir à 1 décimale
        return Math.round(pourcentage * 10.0) / 10.0;
    }
}
