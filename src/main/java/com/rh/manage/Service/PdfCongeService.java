package com.rh.manage.Service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.rh.manage.Dto.AttestationCongeData;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class PdfCongeService {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String DEFAULT_LOGO_PATH = "D:/Stage_smartDev/Projet_Gestion RH/hr_management_front/public/assets/img/smartdev_solutions.png";

    public byte[] genererAttestationCongePdf(AttestationCongeData data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            String html = genererHtmlAttestation(data);

            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(html, outputStream, converterProperties);

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF d'attestation de congé", e);
        }
    }

    public String genererHtmlAttestation(AttestationCongeData data) {
        String nomComplet = (safe(data.getNomEmploye()) + " " + safe(data.getPrenomEmploye())).trim();
        String typeConge = safe(data.getTypeConge());
        String dateDebut = formatDate(data.getDateDebut());
        String dateFin = formatDate(data.getDateFin());
        String nbJours = String.valueOf(data.getNbJours());
        String commentaireManager = safe(data.getCommentaireManager());
        String signatureImg = construireSignatureImage(data);

        return """
                <!DOCTYPE html>
                <html lang="fr">
                <head>
                    <meta charset="UTF-8">
                    <title>Attestation de congé</title>
                    <style>
                        body {
                            font-family: Helvetica, Arial, sans-serif;
                            font-size: 12px;
                            color: #222;
                            margin: 30px;
                            line-height: 1.6;
                        }

                        .container {
                            border: 1px solid #cccccc;
                            padding: 25px;
                        }

                        .header {
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            gap: 12px;
                            margin-bottom: 30px;
                        }

                        .header-logo {
                            max-height: 48px;
                            max-width: 120px;
                        }

                        .titre {
                            font-size: 20px;
                            font-weight: bold;
                            margin-top: 10px;
                            text-decoration: underline;
                        }

                        .section {
                            margin-top: 20px;
                        }

                        .label {
                            font-weight: bold;
                        }

                        table {
                            width: 100%%;
                            border-collapse: collapse;
                            margin-top: 15px;
                        }

                        td {
                            border: 1px solid #999999;
                            padding: 10px;
                            vertical-align: top;
                        }

                        .signature {
                            margin-top: 50px;
                            text-align: right;
                        }

                        .signature img {
                            max-height: 80px;
                            max-width: 220px;
                            display: inline-block;
                        }

                        .footer {
                            margin-top: 40px;
                            font-size: 10px;
                            color: #666666;
                            text-align: center;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <div class="titre">ATTESTATION DE CONGÉ</div>
                        </div>

                        <div class="section">
                            <p>
                                Je soussigne(e),
                                atteste que l'employe(e)
                                <span class="label">%s</span>
                                beneficie d'un conge de type
                                <span class="label">%s</span>.
                            </p>
                        </div>

                        <div class="section">
                            <table>
                                <tr>
                                    <td class="label">Nom complet</td>
                                    <td>%s</td>
                                </tr>
                                <tr>
                                    <td class="label">Date de début</td>
                                    <td>%s</td>
                                </tr>
                                <tr>
                                    <td class="label">Date de fin</td>
                                    <td>%s</td>
                                </tr>
                                <tr>
                                    <td class="label">Nombre de jours</td>
                                    <td>%s</td>
                                </tr>
                                <tr>
                                    <td class="label">Commentaire manager</td>
                                    <td>%s</td>
                                </tr>
                            </table>
                        </div>

                        <div class="signature">
                            <p>Fait pour servir et valoir ce que de droit.</p>
                            <p>Service RH</p>
                        </div>

                        <div class="footer">
                            Document généré automatiquement par le système RH.
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(
                escapeHtml(nomComplet),
                escapeHtml(typeConge),
                escapeHtml(nomComplet),
                escapeHtml(dateDebut),
                escapeHtml(dateFin),
                escapeHtml(nbJours),
                escapeHtml(commentaireManager),
                signatureImg
        );
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMAT) : "";
    }

    private String formatDecimal(BigDecimal value) {
        return value != null ? value.stripTrailingZeros().toPlainString() : "";
    }

    private String safe(String value) {
        return value == null ? "" : value;
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

    private String construireSignatureImage(AttestationCongeData data) {
        String dataUri = chargerLogoDataUri(data);
        if (dataUri.isBlank()) {
            return "";
        }
        return "<img src=\"" + dataUri + "\" alt=\"Signature RH\">";
    }

    private String chargerLogoDataUri(AttestationCongeData data) {
        String logoPath = safe(data.getLogo());
        if (!logoPath.isBlank()) {
            String fromPath = lireImageEnBase64(logoPath);
            if (!fromPath.isBlank()) {
                return "data:image/png;base64," + fromPath;
            }
            if (logoPath.startsWith("data:image")) {
                return logoPath;
            }
        }
        String fallback = lireImageEnBase64(DEFAULT_LOGO_PATH);
        if (!fallback.isBlank()) {
            return "data:image/png;base64," + fallback;
        }
        return "";
    }

    private String lireImageEnBase64(String path) {
        try {
            byte[] bytes = Files.readAllBytes(Path.of(path));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            return "";
        }
    }
}
