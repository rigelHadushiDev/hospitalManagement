package hospital.management.demo.controllers;

import hospital.management.demo.domain.dtos.AdmissionStateDto;
import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.mappers.Mapper;
import hospital.management.demo.services.AdmissionStateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping("/admissionState")
public class AdmissionStateController {

    private AdmissionStateService admissionStateService;
    private Mapper<AdmissionStateEntity, AdmissionStateDto> admissionStateMapper;

    public AdmissionStateController(AdmissionStateService admissionStateService, Mapper<AdmissionStateEntity, AdmissionStateDto> admissionStateMapper) {
        this.admissionStateService = admissionStateService;
        this.admissionStateMapper = admissionStateMapper;
    }

    @PostMapping
    public ResponseEntity<AdmissionStateDto> createAdmissionState(@RequestBody AdmissionStateDto admissionStateDto) {
        AdmissionStateEntity admissionStateEntity = admissionStateMapper.mapFrom(admissionStateDto);
        AdmissionStateEntity savedAdmissionStateEntity = admissionStateService.save(admissionStateEntity);
        return new ResponseEntity<>(admissionStateMapper.mapTo(savedAdmissionStateEntity), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<AdmissionStateDto> listAdmissionState(Pageable pageable) {
        Page<AdmissionStateEntity> admissions = admissionStateService.findAll(pageable);
        return admissions.map(admissionStateMapper::mapTo);
    }

    @GetMapping(path = "/{admission_state_id}")
    public ResponseEntity<AdmissionStateDto> getAdmissionState(@PathVariable("admission_state_id") Long admission_state_id) {
        Optional<AdmissionStateEntity> foundAdmission = admissionStateService.findOne(admission_state_id);
        return foundAdmission.map(admissionStateEntity -> {
            AdmissionStateDto admissionStateDto = admissionStateMapper.mapTo(admissionStateEntity);
            return new ResponseEntity<>(admissionStateDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @DeleteMapping(path = "/{admission_state_id}")
    public ResponseEntity deleteAdmission(@PathVariable("admission_state_id") Long admission_state_id) {
        admissionStateService.delete(admission_state_id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{admission_state_id}")
    public ResponseEntity<AdmissionStateDto> partialUpdate(
            @PathVariable("admission_state_id") Long admission_state_id,
            @RequestBody AdmissionStateDto admissionStateDto
    ) {
        if(!admissionStateService.isExists(admission_state_id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AdmissionStateEntity admissionStateEntity = admissionStateMapper.mapFrom(admissionStateDto);
        AdmissionStateEntity updatedAdmission = admissionStateService.partialUpdate(admission_state_id, admissionStateEntity);
        return new ResponseEntity<>(
                admissionStateMapper.mapTo(updatedAdmission),
                HttpStatus.OK);
    }


}
