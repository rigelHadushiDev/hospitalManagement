package hospital.management.demo.services;

import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface AdmissionStateService {
    AdmissionStateEntity save(AdmissionStateEntity admissionStateEntity);

    Page<AdmissionStateEntity> findAll(Pageable pageable);

    Optional<AdmissionStateEntity> findOne(Long admission_state_id);

    void delete(Long admission_state_id);

    boolean isExists(Long admission_state_id);

    AdmissionStateEntity partialUpdate(Long admission_state_id, AdmissionStateEntity admissionStateEntity);


    AdmissionStateEntity dischargeAdmission(Long admissionStateId, AdmissionStateEntity.Reason reason);

    Page<AdmissionStateEntity> searchByPatientId(String patientId, Pageable pageable);
}
