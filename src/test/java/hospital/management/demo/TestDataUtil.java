package hospital.management.demo;


import hospital.management.demo.domain.entities.DepartmentEntity;

public final class TestDataUtil {
    private TestDataUtil(){}

    public static DepartmentEntity createDepartmentEntity() {
        return DepartmentEntity.builder()
                .department_code("263HJU6H7")
                .department_name("Surgery Department")
                .build();
    }


}