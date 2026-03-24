package com.rh.manage.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.Mouvement;

@Service
public class AutomatisationService {
    public void sendMailDeBienvenueAutomatique(Employe employe) {
        try {
            // String WEBHOOK_URL = "https://n8n.joel-stephanas.com/webhook-test/436860c2-fe5b-4156-8978-8ef17e155b4f"; // test
            String WEBHOOK_URL = "https://n8n.joel-stephanas.com/webhook/436860c2-fe5b-4156-8978-8ef17e155b4f"; //prod

            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> payload = new HashMap<>();
            payload.put("nom", employe.getPrenom());
            payload.put("email", employe.getEmail());
            // payload.put("matricule", employe.getMatricule());

            restTemplate.postForObject(WEBHOOK_URL, payload, String.class);

            System.out.println("Webhook envoyé à n8n avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l’envoi au webhook n8n : " + e.getMessage());
        }
    }

    public void sendEmailNotificationMouvement(Mouvement mouvement){
        try {
            String WEBHOOK_URL = "https://n8n.joel-stephanas.com/webhook/436860c2-fe5b-4156-8978-8ef17e155b4f";
            System.out.println("tafiditra ato ah lalalalala");
            if(mouvement.getTypeMouvement().getId().equalsIgnoreCase("TM-002")){
                RestTemplate restTemplate = new RestTemplate();

                Map<String, Object> payload = new HashMap<>();
                payload.put("ancien département", mouvement.getInfosProActuel().getPoste().getDepartement().getNom());
                payload.put("nouveau département : ", mouvement.getInfosProPropose().getPoste().getDepartement().getNom());
                payload.put("ancien manager", mouvement.getInfosProActuel().getManager().getEmploye().getPrenom());
                payload.put("nouveau manager", mouvement.getInfosProPropose().getManager().getEmploye().getPrenom());
                payload.put("nom_employé_concerné", mouvement.getEmployeConcerne().getPrenom());
                payload.put("statut", mouvement.getStatut());
                payload.put("email_manager", mouvement.getInfosProPropose().getManager().getEmploye().getEmail());

                restTemplate.postForObject(WEBHOOK_URL, payload, String.class);

                System.out.println("Webhook envoyé à n8n avec succès !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l’envoi au webhook n8n : " + e.getMessage());
        }
    }

}

// c/webhook/b80b9613-847f-4380-8c1f-98b18ef03d26
