package hospital.management.demo.repositories;

import hospital.management.demo.domain.entities.PatientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PatientRepository extends CrudRepository<PatientEntity, String>,
        PagingAndSortingRepository<PatientEntity, String> {
}
