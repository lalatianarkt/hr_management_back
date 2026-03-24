package com.rh.manage.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class StatutClotureService {
    // Méthode pour convertir l'ID statut en libellé
    public String getLibelleStatut(int idStatut) {
        return switch (idStatut) {
            case 0 -> "Clôturé";
            case 1 -> "Non clôturé";
            default -> "Inconnu";
        };
    }
    
    // Méthode pour convertir le libellé en ID statut
    public int getIdStatut(String libelle) {
        return switch (libelle.toLowerCase()) {
            case "Clôturé" -> 0;
            case "Non clôturé" -> 1;
            default -> 0;
        };
    }
    
    // Méthode pour vérifier si un statut est actif
    public boolean estActif(int idStatut) {
        return idStatut == 0; // 1 = Actif
    }
    
    // Méthode pour vérifier si un statut est inactif
    public boolean estInactif(int idStatut) {
        return idStatut == 1; // 2 = Inactif
    }
    
    // Méthode pour obtenir la liste de tous les statuts
    public Map<Integer, String> getAllStatuts() {
        Map<Integer, String> statuts = new HashMap<>();
        statuts.put(0, "Clôturé");
        statuts.put(1, "Non clôturé");
        return statuts;
    }
}
