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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private DepartmentEntity departmentEntity;

    @OneToMany(mappedBy = "patientEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AdmissionStateEntity> admissionStateEntities = new HashSet<>();
}