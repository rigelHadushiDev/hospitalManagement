package hospital.management.demo.services;

import hospital.management.demo.TestDataUtil;
import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.ClinicalDataEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import hospital.management.demo.repositories.AdmissionStateRepository;
import hospital.management.demo.repositories.ClinicalDataRepository;
import hospital.management.demo.repositories.PatientRepository;
import hospital.management.demo.services.impl.AdmissionStateServiceImpl;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdmissionStateServiceUnitTests {

    @Mock private AdmissionStateRepository admissionStateRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private ClinicalDataRepository clinicalDataRepository;

    @InjectMocks
    private AdmissionStateServiceImpl underTest;

    @Test
    public void testThatFindAllAdmissionStatesReturnsAllAdmissionStates() {
        final Pageable pageable = Mockito.mock(Pageable.class);

        final Page<AdmissionStateEntity> findAllAdmissionStates =
                new PageImpl<>(List.of(TestDataUtil.createAdmissionStateEntity()), pageable, 1);

        when(admissionStateRepository.findAll(eq(pageable))).thenReturn(findAllAdmissionStates);

        final Page<AdmissionStateEntity> listAdmissionStateResult = underTest.findAll(pageable);

        assertThat(listAdmissionStateResult).isEqualTo(findAllAdmissionStates);
    }

    @Test
    public void testThatDeleteAdmissionStateReturnsAdmissionNotFound() {

        Long admissionStateId = null;

        when(admissionStateRepository.findById(String.valueOf(admissionStateId))).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            underTest.delete(admissionStateId);
        });

        verify(admissionStateRepository).findById(String.valueOf(admissionStateId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No admission found", exception.getReason());
    }

    @Test
    public void testThatDeleteAdmissionStateReturnsCannotDeleteAdmissionState() {
        AdmissionStateEntity admissionState = TestDataUtil.createAdmissionStateEntity();
        Long admissionStateID = admissionState.getAdmission_state_id();

        when(admissionStateRepository.findById(String.valueOf(admissionStateID))).thenReturn(Optional.of(admissionState));
        when(clinicalDataRepository.findByAdmissionStateEntity(admissionState))
                .thenReturn(List.of(new ClinicalDataEntity()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            underTest.delete(admissionStateID);
        });

        verify(admissionStateRepository).findById(String.valueOf(admissionStateID));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Cannot delete admission State. There are clinical data associated with it.", exception.getReason());
        verify(admissionStateRepository, never()).delete(any(AdmissionStateEntity.class));
    }

    @Test
    public void testThatDeleteAdmissionStateReturnsTrue() {
        AdmissionStateEntity admissionState = TestDataUtil.createAdmissionStateEntity();
        Long admissionStateID = admissionState.getAdmission_state_id();

        when(admissionStateRepository.findById(String.valueOf(admissionStateID))).thenReturn(Optional.of(admissionState));
        when(clinicalDataRepository.findByAdmissionStateEntity(admissionState))
                .thenReturn(Collections.emptyList());

        underTest.delete(admissionStateID);

        verify(admissionStateRepository, times(1)).delete(admissionState);
    }

    @Test
    public void testThatFindOneAdmisssionStateReturnsAdmissionNotFound() {
        Long admissionStateId = null;

        when(admissionStateRepository.findById(String.valueOf(admissionStateId)))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            underTest.findOne(admissionStateId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Admission State does not exist", exception.getReason());

        verify(admissionStateRepository).findById(String.valueOf(admissionStateId));
    }


    @Test
    public void testThatFindOneAdmissionStateReturnsTrue() {
        AdmissionStateEntity admissionState = TestDataUtil.createAdmissionStateEntity();
        Long admissionStateId = admissionState.getAdmission_state_id();

        when(admissionStateRepository.findById(String.valueOf(admissionStateId)))
                .thenReturn(Optional.of(admissionState));

        Optional<AdmissionStateEntity> result = underTest.findOne(admissionStateId);

        assertThat(result.isPresent());
        assertEquals(admissionState, result.get());
        verify(admissionStateRepository).findById(String.valueOf(admissionStateId));
    }

    @Test
    public void testThatCreateAnAdmissionStateReturnsPatientNotFound() {
        PatientEntity patient = TestDataUtil.createPatientEntity();
        AdmissionStateEntity admissionState = TestDataUtil.createAdmissionStateEntity(patient);

        when(patientRepository.findById(String.valueOf(patient.getPatient_id())))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            underTest.save(admissionState);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Patient not found", exception.getReason());

        verify(patientRepository, times(1)).findById("1");
        verify(admissionStateRepository, never()).save(any());
    }

    @Test
    public void testThatCreateAnAdmissionStateReturnsTrue() {
        PatientEntity patient = TestDataUtil.createPatientEntity();
        AdmissionStateEntity admissionState = TestDataUtil.createAdmissionStateEntity(patient);

        when(patientRepository.findById(String.valueOf(patient.getPatient_id())))
                .thenReturn(Optional.of(patient));
        when(admissionStateRepository.save(admissionState))
                .thenReturn(admissionState);

        AdmissionStateEntity result = underTest.save(admissionState);

        assertNotNull(result);
        assertEquals(admissionState, result);
        verify(admissionStateRepository, times(1)).save(admissionState);
    }

    @Test
    public void testThatUpdateAnAdmissionStateReturnsAdmissionStateNotFound() {
        Long admissionStateId = 100L;
        AdmissionStateEntity updateAdmissionState = TestDataUtil.createAdmissionStateEntity();

        when(admissionStateRepository.findById(String.valueOf(admissionStateId)))
            .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            underTest.partialUpdate(admissionStateId, updateAdmissionState);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Admission State does not exist", exception.getReason());

        verify(admissionStateRepository, times(1)).findById(String.valueOf(admissionStateId));
        verify(admissionStateRepository, never()).save(any());
    }

    @Test
    public void testThatPartialUpdateAnAdmissionStateReturnsTrue() {
        AdmissionStateEntity updatedAdmissionState = TestDataUtil.createAdmissionStateEntityB();
        Long admissionStateId = updatedAdmissionState.getAdmission_state_id();

        AdmissionStateEntity existingAdmissionState = TestDataUtil.createAdmissionStateEntity();
        existingAdmissionState.setAdmission_state_id(updatedAdmissionState.getAdmission_state_id());
        existingAdmissionState.setCause(updatedAdmissionState.getCause());
        existingAdmissionState.setReason(updatedAdmissionState.getReason());

        when(admissionStateRepository.findById(String.valueOf(admissionStateId)))
                .thenReturn(Optional.of(existingAdmissionState));
        when(admissionStateRepository.save(any(AdmissionStateEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AdmissionStateEntity result = underTest.partialUpdate(admissionStateId, updatedAdmissionState);

        assertEquals(admissionStateId, updatedAdmissionState.getAdmission_state_id());
        assertEquals("Routine Checkup", updatedAdmissionState.getCause());
        assertEquals(AdmissionStateEntity.Reason.HEALTHY, updatedAdmissionState.getReason());

        verify(admissionStateRepository, times(1)).findById(String.valueOf(admissionStateId));
        verify(admissionStateRepository, times(1)).save(existingAdmissionState);

    }

    @Test
    public void testThatDischargeAdmissionReturnsAdmissionNotFound() {

        Long admissionStateId = 100L;

        when(admissionStateRepository.findById(String.valueOf(admissionStateId)))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            underTest.dischargeAdmission(admissionStateId, AdmissionStateEntity.Reason.HEALTHY);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No admission found", exception.getReason());
    }

    @Test
    public void testThatDischargeAdmissionReturnsTrue() {
        AdmissionStateEntity admissionState = TestDataUtil.createAdmissionStateEntity();
        Long admissionStateId = admissionState.getAdmission_state_id();

        AdmissionStateEntity.Reason dischargeReason = AdmissionStateEntity.Reason.HEALTHY;

        AdmissionStateEntity exitingAdmissionState = TestDataUtil.createAdmissionStateEntity();
        exitingAdmissionState.setAdmission_state_id(admissionStateId);

        when(admissionStateRepository.findById(String.valueOf(admissionStateId)))
                .thenReturn(Optional.of(exitingAdmissionState));
        when(admissionStateRepository.save(any(AdmissionStateEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AdmissionStateEntity dischargedAdmissionState = underTest.dischargeAdmission(admissionStateId, dischargeReason);

        assertTrue(dischargedAdmissionState.getDischarge());
        assertEquals(dischargeReason, dischargedAdmissionState.getReason());
        assertNotNull(dischargedAdmissionState.getExiting_date());

        verify(admissionStateRepository, times(1)).findById(String.valueOf(admissionStateId));
        verify(admissionStateRepository, times(1)).save(dischargedAdmissionState);

    }


    @Test
    public void testThatSearchAdmissionStateReturnsTrue() {
        String patientId = "123";
        Pageable pageable = PageRequest.of(0, 10);

        PatientEntity patient = TestDataUtil.createPatientEntity();
        patient.setPatient_id(123L);

        AdmissionStateEntity admissionStates = TestDataUtil.createAdmissionStateEntity();

        Page<AdmissionStateEntity> expectedPage = new PageImpl<>(List.of(admissionStates), pageable, 1);

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));
        when(admissionStateRepository.findByPatientId(Long.valueOf(patientId), pageable))
                .thenReturn(expectedPage);

        Page<AdmissionStateEntity> result = underTest.searchByPatientId(patientId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(admissionStates, result.getContent().get(0));
        verify(patientRepository, times(1)).findById(patientId);
        verify(admissionStateRepository, times(1)).findByPatientId(123L, pageable);
    }

    @Test
    public void testThatSearchAdmissionStateReturnsPatientNotFound() {
        String patientId = "123";
        Pageable pageable = PageRequest.of(0, 10);

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            underTest.searchByPatientId(patientId, pageable);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No patient found with this ID", exception.getReason());
        verify(patientRepository, times(1)).findById(patientId);
        verify(admissionStateRepository, times(0)).findByPatientId(anyLong(), any());
    }

}
