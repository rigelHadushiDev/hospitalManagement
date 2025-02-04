package hospital.management.demo.mappers.impl;

import hospital.management.demo.domain.dtos.DepartmentDto;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class DepartmentMapperImpl implements Mapper<DepartmentEntity, DepartmentDto> {

    private ModelMapper modelMapper;

    public DepartmentMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public DepartmentDto mapTo(DepartmentEntity departmentEntity) {
        return modelMapper.map(departmentEntity, DepartmentDto.class);
    }

    @Override
    public DepartmentEntity mapFrom(DepartmentDto departmentDto) {
        return modelMapper.map(departmentDto, DepartmentEntity.class);
    }
}
