package com.rh.manage.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Dto.TopEmployeDTO;
import com.rh.manage.Model.DemandeConge;

@Service
public class TopEmployeService {
    @Autowired
    DemandeCongeService demandeCongeService;

    // public List<TopEmployeDTO> getStatTopEmploye(){
    //    List<DemandeConge> les_demandes = demandeCongeService.getEmployeTopDemande();
    // } 
    


}
