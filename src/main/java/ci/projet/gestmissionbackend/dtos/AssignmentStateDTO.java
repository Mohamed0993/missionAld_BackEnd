package ci.projet.gestmissionbackend.dtos;

import ci.projet.gestmissionbackend.enums.AssignementStateEnum;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class AssignmentStateDTO {
    private Long idAssignmentState;
    private LocalDateTime stateDate;
    private AssignementStateEnum state;
}
