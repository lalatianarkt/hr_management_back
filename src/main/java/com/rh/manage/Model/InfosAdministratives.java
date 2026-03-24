package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "infos_administratives")
public class InfosAdministratives {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "num_cnaps", length = 50, nullable = false)
    private String numCnaps;

    @Column(name = "cin", length = 20, nullable = false, unique = true)
    private String cin;

    @Column(name = "nombre_enfants")
    private Integer nombreEnfants;

    @ManyToOne
    @JoinColumn(name = "id_situation_familiale", referencedColumnName = "id")
    private SituationFamiliale situationFamiliale;

    // 🔹 Constructeur par défaut : génère automatiquement un ID personnalisé
    public InfosAdministratives() {
        this.id = generateCustomId();
    }

    // 🔹 Constructeur avec paramètres
    public InfosAdministratives(String numCnaps, String cin, Integer nombreEnfants, SituationFamiliale situationFamiliale) {
        this.id = generateCustomId();
        this.numCnaps = numCnaps;
        this.cin = cin;
        this.nombreEnfants = nombreEnfants;
        this.situationFamiliale = situationFamiliale;
    }

    // 🔹 Génération d’un ID du type INFO-20251028-3f9a2c
    private String generateCustomId() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // 20251028
        String shortUuid = UUID.randomUUID().toString().substring(0, 6); // 3f9a2c
        return "INFO-" + date + "-" + shortUuid;
    }

    // ⚙️ Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNumCnaps() { return numCnaps; }
    public void setNumCnaps(String numCnaps) { this.numCnaps = numCnaps; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public Integer getNombreEnfants() { return nombreEnfants; }
    public void setNombreEnfants(Integer nombreEnfants) { this.nombreEnfants = nombreEnfants; }

    public SituationFamiliale getSituationFamiliale() { return situationFamiliale; }
    public void setSituationFamiliale(SituationFamiliale situationFamiliale) { this.situationFamiliale = situationFamiliale; }

    @Override
    public String toString() {
        return "InfosAdministratives{" +
                "id='" + id + '\'' +
                ", numCnaps='" + numCnaps + '\'' +
                ", cin='" + cin + '\'' +
                ", nombreEnfants=" + nombreEnfants +
                ", situationFamiliale=" + (situationFamiliale != null ? situationFamiliale.getType() : "null") +
                '}';
    }
}
