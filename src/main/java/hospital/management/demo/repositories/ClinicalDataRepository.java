package hospital.management.demo.repositories;

import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.ClinicalDataEntity;
import hospital.management.demo.domain.entities.DepartmentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClinicalDataRepository extends CrudRepository<ClinicalDataEntity, String>,
        PagingAndSortingRepository<ClinicalDataEntity, String>, JpaRepository<ClinicalDataEntity, String> {

    List<ClinicalDataEntity> findByAdmissionStateEntity(AdmissionStateEntity admissionStateEntity);

    @Modifying
    @Transactional
    @Query("DELETE FROM ClinicalDataEntity c WHERE c.admissionStateEntity IN :admissionStateIds")
    void deleteByAdmissionStateIds(@Param("admissionStateIds") List<AdmissionStateEntity> admissionStateIds);


    @Query("SELECT c FROM ClinicalDataEntity c " +
            "WHERE c.admissionStateEntity.patientEntity.patient_id = :patientId")
    Page<ClinicalDataEntity> findByPatientId(@Param("patientId") Long patientId, Pageable pageable);
}
