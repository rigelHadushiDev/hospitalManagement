package hospital.management.demo.repositories;


import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.domain.entities.PatientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, String>,
        PagingAndSortingRepository<DepartmentEntity, String>, JpaRepository<DepartmentEntity, String> {

    @Query("SELECT p FROM DepartmentEntity p WHERE LOWER(p.department_name) LIKE LOWER(CONCAT('%', :departmentName, '%'))")
    Page<DepartmentEntity> searchByDepartmentName(@Param("departmentName") String departmentName, Pageable pageable);

}

