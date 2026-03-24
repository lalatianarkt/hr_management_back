package com.rh.manage.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.rh.manage.Dto.CongeParTypeDTO;
import com.rh.manage.Service.CongeParTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CongeParTypeController {
    @Autowired 
    CongeParTypeService congeParTypeService;

    @GetMapping("/congeParType")
    public ResponseEntity<?> getStatCongeParType() {
        try {
            List<CongeParTypeDTO> stats = congeParTypeService.getStatTypeConge();
            
            if (stats == null || stats.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erreur interne du serveur");
        }
    }
}
