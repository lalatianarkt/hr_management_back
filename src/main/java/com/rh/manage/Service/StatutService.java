package com.rh.manage.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class StatutService {

    public String getLibelleStatutMouvement(int idStatut){
        return switch (idStatut) {
            case 0 -> "Actif";
            case 1 -> "Inactif";
            case 2 -> "Validé par Manager";
            case 3 -> "En attente";
            case 4 -> "Validé par RH";
            case 5 -> "Refusé";
            default -> "Inconnu";
        };
    }

    public Map<Integer, String> getAllStatutsMouvementRH() {
        Map<Integer, String> statuts = new HashMap<>();
        statuts.put(4, "validé");
        statuts.put(3, "en attente");      
        return statuts;
    }
    
    // Méthode pour convertir l'ID statut en libellé
    public String getLibelleStatut(int idStatut) {
        return switch (idStatut) {
            case 0 -> "Actif";
            case 1 -> "Inactif";
            case 2 -> "En congé";
            case 3 -> "Archivé";
            case 4 -> "Suspendu";
            default -> "Inconnu";
        };
    }
    
    // Méthode pour convertir le libellé en ID statut
    public int getIdStatut(String libelle) {
        return switch (libelle.toLowerCase()) {
            case "actif" -> 0;
            case "inactif" -> 1;
            case "en congé", "en_conge", "en-conge" -> 2;
            case "archivé", "archive" -> 3;
            case "suspendu" -> 4;
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
        statuts.put(0, "Actif");
        statuts.put(1, "Inactif");
        // statuts.put(2, "En congé");
        statuts.put(2, "Archivé");
        // statuts.put(4, "Suspendu");
        return statuts;
    }
}