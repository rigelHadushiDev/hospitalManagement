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
public class DepartmentChangeHistoryDto {


    private Long department_change_history_id;

    private String old_department;

    private String new_department;

    private LocalDateTime change_date;

    private Long patient_id;
}
