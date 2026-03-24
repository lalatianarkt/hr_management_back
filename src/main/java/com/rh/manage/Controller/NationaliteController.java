package com.rh.manage.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rh.manage.Model.Nationalite;
import com.rh.manage.Service.NationaliteService;

@RestController
@RequestMapping("/api/nationalites")
// @CrossOrigin(origins = "*")
 // autorise ton frontend React
public class NationaliteController {

    @Autowired
    private NationaliteService nationaliteService;

    @GetMapping
    public ResponseEntity<?> getAllNationalites() {
        try {
            return ResponseEntity.ok(nationaliteService.getAllNationalites());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

