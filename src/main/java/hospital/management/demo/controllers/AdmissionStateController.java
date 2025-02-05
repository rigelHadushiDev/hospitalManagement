package hospital.management.demo.controllers;

import hospital.management.demo.domain.dtos.AdmissionStateDto;

import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.mappers.Mapper;
import hospital.management.demo.services.AdmissionStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
