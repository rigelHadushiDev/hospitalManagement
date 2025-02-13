package hospital.management.demo;


import hospital.management.demo.domain.entities.AdmissionStateEntity;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public static AdmissionStateEntity createAdmissionStateEntity() {
        return createAdmissionStateEntity(null);
    }


    public static AdmissionStateEntity createAdmissionStateEntity(PatientEntity patientEntity) {
        AdmissionStateEntity.AdmissionStateEntityBuilder builder = AdmissionStateEntity
                .builder()
                .admission_state_id(1L)
                .entering_date(LocalDateTime.now())
                .exiting_date(null)
                .cause("Routine Checkup")
                .reason(AdmissionStateEntity.Reason.HEALTHY)
                .discharge(false);

        if (patientEntity != null) {
            builder.patientEntity(patientEntity);
        }

        return builder.build();
    }

    public static AdmissionStateEntity createAdmissionStateEntityB() {
        return createAdmissionStateEntity(null);
    }


    public static AdmissionStateEntity createAdmissionStateEntityB(PatientEntity patientEntity) {
        AdmissionStateEntity.AdmissionStateEntityBuilder builder = AdmissionStateEntity
                .builder()
                .admission_state_id(2L)
                .entering_date(LocalDateTime.now())
                .exiting_date(null)
                .cause("Accident")
                .reason(AdmissionStateEntity.Reason.DEATH)
                .discharge(false);

        if (patientEntity != null) {
            builder.patientEntity(patientEntity);
        }

        return builder.build();
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