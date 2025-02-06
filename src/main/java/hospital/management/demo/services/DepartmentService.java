package hospital.management.demo.services;
import hospital.management.demo.domain.entities.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    List<DepartmentEntity> findAll();

    Page<DepartmentEntity> findAll(Pageable pageable);

    DepartmentEntity save(DepartmentEntity departmentEntity);

    void delete(Long department_id);

    Optional<DepartmentEntity> findOne(Long department_id);

    boolean isExists(Long department_id);

    DepartmentEntity partialUpdate(Long department_id, DepartmentEntity departmentEntity);

}
