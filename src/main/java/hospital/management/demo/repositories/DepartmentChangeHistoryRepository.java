package hospital.management.demo.repositories;

import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.DepartmentChangeHistoryEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentChangeHistoryRepository extends JpaRepository<DepartmentChangeHistoryEntity, Long> {

    List<DepartmentChangeHistoryEntity> findByPatientEntity(PatientEntity patientEntity);
}
