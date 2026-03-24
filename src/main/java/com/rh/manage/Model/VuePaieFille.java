package com.rh.manage.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "vue_paie_fille")
@IdClass(VuePaieFilleId.class) 
public class VuePaieFille {
    
    @Id
    @Column(name = "id_paie")
    private String idPaie;
    
    @Id
    @Column(name = "code")
    private String code;
    
    @Column(name = "plafond_mensuel", precision = 19, scale = 2)
    private String plafondMensuel;

    @Column(name = "plafond_annuel", precision = 19, scale = 2)
    private String plafondAnnuel;
    
    @Column(name = "montant", precision = 19, scale = 2)
    private BigDecimal montant = BigDecimal.valueOf(0);
    
    @Column(name = "taux", precision = 10, scale = 4)
    private BigDecimal taux = BigDecimal.valueOf(0);
    
    @Column(name = "base", precision = 19, scale = 2)
    private BigDecimal base = BigDecimal.valueOf(0);

    @Column(name = "type_rubrique")
    private String typeRubrique;
    
    @Column(name = "categorie_rubrique")
    private String categorieRubrique;

    @Column(name = "ordre")
    private int ordre;

    @Column(name = "rubrique_nom")
    private String rubriqueNom;

    @Column(name = "nom_company")
    private String nomCompany;

    @Column(name = "logo")
    private String logo;

    // Constructeur par défaut
    public VuePaieFille() {}
    
    // Constructeur avec paramètres
    public VuePaieFille(String idPaie, String code, String plafondMensuel, 
                       String plafondAnnuel, BigDecimal montant, 
                       BigDecimal taux, BigDecimal base, 
                       String typeRubrique, String categorieRubrique, int ordre, String rubriqueNom) {
        this.idPaie = idPaie;
        this.code = code;
        this.plafondMensuel = plafondMensuel;
        this.plafondAnnuel = plafondAnnuel;
        this.montant = montant;
        this.taux = taux;
        this.base = base;
        this.typeRubrique = typeRubrique;
        this.categorieRubrique = categorieRubrique;
        this.ordre = ordre;
        this.rubriqueNom = rubriqueNom;
    }

    // Getters et Setters
    public String getIdPaie() { return idPaie; }
    public void setIdPaie(String idPaie) { this.idPaie = idPaie; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getPlafondMensuel() { return plafondMensuel; }
    public void setPlafondMensuel(String plafondMensuel) { this.plafondMensuel = plafondMensuel; }

    public String getPlafondAnnuel() { return plafondAnnuel; }
    public void setPlafondAnnuel(String plafondAnnuel) { this.plafondAnnuel = plafondAnnuel; }

    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public BigDecimal getTaux() { return taux; }
    public void setTaux(BigDecimal taux) { this.taux = taux; }

    public BigDecimal getBase() { return base; }
    public void setBase(BigDecimal base) { this.base = base; }

    public String getTypeRubrique() {return typeRubrique;}
    public void setTypeRubrique(String typeRubrique) {this.typeRubrique = typeRubrique;}

    public String getCategorieRubrique() {return categorieRubrique;}

    public void setCategorieRubrique(String categorieRubrique) {this.categorieRubrique = categorieRubrique;}

    public int getOrdre() {return ordre;}
    public void setOrdre(int ordre) {this.ordre = ordre;}

    public String getRubriqueNom() {return rubriqueNom;}
    public void setRubriqueNom(String rubriqueNom) {this.rubriqueNom = rubriqueNom;}

    public String getNomCompany() {return nomCompany;}
    public void setNomCompany(String nomCompany) {this.nomCompany = nomCompany;}

    public String getLogo() {return logo;}
    public void setLogo(String logo) {this.logo = logo;}

}
