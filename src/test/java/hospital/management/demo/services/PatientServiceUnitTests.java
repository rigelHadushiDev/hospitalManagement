package hospital.management.demo.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import hospital.management.demo.TestDataUtil;
import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.DepartmentChangeHistoryEntity;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import hospital.management.demo.repositories.*;
import hospital.management.demo.services.impl.PatientServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/** Unit tests for {@link hospital.management.demo.services.impl.PatientServiceImpl}. */
@Slf4j
@ExtendWith(MockitoExtension.class)
public class PatientServiceUnitTests {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentChangeHistoryRepository departmentChangeHistoryRepository;

    @Mock
    private AdmissionStateRepository admissionStateRepository;

    @Mock
    private ClinicalDataRepository clinicalDataRepository;

    @InjectMocks
    private PatientServiceImpl underTest;

    // findAll method
    @Test
    public void testThatFindAllPatientsReturnsFromRepository() {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<PatientEntity> findAllResult =
                new PageImpl<>(List.of(TestDataUtil.createPatient()),
                        pageable, 1);

        when(patientRepository.findAll(eq(pageable))).thenReturn(findAllResult);
        final Page<PatientEntity> findAllPatientsResults = underTest.findAll(pageable);
        assertThat(findAllPatientsResults).isEqualTo(findAllResult);
    }

    // save methods
    @Test
    void testSave_WithExistingPatient() {

        DepartmentEntity  department = TestDataUtil.createDepartmentEntity();
        PatientEntity patient = TestDataUtil.createPatient(department);

        // Mocking department repository
        when(departmentRepository.findById("1")).thenReturn(Optional.of(department));
        when(patientRepository.save(patient)).thenReturn(patient);

        PatientEntity savedPatient = underTest.save(patient);

        assertNotNull(savedPatient);
        assertEquals("Surgery Department", savedPatient.getDepartmentEntity().getDepartment_name());
        assertEquals("263HJU6H7", savedPatient.getDepartmentEntity().getDepartment_code());
        verify(departmentRepository, times(1)).findById("1");
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void testSave_WithNonExistingPatient() {

        DepartmentEntity department = TestDataUtil.createDepartmentEntity();
        PatientEntity patient = TestDataUtil.createPatient(department);

        when(departmentRepository.findById("1")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> underTest.save(patient));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Department not found", exception.getReason());

        verify(departmentRepository, times(1)).findById("1");
        verify(patientRepository, never()).save(any());
    }

    // Find One methods
    @Test
    public void testFindOne_WithExistingPatient() {

        PatientEntity patient =  TestDataUtil.createPatient();
        Long patId = patient.getPatient_id();

        when(patientRepository.findById(String.valueOf(patId)))
                .thenReturn(Optional.of(patient));

        Optional<PatientEntity> result = underTest.findOne(patId);

        assertTrue(result.isPresent());
        assertEquals(patient, result.get());
        verify(patientRepository, times(1)).findById(String.valueOf(patId));

    }


    @Test
    public void testFindOne_WithNonExistingPatient() {


        Long patId = 32L;

        when(patientRepository.findById(String.valueOf(patId)))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> underTest.findOne(patId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Patient does not exist", exception.getReason());
        verify(patientRepository, times(1)).findById(String.valueOf(patId));
    }

    // update methods

    @Test
    public void testPartialUpdate_Success() {

        DepartmentEntity department = TestDataUtil.createDepartmentEntity();
        PatientEntity updatePatient = TestDataUtil.createPatient(department);
        Long patId = updatePatient.getPatient_id();

        DepartmentEntity departmentB = TestDataUtil.createDepartmentEntityB();
        PatientEntity existingPatient = TestDataUtil.createPatientB(departmentB);

        existingPatient.setPatient_id(patId);
        existingPatient.setDepartmentEntity(updatePatient.getDepartmentEntity());
        existingPatient.setPatient_birthdate(updatePatient.getPatient_birthdate());
        existingPatient.setPatient_name(updatePatient.getPatient_name());
        existingPatient.setPatient_last_name(updatePatient.getPatient_last_name());

    when(patientRepository.findById(String.valueOf(patId)))
            .thenReturn(Optional.of(existingPatient));

    when(departmentRepository.findById(String.valueOf(department.getDepartment_id())))
                .thenReturn(Optional.of(department));

    when(patientRepository.save(any(PatientEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));


    PatientEntity updatedPatient =  underTest.partialUpdate(patId, updatePatient );

    assertEquals(1L, updatedPatient.getDepartmentEntity().getDepartment_id());
    assertEquals("Surgery Department", updatedPatient.getDepartmentEntity().getDepartment_name());
    assertEquals("263HJU6H7", updatedPatient.getDepartmentEntity().getDepartment_code());
    assertEquals("Klajdi", updatedPatient.getPatient_name());
    assertEquals("Bajo", updatedPatient.getPatient_last_name());
    assertEquals(1L, updatedPatient.getPatient_id());
    assertEquals(LocalDate.of(2003, 2, 15), updatedPatient.getPatient_birthdate());

        verify(patientRepository, times(1)).findById(String.valueOf(patId));
        verify(patientRepository, times(1)).save(existingPatient);
    }


    @Test
    public void testPartialUpdate_WithNonExistingPatient() {

        PatientEntity updatePatient = TestDataUtil.createPatient();
        Long patId = updatePatient.getPatient_id() ;


        when(patientRepository.findById(String.valueOf(patId))).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> underTest.findOne(patId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Patient does not exist", exception.getReason());

        verify(patientRepository, times(1)).findById(String.valueOf(patId));
        verify(patientRepository, never()).save(any());
    }

    // search test method
    @Test
    void testSearchPatientsByFullName() {

        String fullName = "Klajdi Bajo";
        Pageable pageable = PageRequest.of(0, 10);

        List<PatientEntity> patients = List.of(TestDataUtil.createPatient());
        Page<PatientEntity> expectedPage = new PageImpl<>(patients, pageable, patients.size());

        when(patientRepository.searchByFullName(fullName, pageable)).thenReturn(expectedPage);

        Page<PatientEntity> result = underTest.searchPatientsByFullName(fullName, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        PatientEntity patient = result.getContent().get(0);
        assertEquals("Klajdi", patient.getPatient_name());
        assertEquals("Bajo", patient.getPatient_last_name());

        verify(patientRepository, times(1)).searchByFullName(fullName, pageable);

    }

    // department change test methods

    @Test
    public void testHandleDepartmentChange_NoChange_WhenSameDepartmentId() {

        DepartmentEntity department = TestDataUtil.createDepartmentEntity();
        PatientEntity patient = TestDataUtil.createPatient(department);
        Long newDepartmentId = department.getDepartment_id();

        when(departmentRepository.findById(String.valueOf(newDepartmentId)))
                .thenReturn(Optional.of(department));

        underTest.handleDepartmentChange(patient, newDepartmentId);

        verify(departmentChangeHistoryRepository, never()).save(any(DepartmentChangeHistoryEntity.class));
    }

    @Test
    public void testHandleDepartmentChange_ChangeDepartment_Success() {

        DepartmentEntity oldDepartment = TestDataUtil.createDepartmentEntity();
        DepartmentEntity newDepartment = TestDataUtil.createDepartmentEntityB();
        PatientEntity patient = TestDataUtil.createPatient(oldDepartment);
        Long newDepartmentId = newDepartment.getDepartment_id();


        when(departmentRepository.findById(String.valueOf(newDepartmentId)))
                .thenReturn(Optional.of(newDepartment));

        underTest.handleDepartmentChange(patient, newDepartmentId);

        assertEquals(newDepartment, patient.getDepartmentEntity());
        ArgumentCaptor<DepartmentChangeHistoryEntity> historyCaptor =
                ArgumentCaptor.forClass(DepartmentChangeHistoryEntity.class);
        verify(departmentChangeHistoryRepository, times(1)).save(historyCaptor.capture());

        DepartmentChangeHistoryEntity savedHistory = historyCaptor.getValue();
        assertEquals(oldDepartment.getDepartment_name(), savedHistory.getOld_department());
        assertEquals(newDepartment.getDepartment_name(), savedHistory.getNew_department());
        assertEquals(patient, savedHistory.getPatientEntity());
        assertNotNull(savedHistory.getChange_date());
    }

    @Test
    public void testHandleDepartmentChange_DepartmentNotFound() {

        DepartmentEntity oldDepartment = TestDataUtil.createDepartmentEntity();
        PatientEntity patient = TestDataUtil.createPatient(oldDepartment);
        Long newDepartmentId = 99L;

        when(departmentRepository.findById(String.valueOf(newDepartmentId)))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> underTest.handleDepartmentChange(patient, newDepartmentId));
        assertEquals("Department not found", exception.getReason());

        verify(departmentRepository, times(1)).findById(String.valueOf(newDepartmentId));
        verify(departmentChangeHistoryRepository, never()).save(any(DepartmentChangeHistoryEntity.class));
    }


    @Test
    public void testDeletePatient_NotFound() {

        Long patientId = 1L;
        when(patientRepository.findById(String.valueOf(patientId)))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> underTest.delete(patientId));
        assertEquals("No patient found with this ID", exception.getReason());

        verify(patientRepository, times(1)).findById(String.valueOf(patientId));
        verifyNoMoreInteractions(departmentChangeHistoryRepository, admissionStateRepository, clinicalDataRepository);
    }

    @Test
    public void testDeletePatient_NoAdmissionStates() {

        PatientEntity patient = TestDataUtil.createPatient();
        Long patientId = patient.getPatient_id();

        when(patientRepository.findById(String.valueOf(patientId)))
                .thenReturn(Optional.of(patient));
        when(departmentChangeHistoryRepository.findByPatientEntity(patient))
                .thenReturn(Collections.emptyList());
        when(admissionStateRepository.findByPatientEntity(patient))
                .thenReturn(Collections.emptyList());

        underTest.delete(patientId);

        verify(patientRepository, times(1)).findById(String.valueOf(patientId));
        verify(departmentChangeHistoryRepository, times(1)).findByPatientEntity(patient);
        verify(departmentChangeHistoryRepository, times(1)).deleteAll(Collections.emptyList());
        verify(admissionStateRepository, times(2)).findByPatientEntity(patient);
        // Because the list is empty, clinicalDataRepository.deleteByAdmissionStateIds should not be invoked.
        verify(clinicalDataRepository, never()).deleteByAdmissionStateIds(any());
        verify(admissionStateRepository, times(1)).deleteAll(Collections.emptyList());
        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    public void testDeletePatient_WithAdmissionStates() {

        PatientEntity patient = TestDataUtil.createPatient();
        Long patientId = patient.getPatient_id();


        when(patientRepository.findById(String.valueOf(patientId)))
                .thenReturn(Optional.of(patient));
        when(departmentChangeHistoryRepository.findByPatientEntity(patient))
                .thenReturn(Collections.emptyList());

        AdmissionStateEntity state1 = new AdmissionStateEntity();
        AdmissionStateEntity state2 = new AdmissionStateEntity();
        List<AdmissionStateEntity> admissionStates = Arrays.asList(state1, state2);
        when(admissionStateRepository.findByPatientEntity(patient))
                .thenReturn(admissionStates);

        underTest.delete(patientId);

        verify(patientRepository, times(1)).findById(String.valueOf(patientId));
        verify(departmentChangeHistoryRepository, times(1)).findByPatientEntity(patient);
        verify(departmentChangeHistoryRepository, times(1)).deleteAll(Collections.emptyList());
        verify(admissionStateRepository, times(2)).findByPatientEntity(patient);
        // Since the list is not empty, the clinicalDataRepository should be called.
        verify(clinicalDataRepository, times(1)).deleteByAdmissionStateIds(admissionStates);
        verify(admissionStateRepository, times(1)).deleteAll(admissionStates);
        verify(patientRepository, times(1)).delete(patient);
    }














    }







