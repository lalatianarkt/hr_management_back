package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "poste_employe")
public class PosteEmploye {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = true)
    private LocalDate dateFin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_poste", referencedColumnName = "id", nullable = false)
    private Poste poste;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_employe", referencedColumnName = "id", nullable = false)
    private Employe employe;

    // === Constructeurs ===
    public PosteEmploye() {
        this.id = generateCustomId();
    }

    public PosteEmploye(Employe employe, Poste poste, LocalDate dateDebut, LocalDate dateFin) {
        this.id = generateCustomId();
        this.employe = employe;
        this.poste = poste;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // 🔹 Génération d’un ID lisible : POSTE-YYYYMMDD-XXXXXX
    private String generateCustomId() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String shortUuid = UUID.randomUUID().toString().substring(0, 6);
        return "POSTE-" + datePart + "-" + shortUuid;
    }

    // === Getters / Setters ===
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public Poste getPoste() { return poste; }
    public void setPoste(Poste poste) { this.poste = poste; }

    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }
}
