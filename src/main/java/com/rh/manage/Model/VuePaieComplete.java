package com.rh.manage.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vue_paie_complete")
@IdClass(VuePaieCompleteId.class)  
public class VuePaieComplete {
    
    @Id
    @Column(name = "id_employe")
    private String idEmploye;

    @Id 
    @Column(name = "paie_id")
    private String paieId;
    
    @Column(name = "matricule")
    private String matricule;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "prenom")
    private String prenom;
    
    @Column(name = "nom_complet")
    private String nomComplet;
    
    @Column(name = "fonction")
    private String fonction;
    
    @Column(name = "departement")
    private String departement;
    
    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;
    
    @Column(name = "anciennete_ans")
    private Integer ancienneteAns;
    
    @Column(name = "anciennete_mois")
    private Integer ancienneteMois;
    
    @Column(name = "anciennete_formatee")
    private String ancienneteFormatee;
    
    @Column(name = "salaire_base", precision = 19, scale = 2)
    private BigDecimal salaireBase;
    
    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "date_cloture")
    private LocalDate dateCloture;
    
    @Column(name = "num_cnaps")
    private String numCnaps;
    
    @Column(name = "statut_periode")
    private Integer statutCloture;
    
    @Column(name = "date_debut_periode")
    private LocalDate dateDebutPeriode;
    
    @Column(name = "date_fin_periode")
    private LocalDate dateFinPeriode;
    
    @Column(name = "nom_company")
    private String nomCompany;
    
    @Column(name = "logo")
    private String logo;

    @Column(name = "mode_paiement")
    private String modePaiement;
    
    @Column(name = "salaire_brut", precision = 19, scale = 2)
    private BigDecimal salaireBrut;

    @Column(name = "total_charges", precision = 10, scale = 4)
    private BigDecimal totalCharges;
    
    @Column(name = "total_taux", precision = 10, scale = 4)
    private BigDecimal totalTaux;
    
    @Column(name = "total_base", precision = 19, scale = 2)
    private BigDecimal totalBase;
    
    @Column(name = "total_retenue", precision = 19, scale = 2)
    private BigDecimal totalRetenue;
    
    @Column(name = "total_cotisations", precision = 19, scale = 2)
    private BigDecimal totalCotisations;
    
    @Column(name = "salaire_net", precision = 19, scale = 2)
    private BigDecimal salaireNet;
    
    @Column(name = "taux_prelevement_percent", precision = 5, scale = 2)
    private BigDecimal tauxPrelevementPercent;
    
    @Column(name = "taux_cotisation_percent", precision = 5, scale = 2)
    private BigDecimal tauxCotisationPercent;
    
    @Column(name = "categorie_salaire")
    private String categorieSalaire;
    
    @Column(name = "mois_paie_id")
    private Integer moisPaieId;

    @Column(name = "mois_paie_nom")
    private String moisPaieNom;
    
    @Column(name = "annee_paie")
    private Integer anneePaie;
    
    @Column(name = "statut_paie_libelle")
    private String statutPaieLibelle;

    @Column(name = "mois_paie")
    private Integer moisPaie;

    // Getters et Setters
    public String getIdEmploye() { return idEmploye; }
    public void setIdEmploye(String idEmploye) { this.idEmploye = idEmploye; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }

    public String getFonction() { return fonction; }
    public void setFonction(String fonction) { this.fonction = fonction; }

    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }

    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public Integer getAncienneteAns() { return ancienneteAns; }
    public void setAncienneteAns(Integer ancienneteAns) { this.ancienneteAns = ancienneteAns; }

    public Integer getAncienneteMois() { return ancienneteMois; }
    public void setAncienneteMois(Integer ancienneteMois) { this.ancienneteMois = ancienneteMois; }

    public String getAncienneteFormatee() { return ancienneteFormatee; }
    public void setAncienneteFormatee(String ancienneteFormatee) { this.ancienneteFormatee = ancienneteFormatee; }

    public BigDecimal getSalaireBase() { return salaireBase; }
    public void setSalaireBase(BigDecimal salaireBase) { this.salaireBase = salaireBase; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getNumCnaps() { return numCnaps; }
    public void setNumCnaps(String numCnaps) { this.numCnaps = numCnaps; }

    public String getPaieId() { return paieId; }
    public void setPaieId(String paieId) { this.paieId = paieId; }

    public Integer getStatutCloture() { return statutCloture; }
    public void setStatutCloture(Integer statutCloture) { this.statutCloture = statutCloture; }

    public LocalDate getDateDebutPeriode() { return dateDebutPeriode; }
    public void setDateDebutPeriode(LocalDate dateDebutPeriode) { this.dateDebutPeriode = dateDebutPeriode; }

    public LocalDate getDateFinPeriode() { return dateFinPeriode; }
    public void setDateFinPeriode(LocalDate dateFinPeriode) { this.dateFinPeriode = dateFinPeriode; }

    public String getNomCompany() { return nomCompany; }
    public void setNomCompany(String nomCompany) { this.nomCompany = nomCompany; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public BigDecimal getSalaireBrut() { return salaireBrut; }
    public void setSalaireBrut(BigDecimal salaireBrut) { this.salaireBrut = salaireBrut; }

    public BigDecimal getTotalTaux() { return totalTaux; }
    public void setTotalTaux(BigDecimal totalTaux) { this.totalTaux = totalTaux; }

    public BigDecimal getTotalBase() { return totalBase; }
    public void setTotalBase(BigDecimal totalBase) { this.totalBase = totalBase; }

    public BigDecimal getTotalRetenue() { return totalRetenue; }
    public void setTotalRetenue(BigDecimal totalRetenue) { this.totalRetenue = totalRetenue; }

    public BigDecimal getTotalCotisations() { return totalCotisations; }
    public void setTotalCotisations(BigDecimal totalCotisations) { this.totalCotisations = totalCotisations; }

    public BigDecimal getSalaireNet() { return salaireNet; }
    public void setSalaireNet(BigDecimal salaireNet) { this.salaireNet = salaireNet; }

    public BigDecimal getTauxPrelevementPercent() { return tauxPrelevementPercent; }
    public void setTauxPrelevementPercent(BigDecimal tauxPrelevementPercent) { this.tauxPrelevementPercent = tauxPrelevementPercent; }

    public BigDecimal getTauxCotisationPercent() { return tauxCotisationPercent; }
    public void setTauxCotisationPercent(BigDecimal tauxCotisationPercent) { this.tauxCotisationPercent = tauxCotisationPercent; }

    public String getCategorieSalaire() { return categorieSalaire; }
    public void setCategorieSalaire(String categorieSalaire) { this.categorieSalaire = categorieSalaire; }

    public Integer getMoisPaie() { return moisPaie; }
    public void setMoisPaie(Integer moisPaie) { this.moisPaie = moisPaie; }

    public Integer getAnneePaie() { return anneePaie; }
    public void setAnneePaie(Integer anneePaie) { this.anneePaie = anneePaie; }

    public String getStatutPaieLibelle() { return statutPaieLibelle; }
    public void setStatutPaieLibelle(String statutPaieLibelle) { this.statutPaieLibelle = statutPaieLibelle; }
    public LocalDate getDateCloture() { return dateCloture;}
    public void setDateCloture(LocalDate dateCloture) { this.dateCloture = dateCloture; }
    public String getMoisPaieNom() {return moisPaieNom;}
    public void setMoisPaieNom(String moisPaieNom) {this.moisPaieNom = moisPaieNom;}
    public Integer getMoisPaieId() {return moisPaieId;}
    public void setMoisPaieId(Integer moisPaieId) {this.moisPaieId = moisPaieId;}
    public String getModePaiement() {return modePaiement;}
    public void setModePaiement(String modePaiement) {this.modePaiement = modePaiement;}

    public BigDecimal getTotalCharges() {return totalCharges;}
    public void setTotalCharges(BigDecimal totalCharges) {this.totalCharges = totalCharges;}
    
}
