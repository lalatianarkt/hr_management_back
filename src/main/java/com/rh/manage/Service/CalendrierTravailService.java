package com.rh.manage.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Model.CalendrierFerie;
import com.rh.manage.Repository.CalendrierFerieRepository;

@Service
public class CalendrierTravailService {

    @Autowired
    private CalendrierFerieRepository calendrierFerieRepository;

    /**
     * Jours ouvrables en excluant uniquement les week-ends.
     */
    public long calculerJoursOuvrables(LocalDate dateDebut, LocalDate dateFin) {
        validerPeriode(dateDebut, dateFin);

        long joursOuvrables = 0;
        LocalDate courant = dateDebut;

        while (!courant.isAfter(dateFin)) {
            if (!estWeekend(courant)) {
                joursOuvrables++;
            }
            courant = courant.plusDays(1);
        }
        return joursOuvrables;
    }

    /**
     * Jours ouvrables en excluant week-ends et jours fériés actifs.
     */
    public long calculerJoursOuvrablesAvecFeries(LocalDate dateDebut, LocalDate dateFin) {
        validerPeriode(dateDebut, dateFin);

        List<LocalDate> joursFeries = getJoursFeriesActifs();
        long joursOuvrables = 0;
        LocalDate courant = dateDebut;

        while (!courant.isAfter(dateFin)) {
            if (!estWeekend(courant) && !joursFeries.contains(courant)) {
                joursOuvrables++;
            }
            courant = courant.plusDays(1);
        }

        return joursOuvrables;
    }

    /**
     * Retourne les dates des jours fériés actifs.
     */
    public List<LocalDate> getJoursFeriesActifs() {
        try {
            List<CalendrierFerie> joursFeries = calendrierFerieRepository.findByEstActifTrue();
            return joursFeries.stream()
                    .map(CalendrierFerie::getDateFerie)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des jours fériés: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Indique si une date donnée est fériée (active).
     */
    public boolean estFerie(LocalDate date) {
        if (date == null) {
            return false;
        }
        try {
            return calendrierFerieRepository.existsByDateFerieAndEstActifTrue(date);
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification du jour férié: " + e.getMessage());
            return false;
        }
    }

    private void validerPeriode(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates sont obligatoires");
        }
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être antérieure ou égale à la date de fin");
        }
    }

    private boolean estWeekend(LocalDate date) {
        DayOfWeek dow = date.getDayOfWeek();
        return dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;
    }
}
