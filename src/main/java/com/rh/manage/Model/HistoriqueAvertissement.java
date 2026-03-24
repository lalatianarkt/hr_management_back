package com.rh.manage.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "historique_avertissement")
public class HistoriqueAvertissement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type_avertissement", length = 150)
    private String typeAvertissement;

    @Column(name = "idEmploye", length = 50)
    private String idEmploye;

    @Column(name = "date_averti", length = 50)
    private String dateAverti;

    // Constructeur par défaut
    public HistoriqueAvertissement() {}

    // Constructeur avec paramètres
    public HistoriqueAvertissement(Long id, String typeAvertissement, String idEmploye, String dateAverti) {
        this.id = id;
        this.typeAvertissement = typeAvertissement;
        this.idEmploye = idEmploye;
        this.dateAverti = dateAverti;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeAvertissement() {
        return typeAvertissement;
    }

    public void setTypeAvertissement(String typeAvertissement) {
        this.typeAvertissement = typeAvertissement;
    }

    public String getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
    }

    public String getDateAverti() {
        return dateAverti;
    }

    public void setDateAverti(String dateAverti) {
        this.dateAverti = dateAverti;
    }

    @Override
    public String toString() {
        return "HistoriqueAvertissement{" +
                "id=" + id +
                ", typeAvertissement='" + typeAvertissement + '\'' +
                ", idEmploye='" + idEmploye + '\'' +
                ", dateAverti='" + dateAverti + '\'' +
                '}';
    }
}
