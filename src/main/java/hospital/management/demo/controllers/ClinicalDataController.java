package hospital.management.demo.controllers;
import hospital.management.demo.domain.dtos.ClinicalDataDto;
import hospital.management.demo.domain.entities.ClinicalDataEntity;
import hospital.management.demo.mappers.Mapper;
import hospital.management.demo.services.ClinicalDataService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("clinicalData")
public class ClinicalDataController {

    private ClinicalDataService clinicalDataService;

    private Mapper<ClinicalDataEntity, ClinicalDataDto> clinicalDataMapper;

    public ClinicalDataController(ClinicalDataService clinicalDataService,
                                  Mapper<ClinicalDataEntity, ClinicalDataDto> clinicalDataMapper) {
        this.clinicalDataService = clinicalDataService;
        this.clinicalDataMapper = clinicalDataMapper;
    }

    @PostMapping
    public ResponseEntity<ClinicalDataDto> createClinicalData(@RequestBody ClinicalDataDto clinicalDataDto) {
        ClinicalDataEntity clinicalDataEntity = clinicalDataMapper.mapFrom(clinicalDataDto);
        ClinicalDataEntity savedClinicalDataEntity = clinicalDataService.save(clinicalDataEntity);
        return new ResponseEntity<>(clinicalDataMapper.mapTo(savedClinicalDataEntity), HttpStatus.CREATED);
    }


    @GetMapping
    public Page<ClinicalDataDto> listClinicalData(Pageable pageable) {
        Page<ClinicalDataEntity> clinicalData = clinicalDataService.findAll(pageable);
        return clinicalData.map(clinicalDataMapper::mapTo);
    }

    @GetMapping("/{clinical_data_id}")
    public Optional<ResponseEntity<ClinicalDataDto>> getClinicalData(@PathVariable("clinical_data_id") Long clinical_data_id) {
        Optional<ClinicalDataEntity> foundClinicalData = clinicalDataService.findOne(clinical_data_id);
        return foundClinicalData.map(ClinicalDataEntity -> {
            ClinicalDataDto clinicalDataDto = clinicalDataMapper.mapTo(ClinicalDataEntity);
            return new ResponseEntity<>(clinicalDataDto, HttpStatus.OK);
        });
    }

    @PatchMapping(path = "/{clinical_data_id}")
    public ResponseEntity<ClinicalDataDto> updateClinicalData(
            @PathVariable("clinical_data_id") Long clinical_data_id,
            @RequestBody ClinicalDataDto clinicalDataDto
    ) {
        if(!clinicalDataService.isExists(clinical_data_id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ClinicalDataEntity clinicalDataEntity = clinicalDataMapper.mapFrom(clinicalDataDto);
        ClinicalDataEntity updatedClinicalData = clinicalDataService.fullUpdate(clinical_data_id, clinicalDataEntity);
        return new ResponseEntity<>(
                clinicalDataMapper.mapTo(updatedClinicalData),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/{clinical_data_id}")
    public ResponseEntity deleteClinicalData(@PathVariable("clinical_data_id") Long clinical_data_id) {
        clinicalDataService.delete(clinical_data_id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/searchByPatient")
    public Page<ClinicalDataDto> searchClinicalDataByPatient(
            @RequestParam("patientId") String patientId,
            Pageable pageable) {

        Page<ClinicalDataEntity> clinicalData = clinicalDataService.searchByPatientId(patientId, pageable);
        return clinicalData.map(clinicalDataMapper::mapTo);
    }

}
