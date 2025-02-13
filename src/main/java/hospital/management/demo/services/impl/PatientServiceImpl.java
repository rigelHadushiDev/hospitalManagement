package hospital.management.demo.services.impl;


import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.DepartmentChangeHistoryEntity;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import hospital.management.demo.repositories.*;
import hospital.management.demo.services.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;
    private DepartmentRepository departmentRepository;
    private AdmissionStateRepository admissionStateRepository;
    private DepartmentChangeHistoryRepository departmentChangeHistoryRepository;
    private ClinicalDataRepository clinicalDataRepository;

    public PatientServiceImpl(PatientRepository patientRepository, DepartmentRepository departmentRepository,
                              AdmissionStateRepository admissionStateRepository,
                              DepartmentChangeHistoryRepository departmentChangeHistoryRepository,
                              ClinicalDataRepository clinicalDataRepository) {
        this.departmentRepository = departmentRepository;
        this.patientRepository = patientRepository;
        this.admissionStateRepository = admissionStateRepository;
        this.departmentChangeHistoryRepository = departmentChangeHistoryRepository;
        this.clinicalDataRepository = clinicalDataRepository;
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
        return Optional.ofNullable(patientRepository.findById(String.valueOf(patientId))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient does not exist")));
    }

    @Override
    public void delete(Long patientId) {

        PatientEntity patient = patientRepository.findById(String.valueOf(patientId))
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No patient found with this ID"));

        departmentChangeHistoryRepository.deleteAll(departmentChangeHistoryRepository.findByPatientEntity(patient));

        List<AdmissionStateEntity> admissionStateEntities = admissionStateRepository.findByPatientEntity(patient);

        if (!admissionStateEntities.isEmpty()) {
                clinicalDataRepository.deleteByAdmissionStateIds(admissionStateEntities);
        }

        admissionStateRepository.deleteAll(admissionStateRepository.findByPatientEntity(patient));

        patientRepository.delete(patient);
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

            if (patientEntity.getDepartmentEntity() != null && patientEntity.getDepartmentEntity().getDepartment_id() != null) {
                Long newDepartmentId = patientEntity.getDepartmentEntity().getDepartment_id();
                handleDepartmentChange(existingPatient, newDepartmentId);
            }

            return patientRepository.save(existingPatient);
        }).orElseThrow(() -> new RuntimeException("Patient does not exist"));
    }


    public void handleDepartmentChange(PatientEntity patientEntity, Long newDepartmentId) {
        DepartmentEntity newDepartment = departmentRepository.findById(String.valueOf(newDepartmentId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        Long oldDepartmentID = patientEntity.getDepartmentEntity().getDepartment_id();

        if(!Objects.equals(oldDepartmentID, newDepartmentId)) {

            String oldDepartmentName = patientEntity.getDepartmentEntity().getDepartment_name() != null ? patientEntity.getDepartmentEntity().getDepartment_name() : "None";

            patientEntity.setDepartmentEntity(newDepartment);
            DepartmentChangeHistoryEntity departmentChangeHistoryEntity = new DepartmentChangeHistoryEntity();
            departmentChangeHistoryEntity.setPatientEntity(patientEntity);
            departmentChangeHistoryEntity.setOld_department(oldDepartmentName);
            departmentChangeHistoryEntity.setNew_department(newDepartment.getDepartment_name());
            departmentChangeHistoryEntity.setChange_date(LocalDateTime.now());

            departmentChangeHistoryRepository.save(departmentChangeHistoryEntity);
        }
}

    @Override
    public Page<PatientEntity> searchPatientsByFullName(String fullName, Pageable pageable) {
        return patientRepository.searchByFullName(fullName, pageable);
    }



}
