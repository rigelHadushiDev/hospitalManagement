package hospital.management.demo.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "clinical_data")
public class ClinicalDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clinical_data_id;

    private String clinical_record;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    private PatientEntity patientEntity;
}
