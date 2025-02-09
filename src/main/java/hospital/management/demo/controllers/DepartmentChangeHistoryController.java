package hospital.management.demo.controllers;

import hospital.management.demo.domain.dtos.ClinicalDataDto;
import hospital.management.demo.domain.dtos.DepartmentChangeHistoryDto;
import hospital.management.demo.domain.entities.ClinicalDataEntity;
import hospital.management.demo.domain.entities.DepartmentChangeHistoryEntity;
import hospital.management.demo.mappers.Mapper;
import hospital.management.demo.services.ClinicalDataService;
import hospital.management.demo.services.DepartmentChangeHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("departmentChangeHistory")
public class DepartmentChangeHistoryController {


    private DepartmentChangeHistoryService departmentChangeHistoryService;

    private Mapper<DepartmentChangeHistoryEntity, DepartmentChangeHistoryDto> departmentChangeHistorMapper;

    public DepartmentChangeHistoryController(DepartmentChangeHistoryService departmentChangeHistoryService,
                                             Mapper<DepartmentChangeHistoryEntity,
                                                     DepartmentChangeHistoryDto> departmentChangeHistorMapper) {
        this.departmentChangeHistoryService = departmentChangeHistoryService;
        this.departmentChangeHistorMapper = departmentChangeHistorMapper;
    }

    @GetMapping("/searchByPatient")
    public Page<DepartmentChangeHistoryDto> searchClinicalDataByPatient(
            @RequestParam("patientId") String patientId,
            Pageable pageable) {

        Page<DepartmentChangeHistoryEntity> departmentChangeHistory = departmentChangeHistoryService.searchByPatientIdWithPagination(patientId, pageable);
        return departmentChangeHistory.map(departmentChangeHistorMapper::mapTo);
    }
}
