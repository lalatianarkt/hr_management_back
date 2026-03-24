// package com.rh.manage.Service;

// import com.rh.manage.Model.Employe;
// import com.rh.manage.Model.Manager;
// import com.rh.manage.Model.ManagerEmploye;
// import com.rh.manage.Repository.ManagerEmployeRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class ManagerEmployeService {

//     private final ManagerEmployeRepository repository;
    
//     @Autowired
//     EmployeService employeService;

//     @Autowired
//     ManagerService managerService;

//     public ManagerEmployeService(ManagerEmployeRepository repository) {
//         this.repository = repository;
//     }

//     public List<ManagerEmploye> getAll() {
//         return repository.findAll();
//     }

//     public Optional<ManagerEmploye> getById(int id) {
//         return repository.findById(id);
//     }

//     public ManagerEmploye create(ManagerEmploye managerEmploye) {
//         return repository.save(managerEmploye);
//     }

//     public ManagerEmploye createDataManagerEmploye(ManagerEmploye managerEmploye) throws Exception {
//         if (managerEmploye.getEmploye() == null || managerEmploye.getEmploye().getId() == null) {
//             throw new Exception("L'ID de l'employé est requis.");
//         }
//         if (managerEmploye.getManager() == null || managerEmploye.getManager().getId() == null) {
//             throw new Exception("L'ID du manager est requis.");
//         }

//         // Récupérer les entités depuis la base
//         Optional<Employe> employeOpt = employeService.getById(managerEmploye.getEmploye().getId());
//         Optional<Manager> managerOpt = managerService.getManagerById(managerEmploye.getManager().getId());

//         if (employeOpt.isEmpty()) {
//             throw new Exception("Employé introuvable avec l'ID : " + managerEmploye.getEmploye().getId());
//         }
//         if (managerOpt.isEmpty()) {
//             throw new Exception("Manager introuvable avec l'ID : " + managerEmploye.getManager().getId());
//         }

//         // Associer les objets réels
//         managerEmploye.setEmploye(employeOpt.get());
//         managerEmploye.setManager(managerOpt.get());

//         return managerEmploye;
//     }

//      public List<ManagerEmploye> getEmployeParManager(String managerId) {
//         // Récupérer l'id du manager (String) depuis ManagerEmploye
//         // String managerId = managerEmploye.getManager().getId();

//         // Récupérer la liste des employés de ce manager
//         // List<Employe> employes = repository.findEmployesByManagerId(managerId);
//         List<ManagerEmploye> les_emp_par_manager = repository.findByManager_Id(managerId);

//         // Vous pouvez construire la liste de ManagerEmploye avec ces employés si nécessaire
//         // Par exemple, créer des objets ManagerEmploye temporaires ou retourner directement les Employe
//         return les_emp_par_manager;
//     }



//     // public ManagerEmploye update(Long id, ManagerEmploye updated) {
//     //     return repository.findById(id).map(existing -> {
//     //         existing.setDateDebut(updated.getDateDebut());
//     //         existing.setDateFin(updated.getDateFin());
//     //         existing.setManager(updated.getManager());
//     //         existing.setEmploye(updated.getEmploye());
//     //         return repository.save(existing);
//     //     }).orElseThrow(() -> new RuntimeException("Affectation non trouvée avec id : " + id));
//     // }

//     public void delete(int id) {
//         repository.deleteById(id);
//     }

//     // public List<ManagerEmploye> getByManager(String managerId) {
//     //     return repository.findByManager_Id(managerId);
//     // }

//     // public List<ManagerEmploye> getByEmploye(String employeId) {
//     //     return repository.findByEmploye_Id(employeId);
//     // }
// }

