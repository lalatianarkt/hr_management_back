package com.rh.manage.Repository;

import com.rh.manage.Model.PosteEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosteEmployeRepository extends JpaRepository<PosteEmploye, String> {

    // Récupérer tous les postes d'un employé
    List<PosteEmploye> findByEmployeId(String employeId);

    // Récupérer tous les employés d'un poste
    List<PosteEmploye> findByPosteId(String posteId);
     
    //  @Query("SELECT pe FROM PosteEmploye pe WHERE pe.employe.id = :idEmp ORDER BY pe.dateDebut DESC")
    // List<PosteEmploye> findByEmployeId(@Param("idEmp") String idEmp);
}
