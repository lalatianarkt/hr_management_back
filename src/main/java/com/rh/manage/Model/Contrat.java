package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contrat")
public class Contrat {
    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "duree")
    private Integer duree;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_poste", referencedColumnName = "id", nullable = false)
    private Poste poste;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_type_contrat", referencedColumnName = "id", nullable = false)
    private TypeContrat typeContrat;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_employe", referencedColumnName = "id", nullable = false)
    private Employe employe;

    // Constructeurs
    public Contrat() {}

    public Contrat(String id, LocalDate dateDebut, LocalDate dateFin, Integer duree) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.duree = duree;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public Integer getDuree() { return duree; }
    public void setDuree(Integer duree) { this.duree = duree; }

    public Poste getPoste() { return poste; }
    public void setPoste(Poste poste) { this.poste = poste; }

    public TypeContrat getTypeContrat() { return typeContrat; }
    public void setTypeContrat(TypeContrat typeContrat) { this.typeContrat = typeContrat; }

    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }
}