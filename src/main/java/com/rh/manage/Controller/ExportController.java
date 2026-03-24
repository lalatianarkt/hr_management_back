package com.rh.manage.Controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.VuePaieComplete;
import com.rh.manage.Model.VuePaieFille;
import com.rh.manage.Service.EmployeService;
import com.rh.manage.Service.ExportService;
import com.rh.manage.Service.InfosProfessionnellesService;
import com.rh.manage.Service.VuePaieCompleteService;
import com.rh.manage.Service.VuePaieFilleService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/export")
public class ExportController {
    @Autowired
    ExportService exportService;

    @Autowired
    InfosProfessionnellesService infosProfessionnellesService;

    @Autowired
    VuePaieCompleteService vuePaieCompleteService;

    @Autowired
    VuePaieFilleService vuePaieFilleService;

    @Autowired
    EmployeService employeService;

    @GetMapping("/bulletin/{idPaie}")
    public ResponseEntity<Map<String, String>> exportPaieEmploye(@PathVariable String idPaie) {
        try {
            VuePaieComplete vuePaieComplete = vuePaieCompleteService.getByIdPaie(idPaie);
            List<VuePaieFille> vuePaieFille = vuePaieFilleService.getByIdPaie(idPaie);
            String htmlContent = exportService.genererBulletinPaieHtml(vuePaieComplete, vuePaieFille);
            
            Map<String, String> response = new HashMap<>();
            response.put("htmlContent", htmlContent);
            response.put("filename", "bulletin_paie_" + idPaie + ".html");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erreur génération HTML : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la génération du bulletin"));
        }
    }

    @GetMapping("/bulletin/{idPaie}/pdf")
    public void exportPaieEmployePDF(
            @PathVariable String idPaie,
            HttpServletResponse response) throws IOException {
        
        try {
            VuePaieComplete vuePaieComplete = vuePaieCompleteService.getByIdPaie(idPaie);
            List<VuePaieFille> vuePaieFille = vuePaieFilleService.getByIdPaie(idPaie);
            
            // Générer le PDF
            byte[] pdfBytes = exportService.genererBulletinPaiePDF(vuePaieComplete, vuePaieFille);
            
            // Configurer la réponse
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", 
                "attachment; filename=\"bulletin_paie_" + idPaie + ".pdf\"");
            response.setContentLength(pdfBytes.length);
            
            // Écrire le PDF
            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Erreur lors de la génération du PDF: " + e.getMessage());
        }
    }
    
    @GetMapping("/format/heureSup")
    public void exportFormatHeureSup(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=employes.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Heures supplémentaires");

        // Créer l'en-tête
        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("Matricule");
        header.createCell(1).setCellValue("HS 130%");
        header.createCell(2).setCellValue("HS 140%");
        header.createCell(3).setCellValue("HS 150%");
        header.createCell(4).setCellValue("HS 130% exo");
        header.createCell(5).setCellValue("HS 150% exo");
        
        // Remplir les données
        List<InfosProfessionnelles> les_infos_pro = infosProfessionnellesService.getAllInfoProActif();
        int rowIndex = 1;
        for (InfosProfessionnelles infosProfessionnelles : les_infos_pro) {
            XSSFRow row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(infosProfessionnelles.getMatricule());
            // Vous pouvez ajouter les autres cellules ici si nécessaire
            row.createCell(1).setCellValue(""); // HS 130%
            row.createCell(2).setCellValue(""); // HS 140%
            row.createCell(3).setCellValue(""); // HS 150%
            row.createCell(4).setCellValue(""); // HS 130% exo
            row.createCell(5).setCellValue(""); // HS 150% exo
        }

        // AUTO-SIZE les colonnes pour une meilleure lisibilité
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }

        // IMPORTANT: Écrire le workbook dans le flux de réponse
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            outputStream.flush();
        } finally {
            workbook.close();
        }
            // exportService.exportHeuresSupplémentaires(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erreur : " + e.getMessage());
        }
    }    

    @GetMapping
    public void exportEmployes(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=employes.xlsx");
            exportService.exportEmployesWithPrep(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erreur ici : " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> exportFicheEmployeEnPdf(
            @PathVariable("id") String idEmp,
            HttpServletResponse response) throws IOException {

        try {
            exportService.exportFicheEmploye(idEmp, response);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'export: " + e.getMessage());
        }
    }

}
