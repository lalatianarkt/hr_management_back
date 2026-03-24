package com.rh.manage.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "historique_sanction")
public class HistoriqueSanction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type_sanction", length = 50)
    private String typeSanction;

    @Column(name = "nomEmploye", length = 150)
    private String nomEmploye;

    @Column(name = "date_sanction", length = 50)
    private String dateSanction;

    // Constructeur par défaut
    public HistoriqueSanction() {}

    // Constructeur avec paramètres
    public HistoriqueSanction(Long id, String typeSanction, String nomEmploye, String dateSanction) {
        this.id = id;
        this.typeSanction = typeSanction;
        this.nomEmploye = nomEmploye;
        this.dateSanction = dateSanction;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeSanction() {
        return typeSanction;
    }

    public void setTypeSanction(String typeSanction) {
        this.typeSanction = typeSanction;
    }

    public String getNomEmploye() {
        return nomEmploye;
    }

    public void setNomEmploye(String nomEmploye) {
        this.nomEmploye = nomEmploye;
    }

    public String getDateSanction() {
        return dateSanction;
    }

    public void setDateSanction(String dateSanction) {
        this.dateSanction = dateSanction;
    }

    @Override
    public String toString() {
        return "HistoriqueSanction{" +
                "id=" + id +
                ", typeSanction='" + typeSanction + '\'' +
                ", nomEmploye='" + nomEmploye + '\'' +
                ", dateSanction='" + dateSanction + '\'' +
                '}';
    }
}
