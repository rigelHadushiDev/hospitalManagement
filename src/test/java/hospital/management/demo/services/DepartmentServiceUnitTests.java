package hospital.management.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import hospital.management.demo.TestDataUtil;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import hospital.management.demo.repositories.DepartmentRepository;
import hospital.management.demo.repositories.PatientRepository;
import hospital.management.demo.services.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Unit tests for {@link hospital.management.demo.services.impl.DepartmentServiceImpl}. */
@ExtendWith(MockitoExtension.class)
public class DepartmentServiceUnitTests {

    @Mock private DepartmentRepository departmentRepository;
    @Mock
    private PatientRepository patientRepository;

    @InjectMocks private DepartmentServiceImpl underTest;

    @Test
    public void testThatListBooksReturnsFromRepository() {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<DepartmentEntity> findAllResult =
                new PageImpl<>(List.of(TestDataUtil.createDepartmentEntity()), pageable, 1);

        when(departmentRepository.findAll(eq(pageable))).thenReturn(findAllResult);
        final Page<DepartmentEntity> listDepartmentsResult = underTest.findAll(pageable);
        assertThat(listDepartmentsResult).isEqualTo(findAllResult);
    }

    @Test
    public void testDelete_DepartmentNotFound() {

        Long deptId = null;

        when(departmentRepository.findById(String.valueOf(deptId))).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            underTest.delete(deptId);
        });
        verify(departmentRepository).findById(String.valueOf(deptId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No department found", exception.getReason());
    }

    @Test
    public void testDelete_DepartmentHasPatients() {

        DepartmentEntity department = TestDataUtil.createDepartmentEntity();
        Long deptId = department.getDepartment_id();

        when(departmentRepository.findById(String.valueOf(deptId))).thenReturn(Optional.of(department));
        when(patientRepository.findByDepartmentEntity(department))
                .thenReturn(List.of(new PatientEntity()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            underTest.delete(deptId);
        });
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Cannot delete department. There are patients associated with it.", exception.getReason());
        verify(departmentRepository, never()).delete(any(DepartmentEntity.class));
    }

    @Test
    public void testDelete_Success() {
        Long deptId = 1L;
        DepartmentEntity department = new DepartmentEntity();
        when(departmentRepository.findById(String.valueOf(deptId))).thenReturn(Optional.of(department));
        when(patientRepository.findByDepartmentEntity(department))
                .thenReturn(Collections.emptyList());

        underTest.delete(deptId);

        verify(departmentRepository, times(1)).delete(department);
    }

    @Test
    public void testFindOne_DepartmentExists() {
        DepartmentEntity department = TestDataUtil.createDepartmentEntity();
        Long deptId = department.getDepartment_id();

        when(departmentRepository.findById(String.valueOf(deptId))).thenReturn(Optional.of(department));

        Optional<DepartmentEntity> result = underTest.findOne(deptId);

        assertTrue(result.isPresent());
        assertEquals(department, result.get());
        verify(departmentRepository).findById(String.valueOf(deptId));
    }

    @Test
    public void testCreate_Success() {


        DepartmentEntity department = TestDataUtil.createDepartmentEntity();

        when(departmentRepository.save(department)).thenReturn(department);

        DepartmentEntity result = underTest.save(department);

        assertEquals(department, result);
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void testPartialUpdate_Success() {

        DepartmentEntity updateRequest = TestDataUtil.createDepartmentEntity();
        Long departmentId = updateRequest.getDepartment_id();

        DepartmentEntity existingDepartment = TestDataUtil.createDepartmentEntityB();
        existingDepartment.setDepartment_id(updateRequest.getDepartment_id());
        existingDepartment.setDepartment_name(updateRequest.getDepartment_name());
        existingDepartment.setDepartment_code(updateRequest.getDepartment_code());


        when(departmentRepository.findById(String.valueOf(departmentId)))
                .thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.save(any(DepartmentEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        DepartmentEntity updatedDepartment = underTest.partialUpdate(departmentId, updateRequest);


        assertEquals(departmentId, updatedDepartment.getDepartment_id());
        assertEquals("Surgery Department", updatedDepartment.getDepartment_name());
        assertEquals("263HJU6H7", updatedDepartment.getDepartment_code());

        verify(departmentRepository, times(1)).findById(String.valueOf(departmentId));
        verify(departmentRepository, times(1)).save(existingDepartment);
    }

    @Test
    void testPartialUpdate_NotFound() {

        Long departmentId = 100L;

        DepartmentEntity updateRequest = TestDataUtil.createDepartmentEntity();

        when(departmentRepository.findById(String.valueOf(departmentId)))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                underTest.partialUpdate(departmentId, updateRequest)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Department does not exist", exception.getReason());

        verify(departmentRepository, times(1)).findById(String.valueOf(departmentId));
        verify(departmentRepository, never()).save(any());
    }

}