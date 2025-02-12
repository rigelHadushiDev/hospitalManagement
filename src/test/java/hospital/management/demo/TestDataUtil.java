package hospital.management.demo;


import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;

import java.time.LocalDate;

public final class TestDataUtil {
    private TestDataUtil(){}

    public static DepartmentEntity createDepartmentEntity() {
        return DepartmentEntity.builder()
                .department_id(1L)
                .department_code("263HJU6H7")
                .department_name("Surgery Department")
                .build();
    }

    public static DepartmentEntity createDepartmentEntityB() {
        return DepartmentEntity.builder()
                .department_id(2L)
                .department_code("34GHT")
                .department_name("Orthopedic Department")
                .build();
    }


    public static PatientEntity createPatient() {
        return createPatient(null);
    }

    public static PatientEntity createPatient(DepartmentEntity departmentEntity) {
        PatientEntity.PatientEntityBuilder builder = PatientEntity.builder()
                .patient_id(1L)
                .patient_birthdate(LocalDate.of(2003, 2, 15))
                .patient_name("Klajdi")
                .patient_last_name("Bajo");

        if (departmentEntity != null) {
            builder.departmentEntity(departmentEntity);
        }

        return builder.build();
    }

    public static PatientEntity createPatientB() {
        return createPatientB(null);
    }

    public static PatientEntity createPatientB(DepartmentEntity departmentEntity) {
        PatientEntity.PatientEntityBuilder builder = PatientEntity.builder()
                .patient_id(1L)
                .patient_birthdate(LocalDate.of(2004, 3, 15))
                .patient_name("Rigel")
                .patient_last_name("Hadushi");

        if (departmentEntity != null) {
            builder.departmentEntity(departmentEntity);
        }

        return builder.build();
    }



}