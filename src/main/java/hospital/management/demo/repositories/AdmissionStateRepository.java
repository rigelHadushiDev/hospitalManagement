package hospital.management.demo.repositories;
import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.ClinicalDataEntity;
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

public interface AdmissionStateRepository extends CrudRepository<AdmissionStateEntity, String>,
        PagingAndSortingRepository<AdmissionStateEntity, String> , JpaRepository<AdmissionStateEntity, String> {

    List<AdmissionStateEntity> findByPatientEntity(PatientEntity patientEntity);


    @Query("SELECT c FROM AdmissionStateEntity c " +
            "WHERE c.patientEntity.patient_id = :patientId")
    Page<AdmissionStateEntity> findByPatientId(@Param("patientId") Long patientId, Pageable pageable);
}
