package hospital.management.demo.repositories;
import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AdmissionStateRepository extends CrudRepository<AdmissionStateEntity, String>,
        PagingAndSortingRepository<AdmissionStateEntity, String> , JpaRepository<AdmissionStateEntity, String> {

    List<AdmissionStateEntity> findByPatientEntity(PatientEntity patientEntity);
}
