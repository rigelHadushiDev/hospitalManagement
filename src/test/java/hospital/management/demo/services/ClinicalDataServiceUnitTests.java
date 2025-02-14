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

import java.util.Arrays;
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

    @Test
    void testPartialUpdate_updatesClinicalRecordOnly() {

        AdmissionStateEntity existingAdmissionState = TestDataUtil.createAdmissionStateEntity();
        ClinicalDataEntity existingClinicalData = TestDataUtil.createClinicalData(existingAdmissionState);
        existingClinicalData.setClinical_record("Old record");
        Long clinicalDataId = existingClinicalData.getClinical_data_id();


        ClinicalDataEntity updateEntity = TestDataUtil.createClinicalDataB();
        updateEntity.setClinical_record("Updated record");

        when(clinicalDataRepository.findById(String.valueOf(clinicalDataId)))
                .thenReturn(Optional.of(existingClinicalData));
        when(clinicalDataRepository.save(any(ClinicalDataEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        ClinicalDataEntity result = underTest.partialUpdate(clinicalDataId, updateEntity);


        assertEquals("Updated record", result.getClinical_record());
        assertNotNull(result.getAdmissionStateEntity());
        assertEquals(1L, result.getAdmissionStateEntity().getAdmission_state_id());
        verify(clinicalDataRepository, never()).findByAdmissionStateEntity(any());
        verify(admissionStateRepository, never()).findById(any());
    }

    @Test
    void testPartialUpdate_clinicalDataNotFound() {
        ClinicalDataEntity updateEntity =  TestDataUtil.createClinicalData();
        Long clinicalDataId = updateEntity.getClinical_data_id();
        updateEntity.setClinical_record("Updated record");

        when(clinicalDataRepository.findById(String.valueOf(clinicalDataId)))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> underTest.partialUpdate(clinicalDataId, updateEntity));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No clinical data is found", exception.getReason());
    }

    @Test
    void testPartialUpdate_updatesAdmissionState() {


        AdmissionStateEntity oldAdmissionState = TestDataUtil.createAdmissionStateEntity();
        ClinicalDataEntity existingClinicalData =  TestDataUtil.createClinicalData(oldAdmissionState);
        Long clinicalDataId = existingClinicalData.getClinical_data_id();
        existingClinicalData.setClinical_record("Old record");


        AdmissionStateEntity newAdmissionState = TestDataUtil.createAdmissionStateEntityB();
        ClinicalDataEntity updateEntity = TestDataUtil.createClinicalData(newAdmissionState);
        updateEntity.setClinical_record("Updated record with new admission state");

        when(clinicalDataRepository.findById(String.valueOf(clinicalDataId)))
                .thenReturn(Optional.of(existingClinicalData));
        when(clinicalDataRepository.findByAdmissionStateEntity(newAdmissionState))
                .thenReturn(Collections.emptyList());
        when(admissionStateRepository.findById(String.valueOf(2L)))
                .thenReturn(Optional.of(newAdmissionState));
        when(clinicalDataRepository.save(any(ClinicalDataEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ClinicalDataEntity result = underTest.partialUpdate(clinicalDataId, updateEntity);

        assertEquals("Updated record with new admission state", result.getClinical_record());
        assertNotNull(result.getAdmissionStateEntity());
        assertEquals(2L, result.getAdmissionStateEntity().getAdmission_state_id());
    }

    @Test
    void testPartialUpdate_throwsConflictForAdmissionState() {

        AdmissionStateEntity oldAdmissionState = TestDataUtil.createAdmissionStateEntity();
        ClinicalDataEntity existingClinicalData = TestDataUtil.createClinicalData(oldAdmissionState);
        Long clinicalDataId = existingClinicalData.getClinical_data_id();
        existingClinicalData.setClinical_record("Old record");

        AdmissionStateEntity newAdmissionState = TestDataUtil.createAdmissionStateEntityB();
        ClinicalDataEntity updateEntity = TestDataUtil.createClinicalData(newAdmissionState);
        updateEntity.setClinical_record("Update with conflicting admission state");

        when(clinicalDataRepository.findById(String.valueOf(clinicalDataId)))
                .thenReturn(Optional.of(existingClinicalData));

        ClinicalDataEntity conflictingClinicalData = TestDataUtil.createClinicalDataB(newAdmissionState);
        when(clinicalDataRepository.findByAdmissionStateEntity(newAdmissionState))
                .thenReturn(List.of(conflictingClinicalData));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> underTest.partialUpdate(clinicalDataId, updateEntity));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("This admission state record already has a clinical data associated.", exception.getReason());
    }

    @Test
    void testPartialUpdate_admissionStateNotFound() {

        AdmissionStateEntity oldAdmissionState = TestDataUtil.createAdmissionStateEntity();
        ClinicalDataEntity existingClinicalData = TestDataUtil.createClinicalData(oldAdmissionState);
        Long clinicalDataId = existingClinicalData.getClinical_data_id();
        existingClinicalData.setClinical_record("Old record");

        AdmissionStateEntity newAdmissionState = TestDataUtil.createAdmissionStateEntityB();
        ClinicalDataEntity updateEntity = TestDataUtil.createClinicalData(newAdmissionState);
        updateEntity.setClinical_record("Update with new admission state not found");

        when(clinicalDataRepository.findById(String.valueOf(clinicalDataId)))
                .thenReturn(Optional.of(existingClinicalData));
        when(clinicalDataRepository.findByAdmissionStateEntity(newAdmissionState))
                .thenReturn(Collections.emptyList());
        when(admissionStateRepository.findById(String.valueOf(2L)))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> underTest.partialUpdate(clinicalDataId, updateEntity));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("AdmissionState Not Found", exception.getReason());
    }

    @Test
    void testDelete_success() {

        ClinicalDataEntity clinicalData = TestDataUtil.createClinicalData();
        Long clinicalDataId = clinicalData.getClinical_data_id();

        when(clinicalDataRepository.findById(String.valueOf(clinicalDataId)))
                .thenReturn(Optional.of(clinicalData));

        underTest.delete(clinicalDataId);

        verify(clinicalDataRepository).findById(String.valueOf(clinicalDataId));
        verify(clinicalDataRepository).deleteById(String.valueOf(clinicalDataId));
    }

    @Test
    void testDelete_notFound() {

        Long clinicalDataId = 1L;
        when(clinicalDataRepository.findById(String.valueOf(clinicalDataId)))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> underTest.delete(clinicalDataId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Clinical Data not found.", exception.getReason());

        verify(clinicalDataRepository, never()).deleteById(anyString());
    }

    @Test
    void testSearchByPatientId() {

        String patientId = "123";
        Pageable pageable = PageRequest.of(0, 10);
        ClinicalDataEntity entity = TestDataUtil.createClinicalData();
        List<ClinicalDataEntity> clinicalDataList = Arrays.asList(entity);
        Page<ClinicalDataEntity> expectedPage = new PageImpl<>(clinicalDataList, pageable, clinicalDataList.size());

        when(clinicalDataRepository.findByPatientId(Long.valueOf(patientId), pageable))
                .thenReturn(expectedPage);

        Page<ClinicalDataEntity> resultPage = underTest.searchByPatientId(patientId, pageable);

        assertEquals(expectedPage, resultPage);
        verify(clinicalDataRepository).findByPatientId(Long.valueOf(patientId), pageable);
    }
}
