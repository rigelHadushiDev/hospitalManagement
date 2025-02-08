package hospital.management.demo.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "admission_state")
public class AdmissionStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long admission_state_id;

    private LocalDateTime entering_date = LocalDateTime.now();

    private LocalDateTime exiting_date = null;

    private String cause = "";

    @Enumerated(EnumType.STRING)
    private Reason reason = null;

    private Boolean discharge = false;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patientEntity;

    public enum Reason {
        DEATH,
        HEALTHY,
        TRANSFERRED
    }
}
