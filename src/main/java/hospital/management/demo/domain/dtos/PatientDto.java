package hospital.management.demo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDto {

    private Long patient_id;

    private String patient_name;

    private String patient_last_name;

    private LocalDate patient_birthdate;
    private Long department_id;

}
