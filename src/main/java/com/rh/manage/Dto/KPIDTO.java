package com.rh.manage.Dto;

public class KPIDTO {
    private String titre;
    private double valeur;
    private String unite;
    private double variation; // % vs période précédente
    private String couleur; // "success" (vert), "warning" (orange), "danger" (rouge), "info" (bleu)
    private String icone; // team, check-circle, warning, clock, etc.
    private String description;

    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public double getValeur() {
        return valeur;
    }
    public void setValeur(double valeur) {
        this.valeur = valeur;
    }
    public String getUnite() {
        return unite;
    }
    public void setUnite(String unite) {
        this.unite = unite;
    }
    public double getVariation() {
        return variation;
    }
    public void setVariation(double variation) {
        this.variation = variation;
    }
    public String getCouleur() {
        return couleur;
    }
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }
    public String getIcone() {
        return icone;
    }
    public void setIcone(String icone) {
        this.icone = icone;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    
}

