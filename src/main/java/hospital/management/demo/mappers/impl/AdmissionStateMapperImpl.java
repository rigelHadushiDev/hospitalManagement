package hospital.management.demo.mappers.impl;


import hospital.management.demo.domain.dtos.AdmissionStateDto;
import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AdmissionStateMapperImpl implements Mapper<AdmissionStateEntity, AdmissionStateDto> {

    private ModelMapper modelMapper;

    public AdmissionStateMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AdmissionStateDto mapTo(AdmissionStateEntity admissionStateEntity) {
        return modelMapper.map(admissionStateEntity, AdmissionStateDto.class);
    }

    @Override
    public AdmissionStateEntity mapFrom(AdmissionStateDto admissionStateDto) {
        return modelMapper.map(admissionStateDto, AdmissionStateEntity.class);
    }
}