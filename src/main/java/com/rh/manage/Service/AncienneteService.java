package com.rh.manage.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

@Service
public class AncienneteService {
    /**
     * Calcule l'ancienneté sous forme "X ans, Y mois, Z jours"
     */

    public String calculerAnciennete(LocalDate dateEmbauche) {
        if (dateEmbauche == null) {
            return "Non spécifiée";
        }
        
        LocalDate aujourdhui = LocalDate.now();
        
        // Vérifier que la date d'embauche n'est pas dans le futur
        if (dateEmbauche.isAfter(aujourdhui)) {
            return "Date future";
        }
        
        // Calculer la période
        Period periode = Period.between(dateEmbauche, aujourdhui);
        
        int annees = periode.getYears();
        int mois = periode.getMonths();
        int jours = periode.getDays();
        
        // Formater selon les règles françaises
        return formaterAnciennete(annees, mois, jours);
    }

    public int calculerAncienneteEnAnnees(LocalDate dateEmbauche) {
        if (dateEmbauche == null) {
            return 0;
        }

        LocalDate aujourdHui = LocalDate.now();

        if (dateEmbauche.isAfter(aujourdHui)) {
            return 0;
        }

        return (int) ChronoUnit.YEARS.between(dateEmbauche, aujourdHui);
    }
    
    /**
     * Calcule l'ancienneté avec une date de référence spécifique
     * (utile pour calculer l'ancienneté à une date donnée)
     */
    public String calculerAncienneteAvecDate(LocalDate dateEmbauche, LocalDate dateReference) {
        if (dateEmbauche == null || dateReference == null) {
            return "Non spécifiée";
        }
        
        if (dateEmbauche.isAfter(dateReference)) {
            return "Date future";
        }
        
        Period periode = Period.between(dateEmbauche, dateReference);
        
        int annees = periode.getYears();
        int mois = periode.getMonths();
        int jours = periode.getDays();
        
        return formaterAnciennete(annees, mois, jours);
    }
    
    /**
     * Calcule l'ancienneté pour une paie spécifique (mois/année donnés)
     */
    public String calculerAnciennetePourPaie(LocalDate dateEmbauche, Integer annee, Integer moisId) {
        if (dateEmbauche == null || annee == null || moisId == null) {
            return "Non spécifiée";
        }
        
        // Déterminer la date de référence (fin du mois de paie)
        LocalDate dateReference = LocalDate.of(annee, moisId, 1)
                .withDayOfMonth(LocalDate.of(annee, moisId, 1).lengthOfMonth());
        
        return calculerAncienneteAvecDate(dateEmbauche, dateReference);
    }
    
    /**
     * Calcule l'ancienneté en années décimales (pour calculs)
     */
    public double calculerAncienneteDecimal(LocalDate dateEmbauche) {
        if (dateEmbauche == null) {
            return 0.0;
        }
        
        LocalDate aujourdhui = LocalDate.now();
        
        if (dateEmbauche.isAfter(aujourdhui)) {
            return 0.0;
        }
        
        // Calculer le nombre total de jours
        long joursTotal = java.time.temporal.ChronoUnit.DAYS.between(dateEmbauche, aujourdhui);
        
        // Convertir en années décimales (365.25 jours par an en moyenne)
        return joursTotal / 365.25;
    }
    
    /**
     * Calcule l'ancienneté pour le décompte des congés
     * (règles spécifiques : un an = 2.5 jours de congé par mois)
     */
    public double calculerAnciennetePourConge(LocalDate dateEmbauche) {
        if (dateEmbauche == null) {
            return 0.0;
        }
        
        LocalDate aujourdhui = LocalDate.now();
        
        if (dateEmbauche.isAfter(aujourdhui)) {
            return 0.0;
        }
        
        Period periode = Period.between(dateEmbauche, aujourdhui);
        
        int annees = periode.getYears();
        int mois = periode.getMonths();
        
        // Calculer les mois complets
        double moisComplets = (annees * 12) + mois;
        
        // Ajouter les jours en fraction de mois
        int jours = periode.getDays();
        double joursEnMois = jours / 30.0; // Approximation
        
        return moisComplets + joursEnMois;
    }
    
    /**
     * Formate l'ancienneté de manière lisible
     */
    private String formaterAnciennete(int annees, int mois, int jours) {
        StringBuilder resultat = new StringBuilder();
        
        if (annees > 0) {
            resultat.append(annees).append(annees == 1 ? " an" : " ans");
        }
        
        if (mois > 0) {
            if (resultat.length() > 0) resultat.append(", ");
            resultat.append(mois).append(mois == 1 ? " mois" : " mois");
        }
        
        if (jours > 0 || (annees == 0 && mois == 0)) {
            if (resultat.length() > 0) resultat.append(", ");
            resultat.append(jours).append(jours == 1 ? " jour" : " jours");
        }
        
        // Si tout est à zéro (embauche aujourd'hui)
        if (resultat.length() == 0) {
            resultat.append("0 jour");
        }
        
        return resultat.toString();
    }
    
    /**
     * Formate l'ancienneté de manière détaillée
     */
    public String calculerAncienneteDetaillee(LocalDate dateEmbauche) {
        if (dateEmbauche == null) {
            return "Date d'embauche non renseignée";
        }
        
        LocalDate aujourdhui = LocalDate.now();
        
        if (dateEmbauche.isAfter(aujourdhui)) {
            return "Date d'embauche dans le futur: " + 
                   dateEmbauche.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        
        Period periode = Period.between(dateEmbauche, aujourdhui);
        
        long joursTotal = java.time.temporal.ChronoUnit.DAYS.between(dateEmbauche, aujourdhui);
        long moisTotal = java.time.temporal.ChronoUnit.MONTHS.between(dateEmbauche, aujourdhui);
        
        int annees = periode.getYears();
        int mois = periode.getMonths();
        int jours = periode.getDays();
        
        return String.format(
            "%d ans, %d mois, %d jours (Total: %d jours, %d mois)",
            annees, mois, jours, joursTotal, moisTotal
        );
    }
    
    /**
     * Calcule si l'employé a plus de X années d'ancienneté
     */
    public boolean aPlusDeXAnnees(LocalDate dateEmbauche, int anneesMinimum) {
        if (dateEmbauche == null) {
            return false;
        }
        
        LocalDate aujourdhui = LocalDate.now();
        Period periode = Period.between(dateEmbauche, aujourdhui);
        
        return periode.getYears() >= anneesMinimum;
    }

    /**
     * Ancienneté en mois COMPLETS
     * (ex: 1 an et 3 mois = 15 mois)
     */
    public int calculerAncienneteEnMois(LocalDate dateEmbauche) {
        if (dateEmbauche == null) {
            return 0;
        }

        LocalDate aujourdHui = LocalDate.now();

        if (dateEmbauche.isAfter(aujourdHui)) {
            return 0;
        }

        return (int) ChronoUnit.MONTHS.between(dateEmbauche, aujourdHui);
    }

    
    /**
     * Calcule la date d'anniversaire d'ancienneté
     */
    public LocalDate calculerDateAnniversaire(LocalDate dateEmbauche, int annees) {
        if (dateEmbauche == null) {
            return null;
        }
        
        return dateEmbauche.plusYears(annees);
    }
    
    /**
     * Calcule le nombre de jours jusqu'au prochain anniversaire d'ancienneté
     */
    public long joursJusquAnniversaire(LocalDate dateEmbauche) {
        if (dateEmbauche == null) {
            return -1;
        }
        
        LocalDate aujourdhui = LocalDate.now();
        LocalDate prochainAnniversaire = dateEmbauche.withYear(aujourdhui.getYear());
        
        // Si l'anniversaire est déjà passé cette année, prendre l'année prochaine
        if (prochainAnniversaire.isBefore(aujourdhui) || prochainAnniversaire.isEqual(aujourdhui)) {
            prochainAnniversaire = prochainAnniversaire.plusYears(1);
        }
        
        return java.time.temporal.ChronoUnit.DAYS.between(aujourdhui, prochainAnniversaire);
    }
}

