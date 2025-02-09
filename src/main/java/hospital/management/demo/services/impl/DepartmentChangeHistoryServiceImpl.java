package hospital.management.demo.services.impl;


import hospital.management.demo.domain.entities.DepartmentChangeHistoryEntity;
import hospital.management.demo.repositories.DepartmentChangeHistoryRepository;
import hospital.management.demo.services.DepartmentChangeHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DepartmentChangeHistoryServiceImpl implements DepartmentChangeHistoryService {

    private DepartmentChangeHistoryRepository departmentChangeHistoryRepository;

    DepartmentChangeHistoryServiceImpl(DepartmentChangeHistoryRepository departmentChangeHistoryRepository) {
        this.departmentChangeHistoryRepository = departmentChangeHistoryRepository;
    }

    @Override
    public Page<DepartmentChangeHistoryEntity> searchByPatientIdWithPagination(String patientId, Pageable pageable) {

        return departmentChangeHistoryRepository.searchByPatientIdWithPagination(Long.valueOf(patientId), pageable);
    }
}
