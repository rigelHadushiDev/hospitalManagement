package hospital.management.demo.repositories;

import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientRepository extends CrudRepository<PatientEntity, String>,
        PagingAndSortingRepository<PatientEntity, String>, JpaRepository<PatientEntity, String> {

    List<PatientEntity> findByDepartmentEntity(DepartmentEntity departmentEntity);

    @Query("SELECT p FROM PatientEntity p WHERE LOWER(CONCAT(p.patient_name, ' ', p.patient_last_name)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    Page<PatientEntity> searchByFullName(@Param("fullName") String fullName, Pageable pageable);
}
