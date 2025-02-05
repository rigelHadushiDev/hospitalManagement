package hospital.management.demo.services.impl;


import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;
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
    public PatientServiceImpl(PatientRepository patientRepository, DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
        this.patientRepository = patientRepository;
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
        patientRepository.deleteById(String.valueOf(patientId));
    }

    @Override
    public boolean isExists(Long patientId) {
        return patientRepository.existsById(String.valueOf(patientId));
    }

    @Override
    public PatientEntity partialUpdate(Long patientId, PatientEntity patientEntity) {
        patientEntity.setPatient_id(patientId);

        return patientRepository.findById(String.valueOf(patientId)).map(existingPatient -> {
            Optional.ofNullable(patientEntity.getPatient_name()).ifPresent(existingPatient::setPatient_name);
            Optional.ofNullable(patientEntity.getPatient_last_name()).ifPresent(existingPatient::setPatient_last_name);
            Optional.ofNullable(patientEntity.getPatient_birthdate()).ifPresent(existingPatient::setPatient_birthdate);
            return patientRepository.save(existingPatient);
        }).orElseThrow(() -> new RuntimeException("Patient does not exist"));
    }



}
