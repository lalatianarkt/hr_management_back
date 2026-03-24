package com.rh.manage.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.rh.manage.Model.PointageImport;

/**
     * Classe pour stocker les détails d'une erreur
     */
public class ErrorDetail {
    private PointageImport pointage;
    private int rowNumber;
    private List<String> errors;
    
    public ErrorDetail(PointageImport pointage, int rowNumber, List<String> errors) {
        this.pointage = pointage;
        this.rowNumber = rowNumber;
        this.errors = new ArrayList<>(errors);
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("rowNumber", rowNumber);
        map.put("lineInFile", rowNumber);
        map.put("id", pointage.getId());
        map.put("name", pointage.getName());
        map.put("errors", errors);
        map.put("data", getPointageData());
        return map;
    }
    
    private Map<String, Object> getPointageData() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", pointage.getId());
        data.put("name", pointage.getName());
        data.put("dept", pointage.getDept());
        data.put("attendance_time", pointage.getAttendance_time());
        data.put("attendance_type", pointage.getAttendance_type());
        data.put("machine_name", pointage.getMachine_name());
        return data;
    }
    
    // Getters
    public PointageImport getPointage() { return pointage; }
    public int getRowNumber() { return rowNumber; }
    public List<String> getErrors() { return errors; }
}
