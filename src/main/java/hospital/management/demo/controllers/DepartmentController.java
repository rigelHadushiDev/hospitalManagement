package hospital.management.demo.controllers;

import hospital.management.demo.domain.dtos.DepartmentDto;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.mappers.Mapper;
import hospital.management.demo.services.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping("/department")
public class DepartmentController {

    private DepartmentService departmentService;

    private Mapper<DepartmentEntity, DepartmentDto> departmentMapper;

    public DepartmentController(DepartmentService departmentService,
                                Mapper<DepartmentEntity, DepartmentDto> departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto) {
        DepartmentEntity departmentEntity = departmentMapper.mapFrom(departmentDto);
        DepartmentEntity savedDepartmentEntity = departmentService.save(departmentEntity);
        return new ResponseEntity<>(departmentMapper.mapTo(savedDepartmentEntity), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<DepartmentDto> listDepartments(Pageable pageable) {
        Page<DepartmentEntity> departments = departmentService.findAll(pageable);
        return departments.map(departmentMapper::mapTo);
    }


    @GetMapping(path = "/{department_id}")
    public Optional<ResponseEntity<DepartmentDto>> getDepartment(@PathVariable("department_id") Long department_id) {
        Optional<DepartmentEntity> foundDepartment = departmentService.findOne(department_id);
        return foundDepartment.map(departmentEntity -> {
            DepartmentDto departmentDto = departmentMapper.mapTo(departmentEntity);
            return new ResponseEntity<>(departmentDto, HttpStatus.OK);
        });
    }



    @DeleteMapping(path = "/{department_id}")
    public ResponseEntity deleteDepartment(@PathVariable("department_id") Long department_id) {
        departmentService.delete(department_id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{department_id}")
    public ResponseEntity<DepartmentDto> partialUpdateDepartment(
            @PathVariable("department_id") Long department_id,
            @RequestBody DepartmentDto departmentDto
    ) {
        DepartmentEntity departmentEntity = departmentMapper.mapFrom(departmentDto);
        DepartmentEntity updatedDepartment = departmentService.partialUpdate(department_id, departmentEntity);
        return new ResponseEntity<>(
                departmentMapper.mapTo(updatedDepartment),
                HttpStatus.OK);
    }

    @GetMapping("/searchDepartment")
    public Page<DepartmentDto> searchDepartmentsByName(
            @RequestParam("departmentName") String departmentName,
            Pageable pageable) {
        Page<DepartmentEntity> departments = departmentService.searchDepartmentsByName(departmentName,pageable );
        return departments.map(departmentMapper::mapTo);
    }
}
