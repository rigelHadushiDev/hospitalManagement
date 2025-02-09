package hospital.management.demo.repositories;

import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.ClinicalDataEntity;
import hospital.management.demo.domain.entities.DepartmentChangeHistoryEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentChangeHistoryRepository extends JpaRepository<DepartmentChangeHistoryEntity, Long> {

    List<DepartmentChangeHistoryEntity> findByPatientEntity(PatientEntity patientEntity);

    @Query("SELECT c FROM DepartmentChangeHistoryEntity c " +
            "WHERE c.patientEntity.patient_id = :patientId")
    Page<DepartmentChangeHistoryEntity> searchByPatientIdWithPagination(@Param("patientId") Long patientId, Pageable pageable);
}
