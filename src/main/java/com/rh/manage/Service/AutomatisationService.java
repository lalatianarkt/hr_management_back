package com.rh.manage.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rh.manage.Dto.AttestationCongeData;
import com.rh.manage.Model.DemandeConge;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.Mouvement;
import com.rh.manage.Model.UserRole;

@Service
public class AutomatisationService {
    @Autowired
    PdfCongeService pdfCongeService;

    public void sendMailDeBienvenueAutomatique() {
        try {
            // String WEBHOOK_URL = "https://n8n.joel-stephanas.com/webhook-test/436860c2-fe5b-4156-8978-8ef17e155b4f"; // test
            // /String WEBHOOK_URL = "https://n8n.joel-stephanas.com/webhook/436860c2-fe5b-4156-8978-8ef17e155b4f"; //prod

            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> payload = new HashMap<>();
            payload.put("nom", "rakoto");
            payload.put("email", "ravotina3@gmail.com");
            // payload.put("matricule", employe.getMatricule());

            // restTemplate.postForObject(WEBHOOK_URL, payload, String.class);

            System.out.println("Webhook envoyé à n8n avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l’envoi au webhook n8n : " + e.getMessage());
        }
    }

    public void sendEmailNotificationDemandeMvt(Mouvement data) {
        try {
            String WEBHOOK_URL = "https://n8n.joel-stephanas.com/webhook/193514cf-09ec-49f7-a925-dc9a0e56b2d6";
            // String WEBHOOK_URL =  "https://n8n.joel-stephanas.com/webhook-test/193514cf-09ec-49f7-a925-dc9a0e56b2d6";
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> payload = construirePayloadMouvement(data);
            restTemplate.postForObject(WEBHOOK_URL, payload, String.class);

            System.out.println("Webhook envoye a n8n avec succes !");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi au webhook n8n : " + e.getMessage());
        }
    }

    public void sendEmailNotificationDemandeConge(AttestationCongeData data) {
        try {
            String webhookUrl = "https://n8n.joel-stephanas.com/webhook/aa4f5e03-21a2-436b-8c96-5b644d4f92b6";
            // String webhookUrlTest = "https://n8n.joel-stephanas.com/webhook-test/aa4f5e03-21a2-436b-8c96-5b644d4f92b6";
            // https://n8n.joel-stephanas.com/webhook-test/aa4f5e03-21a2-436b-8c96-5b644d4f92b6
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> payload = new HashMap<>();

            byte[] pdfBytes = pdfCongeService.genererAttestationCongePdf(data);
            String pdfBase64 = java.util.Base64.getEncoder().encodeToString(pdfBytes);
            if(data.getStatut() == 6){
                System.out.println("Statut de la demande de congé : Validée");
                payload = construirePayloadAttestation(data, pdfBase64);
            } else if(data.getStatut() == 7){
                System.out.println("Statut de la demande de congé : Refusée");
                payload = construirePayloadAttestationWithoutPdf(data);
            } else {
                System.out.println("Statut de la demande de congé : " + data.getStatut());
            }
            restTemplate.postForObject(webhookUrl, payload, String.class);

            System.out.println("Webhook de notification de demande de conge envoye a n8n avec succes !");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi du webhook de notification de demande de conge a n8n : " + e.getMessage());
        }
    }

    private Map<String, Object> construirePayloadAttestationWithoutPdf(AttestationCongeData data) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("email_destinataire", data.getEmailEmploye());
        payload.put("nom_employe", data.getNomEmploye());
        payload.put("prenom_employe", data.getPrenomEmploye());
        payload.put("date_debut", data.getDateDebut());
        payload.put("date_fin", data.getDateFin());
        payload.put("nb_jours", data.getNbJours());
        payload.put("statut", data.getStatut());
        payload.put("type_conge", data.getTypeConge());
        payload.put("commentaire_manager", data.getCommentaireManager());
        payload.put("nom_societe", data.getNomSociete());
        return payload;
    }
    
    private Map<String, Object> construirePayloadAttestation(AttestationCongeData data, String pdfBase64) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("email_destinataire", data.getEmailEmploye());
        payload.put("nom_employe", data.getNomEmploye());
        payload.put("prenom_employe", data.getPrenomEmploye());
        payload.put("date_debut", data.getDateDebut());
        payload.put("date_fin", data.getDateFin());
        payload.put("nb_jours", data.getNbJours());
        payload.put("statut", data.getStatut());
        payload.put("type_conge", data.getTypeConge());
        payload.put("commentaire_manager", data.getCommentaireManager());
        payload.put("nom_societe", data.getNomSociete());
        payload.put("pdf_base64", pdfBase64);
        payload.put("nom_fichier", "attestation_conge.pdf");
        payload.put("mime_type", "application/pdf");
        return payload;
    }


    private Map<String, Object> construirePayloadMouvement(Mouvement data) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("ancien_departement", data.getInfosProActuel().getPoste().getDepartement().getNom());
        payload.put("nouveau_departement", data.getInfosProPropose().getPoste().getDepartement().getNom());
        payload.put("ancien_manager", data.getInfosProActuel().getManager().getEmploye().getPrenom());
        payload.put("nouveau_manager", data.getInfosProPropose().getManager().getEmploye().getPrenom());
        payload.put("nom_employe_concerne", data.getEmployeConcerne().getPrenom());
        payload.put("statut", data.getStatut());
        payload.put("email_manager", data.getInfosProPropose().getManager().getEmploye().getEmail());
        payload.put("sujet", "Demande de :" + data.getTypeMouvement().getType());
        // payload.put("");

        if (data.getStatut() == 6) {
            payload.put("html", buildEmailHtmlMvtRefuse(data));
        } else {
            payload.put("html", buildEmailHtmlMvtValide(data));
        }
        return payload;
    }

    public String buildEmailHtmlCongeValide(AttestationCongeData data) {
        String nomComplet = (safe(data.getPrenomEmploye()) + " " + safe(data.getNomEmploye())).trim();
        return """
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.5; color: #222;">
                  <p>Bonjour %s,</p>
                  <p>Votre demande de conge a été approuvee.</p>
                  <p>L'attestation correspondante est jointe a ce message.</p>
                  <p>Pour toute question, merci de contacter le Service RH.</p>
                  <p>Bien cordialement,<br>Service RH</p>
                </body>
                </html>
                """.formatted(escapeHtml(nomComplet.isBlank() ? "": nomComplet));
    }

    public String buildEmailHtmlCongeRefuse(AttestationCongeData data) {
        String nomComplet = (safe(data.getPrenomEmploye()) + " " + safe(data.getNomEmploye())).trim();
        String commentaire = safe(data.getCommentaireManager());
        String commentaireHtml = commentaire.isBlank()
                ? ""
                : "<p>Motif: " + escapeHtml(commentaire) + "</p>";
        return """
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.5; color: #222;">
                  <p>Bonjour %s,</p>
                  <p>Votre demande de conge a ete refusée.</p>
                  %s
                  <p>Pour toute question, merci de contacter le Service RH.</p>
                  <p>Bien cordialement,<br>Service RH</p>
                </body>
                </html>
                """.formatted(escapeHtml(nomComplet.isBlank() ? "" : nomComplet), commentaireHtml);
    }

    public String buildEmailHtmlMvtValide(Mouvement data) {
        String employe = safe(data.getEmployeConcerne().getPrenom());
        String ancienPoste = safe(data.getInfosProActuel().getPoste().getNom());
        String nouveauPoste = safe(data.getInfosProPropose().getPoste().getNom());
        String ancienDept = safe(data.getInfosProActuel().getPoste().getDepartement().getNom());
        String nouveauDept = safe(data.getInfosProPropose().getPoste().getDepartement().getNom());
        String ancienManager = safe(data.getInfosProActuel().getManager().getEmploye().getPrenom());
        String nouveauManager = safe(data.getInfosProPropose().getManager().getEmploye().getPrenom());
        boolean sameDepartement = false;
        if (data.getInfosProActuel() != null
                && data.getInfosProActuel().getDepartement() != null
                && data.getInfosProPropose() != null
                && data.getInfosProPropose().getDepartement() != null) {
            String idActuel = data.getInfosProActuel().getDepartement().getId();
            String idPropose = data.getInfosProPropose().getDepartement().getId();
            if (idActuel != null && idPropose != null && idActuel.equalsIgnoreCase(idPropose)) {
                sameDepartement = true;
            }
        }

        if (sameDepartement) {
            return """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.5; color: #222;">
                <p>Bonjour,</p>
                <p>Le mouvement de l'employé <strong>%s</strong> a été validé.</p>
                <p>Département: %s</p>
                <p>Ancien poste: %s</p>
                <p>Nouveau poste: %s</p>
                <p>Manager: %s</p>
                <p>Pour toute question, merci de contacter le Service RH.</p>
                <p>Bien cordialement,<br>Service RH</p>
            </body>
            </html>
            """.formatted(
            escapeHtml(employe),
            escapeHtml(ancienDept),
            escapeHtml(ancienPoste),
            escapeHtml(nouveauPoste),
            escapeHtml(nouveauManager.isBlank() ? ancienManager : nouveauManager)
            );
        }

        return """
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.5; color: #222;">
            <p>Bonjour,</p>
            <p>Le mouvement de l'employé <strong>%s</strong> a été validé.</p>
            <p>Ancien departement: %s</p>
            <p>Nouveau departement: %s</p>
            <p>Ancien manager: %s</p>
            <p>Nouveau manager: %s</p>
            <p>Pour toute question, merci de contacter le Service RH.</p>
            <p>Bien cordialement,<br>Service RH</p>
        </body>
        </html>
        """.formatted(
        escapeHtml(employe),
        escapeHtml(ancienDept),
        escapeHtml(nouveauDept),
        escapeHtml(ancienManager),
        escapeHtml(nouveauManager)
        );
    }

    public String buildEmailHtmlMvtRefuse(Mouvement data) {
        String employe = safe(data.getEmployeConcerne().getPrenom());
        return """
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.5; color: #222;">
                  <p>Bonjour,</p>
                  <p>Le mouvement de l'employé <strong>%s</strong> a été refusé.</p>
                  <p>Pour toute question, merci de contacter le Service RH.</p>
                  <p>Bien cordialement,<br>Service RH</p>
                </body>
                </html>
                """.formatted(escapeHtml(employe));
    }

    private String escapeHtml(String value) {
        if (value == null) return "";
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    // public void sendEmailNotificationMouvement(Mouvement mouvement){
    //     try {
    //         String WEBHOOK_URL = "https://n8n.joel-stephanas.com/webhook/436860c2-fe5b-4156-8978-8ef17e155b4f";
    //         System.out.println("tafiditra ato ah lalalalala");
    //         if(mouvement.getTypeMouvement().getId().equalsIgnoreCase("TM-002")){
    //             RestTemplate restTemplate = new RestTemplate();

    //             Map<String, Object> payload = new HashMap<>();
    //             payload.put("ancien département", mouvement.getInfosProActuel().getPoste().getDepartement().getNom());
    //             payload.put("nouveau département : ", mouvement.getInfosProPropose().getPoste().getDepartement().getNom());
    //             payload.put("ancien manager", mouvement.getInfosProActuel().getManager().getEmploye().getPrenom());
    //             payload.put("nouveau manager", mouvement.getInfosProPropose().getManager().getEmploye().getPrenom());
    //             payload.put("nom_employé_concerné", mouvement.getEmployeConcerne().getPrenom());
    //             payload.put("statut", mouvement.getStatut());
    //             payload.put("email_manager", mouvement.getInfosProPropose().getManager().getEmploye().getEmail());

    //             restTemplate.postForObject(WEBHOOK_URL, payload, String.class);

    //             System.out.println("Webhook envoyé à n8n avec succès !");
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         System.out.println("Erreur lors de l’envoi au webhook n8n : " + e.getMessage());
    //     }
    // }
}

// c/webhook/b80b9613-847f-4380-8c1f-98b18ef03d26
