package hospital.management.demo.controllers;

import hospital.management.demo.domain.dtos.PatientDto;
import hospital.management.demo.domain.entities.PatientEntity;
import hospital.management.demo.mappers.Mapper;
import hospital.management.demo.services.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController()
@RequestMapping("/patient")
public class PatientController {

    private PatientService patientService;

    private Mapper<PatientEntity, PatientDto> patientMapper;

    public PatientController(PatientService patientService, Mapper<PatientEntity, PatientDto> patientMapper) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }

    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@RequestBody PatientDto patientDto) {
        PatientEntity patientEntity = patientMapper.mapFrom(patientDto);
        PatientEntity savedPatientEntity = patientService.save(patientEntity);
        return new ResponseEntity<>(patientMapper.mapTo(savedPatientEntity), HttpStatus.CREATED);
    }

    @GetMapping("/searchPatient")
    public Page<PatientDto> searchPatientsByFullName( @RequestParam("fullName") String fullName, Pageable pageable) {
        Page<PatientEntity> patients = patientService.searchPatientsByFullName(fullName, pageable);
        return patients.map(patientMapper::mapTo);
    }

    @GetMapping
    public Page<PatientDto> listPatient(Pageable pageable) {
        Page<PatientEntity> patients = patientService.findAll(pageable);
        return patients.map(patientMapper::mapTo);
    }

    @GetMapping(path = "/{patient_id}")
    public Optional<ResponseEntity<PatientDto>> getPatient(@PathVariable("patient_id") Long patient_id) {
        Optional<PatientEntity> foundPatient = patientService.findOne(patient_id);
        return foundPatient.map(patientEntity -> {
            PatientDto patientDto = patientMapper.mapTo(patientEntity);
            return new ResponseEntity<>(patientDto, HttpStatus.OK);
        });
    }

    @DeleteMapping(path = "/{patient_id}")
    public ResponseEntity deletePatient(@PathVariable("patient_id") Long patient_id) {
        patientService.delete(patient_id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{patient_id}")
    public ResponseEntity<PatientDto> partialUpdate(
            @PathVariable("patient_id") Long patient_id,
            @RequestBody PatientDto patientDto
    ) {
        if(!patientService.isExists(patient_id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PatientEntity patientEntity = patientMapper.mapFrom(patientDto);
        PatientEntity updatedPatient = patientService.partialUpdate(patient_id, patientEntity);
        return new ResponseEntity<>(
                patientMapper.mapTo(updatedPatient),
                HttpStatus.OK);
    }




}

