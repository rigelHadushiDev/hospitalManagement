package hospital.management.demo;


import hospital.management.demo.domain.entities.DepartmentEntity;

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


}