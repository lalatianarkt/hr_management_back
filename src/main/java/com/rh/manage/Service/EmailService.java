package com.rh.manage.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Token;
import com.rh.manage.Model.User;
import com.rh.manage.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenService tokenService;

    public String sendTokenEmail(String recipientEmail) {
        try {
            // Générer un token
            String token = UUID.randomUUID().toString();

            // Créer le message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("admin.smartdevsolutions@gmail.com");
            message.setTo(recipientEmail);
            message.setSubject("Validation de votre compte");
            message.setText(
                "Voici votre code de validation : " + token +
                "\nCe code est valide pour 30 secondes."
            );

            System.out.println("message : " + message.getFrom());
            System.out.println("message : " + message.getTo());
            System.out.println("message : " + message.getSubject());
            System.out.println("message : " + message.getText());

            // Envoi
            mailSender.send(message);

            return token;

        } catch (org.springframework.mail.MailAuthenticationException e) {
            e.printStackTrace();
            // 🔴 ERREUR D'AUTHENTIFICATION SMTP
            throw new RuntimeException(
                "Erreur d'authentification email : vérifiez l'adresse email et le mot de passe SMTP",
                e
            );

        } catch (Exception e) {
            e.printStackTrace();
            // 🔴 AUTRES ERREURS
            throw new RuntimeException(
                "Erreur lors de l'envoi de l'email",
                e
            );
        }
    }


    @Transactional
    public String reSendToken(String recipientEmail){
        String token = sendTokenEmail(recipientEmail);
        User user = userRepository.findByEmail(recipientEmail).get();
        // Token dernierToken = tokenService.getDernierTokenActiveParUser(user);
        // dernierToken.setIsActive(0);
        // tokenService.update(dernierToken);
            Token newToken = new Token();
            LocalDateTime now = LocalDateTime.now();
            newToken.setExpiresAt(now.plusSeconds(120));
            newToken.setCreatedAt(now);
            newToken.setIsActive(1);
            newToken.setTokenGenere(token);
            newToken.setType("inscription");
            newToken.setUser(user);
            tokenService.save(newToken);
            System.out.println(token);
        return token;
    }
}
