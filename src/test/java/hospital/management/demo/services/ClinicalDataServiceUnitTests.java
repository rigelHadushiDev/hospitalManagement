package hospital.management.demo.services;

import hospital.management.demo.TestDataUtil;
import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.ClinicalDataEntity;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import hospital.management.demo.repositories.AdmissionStateRepository;
import hospital.management.demo.repositories.ClinicalDataRepository;
import hospital.management.demo.services.impl.ClinicalDataServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/** Unit tests for {@link hospital.management.demo.services.impl.ClinicalDataServiceImpl}. */
@ExtendWith(MockitoExtension.class)
public class ClinicalDataServiceUnitTests {


    @Mock
    private ClinicalDataRepository clinicalDataRepository;


    @Mock
    private AdmissionStateRepository admissionStateRepository;


    @InjectMocks
    private ClinicalDataServiceImpl underTest;

    @Test
public void testThatFindAllClinicalDataReturnsAllClinicalData() {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<ClinicalDataEntity> findAllResult =
            new PageImpl<>(List.of(TestDataUtil.createClinicalData()), pageable, 1);

        when(clinicalDataRepository.findAll(eq(pageable)))
                .thenReturn(findAllResult);
        final Page<ClinicalDataEntity> findAllClinicalDataResult = underTest.findAll(pageable);
        assertThat(findAllClinicalDataResult).isEqualTo(findAllResult);
    }

    // save methods
    @Test
    void testSave_NoAdmissionState() {

        ClinicalDataEntity clinicalDataEntity = new ClinicalDataEntity();
        clinicalDataEntity.setAdmissionStateEntity(null);
        ClinicalDataEntity savedEntity = new ClinicalDataEntity();

        when(clinicalDataRepository.save(clinicalDataEntity)).thenReturn(savedEntity);

        ClinicalDataEntity result = underTest.save(clinicalDataEntity);

        assertNotNull(result);
        verify(clinicalDataRepository, times(1)).save(clinicalDataEntity);
        verifyNoInteractions(admissionStateRepository);
    }

    @Test
    void testSave_AdmissionStateNotFound() {

        AdmissionStateEntity admissionState = TestDataUtil.createAdmissionStateEntity();
        ClinicalDataEntity clinicalData = TestDataUtil.createClinicalData(admissionState);

        when(admissionStateRepository.findById(String.valueOf(1L)))
                .thenReturn(Optional.empty());


        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            underTest.save(clinicalData);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Admission State not found", exception.getReason());
    }

    @Test
    void testSave_ExistingClinicalData() {

        AdmissionStateEntity admissionState = TestDataUtil.createAdmissionStateEntity();
        ClinicalDataEntity clinicalData = TestDataUtil.createClinicalData(admissionState);


        when(admissionStateRepository.findById(String.valueOf(1L)))
                .thenReturn(Optional.of(admissionState));

        when(clinicalDataRepository.findByAdmissionStateEntity(admissionState))
                .thenReturn(Collections.singletonList(new ClinicalDataEntity()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            underTest.save(clinicalData);
        });
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("A ClinicalData with this AdmissionState associated does already exist", exception.getReason());
    }

    @Test
    void testSave_Success_WithAdmissionState() {

        AdmissionStateEntity admissionState = TestDataUtil.createAdmissionStateEntity();
        ClinicalDataEntity clinicalData = TestDataUtil.createClinicalData(admissionState);

        when(admissionStateRepository.findById(String.valueOf(1L)))
                .thenReturn(Optional.of(admissionState));

        when(clinicalDataRepository.findByAdmissionStateEntity(admissionState))
                .thenReturn(Collections.emptyList());

        ClinicalDataEntity savedEntity = new ClinicalDataEntity();
        savedEntity.setAdmissionStateEntity(admissionState);
        when(clinicalDataRepository.save(clinicalData)).thenReturn(savedEntity);

        ClinicalDataEntity result = underTest.save(clinicalData);

        assertNotNull(result);
        verify(admissionStateRepository, times(1)).findById(String.valueOf(1L));
        verify(clinicalDataRepository, times(1)).findByAdmissionStateEntity(admissionState);
        verify(clinicalDataRepository, times(1)).save(clinicalData);
        assertEquals(admissionState, result.getAdmissionStateEntity());
    }

    // Find One methods
    @Test
    public void testFindOne_WithExistingClinicalData() {

        ClinicalDataEntity clinicalData =  TestDataUtil.createClinicalData();
        Long clinicalDataId = clinicalData.getClinical_data_id();

        when(clinicalDataRepository.findById(String.valueOf(clinicalDataId)))
                .thenReturn(Optional.of(clinicalData));

        Optional<ClinicalDataEntity> result = underTest.findOne(clinicalDataId);

        assertTrue(result.isPresent());
        assertEquals(clinicalData, result.get());
        verify(clinicalDataRepository, times(1)).findById(String.valueOf(clinicalDataId));

    }


    @Test
    public void testFindOne_WithNonExistingClinicalData() {


        Long clinicalDataId = 32L;

        when(clinicalDataRepository.findById(String.valueOf(clinicalDataId)))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> underTest.findOne(clinicalDataId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Clinical Data Not Found", exception.getReason());
        verify(clinicalDataRepository, times(1)).findById(String.valueOf(clinicalDataId));
    }

    // update Clinical Data test Methods





}
