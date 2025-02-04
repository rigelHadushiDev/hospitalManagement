package hospital.management.demo.repositories;


import hospital.management.demo.domain.entities.DepartmentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, String>,
        PagingAndSortingRepository<DepartmentEntity, String> {
}

