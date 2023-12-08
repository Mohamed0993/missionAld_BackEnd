package ci.projet.gestmissionbackend.dtos;

import ci.projet.gestmissionbackend.enums.GradeType;
import lombok.Data;

@Data
public class GradeDTO {
    private Long id;
    private GradeType type;
    private String label;
}
