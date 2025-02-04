package hospital.management.demo.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "department" )
public class DepartmentEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long department_id;

    private String department_name;

    private String department_code;
}
