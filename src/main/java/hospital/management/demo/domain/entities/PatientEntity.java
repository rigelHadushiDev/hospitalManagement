package hospital.management.demo.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "patient" )
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patient_id;

    private String patient_name;

    private String patient_last_name;

    private LocalDate patient_birthdate;

    // Cascade only for persist and merge operations
    @ManyToOne(cascade = CascadeType.PERSIST)
    // so that when we delete a patient that has an existing department is allowed by the foreign key
    @JoinColumn(name = "department_id", nullable = true)
    private DepartmentEntity departmentEntity;

}