package hospital.management.demo.repositories;

import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.ClinicalDataEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ClinicalDataRepository extends CrudRepository<ClinicalDataEntity, String>,
        PagingAndSortingRepository<ClinicalDataEntity, String>, JpaRepository<ClinicalDataEntity, String> {

    List<ClinicalDataEntity> findByAdmissionStateEntity(AdmissionStateEntity admissionStateEntity);

}
