package hospital.management.demo.services.impl;

import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import hospital.management.demo.repositories.AdmissionStateRepository;
import hospital.management.demo.repositories.DepartmentRepository;
import hospital.management.demo.repositories.PatientRepository;
import hospital.management.demo.services.AdmissionStateService;
import hospital.management.demo.services.PatientService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AdmissionStateServiceImpl implements AdmissionStateService {

    private AdmissionStateRepository admissionStateRepository;
    private PatientRepository patientRepository;


    public AdmissionStateServiceImpl(AdmissionStateRepository admissionStateRepository,
                                     PatientRepository patientRepository
                                   ) {
        this.admissionStateRepository = admissionStateRepository;
        this.patientRepository = patientRepository;

    }

    @Override
    public AdmissionStateEntity save(AdmissionStateEntity admissionStateEntity) {

        Optional<Long> patientIdOptional = Optional.ofNullable(admissionStateEntity.getPatientEntity())
                .map(PatientEntity::getPatient_id);

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





}