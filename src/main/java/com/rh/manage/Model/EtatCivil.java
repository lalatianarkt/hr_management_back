package com.rh.manage.Model;

public enum EtatCivil {
    CELIBATAIRE("Célibataire"),
    MARIE("Marié(e)"),
    DIVORCE("Divorcé(e)"),
    VEUF("Veuf/Veuve");

    private final String libelle;

    EtatCivil(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static EtatCivil fromLibelle(String libelle) {
        for (EtatCivil etat : values()) {
            if (etat.libelle.equalsIgnoreCase(libelle)) {
                return etat;
            }
        }
        throw new IllegalArgumentException("Libellé inconnu: " + libelle);
    }

    @Override
    public String toString() {
        return libelle;
    }
}