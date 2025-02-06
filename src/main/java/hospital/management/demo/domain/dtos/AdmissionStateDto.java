package hospital.management.demo.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdmissionStateDto {

    private Long admission_state_id;

    private LocalDateTime entering_date;

    private LocalDateTime exiting_date;

    private String cause;

    private String reason;

    private boolean discharge;

    private Long patient_id;
}
