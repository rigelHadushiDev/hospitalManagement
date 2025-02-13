package hospital.management.demo.services;

import hospital.management.demo.TestDataUtil;
import hospital.management.demo.domain.entities.DepartmentChangeHistoryEntity;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import hospital.management.demo.repositories.DepartmentChangeHistoryRepository;
import hospital.management.demo.repositories.DepartmentRepository;
import hospital.management.demo.services.impl.DepartmentChangeHistoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Unit tests for {@link hospital.management.demo.services.impl.DepartmentChangeHistoryServiceImpl}. */
@ExtendWith(MockitoExtension.class)
public class DepartmentChangeHistoryServiceUnitTest {

    @Mock
    private DepartmentChangeHistoryRepository departmentChangeHistoryRepository;

    @InjectMocks
    private DepartmentChangeHistoryServiceImpl underTest;


    @Test
    public void testSearchByPatientIdWithPagination_validPatientId() {
        PatientEntity patientEntity = TestDataUtil.createPatient();
        Long patientId = patientEntity.getPatient_id();

        Pageable pageable = PageRequest.of(0, 10);
        DepartmentChangeHistoryEntity entity = new DepartmentChangeHistoryEntity();
        entity.setPatientEntity(patientEntity);

        Page<DepartmentChangeHistoryEntity> expectedPage =
                new PageImpl<>(Collections.singletonList(entity), pageable, 1);

        // Use patientId (which is 1L) in your stub
        when(departmentChangeHistoryRepository.searchByPatientIdWithPagination(patientId, pageable))
                .thenReturn(expectedPage);

        Page<DepartmentChangeHistoryEntity> actualPage =
                underTest.searchByPatientIdWithPagination(String.valueOf(patientId), pageable);

        // Verify with patientId (1L)
        verify(departmentChangeHistoryRepository, times(1)).searchByPatientIdWithPagination(patientId, pageable);
        assertEquals(expectedPage, actualPage, "The returned page should match the expected page.");

        DepartmentChangeHistoryEntity actualEntity = actualPage.getContent().get(0);
        assertEquals(patientId, actualEntity.getPatientEntity().getPatient_id(), "The entity's patient id should match.");
    }

    @Test
    public void testSearchByPatientIdWithPagination_invalidPatientId() {

        String invalidPatientId = "invalid";
        Pageable pageable = PageRequest.of(0, 10);


        assertThrows(NumberFormatException.class, () -> {
            underTest.searchByPatientIdWithPagination(invalidPatientId, pageable);
        });

        verify(departmentChangeHistoryRepository, never())
                .searchByPatientIdWithPagination(anyLong(), any(Pageable.class));
    }
}
