package hospital.management.demo.domain.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentDto {


    private Long department_id;

    private String department_name;

    private String department_code;
}
