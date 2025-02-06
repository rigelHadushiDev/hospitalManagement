package hospital.management.demo.services;

import hospital.management.demo.domain.entities.PatientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PatientService {
    PatientEntity save(PatientEntity patientEntity);

    Page<PatientEntity> findAll(Pageable pageable);

    Optional<PatientEntity> findOne(Long patient_id);

    void delete(Long patient_id);

    boolean isExists(Long patient_id);

    PatientEntity partialUpdate(Long patient_id, PatientEntity patientEntity);

}
