package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.PointageFille;
import com.rh.manage.Service.PointageFilleService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pointages-filles")
public class PointageFilleController {
    @Autowired
    PointageFilleService pointageFilleService;
    
    @PostMapping
    public ResponseEntity<PointageFille> createPointageFille(@RequestBody PointageFille pointageFille) {
        PointageFille created = pointageFilleService.createPointageFille(pointageFille);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PostMapping("/batch")
    public ResponseEntity<List<PointageFille>> createMultiplePointageFilles(
            @RequestBody List<PointageFille> pointageFilles) {
        List<PointageFille> created = pointageFilleService.createMultiplePointageFilles(pointageFilles);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PointageFille> getPointageFille(@PathVariable String id) {
        PointageFille pointageFille = pointageFilleService.getPointageFilleById(id);
        return ResponseEntity.ok(pointageFille);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PointageFille> updatePointageFille(
            @PathVariable String id, 
            @RequestBody PointageFille pointageFilleDetails) {
        PointageFille updated = pointageFilleService.updatePointageFille(id, pointageFilleDetails);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePointageFille(@PathVariable String id) {
        pointageFilleService.deletePointageFille(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/pointage/{pointageId}")
    public ResponseEntity<List<PointageFille>> getPointagesFillesByPointage(@PathVariable String pointageId) {
        List<PointageFille> pointagesFilles = pointageFilleService.getPointagesFillesByPointage(pointageId);
        return ResponseEntity.ok(pointagesFilles);
    }
    
    @GetMapping("/pointage/{pointageId}/ordonnes")
    public ResponseEntity<List<PointageFille>> getPointagesFillesByPointageOrdered(@PathVariable String pointageId) {
        List<PointageFille> pointagesFilles = pointageFilleService.getPointagesFillesByPointageOrdered(pointageId);
        return ResponseEntity.ok(pointagesFilles);
    }
    
    @GetMapping("/type/{typeAction}")
    public ResponseEntity<List<PointageFille>> getPointagesFillesByType(@PathVariable String typeAction) {
        List<PointageFille> pointagesFilles = pointageFilleService.getPointagesFillesByType(typeAction);
        return ResponseEntity.ok(pointagesFilles);
    }
    
    @GetMapping("/source/{source}")
    public ResponseEntity<List<PointageFille>> getPointagesFillesBySource(@PathVariable String source) {
        List<PointageFille> pointagesFilles = pointageFilleService.getPointagesFillesBySource(source);
        return ResponseEntity.ok(pointagesFilles);
    }
    
    @GetMapping("/periode")
    public ResponseEntity<List<PointageFille>> getPointagesFillesByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<PointageFille> pointagesFilles = pointageFilleService.getPointagesFillesByPeriod(start, end);
        return ResponseEntity.ok(pointagesFilles);
    }
    
    // @DeleteMapping("/pointage/{pointageId}/all")
    // public ResponseEntity<Void> deleteAllByPointage(@PathVariable String pointageId) {
    //     pointageFilleService.deleteAllByPointage(pointageId);
    //     return ResponseEntity.noContent().build();
    // }
    
    @GetMapping("/pointage/{pointageId}/first")
    public ResponseEntity<PointageFille> getFirstPointageFille(@PathVariable String pointageId) {
        PointageFille pointageFille = pointageFilleService.getFirstPointageFille(pointageId);
        return ResponseEntity.ok(pointageFille);
    }
    
    @GetMapping("/pointage/{pointageId}/last")
    public ResponseEntity<PointageFille> getLastPointageFille(@PathVariable String pointageId) {
        PointageFille pointageFille = pointageFilleService.getLastPointageFille(pointageId);
        return ResponseEntity.ok(pointageFille);
    }
    
    @GetMapping("/entrees-sans-sortie")
    public ResponseEntity<List<PointageFille>> getEntreesSansSortie() {
        List<PointageFille> pointagesFilles = pointageFilleService.getEntreesSansSortie();
        return ResponseEntity.ok(pointagesFilles);
    }
    
    @GetMapping("/pointage/{pointageId}/stats")
    public ResponseEntity<Map<String, Object>> getStatsByPointage(@PathVariable String pointageId) {
        long count = pointageFilleService.countPointagesFilles(pointageId);
        PointageFille first = pointageFilleService.getFirstPointageFille(pointageId);
        PointageFille last = pointageFilleService.getLastPointageFille(pointageId);
        
        return ResponseEntity.ok(Map.of(
                "pointageId", pointageId,
                "nombrePointagesFilles", count,
                "premierPointage", first != null ? first.getDateHeurePointage() : null,
                "dernierPointage", last != null ? last.getDateHeurePointage() : null,
                "hasPointagesFilles", count > 0
        ));
    }
}
