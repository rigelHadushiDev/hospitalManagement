package hospital.management.demo.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "department_change_history" )
public class DepartmentChangeHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long department_change_history_id;

    private String old_department;

    private String new_department;

    private LocalDateTime change_date;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patientEntity;
}