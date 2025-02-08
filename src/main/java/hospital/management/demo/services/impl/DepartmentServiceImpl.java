package hospital.management.demo.services.impl;


import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.repositories.DepartmentRepository;
import hospital.management.demo.repositories.PatientRepository;
import hospital.management.demo.services.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;
    private PatientRepository patientRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,PatientRepository patientRepository) {
        this.departmentRepository = departmentRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public DepartmentEntity save(DepartmentEntity departmentEntity) {
        return departmentRepository.save(departmentEntity);
    }

    @Override
    public List<DepartmentEntity> findAll() {
        return StreamSupport.stream(departmentRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DepartmentEntity> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }



    @Override
    public Optional<DepartmentEntity> findOne(Long department_id) {
        return departmentRepository.findById(String.valueOf(department_id));
    }

    @Override
    public void delete(Long department_id) {
        DepartmentEntity department = departmentRepository.findById(String.valueOf(department_id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No department found"));


        if (!patientRepository.findByDepartmentEntity(department).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete department. There are patients associated with it.");
        }

        departmentRepository.delete(department);
    }

    @Override
    public boolean isExists(Long department_id) {
        return departmentRepository.existsById(String.valueOf(department_id));
    }

    @Override
    public DepartmentEntity partialUpdate(Long department_id, DepartmentEntity departmentEntity) {
        departmentEntity.setDepartment_id(department_id);

        return departmentRepository.findById(String.valueOf(department_id)).map(existingDepartment -> {
            Optional.ofNullable(departmentEntity.getDepartment_name()).ifPresent(existingDepartment::setDepartment_name);
            Optional.ofNullable(departmentEntity.getDepartment_code()).ifPresent(existingDepartment::setDepartment_code);
            return departmentRepository.save(existingDepartment);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department does not exist"));
    }

    @Override
    public Page<DepartmentEntity> searchDepartmentsByName(String departmentName, Pageable pageable) {
        return departmentRepository.searchByDepartmentName(departmentName, pageable);
    }
}