// package com.rh.manage.Repository;

// import com.rh.manage.Model.Employe;
// import com.rh.manage.Model.Manager;
// import com.rh.manage.Model.ManagerEmploye;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;

// import java.util.List;

// @Repository
// public interface ManagerEmployeRepository extends JpaRepository<ManagerEmploye, Integer> {

//     List<ManagerEmploye> findByManager_Id(String managerId);

//     @Query("SELECT me.employe FROM ManagerEmploye me WHERE me.manager.id = :managerId")
//     List<Employe> findEmployesByManagerId(@Param("managerId") String managerId);

//     // @Query("SELECT me FROM ManagerEmploye me WHERE me.manager.id = :managerId")
//     // List<ManagerEmploye> findByManagerId(@Param("managerId") String managerId);
// }

