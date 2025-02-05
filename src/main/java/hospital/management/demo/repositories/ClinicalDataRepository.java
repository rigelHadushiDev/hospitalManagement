package hospital.management.demo.repositories;

import hospital.management.demo.domain.entities.ClinicalDataEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ClinicalDataRepository extends CrudRepository<ClinicalDataEntity, String>,
        PagingAndSortingRepository<ClinicalDataEntity, String> {
}
