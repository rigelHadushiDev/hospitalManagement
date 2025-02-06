package hospital.management.demo.services.impl;

import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.ClinicalDataEntity;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import hospital.management.demo.repositories.AdmissionStateRepository;
import hospital.management.demo.repositories.ClinicalDataRepository;
import hospital.management.demo.repositories.DepartmentRepository;
import hospital.management.demo.repositories.PatientRepository;
import hospital.management.demo.services.AdmissionStateService;
import hospital.management.demo.services.ClinicalDataService;
import hospital.management.demo.services.PatientService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AdmissionStateServiceImpl implements AdmissionStateService {

    private AdmissionStateRepository admissionStateRepository;
    private PatientRepository patientRepository;
    private ClinicalDataRepository clinicalDataRepository;


    public AdmissionStateServiceImpl(AdmissionStateRepository admissionStateRepository,
                                     PatientRepository patientRepository,ClinicalDataRepository clinicalDataRepository
                                   ) {
        this.admissionStateRepository = admissionStateRepository;
        this.patientRepository = patientRepository;
        this.clinicalDataRepository = clinicalDataRepository;

    }


    @Override
    public AdmissionStateEntity save(AdmissionStateEntity admissionStateEntity) {

        Optional<Long> patientIdOptional = Optional.ofNullable(admissionStateEntity.getPatientEntity())
                .map(PatientEntity::getPatient_id);

        // Check if patient is given

        if (patientIdOptional.isPresent()) {
            Long patientId = patientIdOptional.get();  // Extract the Long value

            PatientEntity patientEntity = patientRepository.findById(String.valueOf(patientId))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

            admissionStateEntity.setPatientEntity(patientEntity);
            return admissionStateRepository.save(admissionStateEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No patient assigned");
        }
    }

    @Override
    public Page<AdmissionStateEntity> findAll(Pageable pageable) {
        return admissionStateRepository.findAll(pageable);
    }



    @Override
    public Optional<AdmissionStateEntity> findOne(Long admission_state_id) {
        return admissionStateRepository.findById(String.valueOf(admission_state_id));
    }

    @Override
    public void delete(Long admission_state_id) {
        AdmissionStateEntity admission = admissionStateRepository.findById(String.valueOf(admission_state_id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No admission found"));

        if (!clinicalDataRepository.findByAdmissionStateEntity(admission).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete admission State. There are clinical data associated with it.");
        }

        admissionStateRepository.delete(admission);
    }

    @Override
    public boolean isExists(Long admission_state_id) {
        return admissionStateRepository.existsById(String.valueOf(admission_state_id));
    }


    @Override
    public AdmissionStateEntity partialUpdate(Long admission_state_id, AdmissionStateEntity admissionStateEntity) {
        admissionStateEntity.setAdmission_state_id(admission_state_id);

        return admissionStateRepository.findById(String.valueOf(admission_state_id)).map(existingAdmissionStateId -> {
            Optional.ofNullable(admissionStateEntity.getEntering_date()).ifPresent(existingAdmissionStateId::setEntering_date);
            Optional.ofNullable(admissionStateEntity.getExiting_date()).ifPresent(existingAdmissionStateId::setExiting_date);
            Optional.ofNullable(admissionStateEntity.getCause()).ifPresent(existingAdmissionStateId::setCause);
            Optional.ofNullable(admissionStateEntity.getReason()).ifPresent(existingAdmissionStateId::setReason);
            Optional.ofNullable(admissionStateEntity.getDischarge()).ifPresent(existingAdmissionStateId::setDischarge);
            return admissionStateRepository.save(existingAdmissionStateId);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admission State does not exist"));
    }



}