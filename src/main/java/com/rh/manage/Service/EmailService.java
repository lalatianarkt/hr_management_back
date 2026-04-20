package com.rh.manage.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Token;
import com.rh.manage.Model.User;
import com.rh.manage.Repository.UserRepository;

import jakarta.mail.internet.MimeMessage;

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
            String verificationCode = generateVerificationCode();

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom("admin.smartdevsolutions@gmail.com");
            helper.setTo(recipientEmail);
            helper.setSubject("Validation de votre compte");
            helper.setText(buildVerificationEmailHtml(verificationCode), true);

            mailSender.send(mimeMessage);
            return verificationCode;

        } catch (org.springframework.mail.MailAuthenticationException e) {
            e.printStackTrace();
            throw new RuntimeException(
                "Erreur d'authentification email : verifiez l'adresse email et le mot de passe SMTP",
                e
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(
                "Erreur lors de l'envoi de l'email",
                e
            );
        }
    }

    @Transactional
    public String reSendToken(String recipientEmail) {
        String verificationCode = sendTokenEmail(recipientEmail);
        User user = userRepository.findByEmail(recipientEmail).get();

        Token newToken = new Token();
        LocalDateTime now = LocalDateTime.now();
        newToken.setExpiresAt(now.plusSeconds(120));
        newToken.setCreatedAt(now);
        newToken.setIsActive(1);
        newToken.setTokenGenere(org.springframework.security.crypto.bcrypt.BCrypt.hashpw(verificationCode, org.springframework.security.crypto.bcrypt.BCrypt.gensalt()));
        newToken.setType("inscription");
        newToken.setUser(user);
        tokenService.save(newToken);

        return verificationCode;
    }

    private String generateVerificationCode() {
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(code);
    }

    private String buildVerificationEmailHtml(String verificationCode) {
        return """
            <div style=\"margin:0;padding:24px;background-color:#f4f6fb;font-family:Arial,sans-serif;color:#1f2937;\">
              <div style=\"max-width:560px;margin:0 auto;background:#ffffff;border-radius:18px;overflow:hidden;box-shadow:0 10px 30px rgba(15,23,42,0.08);\">
                <div style=\"padding:32px 32px 20px;background:linear-gradient(135deg,#0f766e,#2563eb);color:#ffffff;\">
                  <h1 style=\"margin:0;font-size:24px;\">Validation de votre compte</h1>
                  <p style=\"margin:12px 0 0;font-size:14px;opacity:0.92;\">Utilisez le code ci-dessous pour activer votre acces.</p>
                </div>
                <div style=\"padding:32px;\">
                  <p style=\"margin:0 0 18px;font-size:15px;line-height:1.6;\">Bonjour,</p>
                  <p style=\"margin:0 0 24px;font-size:15px;line-height:1.6;\">Voici votre code de verification a 6 chiffres :</p>
                  <div style=\"margin:0 auto 24px;max-width:260px;background:#eff6ff;border:1px solid #bfdbfe;border-radius:16px;padding:18px 24px;text-align:center;\">
                    <div style=\"font-size:32px;letter-spacing:10px;font-weight:700;color:#1d4ed8;\">%s</div>
                  </div>
                  <p style=\"margin:0 0 12px;font-size:14px;line-height:1.6;\">Ce code est valide pendant <strong>2 minutes</strong>.</p>
                  <p style=\"margin:0;font-size:13px;line-height:1.6;color:#6b7280;\">Si vous n'etes pas a l'origine de cette demande, vous pouvez ignorer cet email.</p>
                </div>
              </div>
            </div>
            """.formatted(verificationCode);
    }
}

