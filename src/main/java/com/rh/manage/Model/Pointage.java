package com.rh.manage.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "pointage")
public class Pointage {
    private static final AtomicInteger ID_COUNTER = new AtomicInteger(0);

    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "date_pointage", nullable = false)
    private LocalDate datePointage;
    
    @Column(name = "duree_heure_travaillee_minute")
    private Double dureeHeureTravailleeMinute;
    
    @Column(name = "duree_retard_minute")
    private Double dureeRetardMinute;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @Column(name = "duree_heure_supplementaire")
    private Double dureeHeureSupplementaire;
    
    @Column(name = "is_shift_jour")
    private Boolean isShiftJour;
    
    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;
    
    @Column(name = "is_week_end")
    private Boolean isWeekEnd;

    @Column(name = "est_en_conge")
    private Boolean estEnConge = false;
    
    @Column(name = "statut_pointage", nullable = false)
    private Integer statutPointage;
    
    @Column(name = "is_ferie")
    private Boolean isFerie;
    
    @Column(name = "date_heure_arrivee", nullable = false)
    private LocalDateTime dateHeureArrivee;
    
    @Column(name = "date_heure_depart", nullable = false)
    private LocalDateTime dateHeureDepart;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_employe", nullable = false, referencedColumnName = "id")
    private Employe employe;

   @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.trim().isEmpty()) {
            // Format court: PT-0001, PT-0002, ...
            int next = ID_COUNTER.updateAndGet(current -> (current % 9999) + 1);
            this.id = String.format("PT-%04d", next);
        }

        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        
        if (this.modifiedAt == null) {
            this.modifiedAt = this.createdAt;
        }
    }
        
    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDatePointage() {
        return datePointage;
    }

    public void setDatePointage(LocalDate datePointage) {
        this.datePointage = datePointage;
    }

    public Double getDureeHeureTravailleeMinute() {
        return dureeHeureTravailleeMinute;
    }

    public void setDureeHeureTravailleeMinute(Double dureeHeureTravailleeMinute) {
        this.dureeHeureTravailleeMinute = dureeHeureTravailleeMinute;
    }

    public Double getDureeRetardMinute() {
        return dureeRetardMinute;
    }

    public void setDureeRetardMinute(Double dureeRetardMinute) {
        this.dureeRetardMinute = dureeRetardMinute;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Double getDureeHeureSupplementaire() {
        return dureeHeureSupplementaire;
    }

    public void setDureeHeureSupplementaire(Double dureeHeureSupplementaire) {
        this.dureeHeureSupplementaire = dureeHeureSupplementaire;
    }

    public Boolean getIsShiftJour() {
        return isShiftJour;
    }

    public void setIsShiftJour(Boolean isShiftJour) {
        this.isShiftJour = isShiftJour;
    }



    public String getCommentaire() {
        return commentaire;
    }



    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }



    public Boolean getIsWeekEnd() {
        return isWeekEnd;
    }



    public void setIsWeekEnd(Boolean isWeekEnd) {
        this.isWeekEnd = isWeekEnd;
    }



    public Integer getStatutPointage() {
        return statutPointage;
    }



    public void setStatutPointage(Integer statutPointage) {
        this.statutPointage = statutPointage;
    }



    public Boolean getIsFerie() {
        return isFerie;
    }



    public void setIsFerie(Boolean isFerie) {
        this.isFerie = isFerie;
    }



    public LocalDateTime getDateHeureArrivee() {
        return dateHeureArrivee;
    }



    public void setDateHeureArrivee(LocalDateTime dateHeureArrivee) {
        this.dateHeureArrivee = dateHeureArrivee;
    }



    public LocalDateTime getDateHeureDepart() {
        return dateHeureDepart;
    }



    public void setDateHeureDepart(LocalDateTime dateHeureDepart) {
        this.dateHeureDepart = dateHeureDepart;
    }



    public Employe getEmploye() {
        return employe;
    }



    public void setEmploye(Employe employe) {
        this.employe = employe;
    }



    // Enums pour le statut
    public enum Statut {
        BROUILLON(0),
        VALIDE(1),
        PAYE(2),
        ANNULE(3);
        
        private final int code;
        
        Statut(int code) {
            this.code = code;
        }
        
        public int getCode() {
            return code;
        }
    }



    public Boolean getEstEnConge() {
        return estEnConge;
    }

    public void setEstEnConge(Boolean estEnConge) {
        this.estEnConge = estEnConge;
    }
}

