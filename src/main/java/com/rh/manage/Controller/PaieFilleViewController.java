package com.rh.manage.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rh.manage.Service.PaieFilleViewService;
import com.rh.manage.View.PaieFilleView;

@RestController
@RequestMapping("/api/paie-filles-complet")
public class PaieFilleViewController {
    @Autowired
    PaieFilleViewService paieFilleViewService;
   
    @GetMapping
    public ResponseEntity<List<PaieFilleView>> getAllPaieFilles() {
        return ResponseEntity.ok(paieFilleViewService.getAllPaieFilles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaieFilleView> getPaieFilleById(@PathVariable Long id) {
        return paieFilleViewService.getPaieFilleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
