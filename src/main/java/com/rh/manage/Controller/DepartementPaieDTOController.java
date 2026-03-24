package com.rh.manage.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rh.manage.Dto.DepartementPaieDTO;
import com.rh.manage.Service.DepartementPaieDTOService;

@RestController
@RequestMapping("/api/paie")
public class DepartementPaieDTOController {
    @Autowired
    DepartementPaieDTOService departementPaieDTOService;

    @GetMapping("/bulletin")
    public ResponseEntity<List<DepartementPaieDTO>> getBulletinParDepartement() {
        try {
            List<DepartementPaieDTO> lesDepartementsPaie =
                    departementPaieDTOService.getPaiesParDepartement();
            return ResponseEntity.ok(lesDepartementsPaie);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
