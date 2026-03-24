package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Historique_mouvement")
public class HistoriqueMouvement {

    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "ancien_info_id", length = 50)
    private String ancienInfoId;

    @Column(name = "nouvelle_info_id", length = 50)
    private String nouvelleInfoId;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDate dateMouvement;

    @Column(length = 255)
    private String commentaire;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "ancien_poste", length = 150)
    private String ancienPoste;

    @Column(name = "nouvel_poste", length = 150)
    private String nouvelPoste;

    @Column(name = "ancien_departement", length = 150)
    private String ancienDepartement;

    @Column(name = "nouveau_departement", length = 150)
    private String nouveauDepartement;

    @Column(name = "ancien_type_contrat", length = 150)
    private String ancienTypeContrat;

    @Column(name = "nouvel_type_contrat", length = 150)
    private String nouvelTypeContrat;

    @Column(name = "isManager", nullable = false)
    private Integer isManager; // 0 = non, 1 = oui

    @Column(name = "effectue_par_id", length = 50)
    private String effectueParId; // id de l'utilisateur/admin qui effectue le mouvement

    @Column(name = "type_mouvement", length = 250)
    private String typeMouvement; // texte décrivant le type de mouvement

    // Constructeur par défaut
    public HistoriqueMouvement() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAncienInfoId() { return ancienInfoId; }
    public void setAncienInfoId(String ancienInfoId) { this.ancienInfoId = ancienInfoId; }

    public String getNouvelleInfoId() { return nouvelleInfoId; }
    public void setNouvelleInfoId(String nouvelleInfoId) { this.nouvelleInfoId = nouvelleInfoId; }

    public LocalDate getDateMouvement() { return dateMouvement; }
    public void setDateMouvement(LocalDate dateMouvement) { this.dateMouvement = dateMouvement; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getAncienPoste() { return ancienPoste; }
    public void setAncienPoste(String ancienPoste) { this.ancienPoste = ancienPoste; }

    public String getNouvelPoste() { return nouvelPoste; }
    public void setNouvelPoste(String nouvelPoste) { this.nouvelPoste = nouvelPoste; }

    public String getAncienDepartement() { return ancienDepartement; }
    public void setAncienDepartement(String ancienDepartement) { this.ancienDepartement = ancienDepartement; }

    public String getNouveauDepartement() { return nouveauDepartement; }
    public void setNouveauDepartement(String nouveauDepartement) { this.nouveauDepartement = nouveauDepartement; }

    public String getAncienTypeContrat() { return ancienTypeContrat; }
    public void setAncienTypeContrat(String ancienTypeContrat) { this.ancienTypeContrat = ancienTypeContrat; }

    public String getNouvelTypeContrat() { return nouvelTypeContrat; }
    public void setNouvelTypeContrat(String nouvelTypeContrat) { this.nouvelTypeContrat = nouvelTypeContrat; }

    public Integer getIsManager() { return isManager; }
    public void setIsManager(Integer isManager) { this.isManager = isManager; }

    public String getEffectueParId() { return effectueParId; }
    public void setEffectueParId(String effectueParId) { this.effectueParId = effectueParId; }

    public String getTypeMouvement() { return typeMouvement; }
    public void setTypeMouvement(String typeMouvement) { this.typeMouvement = typeMouvement; }
}


