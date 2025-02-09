package hospital.management.demo.services;

import hospital.management.demo.domain.entities.DepartmentChangeHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentChangeHistoryService {

    Page<DepartmentChangeHistoryEntity> searchByPatientIdWithPagination(String patientId, Pageable pageable);
}
