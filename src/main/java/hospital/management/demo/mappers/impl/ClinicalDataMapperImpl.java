package hospital.management.demo.mappers.impl;

import hospital.management.demo.domain.dtos.ClinicalDataDto;
import hospital.management.demo.domain.entities.ClinicalDataEntity;
import hospital.management.demo.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ClinicalDataMapperImpl implements Mapper<ClinicalDataEntity, ClinicalDataDto> {

    private ModelMapper modelMapper;

    public ClinicalDataMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ClinicalDataDto mapTo(ClinicalDataEntity clinicalDataEntity) {
        return modelMapper.map(clinicalDataEntity, ClinicalDataDto.class);
    }

    @Override
    public ClinicalDataEntity mapFrom(ClinicalDataDto clinicalDataDto) {
        return modelMapper.map(clinicalDataDto, ClinicalDataEntity.class);
    }





}
