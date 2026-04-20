package com.rh.manage.Service;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
// import com.lowagie.text.DocumentException;
// import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.rh.manage.Dto.EmployeInfosDTO;
import com.rh.manage.Model.EmergencyContact;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.ModePaiement;
import com.rh.manage.Model.PreparationExportEmploye;
import com.rh.manage.Model.VuePaieComplete;
import com.rh.manage.Model.VuePaieFille;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.geom.PageSize;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ExportService {
    @Autowired
    EmployeService employeService;

    @Autowired
    InfosProfessionnellesService infosProfessionnellesService;

    @Autowired
    PreparationExportEmployeService preparationExportEmployeService;

    @Autowired
    ModePaiementService modePaiementService;

    @Autowired
    VuePaieFilleService vuePaieFilleService;

    @Autowired
    VuePaieCompleteService vuePaieCompleteService;

    public InfosProfessionnelles preparationExportEmploye(){
        return new InfosProfessionnelles();
    }

    public void exportHeuresSupplementaires(HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Heures supplémentaires");

        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("Matricule");
        header.createCell(1).setCellValue("HS 130%");
        header.createCell(2).setCellValue("HS 140%");
        header.createCell(3).setCellValue("HS 150%");
        header.createCell(4).setCellValue("HS 130% exo");
        header.createCell(5).setCellValue("HS 150% exo");
        
        List<InfosProfessionnelles> les_infos_pro = infosProfessionnellesService.getAllInfoProActif();
        int rowIndex = 1;
        for (InfosProfessionnelles infosProfessionnelles : les_infos_pro) {
            XSSFRow row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(infosProfessionnelles.getMatricule());
            // Vous pouvez ajouter les autres cellules ici si nÃ©cessaire
            row.createCell(1).setCellValue(""); // HS 130%
            row.createCell(2).setCellValue(""); // HS 140%
            row.createCell(3).setCellValue(""); // HS 150%
            row.createCell(4).setCellValue(""); // HS 130% exo
            row.createCell(5).setCellValue(""); // HS 150% exo
        }

        // AUTO-SIZE les colonnes pour une meilleure lisibilitÃ©
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            outputStream.flush();
        } finally {
            workbook.close();
        }
    }

    public void exportEmployesWithPrep(HttpServletResponse response) throws IOException {
        List<Employe> lesEmployes = employeService.getEmployesActifs();
        
        PreparationExportEmploye preparationExportEmploye = preparationExportEmployeService.getActiveConfiguration();
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Employés");
        
        Map<String, InfosProfessionnelles> infosProMap = new HashMap<>();
        for (Employe emp : lesEmployes) {
                InfosProfessionnelles infoActuel = infosProfessionnellesService
                        .getDerniereInfoProfessionnelleByEmployeId(emp.getId())
                        .orElse(null);
                if (infoActuel == null) {
                        System.out.println("[EXPORT] Aucune info pro active (statut=0) pour l'employe : " + emp.getId());
                }
                infosProMap.put(emp.getId(), infoActuel);
        }
        
        XSSFRow header = sheet.createRow(0);
        int headerCellIndex = 0;
        
        // === INFORMATIONS PERSONNELLES ===
        if (preparationExportEmploye.isMatricule()) {
                header.createCell(headerCellIndex++).setCellValue("Matricule");
        }
        if (preparationExportEmploye.isNom()) {
                header.createCell(headerCellIndex++).setCellValue("Nom");
        }
        if (preparationExportEmploye.isPrenom()) {
                header.createCell(headerCellIndex++).setCellValue("Prénom");
        }
        if (preparationExportEmploye.isDateNaissance()) {
                header.createCell(headerCellIndex++).setCellValue("Date de naissance");
        }
        if (preparationExportEmploye.isEmail()) {
                header.createCell(headerCellIndex++).setCellValue("Email");
        }
        if (preparationExportEmploye.isCin()) {
                header.createCell(headerCellIndex++).setCellValue("CIN");
        }
        if (preparationExportEmploye.isLieuNaissance()) {
                header.createCell(headerCellIndex++).setCellValue("Lieu de naissance");
        }
        if (preparationExportEmploye.isTelephone()) {
                header.createCell(headerCellIndex++).setCellValue("Téléphone");
        }
        if (preparationExportEmploye.isCodePostal()) {
                header.createCell(headerCellIndex++).setCellValue("Code postal");
        }
        if (preparationExportEmploye.isAdresse()) {
                header.createCell(headerCellIndex++).setCellValue("Adresse");
        }
        if (preparationExportEmploye.isNumCnaps()) {
                header.createCell(headerCellIndex++).setCellValue("Numéro CNAPS");
        }
        if (preparationExportEmploye.isNumOstie()) {
                header.createCell(headerCellIndex++).setCellValue("Numéro OSTIE");
        }
        if (preparationExportEmploye.isNationalite()) {
                header.createCell(headerCellIndex++).setCellValue("Nationalité");
        }
        if (preparationExportEmploye.isSexe()) {
                header.createCell(headerCellIndex++).setCellValue("Sexe");
        }
        
        // === INFORMATIONS FAMILIALES ===
        if (preparationExportEmploye.isSituationFamiliale()) {
                header.createCell(headerCellIndex++).setCellValue("Situation familiale");
        }
        if (preparationExportEmploye.isNomCompletMere()) {
                header.createCell(headerCellIndex++).setCellValue("Nom complet de la mère");
        }
        if (preparationExportEmploye.isNomCompletPere()) {
                header.createCell(headerCellIndex++).setCellValue("Nom complet du père");
        }
        if (preparationExportEmploye.isNbEnfants()) {
                header.createCell(headerCellIndex++).setCellValue("Nombre d'enfants");
        }
        if (preparationExportEmploye.isNomConjoint()) {
                header.createCell(headerCellIndex++).setCellValue("Nom du conjoint");
        }
        
        // === INFORMATIONS PROFESSIONNELLES ===
        if (preparationExportEmploye.isInfoProDateEmbauche()) {
                header.createCell(headerCellIndex++).setCellValue("Date d'embauche");
        }
        if (preparationExportEmploye.isInfoProSalaireBase()) {
                header.createCell(headerCellIndex++).setCellValue("Salaire de base");
        }
        if (preparationExportEmploye.isInfoProClassification()) {
                header.createCell(headerCellIndex++).setCellValue("Classification");
        }
        if (preparationExportEmploye.isInfoProPeriodicitePaiement()) {
                header.createCell(headerCellIndex++).setCellValue("Périodicité de paiement");
        }
        if (preparationExportEmploye.isInfoProCategorie()) {
                header.createCell(headerCellIndex++).setCellValue("Catégorie professionnelle");
        }
        if (preparationExportEmploye.isInfoProPoste()) {
                header.createCell(headerCellIndex++).setCellValue("Poste");
        }
        if (preparationExportEmploye.isInfoProDepartement()) {
                header.createCell(headerCellIndex++).setCellValue("Département");
        }
        
        // === CONTACTS D'URGENCE ===
        if (preparationExportEmploye.isEmergencyContactNom()) {
                header.createCell(headerCellIndex++).setCellValue("Contact d'urgence - Nom");
        }
        if (preparationExportEmploye.isEmergencyContactTelephone()) {
                header.createCell(headerCellIndex++).setCellValue("Contact d'urgence - Téléphone");
        }
        if (preparationExportEmploye.isEmergencyContactEmail()) {
                header.createCell(headerCellIndex++).setCellValue("Contact d'urgence - Email");
        }
        
        // === MODES DE PAIEMENT ===
        if (preparationExportEmploye.isModePaiementNomBanque()) {
                header.createCell(headerCellIndex++).setCellValue("Nom de la banque");
        }
        if (preparationExportEmploye.isModePaiementCodeBanque()) {
                header.createCell(headerCellIndex++).setCellValue("Code banque");
        }
        if (preparationExportEmploye.isModePaiementCodeGuichet()) {
                header.createCell(headerCellIndex++).setCellValue("Code guichet");
        }
        
        // Remplir les données
        int rowIndex = 1;
        for (Employe emp : lesEmployes) {
                XSSFRow row = sheet.createRow(rowIndex++);
                int cellIndex = 0;
                InfosProfessionnelles infosPro = infosProMap.get(emp.getId());
                EmergencyContact emergencyContact = emp.getEmergencyContact();
                ModePaiement modePaiementParDefaut = null;
                if (preparationExportEmploye.isModePaiementNomBanque()
                        || preparationExportEmploye.isModePaiementCodeBanque()
                        || preparationExportEmploye.isModePaiementCodeGuichet()) {
                        try {
                                modePaiementParDefaut = modePaiementService.getDefaultModePaiementByEmploye(emp.getId());
                        } catch (Exception e) {
                                modePaiementParDefaut = null;
                        }
                }
                
                // === INFORMATIONS PERSONNELLES ===
                if (preparationExportEmploye.isMatricule()) {
                row.createCell(cellIndex++).setCellValue(infosPro != null ? infosPro.getMatricule() : "");
                }
                if (preparationExportEmploye.isNom()) {
                row.createCell(cellIndex++).setCellValue(emp.getNom() != null ? emp.getNom() : "");
                }
                if (preparationExportEmploye.isPrenom()) {
                row.createCell(cellIndex++).setCellValue(emp.getPrenom() != null ? emp.getPrenom() : "");
                }
                if (preparationExportEmploye.isDateNaissance()) {
                row.createCell(cellIndex++).setCellValue(emp.getDateNaissance() != null ? 
                        emp.getDateNaissance().toString() : "");
                }
                if (preparationExportEmploye.isEmail()) {
                row.createCell(cellIndex++).setCellValue(emp.getEmail() != null ? emp.getEmail() : "");
                }
                if (preparationExportEmploye.isCin()) {
                row.createCell(cellIndex++).setCellValue(emp.getCin() != null ? emp.getCin() : "");
                }
                if (preparationExportEmploye.isLieuNaissance()) {
                row.createCell(cellIndex++).setCellValue(emp.getLieuNaissance() != null ? emp.getLieuNaissance() : "");
                }
                if (preparationExportEmploye.isTelephone()) {
                row.createCell(cellIndex++).setCellValue(emp.getTelephone() != null ? emp.getTelephone() : "");
                }
                if (preparationExportEmploye.isCodePostal()) {
                row.createCell(cellIndex++).setCellValue(emp.getCodePostal() > 0 ? String.valueOf(emp.getCodePostal()) : "");
                }
                if (preparationExportEmploye.isAdresse()) {
                row.createCell(cellIndex++).setCellValue(emp.getAdresse() != null ? emp.getAdresse() : "");
                }
                if (preparationExportEmploye.isNumCnaps()) {
                row.createCell(cellIndex++).setCellValue(emp.getNumCnaps() != null ? emp.getNumCnaps() : "");
                }
                if (preparationExportEmploye.isNumOstie()) {
                row.createCell(cellIndex++).setCellValue(emp.getNumOstie() != null ? emp.getNumOstie() : "");
                }
                if (preparationExportEmploye.isNationalite()) {
                row.createCell(cellIndex++).setCellValue(emp.getNationalite() != null ? emp.getNationalite().getNationalite() : "");
                }
                if (preparationExportEmploye.isSexe()) {
                row.createCell(cellIndex++).setCellValue(emp.getSexe() != null ? emp.getSexe().getsexe() : "");
                }
                
                // === INFORMATIONS FAMILIALES ===
                if (preparationExportEmploye.isSituationFamiliale()) {
                row.createCell(cellIndex++).setCellValue(emp.getEtatCivil() != null ? 
                        emp.getEtatCivil().toString() : "");
                }
                if (preparationExportEmploye.isNomCompletMere()) {
                row.createCell(cellIndex++).setCellValue(emp.getNomMere() != null ? emp.getNomMere() : "");
                }
                if (preparationExportEmploye.isNomCompletPere()) {
                row.createCell(cellIndex++).setCellValue(emp.getNomPere() != null ? emp.getNomPere() : "");
                }
                if (preparationExportEmploye.isNbEnfants()) {
                row.createCell(cellIndex++).setCellValue(emp.getNbEnfants() != null ? 
                        String.valueOf(emp.getNbEnfants()) : "");
                }
                if (preparationExportEmploye.isNomConjoint()) {
                row.createCell(cellIndex++).setCellValue(emp.getNomConjoint() != null ? emp.getNomConjoint() : "");
                }
                
                // === INFORMATIONS PROFESSIONNELLES ===
                if (preparationExportEmploye.isInfoProDateEmbauche()) {
                row.createCell(cellIndex++).setCellValue(infosPro != null && infosPro.getDateEmbauche() != null ? 
                        infosPro.getDateEmbauche().toString() : "");
                }
                if (preparationExportEmploye.isInfoProSalaireBase()) {
                row.createCell(cellIndex++).setCellValue(infosPro != null ? 
                        String.valueOf(infosPro.getSalaireBase()) : "");
                }
                if (preparationExportEmploye.isInfoProClassification()) {
                row.createCell(cellIndex++).setCellValue(infosPro != null && infosPro.getClassification() != null ? 
                        infosPro.getClassification() : "");
                }
                if (preparationExportEmploye.isInfoProPeriodicitePaiement()) {
                row.createCell(cellIndex++).setCellValue(""); // À adapter selon votre modèle
                }
                if (preparationExportEmploye.isInfoProCategorie()) {
                row.createCell(cellIndex++).setCellValue(infosPro != null && infosPro.getCategorieProfessionnelle() != null ? 
                        infosPro.getCategorieProfessionnelle().getLibelle() : "");
                }
                if (preparationExportEmploye.isInfoProPoste()) {
                row.createCell(cellIndex++).setCellValue(infosPro != null && infosPro.getPoste() != null ? 
                        infosPro.getPoste().getNom() : "");
                }
                if (preparationExportEmploye.isInfoProDepartement()) {
                row.createCell(cellIndex++).setCellValue(infosPro != null && infosPro.getDepartement() != null ? 
                        infosPro.getDepartement().getNom() : "");
                }
                
                // === CONTACTS D'URGENCE ===
                if (preparationExportEmploye.isEmergencyContactNom()) {
                row.createCell(cellIndex++).setCellValue(emergencyContact != null && emergencyContact.getNom() != null ? 
                        emergencyContact.getNom() : "");
                }
                if (preparationExportEmploye.isEmergencyContactTelephone()) {
                row.createCell(cellIndex++).setCellValue(emergencyContact != null && emergencyContact.getContact() != null ? 
                        emergencyContact.getContact() : "");
                }
                if (preparationExportEmploye.isEmergencyContactEmail()) {
                row.createCell(cellIndex++).setCellValue(emergencyContact != null && emergencyContact.getEmail() != null ? 
                        emergencyContact.getEmail() : "");
                }
                
                // === MODES DE PAIEMENT ===
                if (preparationExportEmploye.isModePaiementNomBanque()) {
                row.createCell(cellIndex++).setCellValue(modePaiementParDefaut != null && modePaiementParDefaut.getNomBanque() != null ? 
                        modePaiementParDefaut.getNomBanque() : "");
                }
                if (preparationExportEmploye.isModePaiementCodeBanque()) {
                row.createCell(cellIndex++).setCellValue(modePaiementParDefaut != null && modePaiementParDefaut.getCodeBanque() != null ? 
                        modePaiementParDefaut.getCodeBanque() : "");
                }
                if (preparationExportEmploye.isModePaiementCodeGuichet()) {
                row.createCell(cellIndex++).setCellValue(modePaiementParDefaut != null && modePaiementParDefaut.getCodeGuichet() != null ? 
                        modePaiementParDefaut.getCodeGuichet() : "");
                }
        }
        
        // Ajuster automatiquement la largeur des colonnes
        for (int i = 0; i < headerCellIndex; i++) {
                sheet.autoSizeColumn(i);
        }
        
        // Écrire la réponse
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void exportEmployes(HttpServletResponse response) throws IOException {
        List<Employe> employes = employeService.findAllEmployeesActived();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Employés");

        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("Nom");
        header.createCell(1).setCellValue("Prénom");
        header.createCell(2).setCellValue("Email");
        header.createCell(3).setCellValue("Téléphone");

        int rowIndex = 1;
        for (Employe e : employes) {
            XSSFRow row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(e.getNom());
            row.createCell(1).setCellValue(e.getPrenom());
            row.createCell(2).setCellValue(e.getEmail());
            row.createCell(3).setCellValue(e.getTelephone());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void exportEtatPaieExcel(List<VuePaieComplete> paies, HttpServletResponse response, boolean includeDetails) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Etat de paie");
        XSSFRow header = sheet.createRow(0);

        int col = 0;
        header.createCell(col++).setCellValue("Paie ID");
        header.createCell(col++).setCellValue("Matricule");
        header.createCell(col++).setCellValue("Nom");
        header.createCell(col++).setCellValue("Prenom");
        header.createCell(col++).setCellValue("Nom complet");
        header.createCell(col++).setCellValue("Departement");
        header.createCell(col++).setCellValue("Fonction");
        header.createCell(col++).setCellValue("Periode debut");
        header.createCell(col++).setCellValue("Periode fin");
        header.createCell(col++).setCellValue("Mois");
        header.createCell(col++).setCellValue("Annee");
        header.createCell(col++).setCellValue("Statut");
        header.createCell(col++).setCellValue("Mode paiement");
        header.createCell(col++).setCellValue("Salaire base");
        header.createCell(col++).setCellValue("Salaire brut");
        header.createCell(col++).setCellValue("Total retenues");
        header.createCell(col++).setCellValue("Total cotisations");
        header.createCell(col++).setCellValue("Salaire net");

        int rowIndex = 1;
        for (VuePaieComplete paie : paies) {
            XSSFRow row = sheet.createRow(rowIndex++);
            int c = 0;
            row.createCell(c++).setCellValue(safeStr(paie.getPaieId()));
            row.createCell(c++).setCellValue(safeStr(paie.getMatricule()));
            row.createCell(c++).setCellValue(safeStr(paie.getNom()));
            row.createCell(c++).setCellValue(safeStr(paie.getPrenom()));
            row.createCell(c++).setCellValue(safeStr(paie.getNomComplet()));
            row.createCell(c++).setCellValue(safeStr(paie.getDepartement()));
            row.createCell(c++).setCellValue(safeStr(paie.getFonction()));
            row.createCell(c++).setCellValue(paie.getDateDebutPeriode() != null ? paie.getDateDebutPeriode().toString() : "");
            row.createCell(c++).setCellValue(paie.getDateFinPeriode() != null ? paie.getDateFinPeriode().toString() : "");
            row.createCell(c++).setCellValue(paie.getMoisPaieNom() != null ? paie.getMoisPaieNom() : "");
            row.createCell(c++).setCellValue(paie.getAnneePaie() != null ? paie.getAnneePaie().toString() : "");
            row.createCell(c++).setCellValue(safeStr(paie.getStatutPaieLibelle()));
            row.createCell(c++).setCellValue(safeStr(paie.getModePaiement()));
            row.createCell(c++).setCellValue(paie.getSalaireBase() != null ? paie.getSalaireBase().toString() : "");
            row.createCell(c++).setCellValue(paie.getSalaireBrut() != null ? paie.getSalaireBrut().toString() : "");
            row.createCell(c++).setCellValue(paie.getTotalRetenue() != null ? paie.getTotalRetenue().toString() : "");
            row.createCell(c++).setCellValue(paie.getTotalCotisations() != null ? paie.getTotalCotisations().toString() : "");
            row.createCell(c++).setCellValue(paie.getSalaireNet() != null ? paie.getSalaireNet().toString() : "");
        }

        for (int i = 0; i < col; i++) {
            sheet.autoSizeColumn(i);
        }

        if (includeDetails) {
            XSSFSheet detailsSheet = workbook.createSheet("Rubriques");
            XSSFRow detailsHeader = detailsSheet.createRow(0);
            int dcol = 0;
            detailsHeader.createCell(dcol++).setCellValue("Paie ID");
            detailsHeader.createCell(dcol++).setCellValue("Matricule");
            detailsHeader.createCell(dcol++).setCellValue("Nom complet");
            detailsHeader.createCell(dcol++).setCellValue("Code");
            detailsHeader.createCell(dcol++).setCellValue("Rubrique");
            detailsHeader.createCell(dcol++).setCellValue("Type");
            detailsHeader.createCell(dcol++).setCellValue("Base");
            detailsHeader.createCell(dcol++).setCellValue("Taux");
            detailsHeader.createCell(dcol++).setCellValue("Montant");
            detailsHeader.createCell(dcol++).setCellValue("Ordre");

            int drowIndex = 1;
            for (VuePaieComplete paie : paies) {
                List<VuePaieFille> lignes = vuePaieFilleService.getByIdPaie(paie.getPaieId());
                if (lignes == null || lignes.isEmpty()) {
                    continue;
                }
                for (VuePaieFille ligne : lignes) {
                    XSSFRow row = detailsSheet.createRow(drowIndex++);
                    int dc = 0;
                    row.createCell(dc++).setCellValue(safeStr(paie.getPaieId()));
                    row.createCell(dc++).setCellValue(safeStr(paie.getMatricule()));
                    row.createCell(dc++).setCellValue(safeStr(paie.getNomComplet()));
                    row.createCell(dc++).setCellValue(safeStr(ligne.getCode()));
                    row.createCell(dc++).setCellValue(safeStr(ligne.getRubriqueNom()));
                    row.createCell(dc++).setCellValue(safeStr(ligne.getTypeRubrique()));
                    row.createCell(dc++).setCellValue(ligne.getBase() != null ? ligne.getBase().toString() : "");
                    row.createCell(dc++).setCellValue(ligne.getTaux() != null ? ligne.getTaux().toString() : "");
                    row.createCell(dc++).setCellValue(ligne.getMontant() != null ? ligne.getMontant().toString() : "");
                    row.createCell(dc++).setCellValue(String.valueOf(ligne.getOrdre()));
                }
            }

            for (int i = 0; i < dcol; i++) {
                detailsSheet.autoSizeColumn(i);
            }
        }

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            outputStream.flush();
        } finally {
            workbook.close();
        }
    }

    
//     public void exportFicheEmploye(String idEmp, HttpServletResponse response) throws IOException {
//     // RÃ©cupÃ©rer l'employé
//     EmployeInfosDTO employe = employeService.getEmployeWithInfosById(idEmp);
//     if (employe == null) {
//         response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employé non trouvé");
//         return;
//     }


//     // DÃ©finir les headers de réponse pour tÃ©lÃ©chargement
//     response.setContentType("application/pdf");
//     response.setCharacterEncoding("UTF-8");
//     String fileName = "Fiche_Employe_" + 
//                      employe.getEmploye().getNom() + "_" + 
//                      employe.getEmploye().getPrenom() + "_" + 
//                      LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";
//     response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

//     // CrÃ©er le PDF avec format A4
//     PdfWriter writer = new PdfWriter(response.getOutputStream());
//     PdfDocument pdfDoc = new PdfDocument(writer);
//     Document document = new Document(pdfDoc, PageSize.A4);
//     document.setMargins(16, 16, 46, 16);
    
//     // ========== STYLES  ==========
//     // Palette alignÃ©e au logo SmartDev (teinte dominante observÃ©e: ~178,82,169)
//     DeviceRgb primaryColor = new DeviceRgb(178, 82, 169);
//     DeviceRgb secondaryColor = new DeviceRgb(245, 231, 243);
//     DeviceRgb accentColor = new DeviceRgb(152, 65, 144);
//     DeviceRgb darkGray = new DeviceRgb(95, 44, 90);
    
//     // Style pour le titre principal
//     Style titleStyle = new Style()
//             .setBold()
//             .setFontSize(14)
//             .setTextAlignment(TextAlignment.CENTER)
//             .setFontColor(primaryColor)
//             .setMarginBottom(3);
    
//     // Style pour le sous-titre
//     Style subtitleStyle = new Style()
//             .setBold()
//             .setFontSize(11)
//             .setTextAlignment(TextAlignment.CENTER)
//             .setFontColor(darkGray)
//             .setMarginBottom(6);
    
//     // Style pour les sections
//     Style sectionStyle = new Style()
//             .setBold()
//             .setFontSize(11)
//             .setMarginTop(6)
//             .setMarginBottom(4)
//             .setFontColor(primaryColor)
//             .setPaddingLeft(4)
//             .setBorderLeft(new SolidBorder(primaryColor, 2));
    
//     // Style pour les en-tÃªtes de cellule
//     Style cellHeaderStyle = new Style()
//             .setBold()
//             .setFontSize(11)
//             .setBackgroundColor(secondaryColor)
//             .setPadding(3)
//             .setBorder(new SolidBorder(secondaryColor, 1))
//             .setFontColor(darkGray);
    
//     // Style pour le contenu des cellules
//     Style cellContentStyle = new Style()
//             .setFontSize(11)
//             .setPadding(3)
//             .setBorder(new SolidBorder(secondaryColor, 1));
    
//     // Style pour le statut actif
//     Style activeStatusStyle = new Style()
//             .setBold()
//             .setFontSize(7)
//             .setFontColor(primaryColor)
//             .setPadding(3)
//             .setBorder(new SolidBorder(secondaryColor, 1))
//             .setTextAlignment(TextAlignment.CENTER);
    
//     // Style pour le statut inactif
//     Style inactiveStatusStyle = new Style()
//             .setBold()
//             .setFontSize(7)
//             .setFontColor(accentColor)
//             .setPadding(3)
//             .setBorder(new SolidBorder(secondaryColor, 1))
//             .setTextAlignment(TextAlignment.CENTER);

//     // ========== EN-TêTE DU DOCUMENT ==========
//     String logoPath = "D:\\Stage_smartDev\\Projet_Gestion RH\\Back\\manage\\manage\\src\\main\\resources\\static\\uploads\\logo\\logo.png";
//     Path logoFilePath = Paths.get(logoPath);
//     if (Files.exists(logoFilePath)) {
//         ImageData imageData = ImageDataFactory.create(logoFilePath.toAbsolutePath().toString());
//         Image logo = new Image(imageData)
//                 .scaleToFit(90, 36)
//                 .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER)
//                 .setMarginBottom(2);
//         document.add(logo);
//     }

//     // Ligne de sÃ©paration dÃ©corative en haut
//     document.add(new LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine(2))
//             .setStrokeColor(primaryColor)
//             .setMarginBottom(6));
    
//     // Titre principal
//     Paragraph title = new Paragraph("FICHE INDIVIDUELLE DU SALARIE")
//             .addStyle(titleStyle);
//     document.add(title);
    
//     // Nom complet de l'employÃ©
//     Paragraph subtitle = new Paragraph(
//             employe.getEmploye().getNom().toUpperCase() + " " + 
//             employe.getEmploye().getPrenom())
//             .addStyle(subtitleStyle);
//     document.add(subtitle);

//     String statutGlobal = "Non spécifié";
//     if (employe.getInfosProfessionnelles() != null && !employe.getInfosProfessionnelles().isEmpty()) {
//         InfosProfessionnelles statutRef = null;
//         for (InfosProfessionnelles info : employe.getInfosProfessionnelles()) {
//             if (info.getStatut() != -1) {
//                 statutRef = info;
//                 break;
//             }
//         }
//         if (statutRef == null) {
//             statutRef = employe.getInfosProfessionnelles().get(0);
//         }
//         if (statutRef.getStatut() != -1) {
//             statutGlobal = statutRef.getStatut() == 0 ? "ACTIF" : "INACTIF";
//         }
//     }
    
//     // Ligne d'information (ID et date)
//     Paragraph infoLine = new Paragraph()
//             .add("Référence: " + employe.getEmploye().getId())
//             .add("  •  ")
//             .add("Généré le: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
//             .add("  •  ")
//             .add("Statut: " + statutGlobal)
//             .setFontSize(7)
//             .setFontColor(accentColor)
//             .setTextAlignment(TextAlignment.CENTER)
//             .setMarginBottom(4);
//     document.add(infoLine);

//     // ========== INFORMATIONS PERSONNELLES ==========
//     Paragraph persoTitle = new Paragraph("INFORMATIONS PERSONNELLES")
//             .addStyle(sectionStyle)
//             .setKeepWithNext(true);
//     document.add(persoTitle);
    
//     Table tablePerso = new Table(UnitValue.createPercentArray(new float[]{35, 65}))
//             .useAllAvailableWidth()
//             .setMarginBottom(6)
//             .setKeepTogether(true);
    
//     addStyledCell(tablePerso, "Nom et Prénom(s)",
//             (employe.getEmploye().getNom() != null ? employe.getEmploye().getNom() : "Non spécifié") + " " +
//             (employe.getEmploye().getPrenom() != null ? employe.getEmploye().getPrenom() : "Non spécifié"),
//             cellHeaderStyle, cellContentStyle);
//     addStyledCell(tablePerso, "Date et lieu de naissance",
//             formatDate(employe.getEmploye().getDateNaissance()) +
//             " à " + (employe.getEmploye().getLieuNaissance() != null ? employe.getEmploye().getLieuNaissance() : "Non spécifié"),
//             cellHeaderStyle, cellContentStyle);
//     addStyledCell(tablePerso, "Genre et nationalité",
//             (employe.getEmploye().getSexe() != null ? employe.getEmploye().getSexe().getsexe() : "Non spécifié") +
//             ", Nationalité: " + (employe.getEmploye().getNationalite() != null ? employe.getEmploye().getNationalite().getNationalite() : "Non spécifié"),
//             cellHeaderStyle, cellContentStyle);
//     addStyledCell(tablePerso, "Contact(Tel, email)",
//             (employe.getEmploye().getTelephone() != null ? employe.getEmploye().getTelephone() : "Non spécifié") +
//             ", " + (employe.getEmploye().getEmail() != null ? employe.getEmploye().getEmail() : "Non spécifié"),
//             cellHeaderStyle, cellContentStyle);
//     addStyledCell(tablePerso, "PiÃ¨ces(CIN, CNAPS, OSTIE)",
//             (employe.getEmploye().getCin() != null ? employe.getEmploye().getCin() : "Non spécifié") +
//             (employe.getEmploye().getNumCnaps() != null ? ", " + employe.getEmploye().getNumCnaps() : "") +
//             (employe.getEmploye().getNumOstie() != null ? ", " + employe.getEmploye().getNumOstie() : ""),
//             cellHeaderStyle, cellContentStyle);
//     addStyledCell(tablePerso, "Situation familiale",
//             (employe.getEmploye().getEtatCivil() != null ? employe.getEmploye().getEtatCivil().getLibelle() : "Non spécifié") +
//             ", enfants: " + (employe.getEmploye().getNbEnfants() != null ? employe.getEmploye().getNbEnfants() : 0),
//             cellHeaderStyle, cellContentStyle);
//     addStyledCell(tablePerso, "Adresse", employe.getEmploye().getAdresse(), cellHeaderStyle, cellContentStyle);
    
//     document.add(tablePerso);

//     // ========== PARENTS ==========
//     if (employe.getEmploye().getNomPere() != null || employe.getEmploye().getNomMere() != null) {
//         Paragraph parentsTitle = new Paragraph("INFORMATIONS FAMILIALES")
//                 .addStyle(sectionStyle)
//                 .setKeepWithNext(true);
//         document.add(parentsTitle);

//         Table tableParents = new Table(UnitValue.createPercentArray(new float[]{35, 65}))
//                 .useAllAvailableWidth()
//                 .setMarginBottom(6)
//                 .setKeepTogether(true);

//         addStyledCell(tableParents, "Parents",
//                 (employe.getEmploye().getNomPere() != null ? employe.getEmploye().getNomPere() : "-")
//                 + " et " + (employe.getEmploye().getNomMere() != null ? employe.getEmploye().getNomMere() : "-"),
//                 cellHeaderStyle, cellContentStyle);

//         document.add(tableParents);
//     }

//     // ========== INFORMATIONS PROFESSIONNELLES ==========
//     if(employe.getInfosProfessionnelles() != null && !employe.getInfosProfessionnelles().isEmpty()){
//         Paragraph proTitle = new Paragraph("INFORMATIONS PROFESSIONNELLES")
//                 .addStyle(sectionStyle)
//                 .setKeepWithNext(true);
//         document.add(proTitle);
        
//         InfosProfessionnelles infoPro = employe.getInfosProfessionnelles().stream()
//                 .filter(info -> info.getStatut() == 0)
//                 .findFirst()
//                 .orElse(employe.getInfosProfessionnelles().get(0));

//         Table tableInfosPro = new Table(UnitValue.createPercentArray(new float[]{35, 65}))
//                 .useAllAvailableWidth()
//                 .setMarginBottom(6)
//                 .setKeepTogether(true);

//         addStyledCell(tableInfosPro, "Matricule", infoPro.getMatricule(), cellHeaderStyle, cellContentStyle);
//         addStyledCell(tableInfosPro, "Poste / Departement",
//                 (infoPro.getPoste() != null ? infoPro.getPoste().getNom() : "Non specifie")
//                 + "/"
//                 + (infoPro.getPoste() != null && infoPro.getPoste().getDepartement() != null
//                         ? infoPro.getPoste().getDepartement().getNom() : "Non specifie"),
//                 cellHeaderStyle, cellContentStyle);
//         addStyledCell(tableInfosPro, "Periode",
//                 "du " + formatDate(infoPro.getDateEmbauche())
//                 + " au " + (infoPro.getDateDebauche() != null ? formatDate(infoPro.getDateDebauche()) : "en cours"),
//                 cellHeaderStyle, cellContentStyle);
//         addStyledCell(tableInfosPro, "Salaire de base",
//                 formatCurrency(infoPro.getSalaireBase()), cellHeaderStyle, cellContentStyle);

//         document.add(tableInfosPro);
//     } else {
//         Paragraph noProInfo = new Paragraph("Aucune information professionnelle disponible")
//                 .setItalic()
//                 .setFontColor(accentColor)
//                 .setTextAlignment(TextAlignment.CENTER)
//                 .setMarginBottom(4);
//         document.add(noProInfo);
//     }

//     // ========== CONTACT D'URGENCE ==========
//     if (employe.getEmploye().getEmergencyContact() != null) {
//         Paragraph emergencyTitle = new Paragraph("CONTACT D'URGENCE")
//                 .addStyle(sectionStyle)
//                 .setKeepWithNext(true);
//         document.add(emergencyTitle);
        
//         Table tableUrgence = new Table(UnitValue.createPercentArray(new float[]{35, 65}))
//                 .useAllAvailableWidth()
//                 .setMarginBottom(6)
//                 .setKeepTogether(true);
        
//         addStyledCell(tableUrgence, "Contact",
//                 "Nom: " + (employe.getEmploye().getEmergencyContact().getNom() != null ? employe.getEmploye().getEmergencyContact().getNom() : "Non spécifié") +
//                 ", Tel: " + (employe.getEmploye().getEmergencyContact().getContact() != null ? employe.getEmploye().getEmergencyContact().getContact() : "Non spécifié") +
//                 ", email: " + (employe.getEmploye().getEmergencyContact().getEmail() != null ? employe.getEmploye().getEmergencyContact().getEmail() : "Non spécifié"),
//                 cellHeaderStyle, cellContentStyle);
//         addStyledCell(tableUrgence, "Adresse",
//                 employe.getEmploye().getEmergencyContact().getAdresse(),
//                 cellHeaderStyle, cellContentStyle);
        
//         document.add(tableUrgence);
//     }

//     // ========== PIED DE PAGE FIXE (PAGE 1) ==========
//     float pageWidth = pdfDoc.getDefaultPageSize().getWidth();
//     float footerLeft = 16f;
//     float footerWidth = pageWidth - 32f;

//     LineSeparator footerSeparator = new LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine(1))
//             .setStrokeColor(primaryColor)
//             .setFixedPosition(footerLeft, 34f, footerWidth);
//     document.add(footerSeparator);

//     Paragraph footer = new Paragraph()
//             .setFontSize(7)
//             .setFontColor(darkGray)
//             .setTextAlignment(TextAlignment.CENTER)
//              .setFixedPosition(1, footerLeft, 20f, footerWidth)
//             .setMultipliedLeading(1f);

//     footer.add("Document confidentiel • ")
//           .add("Système de Gestion RH • ")
//           .add("Page 1/1 • ")
//           .add("Génére le " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy a HH:mm")));

//     document.add(footer);

//     document.close();
// }

    public void exportFicheEmploye(String idEmp, HttpServletResponse response) throws IOException {
        // Récupérer l'employé
        EmployeInfosDTO employe = employeService.getEmployeWithInfosById(idEmp);
        if (employe == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employé non trouvé");
            return;
        }

        // Définir les headers de réponse pour téléchargement
        response.setContentType("application/pdf");
        response.setCharacterEncoding("UTF-8");
        String fileName = "Fiche_Employe_" + 
                        employe.getEmploye().getNom() + "_" + 
                        employe.getEmploye().getPrenom() + "_" + 
                        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // Créer le PDF avec format A4
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);
        
        // Marges plus serrées pour tout tenir sur une page
        document.setMargins(15, 15, 40, 15);
        
        // ========== STYLES OPTIMISÉS POUR UNE PAGE ==========
        DeviceRgb primaryColor = new DeviceRgb(88, 41, 84);
        DeviceRgb secondaryColor = new DeviceRgb(250, 242, 249);
        DeviceRgb accentColor = new DeviceRgb(128, 0, 128);
        DeviceRgb textColor = new DeviceRgb(51, 51, 51);
        DeviceRgb lightGray = new DeviceRgb(230, 230, 230);
        
        // Style pour le titre principal
        Style titleStyle = new Style()
                //.setBold()
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(primaryColor)
                .setMarginBottom(0);
        
        // Style pour le sous-titre
        Style subtitleStyle = new Style()
                // .setBold()
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(accentColor)
                .setMarginBottom(4);
        
        // Style pour les sections
        Style sectionStyle = new Style()
                // .setBold()
                .setFontSize(10)
                .setMarginTop(6)
                .setMarginBottom(4)
                .setFontColor(primaryColor)
                .setPaddingLeft(4)
                .setBorderLeft(new SolidBorder(primaryColor, 2));
        
        // Style pour les en-têtes de cellule
        Style cellHeaderStyle = new Style()
                // .setBold()
                .setFontSize(8)
                .setBackgroundColor(secondaryColor)
                .setPadding(3)
                .setBorder(new SolidBorder(lightGray, 1))
                .setFontColor(primaryColor);
        
        // Style pour le contenu des cellules
        Style cellContentStyle = new Style()
                .setFontSize(8)
                .setPadding(3)
                .setBorder(new SolidBorder(lightGray, 1))
                .setFontColor(textColor);
        
        // Style pour les labels (en-têtes internes)
        Style labelStyle = new Style()
                //.setBold()
                .setFontSize(7)
                .setBackgroundColor(secondaryColor)
                .setPadding(3)
                .setFontColor(primaryColor)
                .setTextAlignment(TextAlignment.RIGHT);
        
        // Style pour les valeurs
        Style valueStyle = new Style()
                .setFontSize(7)
                .setPadding(3)
                .setFontColor(textColor)
                .setTextAlignment(TextAlignment.LEFT);
        
        // Style pour la ligne d'information
        Style infoLineStyle = new Style()
                .setFontSize(6)
                .setFontColor(accentColor)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(4);

        // ========== EN-TÊTE DU DOCUMENT ==========
        String logoPath = "D:\\Stage_smartDev\\Projet_Gestion RH\\Back\\manage\\manage\\src\\main\\resources\\static\\uploads\\logo\\logo.png";
        Path logoFilePath = Paths.get(logoPath);
        if (Files.exists(logoFilePath)) {
            ImageData imageData = ImageDataFactory.create(logoFilePath.toAbsolutePath().toString());
            Image logo = new Image(imageData)
                    .scaleToFit(70, 28)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setMarginBottom(2);
            document.add(logo);
        }

        // Ligne de séparation
        document.add(new LineSeparator(new SolidLine(1))
                .setStrokeColor(primaryColor)
                .setMarginBottom(4));
        
        // Titre principal
        Paragraph title = new Paragraph("FICHE INDIVIDUELLE DU SALARIÉ")
                .addStyle(titleStyle);
        document.add(title);
        
        // Nom complet de l'employé
        Paragraph subtitle = new Paragraph(
                employe.getEmploye().getNom().toUpperCase() + " " + 
                employe.getEmploye().getPrenom())
                .addStyle(subtitleStyle);
        document.add(subtitle);

        // Détermination du statut
        String statutGlobal = "Non spécifié";
        if (employe.getInfosProfessionnelles() != null && !employe.getInfosProfessionnelles().isEmpty()) {
            InfosProfessionnelles statutRef = employe.getInfosProfessionnelles().stream()
                    .filter(info -> info.getStatut() != -1)
                    .findFirst()
                    .orElse(employe.getInfosProfessionnelles().get(0));
            if (statutRef.getStatut() != -1) {
                statutGlobal = statutRef.getStatut() == 0 ? "ACTIF" : "INACTIF";
            }
        }
        
        // // Ligne d'information
        // Paragraph infoLine = new Paragraph()
        //         .add("Réf: " + employe.getEmploye().getId())
        //         .add("  •  ")
        //         .add("Généré: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        //         .add("  •  ")
        //         .add("Statut: " + statutGlobal)
        //         .addStyle(infoLineStyle);
        // document.add(infoLine);




        // ========== INFORMATIONS PERSONNELLES ==========
        Paragraph persoTitle = new Paragraph("INFORMATIONS PERSONNELLES")
                .addStyle(sectionStyle)
                .setKeepWithNext(true);
        document.add(persoTitle);
        
        Table tablePerso = new Table(UnitValue.createPercentArray(new float[]{25, 75}))
                .useAllAvailableWidth()
                .setMarginBottom(4)
                .setKeepTogether(true);
        
        // Nom et Prénom(s)
        addStyledCell(tablePerso, "Nom et Prénom(s)",
                (employe.getEmploye().getNom() != null ? employe.getEmploye().getNom() : "") + " " +
                (employe.getEmploye().getPrenom() != null ? employe.getEmploye().getPrenom() : ""),
                cellHeaderStyle, cellContentStyle);
        
        // Né(e) le
        addStyledCell(tablePerso, "Né(e) le",
                formatDate(employe.getEmploye().getDateNaissance()) +
                " à " + (employe.getEmploye().getLieuNaissance() != null ? employe.getEmploye().getLieuNaissance() : ""),
                cellHeaderStyle, cellContentStyle);
        
        // Genre/Nationalité
        // addStyledCell(tablePerso, "Genre/Nationalité",
        //         (employe.getEmploye().getSexe() != null ? employe.getEmploye().getSexe().getsexe() : "") +
        //         " - " + (employe.getEmploye().getNationalite() != null ? employe.getEmploye().getNationalite().getNationalite() : ""),
        //         cellHeaderStyle, cellContentStyle);



        // ===== GENRE/NATIONALITÉ - TABLEAU À 4 COLONNES =====
        Cell genreHeaderCell = new Cell().add(new Paragraph("Genre/Nationalité")).addStyle(cellHeaderStyle);
        tablePerso.addCell(genreHeaderCell);

        Cell genreContentCell = new Cell().addStyle(cellContentStyle);
        genreContentCell.setPadding(0);

        // Tableau interne à 4 colonnes pour Genre et Nationalité
        Table genreInnerTable = new Table(UnitValue.createPercentArray(new float[]{15, 35, 15, 35}))
                .useAllAvailableWidth();

        // Genre
        Cell genreLabelCell = new Cell()
                .add(new Paragraph("Genre :"))
                .addStyle(labelStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null);
        genreInnerTable.addCell(genreLabelCell);

        String genreValue = employe.getEmploye().getSexe() != null ? employe.getEmploye().getSexe().getsexe() : "-";
        Cell genreValueCell = new Cell()
                .add(new Paragraph(genreValue))
                .addStyle(valueStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null)
                .setBorderRight(new SolidBorder(lightGray, 1));
        genreInnerTable.addCell(genreValueCell);

        // Nationalité
        Cell nationaliteLabelCell = new Cell()
                .add(new Paragraph("Nationalité :"))
                .addStyle(labelStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null);
        genreInnerTable.addCell(nationaliteLabelCell);

        String nationaliteValue = employe.getEmploye().getNationalite() != null ? employe.getEmploye().getNationalite().getNationalite() : "-";
        Cell nationaliteValueCell = new Cell()
                .add(new Paragraph(nationaliteValue))
                .addStyle(valueStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null)
                .setBorderRight(null);
        genreInnerTable.addCell(nationaliteValueCell);

        genreContentCell.add(genreInnerTable);
        tablePerso.addCell(genreContentCell);







        
        // ===== CONTACT - TABLEAU À 4 COLONNES =====
        Cell contactHeaderCell = new Cell().add(new Paragraph("Contact")).addStyle(cellHeaderStyle);
        tablePerso.addCell(contactHeaderCell);
        
        Cell contactContentCell = new Cell().addStyle(cellContentStyle);
        contactContentCell.setPadding(0);
        
        Table contactInnerTable = new Table(UnitValue.createPercentArray(new float[]{20, 30, 15, 35}))
                .useAllAvailableWidth();
        
        // Téléphone
        Cell telLabelCell = new Cell()
                .add(new Paragraph("Téléphone :"))
                .addStyle(labelStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null);
        contactInnerTable.addCell(telLabelCell);
        
        Cell telValueCell = new Cell()
                .add(new Paragraph(employe.getEmploye().getTelephone() != null ? employe.getEmploye().getTelephone() : "-"))
                .addStyle(valueStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null)
                .setBorderRight(new SolidBorder(lightGray, 1));
        contactInnerTable.addCell(telValueCell);
        
        // Email
        Cell emailLabelCell = new Cell()
                .add(new Paragraph("Email :"))
                .addStyle(labelStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null);
        contactInnerTable.addCell(emailLabelCell);
        
        Cell emailValueCell = new Cell()
                .add(new Paragraph(employe.getEmploye().getEmail() != null ? employe.getEmploye().getEmail() : "-"))
                .addStyle(valueStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null)
                .setBorderRight(null);
        contactInnerTable.addCell(emailValueCell);
        
        contactContentCell.add(contactInnerTable);
        tablePerso.addCell(contactContentCell);
        
        // ===== PIÈCES D'IDENTITÉ - TABLEAU À 6 COLONNES =====
        Cell piecesHeaderCell = new Cell().add(new Paragraph("Pièces d'identité")).addStyle(cellHeaderStyle);
        tablePerso.addCell(piecesHeaderCell);
        
        Cell piecesContentCell = new Cell().addStyle(cellContentStyle);
        piecesContentCell.setPadding(0);
        
        // Tableau à 6 colonnes pour CIN, CNAPS, OSTIE
        Table piecesInnerTable = new Table(UnitValue.createPercentArray(new float[]{10, 23, 10, 23, 10, 24}))
                .useAllAvailableWidth();
        
        // CIN
        Cell cinLabelCell = new Cell()
                .add(new Paragraph("CIN:"))
                .addStyle(labelStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null);
        piecesInnerTable.addCell(cinLabelCell);
        
        Cell cinValueCell = new Cell()
                .add(new Paragraph(employe.getEmploye().getCin() != null ? employe.getEmploye().getCin() : "-"))
                .addStyle(valueStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null)
                .setBorderRight(new SolidBorder(lightGray, 1));
        piecesInnerTable.addCell(cinValueCell);
        
        // CNAPS
        Cell cnapsLabelCell = new Cell()
                .add(new Paragraph("CNAPS:"))
                .addStyle(labelStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null);
        piecesInnerTable.addCell(cnapsLabelCell);
        
        Cell cnapsValueCell = new Cell()
                .add(new Paragraph(employe.getEmploye().getNumCnaps() != null ? employe.getEmploye().getNumCnaps() : "-"))
                .addStyle(valueStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null)
                .setBorderRight(new SolidBorder(lightGray, 1));
        piecesInnerTable.addCell(cnapsValueCell);
        
        // OSTIE
        Cell ostieLabelCell = new Cell()
                .add(new Paragraph("OSTIE:"))
                .addStyle(labelStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null);
        piecesInnerTable.addCell(ostieLabelCell);
        
        Cell ostieValueCell = new Cell()
                .add(new Paragraph(employe.getEmploye().getNumOstie() != null ? employe.getEmploye().getNumOstie() : "-"))
                .addStyle(valueStyle)
                .setBorderLeft(null)
                .setBorderTop(null)
                .setBorderBottom(null)
                .setBorderRight(null);
        piecesInnerTable.addCell(ostieValueCell);
        
        piecesContentCell.add(piecesInnerTable);
        tablePerso.addCell(piecesContentCell);
        
        // Situation familiale
        addStyledCell(tablePerso, "Situation familiale",
                (employe.getEmploye().getEtatCivil() != null ? employe.getEmploye().getEtatCivil().getLibelle() : "") +
                " - " + (employe.getEmploye().getNbEnfants() != null ? employe.getEmploye().getNbEnfants() : 0) + " enf.",
                cellHeaderStyle, cellContentStyle);
        
        // Adresse
        addStyledCell(tablePerso, "Adresse", 
                employe.getEmploye().getAdresse() != null ? employe.getEmploye().getAdresse() : "", 
                cellHeaderStyle, cellContentStyle);
        
        document.add(tablePerso);



        // ========== INFORMATIONS FAMILIALES ==========
        if (employe.getEmploye().getNomPere() != null || employe.getEmploye().getNomMere() != null) {
            Paragraph parentsTitle = new Paragraph("INFORMATIONS FAMILIALES")
                    .addStyle(sectionStyle)
                    .setKeepWithNext(true);
            document.add(parentsTitle);

            Table tableParents = new Table(UnitValue.createPercentArray(new float[]{25, 75}))
                    .useAllAvailableWidth()
                    .setMarginBottom(4)
                    .setKeepTogether(true);
            
            // En-tête Parents
            Cell parentsHeaderCell = new Cell().add(new Paragraph("Parents")).addStyle(cellHeaderStyle);
            tableParents.addCell(parentsHeaderCell);
            
            // Cellule de contenu pour les parents
            Cell parentsContentCell = new Cell().addStyle(cellContentStyle);
            parentsContentCell.setPadding(0);
            
            // Tableau interne à 4 colonnes pour Père et Mère
            Table parentsInnerTable = new Table(UnitValue.createPercentArray(new float[]{15, 35, 15, 35}))
                    .useAllAvailableWidth();
            
            // Père
            Cell pereLabelCell = new Cell()
                    .add(new Paragraph("Père :"))
                    .addStyle(labelStyle)
                    .setBorderLeft(null)
                    .setBorderTop(null)
                    .setBorderBottom(null);
            parentsInnerTable.addCell(pereLabelCell);
            
            Cell pereValueCell = new Cell()
                    .add(new Paragraph(employe.getEmploye().getNomPere() != null ? employe.getEmploye().getNomPere() : "-"))
                    .addStyle(valueStyle)
                    .setBorderLeft(null)
                    .setBorderTop(null)
                    .setBorderBottom(null)
                    .setBorderRight(new SolidBorder(lightGray, 1));
            parentsInnerTable.addCell(pereValueCell);
            
            // Mère
            Cell mereLabelCell = new Cell()
                    .add(new Paragraph("Mère :"))
                    .addStyle(labelStyle)
                    .setBorderLeft(null)
                    .setBorderTop(null)
                    .setBorderBottom(null);
            parentsInnerTable.addCell(mereLabelCell);
            
            Cell mereValueCell = new Cell()
                    .add(new Paragraph(employe.getEmploye().getNomMere() != null ? employe.getEmploye().getNomMere() : "-"))
                    .addStyle(valueStyle)
                    .setBorderLeft(null)
                    .setBorderTop(null)
                    .setBorderBottom(null)
                    .setBorderRight(null);
            parentsInnerTable.addCell(mereValueCell);
            
            parentsContentCell.add(parentsInnerTable);
            tableParents.addCell(parentsContentCell);
            
            document.add(tableParents);
        }



        // ========== INFORMATIONS PROFESSIONNELLES ==========
        if(employe.getInfosProfessionnelles() != null && !employe.getInfosProfessionnelles().isEmpty()){
            Paragraph proTitle = new Paragraph("INFORMATIONS PROFESSIONNELLES")
                    .addStyle(sectionStyle)
                    .setKeepWithNext(true);
            document.add(proTitle);
            
            InfosProfessionnelles infoPro = employe.getInfosProfessionnelles().stream()
                    .filter(info -> info.getStatut() == 0)
                    .findFirst()
                    .orElse(employe.getInfosProfessionnelles().get(0));

            Table tableInfosPro = new Table(UnitValue.createPercentArray(new float[]{25, 75}))
                    .useAllAvailableWidth()
                    .setMarginBottom(4)
                    .setKeepTogether(true);

            // Matricule
            addStyledCell(tableInfosPro, "Matricule", 
                    infoPro.getMatricule() != null ? infoPro.getMatricule() : "", 
                    cellHeaderStyle, cellContentStyle);
            
            // ===== POSTE/DÉPARTEMENT - TABLEAU À 4 COLONNES =====
            Cell posteHeaderCell = new Cell().add(new Paragraph("Poste/Département")).addStyle(cellHeaderStyle);
            tableInfosPro.addCell(posteHeaderCell);
            
            Cell posteContentCell = new Cell().addStyle(cellContentStyle);
            posteContentCell.setPadding(0);
            
            // Tableau interne à 4 colonnes pour Poste et Département
            Table posteInnerTable = new Table(UnitValue.createPercentArray(new float[]{15, 35, 15, 35}))
                    .useAllAvailableWidth();
            
            // Poste
            Cell posteLabelCell = new Cell()
                    .add(new Paragraph("Poste :"))
                    .addStyle(labelStyle)
                    .setBorderLeft(null)
                    .setBorderTop(null)
                    .setBorderBottom(null);
            posteInnerTable.addCell(posteLabelCell);
            
            String posteNom = infoPro.getPoste() != null ? infoPro.getPoste().getNom() : "-";
            Cell posteValueCell = new Cell()
                    .add(new Paragraph(posteNom))
                    .addStyle(valueStyle)
                    .setBorderLeft(null)
                    .setBorderTop(null)
                    .setBorderBottom(null)
                    .setBorderRight(new SolidBorder(lightGray, 1));
            posteInnerTable.addCell(posteValueCell);
            
            // Département
            Cell deptLabelCell = new Cell()
                    .add(new Paragraph("Département :"))
                    .addStyle(labelStyle)
                    .setBorderLeft(null)
                    .setBorderTop(null)
                    .setBorderBottom(null);
            posteInnerTable.addCell(deptLabelCell);
            
            String deptNom = (infoPro.getPoste() != null && infoPro.getPoste().getDepartement() != null) 
                            ? infoPro.getPoste().getDepartement().getNom() : "-";
            Cell deptValueCell = new Cell()
                    .add(new Paragraph(deptNom))
                    .addStyle(valueStyle)
                    .setBorderLeft(null)
                    .setBorderTop(null)
                    .setBorderBottom(null)
                    .setBorderRight(null);
            posteInnerTable.addCell(deptValueCell);
            
            posteContentCell.add(posteInnerTable);
            tableInfosPro.addCell(posteContentCell);
            
            // Période
            addStyledCell(tableInfosPro, "Période",
                    "du " + formatDate(infoPro.getDateEmbauche())
                    + " au " + (infoPro.getDateDebauche() != null ? formatDate(infoPro.getDateDebauche()) : "en cours"),
                    cellHeaderStyle, cellContentStyle);
            
            // Salaire
            addStyledCell(tableInfosPro, "Salaire",
                    formatCurrency(infoPro.getSalaireBase()), 
                    cellHeaderStyle, cellContentStyle);

            document.add(tableInfosPro);
        }

        // ========== CONTACT D'URGENCE ==========
        if (employe.getEmploye().getEmergencyContact() != null) {
            Paragraph emergencyTitle = new Paragraph("CONTACT D'URGENCE")
                    .addStyle(sectionStyle)
                    .setKeepWithNext(true);
            document.add(emergencyTitle);
            
            Table tableUrgence = new Table(UnitValue.createPercentArray(new float[]{25, 75}))
                    .useAllAvailableWidth()
                    .setMarginBottom(4)
                    .setKeepTogether(true);
            
            EmergencyContact contact = employe.getEmploye().getEmergencyContact();
            
            // En-tête Contact
            Cell contactUrgenceHeaderCell = new Cell().add(new Paragraph("Contact")).addStyle(cellHeaderStyle);
            tableUrgence.addCell(contactUrgenceHeaderCell);
            
            // Cellule de contenu pour le contact d'urgence
            Cell contactUrgenceContentCell = new Cell().addStyle(cellContentStyle);
            contactUrgenceContentCell.setPadding(0);
            
            // Tableau interne pour le contact d'urgence
            Table contactUrgenceInnerTable = new Table(UnitValue.createPercentArray(new float[]{20, 30, 15, 35}))
                    .useAllAvailableWidth();
            
            // Nom
            Cell nomLabelCell = new Cell()
                    .add(new Paragraph("Nom :"))
                    .addStyle(labelStyle)
                    .setBorderLeft(null)
                    .setBorderTop(null)
                    .setBorderBottom(null);
            contactUrgenceInnerTable.addCell(nomLabelCell);
            
            Cell nomValueCell = new Cell()
                    .add(new Paragraph(contact.getNom() != null ? contact.getNom() : "-"))
                    .addStyle(valueStyle)
                    .setBorderLeft(null)
                    .setBorderTop(null)
                    .setBorderBottom(null)
                    .setBorderRight(new SolidBorder(lightGray, 1));
            contactUrgenceInnerTable.addCell(nomValueCell);
            
            // Téléphone
            Cell telUrgenceLabelCell = new Cell()
                    .add(new Paragraph("Tél :"))
                    .addStyle(labelStyle)
                    .setBorderLeft(null)
                    .setBorderTop(null)
                    .setBorderBottom(null);
            contactUrgenceInnerTable.addCell(telUrgenceLabelCell);
            
            Cell telUrgenceValueCell = new Cell()
                    .add(new Paragraph(contact.getContact() != null ? contact.getContact() : "-"))
                    .addStyle(valueStyle)
                    .setBorderLeft(null)
                    .setBorderTop(null)
                    .setBorderBottom(null)
                    .setBorderRight(null);
            contactUrgenceInnerTable.addCell(telUrgenceValueCell);
            
            contactUrgenceContentCell.add(contactUrgenceInnerTable);
            tableUrgence.addCell(contactUrgenceContentCell);
            
            // Adresse
            if (contact.getAdresse() != null && !contact.getAdresse().isEmpty()) {
                addStyledCell(tableUrgence, "Adresse",
                        contact.getAdresse(),
                        cellHeaderStyle, cellContentStyle);
            }
            
            document.add(tableUrgence);
        }

        // ========== PIED DE PAGE ==========
        float pageWidth = pdfDoc.getDefaultPageSize().getWidth();
        float footerLeft = 15f;
        float footerWidth = pageWidth - 30f;

        LineSeparator footerSeparator = new LineSeparator(new SolidLine(1))
                .setStrokeColor(primaryColor)
                .setFixedPosition(footerLeft, 30f, footerWidth);
        document.add(footerSeparator);



                // Ligne d'information
        // Paragraph infoLine = new Paragraph()
        //         .add("Réf: " + employe.getEmploye().getId())
        //         .add("  •  ")
        //         .add("Généré: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        //         .add("  •  ")
        //         .add("Statut: " + statutGlobal)
        //         .addStyle(infoLineStyle);
        // document.add(infoLine);

        Paragraph footer = new Paragraph()
                .setFontSize(6)
                .setFontColor(textColor)
                .setTextAlignment(TextAlignment.CENTER)
                .setFixedPosition(1, footerLeft, 18f, footerWidth)
                .setMultipliedLeading(1f);

        footer.add("Réf: " + employe.getEmploye().getId())
                .add("  |  ")
                .add("Généré: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .add("  |  ")
                .add("Statut: " + statutGlobal)
                .addStyle(infoLineStyle)
                .add("| Document confidentiel - RH")
                .add(" | Page 1/1")
                .add(" | " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        document.add(footer);
        
        document.close();
    }


// MÃ©thode utilitaire pour ajouter des cellules avec style
private void addStyledCell(Table table, String header, String content, Style headerStyle, Style contentStyle) {
    Cell headerCell = new Cell().add(new Paragraph(header)).addStyle(headerStyle);
    Cell contentCell = new Cell().add(new Paragraph(content != null ? content : "Non spécifié")).addStyle(contentStyle);
    table.addCell(headerCell);
    table.addCell(contentCell);
}

// MÃ©thode utilitaire pour formater les dates
private String formatDate(LocalDate date) {
    if (date == null) {
        return "Non spécifié";
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return date.format(formatter);
}

// MÃ©thode utilitaire pour formater les montants
private String formatCurrency(Double amount) {
    if (amount == null) {
        return "Non spécifié";
    }
    DecimalFormat formatter = new DecimalFormat("#,##0.00 MGA");
    return formatter.format(amount);
}

public byte[] genererBulletinPaiePDF(VuePaieComplete paie, List<VuePaieFille> rubriques) throws IOException {
    
    String htmlContent = genererBulletinPaieHtml(paie, rubriques);
    
    // 2. Convertir HTML en PDF
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    try {
        com.itextpdf.html2pdf.HtmlConverter.convertToPdf(htmlContent, baos);
        return baos.toByteArray();
    } catch (Exception e) {
        e.printStackTrace();
        return genererPDFSimple(paie, rubriques);
    }
}

private byte[] genererPDFSimple(VuePaieComplete paie, List<VuePaieFille> rubriques) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    try {
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(40, 40, 40, 40);
        
        // Titre
        document.add(new Paragraph("BULLETIN DE PAIE")
            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
            .setBold()
            .setFontSize(16));
        
        document.add(new Paragraph("SALAIRE MENSUEL")
            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
            .setFontSize(12));
        
        document.add(new Paragraph("\nInformations de l'employé").setBold());
        document.add(new Paragraph("Matricule: " + paie.getMatricule()));
        document.add(new Paragraph("Nom: " + paie.getNomComplet()));
        document.add(new Paragraph("Fonction: " + paie.getFonction()));
        document.add(new Paragraph("Salaire Net: " + formatCurrency(paie.getSalaireNet())));
        
        // PÃ©riode
        document.add(new Paragraph("\nPériode: " + 
            formatDate(paie.getDateDebutPeriode()) + " au " + 
            formatDate(paie.getDateFinPeriode())));
        
        document.close();
        return baos.toByteArray();
        
    } catch (Exception e) {
        throw new IOException("Erreur création PDF simple: " + e.getMessage());
    }
}





// +++++++++++++++++++++++++++++++++++++++export PAIE++++++++++++++++++++++++++++++++++++++++
private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yy");

public String genererBulletinPaieHtml(VuePaieComplete paie, List<VuePaieFille> rubriques) {
    StringBuilder html = new StringBuilder();
    InfosProfessionnelles infoPro = null;
    try {
        if (paie != null && paie.getIdEmploye() != null) {
            infoPro = infosProfessionnellesService
                    .getDerniereInfoProfessionnelleByEmployeId(paie.getIdEmploye())
                    .orElse(null);
        }
    } catch (Exception e) {
        infoPro = null;
    }

    html.append("<!DOCTYPE html>\n<html>\n<head>\n")
        .append("<meta charset=\"UTF-8\">\n")
        .append("<title>Bulletin de Paie</title>\n")
        .append("<style>\n")
        .append("  * { margin: 0; padding: 0; box-sizing: border-box; }\n")
        .append("  body { font-family: 'Arial', sans-serif; font-size: 11px; line-height: 1.3; margin: 0; padding: 0; background-color: #fff; }\n")
        .append("  .page { width: 18cm; min-height: 29.7cm; margin: 0 auto; padding: 15px; position: relative; }\n")
        .append("  .header-container { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 15px; padding-bottom: 10px; }\n")
        .append("  .logo-container { width: 30%; text-align: left; }\n")
        .append("  .logo { max-width: 100%; height: auto; }\n")
        .append("  .title-container { width: 70%; text-align: center; }\n")
        .append("  .company-name { font-size: 14px; font-weight: bold; margin-bottom: 5px; }\n")
        .append("  .bulletin-title { font-size: 16px; font-weight: bold; text-transform: uppercase; margin: 5px 0; }\n")
        .append("  .subtitle { font-size: 12px; margin-bottom: 3px; }\n")
        .append("  .period-header { font-size: 11px; margin-top: 3px; }\n")
        .append("  .section-title { font-weight: bold; margin: 15px 0 8px 0; }\n")
        .append("  .section_date_payment { overflow: auto; }\n")
        .append("  .date_p { float : left; }\n")
        .append("  .mode_payment { float:right; }\n")
        .append("  .info { overflow: auto;}\n")
        .append("  .info_perso {width: 40%; float : left;} \n")
        .append("  .info_pro {width: 40%;  float:right;}\n")
        .append("  table { width: 100%; border-collapse: collapse; margin-bottom: 10px; }\n")
        .append("  td, th { border: 1px solid #000; padding: 4px 6px; vertical-align: top; font-size: 10px; }\n")
        .append("  th { font-weight: bold; text-align: center; background-color: #c2c0c0ff; }\n")
        .append("  .info-table { margin-bottom: 15px; }\n")
        .append("  .info-table td:first-child { width: 35%; font-weight: bold; background-color: #f8f8f8; }\n")
        .append("  .info-table td:nth-child(2) { width: 65%; }\n")
        .append("  .main-table th { text-align: center; padding: 6px 4px; }\n")
        .append("  .main-table td { padding: 4px 6px; }\n")
        .append("  .text-center { text-align: center; }\n")
        .append("  .text-right { text-align: right; }\n")
        .append("  .text-left { text-align: left; }\n")
        .append("  .bold { font-weight: bold; }\n")
        .append("  .total-row { background-color: #f0f0f0; }\n")
        .append("  .leave-section { margin: 15px 0; }\n")
        .append("  .signatures { margin-top: 20px; }\n")
        .append("  .signatures table { border: none; }\n")
        .append("  .signatures td { border: none; text-align: center; width: 50%; padding-top: 40px; }\n")
        .append("  .net-payer { text-align: center; margin-top: 30px; font-weight: bold; }\n")
        .append("  .net-payer-box { display: inline-block; border: 1px solid #000; padding: 10px 20px; margin: 10px 0; }\n")
        .append("  .period-info { text-align: center; margin-top: 10px; font-size: 10px; }\n")
        .append("  .empty-row { height: 25px; }\n")
        .append("  @media print {\n")
        .append("    .page { width: 100%; margin: 0; padding: 10px; }\n")
        .append("    body { font-size: 10px; }\n")
        .append("  }\n")
        .append("</style>\n</head>\n<body>\n");

    html.append("<div class=\"page\">");
    
    // Logo & Titre 
    html.append("<div class=\"header-container\">")
        .append("<div class=\"logo-container\">");
        
    String logoSrc = resolveLogoSrc(paie.getLogo());
    if (logoSrc != null) {
        html.append("<img src=\"").append(logoSrc).append("\" height=\"50\" alt=\"Logo\" class=\"logo\">");
    }

    
    html.append("</div>") // Fermeture div logo
        .append("<div class=\"title-container\">")
        //.append("<div class=\"company-name\">").append(paie.getNomCompany()).append("</div>")
        .append("<div class=\"bulletin-title\">BULLETIN DE PAIE</div>")
        .append("<div class=\"subtitle\">SALAIRE MENSUEL</div>");
    
    html.append("</div>") // Fermeture div titre
        .append("</div>"); // Fermeture div container

    // date de payment
    html.append("<div class=\"section_date_payment\">");

        html.append("<div class=\"date_p\">");

        // Ajouter les informations de pÃ©riode si disponibles
        if (paie.getDateDebutPeriode() != null && paie.getDateFinPeriode() != null) {

                html.append("<div class=\"period-header\">")
                .append("Période du ").append(formatDate(paie.getDateDebutPeriode()))
                .append(" au ").append(formatDate(paie.getDateFinPeriode()))
                .append("</div>");

                html.append("<div class=\"period-header\">")
                .append("Paiment le ").append(formatDate(paie.getDateFinPeriode()))
                .append("</div>");
        }

        html.append("</div>");


        // Mode de Payment
        html.append("<div class=\"mode_payment\">");
                // Ajouter les informations de pÃ©riode si disponibles
        //if (paie.getDateDebutPeriode() != null && paie.getDateFinPeriode() != null) {

        html.append("<div class=\"period-header\">")
                .append("Mode de Paiement : ").append(paie.getModePaiement())
                .append("</div>");

        html.append("</div>");
        

    html.append("</div>");


        // Informations employé

    html.append("<div class=\"info\">");
        html.append("<div class=\"section-title\">Informations de l'employé</div>");


        html.append("<div class=\"info_perso\">");
                html.append("<table class=\"info-table\">");

                html.append("<tr>")
                        .append("<td>Matricule:</td>")
                        .append("<td>").append(paie.getMatricule()).append("</td>")
                        .append("</tr>");
                html.append("<tr>")
                        .append("<td>Nom et prénom:</td>")
                        .append("<td>").append(paie.getNomComplet()).append("</td>")
                        .append("</tr>");
                        
                
                html.append("<tr>")
                        .append("<td>Fonction:</td>")
                        .append("<td>").append(paie.getFonction()).append("</td>")
                        .append("</tr>");

                html.append("<tr>")
                        .append("<td>N° CNAPS:</td>")
                        .append("<td>").append(safeStr(paie.getNumCnaps())).append("</td>")
                        .append("</tr>");
                
                // Troisième ligne: Classification seule
                html.append("<tr>")
                        .append("<td>Classification:</td>")
                        .append("<td>")
                        .append(infoPro != null && infoPro.getClassification() != null
                                ? infoPro.getClassification()
                                : safeStr(paie.getCategorieSalaire()))
                        .append("</td>")
                        .append("</tr>")
                        .append("</table>");

        html.append("</div>");


        html.append("<div class=\"info_pro\">");

        // Informations supplémentaires
                html.append("<table class=\"info-table\">")

                        .append("<tr>")
                        .append("<td>Date d'embauche:</td>")
                        .append("<td>").append(formatDate(paie.getDateEmbauche())).append("</td>")
                        .append("</tr>")

                        .append("<tr>")
                        .append("<td>Ancienneté:</td>")
                        .append("<td>").append(paie.getAncienneteFormatee()).append("</td>")
                        .append("</tr>")

                        .append("<tr>")
                        .append("<td>Département:</td>")
                        .append("<td>").append(safeStr(paie.getDepartement())).append("</td>")
                        .append("</tr>")
                        // .append("<td>Service:</td>")
                        // .append("<td>").append(safeStr(paie.getService())).append("</td>")
                        // .append("</tr>")
                        // .append("<tr>")
                        // .append("<td>UnitÃ©:</td>")
                        // .append("<td colspan=\"3\">").append(safeStr(paie.getUnite())).append("</td>")
                        // .append("</tr>")
                        .append("</table>");

        html.append("</div>");
   html.append("</div>");





    

    // Mode de paiement
//     html.append("<table class=\"info-table\">")
//         .append("<tr>")
//         .append("<td colspan=\"4\">Mode de paiement</td>")
//         .append("</tr>")
//         .append("<tr>")
//         .append("<td colspan=\"4\">Virement</td>")
//         .append("</tr>")
//         .append("</table>");

    // Tableau principal des rubriques (exactement comme dans le Word)
    html.append("<div class=\"section-title\">Détails de la paie</div>")
        .append("<table class=\"main-table\">")
        .append("<thead>")
        .append("<tr>")
        .append("<th>Code</th>")
        .append("<th>Désignation</th>")
        .append("<th>Nombre</th>")
        .append("<th>Base</th>")
        .append("<th>Taux</th>")
        .append("<th>Gain</th>")
        .append("<th>Retenue</th>")
        .append("</tr>")
        .append("</thead>")
        .append("<tbody>");

    // Ligne 1: SALAIRE DE BASE
    BigDecimal salaireBase = getSalaireBaseFromRubriques(rubriques);
    String salaireBaseCode = getRubriqueCode(rubriques, "SALAIRE DE BASE", "Gain", "1010");
    html.append("<tr>")
        .append("<td class=\"text-center\">").append(salaireBaseCode).append("</td>")
        .append("<td>SALAIRE DE BASE</td>")
        .append("<td class=\"text-center\"></td>")
        .append("<td class=\"text-right\"></td>")
        .append("<td class=\"text-right\"></td>")
        .append("<td class=\"text-right\">").append(formatCurrency(salaireBase)).append("</td>")
        .append("<td class=\"text-right\"></td>")
        .append("</tr>");

    // Ligne IRSA
//     BigDecimal irsaMontant = getRubriqueMontant(rubriques, "IRSA", "Retenue");
//     String irsaCode = getRubriqueCode(rubriques, "IRSA", "Retenue", "IRSA");
//     html.append("<tr>")
//         .append("<td class=\"text-center\">").append(irsaCode).append("</td>")
//         .append("<td>IRSA</td>")
//         .append("<td class=\"text-center\"></td>")
//         .append("<td class=\"text-right\"></td>")
//         .append("<td class=\"text-right\"></td>")
//         .append("<td class=\"text-right\"></td>")
//         .append("<td class=\"text-right\">").append(formatCurrency(irsaMontant)).append("</td>")
//         .append("</tr>");

    // Ligne RETENUE MUTUELLE
//     BigDecimal mutuelleMontant = getRubriqueMontant(rubriques, "RETENUE MUTUELLE", "Retenue");
//     String mutuelleCode = getRubriqueCode(rubriques, "RETENUE MUTUELLE", "Retenue", "RET");
//     html.append("<tr>")
//         .append("<td class=\"text-center\">").append(mutuelleCode).append("</td>")
//         .append("<td>RETENUE MUTUELLE</td>")
//         .append("<td class=\"text-center\"></td>")
//         .append("<td class=\"text-right\"></td>")
//         .append("<td class=\"text-right\"></td>")
//         .append("<td class=\"text-right\"></td>")
//         .append("<td class=\"text-right\">").append(formatCurrency(mutuelleMontant)).append("</td>")
//         .append("</tr>");

    // Ajouter d'autres rubriques s'il y en a
    for (VuePaieFille r : rubriques) {
        String rubriqueNom = safeStr(r.getRubriqueNom());
        String code = safeStr(r.getCode());
        
        if (!"SALAIRE DE BASE".equalsIgnoreCase(rubriqueNom) && 
            !"CNAPS".equalsIgnoreCase(rubriqueNom) && 
            !"IRSA".equalsIgnoreCase(rubriqueNom) && 
            !"RETENUE MUTUELLE".equalsIgnoreCase(rubriqueNom)) {
            
            String type = isGain(r) ? "Gain" : "Retenue";

           
        html.append("<tr>")
                .append("<td class=\"text-center\">").append(code).append("</td>")
                .append("<td>").append(rubriqueNom).append("</td>")
                .append("<td class=\"text-center\"></td>")
                .append("<td class=\"text-right\">").append(formatCurrency(r.getBase())).append("</td>")
                .append("<td class=\"text-right\">").append(formatTaux(r.getTaux())).append("</td>")
                .append("<td class=\"text-right\">").append(isGain(r) ? formatCurrency(r.getMontant()) : "").append("</td>")
                .append("<td class=\"text-right\">").append(isGain(r) ? "" : formatCurrency(r.getMontant())).append("</td>")
                .append("</tr>");
            
            
        }
    }

    // Ligne Total Brut
    html.append("<tr class=\"total-row\">")
        .append("<td colspan=\"2\"><strong>Total Brut</strong></td>")
        .append("<td></td>")
        .append("<td></td>")
        .append("<td></td>")
        .append("<td class=\"text-right bold\">").append(formatCurrency(paie.getSalaireBrut())).append("</td>")
        .append("<td></td>")
        .append("</tr>");

        //     // Lignes vides pour complÃ©ter comme dans le modÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¨le Word
        //     for (int i = 0; i < 5; i++) {
        //         html.append("<tr class=\"empty-row\">")
        //             .append("<td></td><td></td><td></td><td></td><td></td><td></td><td></td>")
        //             .append("</tr>");
        //     }

        // Ligne Total Cotisations
    html.append("<tr class=\"total-row\">")
        .append("<td colspan=\"2\"><strong>Total Cotisations</strong></td>")
        .append("<td></td>")
        .append("<td></td>")
        .append("<td></td>")
        .append("<td></td>")
        .append("<td class=\"text-right bold\">").append(formatCurrency(paie.getTotalCotisations())).append("</td>")
        .append("</tr>"); 

    html.append("</tbody></table>");



// Section supérieure 
html.append("<div style=\"display: flex; justify-content: space-between; align-items: flex-start; margin-top: 20px;\">")
    
    // Congés‚à gauche
    .append("<div style=\"width: 32%;\">")
    .append("<div style=\"margin-bottom: 5px; font-weight: bold;\">Compteurs de congés</div>")
    .append("<table style=\"width: 100%;\">")
    .append("<tr><th>Compteurs</th><th>Pris</th><th>Solde</th></tr>")
    .append("<tr><td>Congés</td><td class=\"text-center\">3,000</td><td class=\"text-center\">30,000</td></tr>")
    .append("</table>")
    .append("</div>")
    
    // Dates de congés au centre
    .append("<div style=\"width: 20%; margin: 0 20px;  text-align: center;\">")
    .append("<div style=\"margin-bottom: 5px; font-weight: bold;\">Dates de congés</div>")
    .append("<table style=\"width: 100%; margin: 0 auto;\">")
    .append("<tr><th style=\"width: 50%;\">Du</th><th style=\"width: 50%;\">Au</th></tr>")
    .append("<tr><td style=\"height: 25px;\">01/01/2026</td><td style=\"height: 25px;\">01/01/2026</td></tr>")
    .append("<tr><td style=\"height: 25px;\">02/01/2026</td><td style=\"height: 25px;\">02/01/2026</td></tr>")
    .append("<tr><td style=\"height: 25px;\">03/01/2026</td><td style=\"height: 25px;\">03/01/2026</td></tr>")
    .append("</table>")
    .append("</div>")
    
    // NET A PAYER
    .append("<div style=\"width: 32%; text-align: right;\">")
    .append("<div style=\"display: inline-block; border: 1px solid #000; padding: 15px 30px; text-align: center; margin-top: 20px;\">")
    .append("<div style=\"font-size: 16px; font-weight: bold; margin-bottom: 10px;\">NET A PAYER</div>")
    .append("<div style=\"font-size: 14px; font-weight: bold;\">")
    .append(formatCurrency(paie.getSalaireNet()))
    .append("</div>")
    .append("</div>")
    .append("</div>")
    .append("</div>"); 


// Section inférieure : Signatures en bas de page
html.append("<p style=\"margin-top: 7px; text-align: center;\">")
    .append("L'employeur")
    .append("</p>");


    html.append("</div>") 
        .append("</body>\n</html>");
    
    return html.toString();
}

// Méthodes utilitaires pour extraire les donnÃ©es spÃ©cifiques
private BigDecimal getSalaireBaseFromRubriques(List<VuePaieFille> rubriques) {
    for (VuePaieFille r : rubriques) {
        if ("SALAIRE DE BASE".equalsIgnoreCase(r.getRubriqueNom()) && isGain(r)) {
            return r.getMontant();
        }
    }
    return BigDecimal.ZERO;
}

private BigDecimal getRubriqueMontant(List<VuePaieFille> rubriques, String rubriqueNom, String type) {
    for (VuePaieFille r : rubriques) {
        if (rubriqueNom.equalsIgnoreCase(r.getRubriqueNom())) {
            if ("Retenue".equalsIgnoreCase(type) && !isGain(r)) {
                return r.getMontant();
            } else if ("Gain".equalsIgnoreCase(type) && isGain(r)) {
                return r.getMontant();
            }
        }
    }
    return BigDecimal.ZERO;
}

private String getRubriqueCode(List<VuePaieFille> rubriques, String rubriqueNom, String type, String fallback) {
    for (VuePaieFille r : rubriques) {
        if (rubriqueNom.equalsIgnoreCase(r.getRubriqueNom())) {
            if ("Retenue".equalsIgnoreCase(type) && !isGain(r)) {
                return safeStr(r.getCode()).isEmpty() ? fallback : r.getCode();
            } else if ("Gain".equalsIgnoreCase(type) && isGain(r)) {
                return safeStr(r.getCode()).isEmpty() ? fallback : r.getCode();
            }
        }
    }
    return fallback;
}

private BigDecimal getRubriqueBase(List<VuePaieFille> rubriques, String rubriqueNom) {
    for (VuePaieFille r : rubriques) {
        if (rubriqueNom.equalsIgnoreCase(r.getRubriqueNom())) {
            return r.getBase();
        }
    }
    return BigDecimal.ZERO;
}

private BigDecimal getRubriqueTaux(List<VuePaieFille> rubriques, String rubriqueNom) {
    for (VuePaieFille r : rubriques) {
        if (rubriqueNom.equalsIgnoreCase(r.getRubriqueNom())) {
            return r.getTaux();
        }
    }
    return BigDecimal.ZERO;
}































































    public void exportBulletinPaieExcel(String idPaie, HttpServletResponse response) throws IOException {
        VuePaieComplete paie = vuePaieCompleteService.getByIdPaie(idPaie);
        List<VuePaieFille> rubriques = vuePaieFilleService.getByIdPaie(idPaie);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Bulletin");

        int rowIndex = 0;
        XSSFRow titleRow = sheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("Bulletin de paie");

        XSSFRow infoRow1 = sheet.createRow(rowIndex++);
        infoRow1.createCell(0).setCellValue("Paie ID");
        infoRow1.createCell(1).setCellValue(idPaie);
        infoRow1.createCell(3).setCellValue("Employe");
        infoRow1.createCell(4).setCellValue(paie != null ? safeStr(paie.getNomComplet()) : "");

        XSSFRow infoRow2 = sheet.createRow(rowIndex++);
        infoRow2.createCell(0).setCellValue("Matricule");
        infoRow2.createCell(1).setCellValue(paie != null ? safeStr(paie.getMatricule()) : "");
        infoRow2.createCell(3).setCellValue("Departement");
        infoRow2.createCell(4).setCellValue(paie != null ? safeStr(paie.getDepartement()) : "");


        XSSFRow infoRow3 = sheet.createRow(rowIndex++);
        infoRow3.createCell(0).setCellValue("Periode debut");
        infoRow3.createCell(1).setCellValue(paie != null && paie.getDateDebutPeriode() != null ? paie.getDateDebutPeriode().toString() : "");
        infoRow3.createCell(3).setCellValue("Periode fin");
        infoRow3.createCell(4).setCellValue(paie != null && paie.getDateFinPeriode() != null ? paie.getDateFinPeriode().toString() : "");

        rowIndex++;

        XSSFRow header = sheet.createRow(rowIndex++);
        header.createCell(0).setCellValue("Code");
        header.createCell(1).setCellValue("Rubrique");
        header.createCell(2).setCellValue("Base");
        header.createCell(3).setCellValue("Taux");
        header.createCell(4).setCellValue("Gain");
        header.createCell(5).setCellValue("Retenue");
        header.createCell(6).setCellValue("Charge patronale");

        if (rubriques != null) {
            for (VuePaieFille r : rubriques) {
                XSSFRow row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(safeStr(r.getCode()));
                row.createCell(1).setCellValue(safeStr(r.getRubriqueNom()));
                row.createCell(2).setCellValue(r.getBase() != null ? r.getBase().doubleValue() : 0);
                row.createCell(3).setCellValue(r.getTaux() != null ? r.getTaux().doubleValue() : 0);                boolean gain = isGain(r);
                boolean chargePatronale = r.getTypeRubrique() != null && r.getTypeRubrique().toLowerCase().contains("charge patronale");
                row.createCell(4).setCellValue(gain && r.getMontant() != null ? r.getMontant().doubleValue() : 0);
                row.createCell(5).setCellValue(!gain && !chargePatronale && r.getMontant() != null ? r.getMontant().doubleValue() : 0);
                row.createCell(6).setCellValue(chargePatronale && r.getMontant() != null ? r.getMontant().doubleValue() : 0);}
        }

        for (int i = 0; i <= 6; i++) {
            sheet.autoSizeColumn(i);
        }

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            outputStream.flush();
        } finally {
            workbook.close();
        }
    }

    public void exportBulletinsDepartementExcel(String departement, HttpServletResponse response) throws IOException {
        List<VuePaieComplete> paies = vuePaieCompleteService.getByDepartement(departement);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Bulletins_Departement");

        int rowIndex = 0;
        XSSFRow titleRow = sheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("Bulletins de paie - Departement");
        titleRow.createCell(1).setCellValue(safeStr(departement));

        // Construire les colonnes des rubriques (1 ligne par employe)
        List<String> rubriqueKeys = new ArrayList<>();
        List<String> rubriqueLabels = new ArrayList<>();
        if (paies != null) {
            java.util.LinkedHashSet<String> seen = new java.util.LinkedHashSet<>();
            for (VuePaieComplete p : paies) {
                if (p == null) continue;
                List<VuePaieFille> rubriques = vuePaieFilleService.getByIdPaie(p.getPaieId());
                if (rubriques == null) continue;
                for (VuePaieFille r : rubriques) {
                    if (r == null) continue;
                    String code = safeStr(r.getCode());
                    String nom = safeStr(r.getRubriqueNom());
                    String key = !code.isEmpty() ? code : nom;
                    String label = !code.isEmpty() ? (code + " - " + nom) : nom;
                    if (!key.isEmpty() && seen.add(key)) {
                        rubriqueKeys.add(key);
                        rubriqueLabels.add(label);
                    }
                }
            }
        }

        rowIndex++;

        XSSFRow header = sheet.createRow(rowIndex++);
        int h = 0;
        header.createCell(h++).setCellValue("Paie ID");
        header.createCell(h++).setCellValue("Matricule");
        header.createCell(h++).setCellValue("Nom complet");
        header.createCell(h++).setCellValue("Departement");
        header.createCell(h++).setCellValue("Mois");
        header.createCell(h++).setCellValue("Annee");
        header.createCell(h++).setCellValue("Date debut");
        header.createCell(h++).setCellValue("Date fin");
        header.createCell(h++).setCellValue("Salaire brut");
        header.createCell(h++).setCellValue("Salaire net");        for (String label : rubriqueLabels) {
            header.createCell(h++).setCellValue(label);
        }

        if (paies != null) {
            for (VuePaieComplete p : paies) {
                XSSFRow row = sheet.createRow(rowIndex++);
                int c = 0;
                row.createCell(c++).setCellValue(safeStr(p.getPaieId()));
                row.createCell(c++).setCellValue(safeStr(p.getMatricule()));
                row.createCell(c++).setCellValue(safeStr(p.getNomComplet()));
                row.createCell(c++).setCellValue(safeStr(p.getDepartement()));
                row.createCell(c++).setCellValue(p.getMoisPaieNom() != null ? p.getMoisPaieNom() : "");
                row.createCell(c++).setCellValue(p.getAnneePaie() != null ? p.getAnneePaie() : 0);
                row.createCell(c++).setCellValue(p.getDateDebutPeriode() != null ? p.getDateDebutPeriode().toString() : "");
                row.createCell(c++).setCellValue(p.getDateFinPeriode() != null ? p.getDateFinPeriode().toString() : "");
                row.createCell(c++).setCellValue(p.getSalaireBrut() != null ? p.getSalaireBrut().doubleValue() : 0);
                row.createCell(c++).setCellValue(p.getSalaireNet() != null ? p.getSalaireNet().doubleValue() : 0);                Map<String, BigDecimal> rubriqueMontants = new HashMap<>();
                List<VuePaieFille> rubriques = vuePaieFilleService.getByIdPaie(p.getPaieId());
                if (rubriques != null) {
                    for (VuePaieFille r : rubriques) {
                        if (r == null) continue;
                        String code = safeStr(r.getCode());
                        String nom = safeStr(r.getRubriqueNom());
                        String key = !code.isEmpty() ? code : nom;
                        if (key.isEmpty()) continue;
                        BigDecimal montant = r.getMontant();
                        if (montant == null) continue;
                        BigDecimal current = rubriqueMontants.getOrDefault(key, BigDecimal.ZERO);
                        rubriqueMontants.put(key, current.add(montant));
                    }
                }

                for (String key : rubriqueKeys) {
                    BigDecimal val = rubriqueMontants.get(key);
                    row.createCell(c++).setCellValue(val != null ? val.doubleValue() : 0);
                }
            }
        }

        int totalCols = 11 + rubriqueKeys.size();
        for (int i = 0; i < totalCols; i++) {
            sheet.autoSizeColumn(i);
        }

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            outputStream.flush();
        } finally {
            workbook.close();
        }
    }
    // --- Utilitaires ---
    private String safeStr(String s) {
        return s == null ? "" : s;
    }

    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return "";
        try {
            LocalDate d = LocalDate.parse(dateStr);
            return d.format(DATE_FORMAT);
        } catch (Exception e) {
            return dateStr;
        }
    }

    private String formatCurrency(BigDecimal value) {
        if (value == null) return "";
        return String.format("%,.2f", value)
                      .replace(",", " ")
                      .replace(".", ",");
    }

    private String formatTaux(BigDecimal taux) {
        if (taux == null) return "";
        // Si le taux est stockÃ© en % (ex: 1.0 ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ 1%), on affiche tel quel
        // Sinon ajuste selon ton modÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¨le mÃ©tier
        return String.format("%.3f", taux).replace(".", ",");
    }

    private boolean isGain(VuePaieFille r) {
        return "Gain".equalsIgnoreCase(r.getTypeRubrique());
    }

    private String resolveLogoSrc(String logoValue) {
        if (logoValue == null || logoValue.trim().isEmpty()) {
            return null;
        }

        String logo = logoValue.trim();
        if (logo.startsWith("http://") || logo.startsWith("https://") || logo.startsWith("data:")) {
            return logo;
        }

        java.util.List<java.nio.file.Path> candidates = java.util.List.of(
                java.nio.file.Paths.get(logo),
                java.nio.file.Paths.get("uploads").resolve(logo),
                java.nio.file.Paths.get("uploads", "logo").resolve(logo),
                java.nio.file.Paths.get("src", "main", "resources", "static").resolve(logo),
                java.nio.file.Paths.get("src", "main", "resources", "static", "uploads").resolve(logo),
                java.nio.file.Paths.get("src", "main", "resources", "static", "uploads", "logo").resolve(logo)
        );

        for (java.nio.file.Path pathCandidate : candidates) {
            if (java.nio.file.Files.exists(pathCandidate)) {
                try {
                    byte[] bytes = java.nio.file.Files.readAllBytes(pathCandidate);
                    String mime = java.nio.file.Files.probeContentType(pathCandidate);
                    if (mime == null) {
                        String lower = pathCandidate.getFileName().toString().toLowerCase();
                        if (lower.endsWith(".png")) mime = "image/png";
                        else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) mime = "image/jpeg";
                        else if (lower.endsWith(".gif")) mime = "image/gif";
                        else if (lower.endsWith(".svg")) mime = "image/svg+xml";
                        else mime = "application/octet-stream";
                    }
                    return "data:" + mime + ";base64," + java.util.Base64.getEncoder().encodeToString(bytes);
                } catch (java.io.IOException e) {
                    return null;
                }
            }
        }

        String baseUrl = "http://localhost:8080";
        return baseUrl + (logo.startsWith("/") ? "" : "/") + logo;
    }

    public void exporterBulletinPaieEnHtml(String idPaie, VuePaieComplete vuePaieComplete, List<VuePaieFille> rubriques, HttpServletResponse response) throws IOException {
        // GÃ©nÃ©ration du HTML
        String htmlContent = genererBulletinPaieHtml(vuePaieComplete, rubriques);

        // Configuration de la rÃ©ponse HTTP
        response.setContentType(MediaType.TEXT_HTML_VALUE + "; charset=" + StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "inline; filename=\"bulletin_paie_" + idPaie + ".html\"");

        // ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â°criture du contenu dans la rÃ©ponse
        try (ServletOutputStream out = response.getOutputStream()) {
                out.write(htmlContent.getBytes(StandardCharsets.UTF_8));
                out.flush();
        }
    }


}




