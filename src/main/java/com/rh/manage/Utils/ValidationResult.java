package com.rh.manage.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.rh.manage.Model.*;
/**
     * Classe pour stocker les résultats de validation
     */
public  class ValidationResult {
    private List<PointageImport> validPointages;
    private List<ErrorDetail> errorDetails;
    private List<String> globalErrors;
    private Map<String, Integer> errorStatistics;
    private int totalCount;
    private int validCount;
    private int errorCount;
    
    public ValidationResult() {
        this.validPointages = new ArrayList<>();
        this.errorDetails = new ArrayList<>();
        this.globalErrors = new ArrayList<>();
        this.errorStatistics = new HashMap<>();
    }
    
    public void addValidPointage(PointageImport pointage) {
        validPointages.add(pointage);
    }
    
    public void addErrorPointage(PointageImport pointage, int rowNumber, List<String> errors) {
        ErrorDetail errorDetail = new ErrorDetail(pointage, rowNumber, errors);
        errorDetails.add(errorDetail);
        
        // Mettre à jour les statistiques
        for (String error : errors) {
            errorStatistics.put(error, errorStatistics.getOrDefault(error, 0) + 1);
        }
    }
    
    public void addGlobalError(String error) {
        globalErrors.add(error);
    }
    
    public void calculateStatistics() {
        totalCount = validPointages.size() + errorDetails.size();
        validCount = validPointages.size();
        errorCount = errorDetails.size();
    }
    
    public boolean isValid() {
        return errorDetails.isEmpty() && globalErrors.isEmpty();
    }
    
    public boolean hasErrors() {
        return !errorDetails.isEmpty() || !globalErrors.isEmpty();
    }
    
    // Getters
    public List<PointageImport> getValidPointages() { return validPointages; }
    public List<ErrorDetail> getErrorDetails() { return errorDetails; }
    public List<String> getGlobalErrors() { return globalErrors; }
    public Map<String, Integer> getErrorStatistics() { return errorStatistics; }
    public int getTotalCount() { return totalCount; }
    public int getValidCount() { return validCount; }
    public int getErrorCount() { return errorCount; }
    
    /**
     * Convertit le résultat en Map pour la réponse JSON
     */
    public Map<String, Object> toResponse() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", isValid());
        response.put("hasErrors", hasErrors());
        response.put("totalCount", totalCount);
        response.put("validCount", validCount);
        response.put("errorCount", errorCount);
        
        if (!globalErrors.isEmpty()) {
            response.put("globalErrors", globalErrors);
        }
        
        if (!errorDetails.isEmpty()) {
            response.put("errorDetails", errorDetails.stream()
                .map(ErrorDetail::toMap)
                .collect(Collectors.toList()));
            response.put("errorStatistics", errorStatistics);
        }
        
        if (!validPointages.isEmpty()) {
            response.put("sampleValidData", validPointages.stream()
                .limit(5)
                .map(this::pointageToMap)
                .collect(Collectors.toList()));
        }
        
        // Ajouter des métriques
        response.put("metrics", calculateMetrics());
        
        return response;
    }
    
    private Map<String, Object> pointageToMap(PointageImport pointage) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", pointage.getId());
        map.put("name", pointage.getName());
        map.put("dept", pointage.getDept());
        map.put("attendance_time", pointage.getAttendance_time());
        map.put("attendance_type", pointage.getAttendance_type());
        map.put("machine_name", pointage.getMachine_name());
        return map;
    }
    
    private Map<String, Object> calculateMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("successRate", totalCount > 0 ? (validCount * 100.0 / totalCount) : 0);
        metrics.put("errorRate", totalCount > 0 ? (errorCount * 100.0 / totalCount) : 0);
        metrics.put("mostCommonError", errorStatistics.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Aucune"));
        return metrics;
    }
}

    
