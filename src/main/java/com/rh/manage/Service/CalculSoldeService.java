package com.rh.manage.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.RegleGestionConges;

@Service
public class CalculSoldeService {

    @Autowired
    InfosProfessionnellesService infosProfessionnellesService;

    @Autowired
    RegleGestionCongesService regleGestionCongesService;

    public BigDecimal calculAncienneteEnMois(Employe employe) {
        try {
            InfosProfessionnelles infosPro = infosProfessionnellesService
                .getInfosProfessionnellesByEmployeId(employe.getId()).get(0);
            
            if (infosPro == null || infosPro.getDateEmbauche() == null) {
                // Gérer le cas où la date d'embauche est manquante
                return BigDecimal.ZERO;
            }
            
            LocalDate dateEmbauche = infosPro.getDateEmbauche();
            LocalDate dateActuelle = LocalDate.now();
            
            // Calculer la différence en années et mois
            Period periode = Period.between(dateEmbauche, dateActuelle);
            
            // Convertir en mois totaux
            int moisTotaux = (periode.getYears() * 12) + periode.getMonths();
            
            // Ajouter les jours fractionnels (ex: 15 jours = 0.5 mois)
            int joursDansMois = dateEmbauche.lengthOfMonth(); // nombre de jours dans le mois d'embauche
            BigDecimal joursFraction = new BigDecimal(periode.getDays())
                .divide(new BigDecimal(joursDansMois), 2, RoundingMode.HALF_UP);
            
            BigDecimal ancienneteMois = new BigDecimal(moisTotaux).add(joursFraction);
            
            return ancienneteMois.setScale(2, RoundingMode.HALF_UP);
            
        } catch (Exception e) {
            // Log l'erreur et retourner une valeur par défaut
            // log.error("Erreur calcul ancienneté pour employé: " + employe.getId(), e);
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal calculSoldeCongeParAnciennete(Employe employe) {
        RegleGestionConges regle = regleGestionCongesService
                .getDerniereRegleGestionCongesParStatutActive();

        if (regle == null) {
            throw new RuntimeException("Aucune règle de gestion des congés active trouvée");
        }

        BigDecimal soldeMensuel = BigDecimal.valueOf(regle.getSoldeMensuel());
        BigDecimal anciennete = calculAncienneteEnMois(employe); // doit retourner un BigDecimal

        // (anciennete * soldeMensuel) = total acquis
        BigDecimal soldeCalcule = soldeMensuel.multiply(anciennete);

        return soldeCalcule;
    }

    public boolean isEligible(Employe employe) {
        RegleGestionConges regle = regleGestionCongesService
                .getDerniereRegleGestionCongesParStatutActive();
        int anciennete = calculAncienneteEnMois(employe).intValue();
        return anciennete >= regle.getAncienneteRequis();
    }
}
