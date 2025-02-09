package hospital.management.demo.services.impl;

import hospital.management.demo.domain.entities.ClinicalDataEntity;
import hospital.management.demo.repositories.ClinicalDataRepository;
import hospital.management.demo.services.ClinicalDataService;
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
public class ClinicalDataServiceImpl implements ClinicalDataService {

    private ClinicalDataRepository clinicalDataRepository;

    public ClinicalDataServiceImpl(ClinicalDataRepository clinicalDataRepository) {
        this.clinicalDataRepository = clinicalDataRepository;
    }


    @Override
    public ClinicalDataEntity save( ClinicalDataEntity clinicalDataEntity) {
        return clinicalDataRepository.save(clinicalDataEntity);
    }


    @Override
    public Page<ClinicalDataEntity> findAll(Pageable pageable) {
        return clinicalDataRepository.findAll(pageable);
    }

    @Override
    public Optional<ClinicalDataEntity> findOne(Long clinical_data_id) {
        return clinicalDataRepository.findById(String.valueOf(clinical_data_id));
    }

    @Override
    public ClinicalDataEntity fullUpdate(Long clinical_data_id, ClinicalDataEntity clinicalDataEntity) {
        clinicalDataEntity.setClinical_data_id(clinical_data_id);

        return clinicalDataRepository.findById(String.valueOf(clinical_data_id)).map(existingClinicalData -> {
            Optional.ofNullable(clinicalDataEntity.getClinical_record()).ifPresent(existingClinicalData::setClinical_record);
            return clinicalDataRepository.save(existingClinicalData);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No clinical data is found"));
    }

    @Override
    public boolean isExists(Long clinical_data_id) {
        return clinicalDataRepository.existsById(String.valueOf(clinical_data_id));
    }

    @Override
    public void delete(Long clinical_data_id) {
        ClinicalDataEntity clinicalRecord = clinicalDataRepository.findById(String.valueOf(clinical_data_id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clinical Data not found."));

        clinicalDataRepository.deleteById(String.valueOf(clinical_data_id));
    }


    @Override
    public Page<ClinicalDataEntity> searchByPatientId(String patientId, Pageable pageable) {
        return clinicalDataRepository.findByPatientId(Long.valueOf(patientId), pageable);
    }
}
