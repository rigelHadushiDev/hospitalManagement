package hospital.management.demo.repositories;
import hospital.management.demo.domain.entities.AdmissionStateEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AdmissionStateRepository extends CrudRepository<AdmissionStateEntity, String>,
        PagingAndSortingRepository<AdmissionStateEntity, String> {
}
