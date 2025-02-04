package hospital.management.demo.domain.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClinicalDataDto {

    private Long clinical_data_id;

    private String clinical_record;

}
