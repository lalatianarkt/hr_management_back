package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Service.PaieCompleteViewService;
import com.rh.manage.View.PaieCompleteView;

import java.util.List;

@RestController
@RequestMapping("/api/paie-complete")
public class PaieCompleteViewController {
    @Autowired
    private PaieCompleteViewService paieCompleteViewService;

    @GetMapping
    public ResponseEntity<List<PaieCompleteView>> getAllPaies() {
        return ResponseEntity.ok(paieCompleteViewService.getAllPaies());
    }

    @GetMapping("/employe/{id}")
    public ResponseEntity<List<PaieCompleteView>> getPaiesByEmploye(@PathVariable Long id) {
        return ResponseEntity.ok(paieCompleteViewService.getPaiesByEmploye(id));
    }

    @GetMapping("/periode")
    public ResponseEntity<List<PaieCompleteView>> getPaiesByPeriode(
            @RequestParam Integer annee,
            @RequestParam Integer mois) {
        return ResponseEntity.ok(paieCompleteViewService.getPaiesByPeriode(annee, mois));
    }
}
