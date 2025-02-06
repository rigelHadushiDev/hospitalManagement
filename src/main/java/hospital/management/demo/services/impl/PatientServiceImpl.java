package hospital.management.demo.services.impl;


import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import hospital.management.demo.repositories.AdmissionStateRepository;
import hospital.management.demo.repositories.DepartmentRepository;
import hospital.management.demo.repositories.PatientRepository;
import hospital.management.demo.services.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;
    private DepartmentRepository departmentRepository;
    private AdmissionStateRepository admissionStateRepository;

    public PatientServiceImpl(PatientRepository patientRepository, DepartmentRepository departmentRepository,
                              AdmissionStateRepository admissionStateRepository) {
        this.departmentRepository = departmentRepository;
        this.patientRepository = patientRepository;
        this.admissionStateRepository = admissionStateRepository;
    }

    @Override
    public PatientEntity save(PatientEntity patientEntity) {
        if (patientEntity.getDepartmentEntity() != null) {
            Long departmentId = patientEntity.getDepartmentEntity().getDepartment_id();
            DepartmentEntity departmentEntity = departmentRepository.findById(String.valueOf(departmentId))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
            patientEntity.setDepartmentEntity(departmentEntity);
        }
        return patientRepository.save(patientEntity);
    }

    @Override
    public Page<PatientEntity> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    @Override
    public Optional<PatientEntity> findOne(Long patientId) {
        return  patientRepository.findById(String.valueOf(patientId));
    }

    @Override
    public void delete(Long patientId) {

        PatientEntity patient = patientRepository.findById(String.valueOf(patientId))
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No patient found with this ID"));

        if (!admissionStateRepository.findByPatientEntity(patient).isEmpty()) {
          throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete patient. There is an admission state associated with it.");
        }

        patientRepository.delete(patient);
    }

    @Override
    public boolean isExists(Long patientId) {
        return patientRepository.existsById(String.valueOf(patientId));
    }
    // in the patient we should also change in the partial update the departmentId of the foreign key
    @Override
    public PatientEntity partialUpdate(Long patientId, PatientEntity patientEntity) {
        patientEntity.setPatient_id(patientId);

        return patientRepository.findById(String.valueOf(patientId)).map(existingPatient -> {
            Optional.ofNullable(patientEntity.getPatient_name()).ifPresent(existingPatient::setPatient_name);
            Optional.ofNullable(patientEntity.getPatient_last_name()).ifPresent(existingPatient::setPatient_last_name);
            Optional.ofNullable(patientEntity.getPatient_birthdate()).ifPresent(existingPatient::setPatient_birthdate);
            return patientRepository.save(existingPatient);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient does not exist."));
    }

    @Override
    public Page<PatientEntity> searchPatientsByFullName(String fullName, Pageable pageable) {
        return patientRepository.searchByFullName(fullName, pageable);
    }

}
