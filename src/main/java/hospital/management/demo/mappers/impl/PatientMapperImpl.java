package hospital.management.demo.mappers.impl;

import hospital.management.demo.domain.dtos.PatientDto;
import hospital.management.demo.domain.entities.PatientEntity;
import hospital.management.demo.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PatientMapperImpl implements Mapper<PatientEntity, PatientDto> {

    private ModelMapper modelMapper;

    public PatientMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PatientDto mapTo(PatientEntity patientEntity) {
        return modelMapper.map(patientEntity, PatientDto.class);
    }

    @Override
    public PatientEntity mapFrom(PatientDto patientDto) {
        return modelMapper.map(patientDto, PatientEntity.class);
    }

}