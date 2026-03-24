package com.rh.manage.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Model.Formule;
import com.rh.manage.Repository.FormuleRepository;

@Service
public class FormuleService {

    @Autowired 
    FormuleIdGenerator formuleIdGenerator;

    private final FormuleRepository formuleRepository;

    public FormuleService(FormuleRepository formuleRepository) {
        this.formuleRepository = formuleRepository;
    }

    public Formule createFormule(Formule formule) {
        formule.setId(formuleIdGenerator.generateNextId("FO-")); 
        System.out.println("tafiditra ato iany e +++++++++++++++++++++++++ : " + formule.getId());
        return formuleRepository.save(formule);
    }

    public Optional<Formule> getFormuleById(String id) {
        return formuleRepository.findById(id);
    }

    public List<Formule> getAllFormules() {
        return formuleRepository.findAll();
    }

    public Formule updateFormule(Formule formule, String id) {
        formule.setId(id);
        return formuleRepository.save(formule);
        // Formule existingFormule = formuleRepository.findById(id).get();
        // if (existingFormule != null) {
        //     existingFormule.setBase(formule.getBase());
        //     existingFormule.setMontantFixe(formule.getMontantFixe());
        //     existingFormule.setNombre(formule.getNombre());
        //     existingFormule.setTaux(formule.getTaux());
        //     return formuleRepository.save(formule);
        // } 
        // return null;
    }

    public void deleteFormule(String id) {
        formuleRepository.deleteById(id);
    }

    public boolean formuleExists(String id) {
        return formuleRepository.existsById(id);
    }
}   

