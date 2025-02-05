package hospital.management.demo.services;

import hospital.management.demo.domain.dtos.ClinicalDataDto;
import hospital.management.demo.domain.entities.ClinicalDataEntity;
import hospital.management.demo.domain.entities.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClinicalDataService {

    ClinicalDataEntity save(ClinicalDataEntity clinicalDataEntity);

    Page<ClinicalDataEntity> findAll(Pageable pageable);

    List<ClinicalDataEntity> findAll();

    Optional<ClinicalDataEntity> findOne(Long clinicalDataId);

    boolean isExists(Long clinicalDataId);

    ClinicalDataEntity fullUpdate(Long clinicalDataId, ClinicalDataEntity clinicalDataEntity);

    void delete(Long clinicalDataId);
}
