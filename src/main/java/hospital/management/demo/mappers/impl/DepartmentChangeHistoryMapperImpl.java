package hospital.management.demo.mappers.impl;

import hospital.management.demo.domain.dtos.ClinicalDataDto;
import hospital.management.demo.domain.dtos.DepartmentChangeHistoryDto;
import hospital.management.demo.domain.entities.ClinicalDataEntity;
import hospital.management.demo.domain.entities.DepartmentChangeHistoryEntity;
import hospital.management.demo.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DepartmentChangeHistoryMapperImpl implements Mapper<DepartmentChangeHistoryEntity, DepartmentChangeHistoryDto> {

    private ModelMapper modelMapper;

    public DepartmentChangeHistoryMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public DepartmentChangeHistoryDto mapTo(DepartmentChangeHistoryEntity departmentChangeHistoryEntity) {
        return modelMapper.map(departmentChangeHistoryEntity, DepartmentChangeHistoryDto.class);
    }

    @Override
    public DepartmentChangeHistoryEntity mapFrom(DepartmentChangeHistoryDto departmentChangeHistoryDto) {
        return modelMapper.map(departmentChangeHistoryDto, DepartmentChangeHistoryEntity.class);
    }

}


