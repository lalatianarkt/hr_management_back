// package com.rh.manage.Service;

// import org.springframework.stereotype.Service;

// import com.rh.manage.Model.DemandeMouvement;
// import com.rh.manage.Repository.DemandeMouvementRepository;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class DemandeMouvementService {

//     private final DemandeMouvementRepository repository;

//     public DemandeMouvementService(DemandeMouvementRepository repository) {
//         this.repository = repository;
//     }

//     public List<DemandeMouvement> getAll() {
//         return repository.findAll();
//     }

//     public Optional<DemandeMouvement> getById(String id) {
//         return repository.findById(id);
//     }

//     public DemandeMouvement save(DemandeMouvement demande) {
//         return repository.save(demande);
//     }

//     public void deleteById(String id) {
//         repository.deleteById(id);
//     }
// }

