package ci.projet.gestmissionbackend.dtos;

import ci.projet.gestmissionbackend.enums.AssignementStateEnum;
import ci.projet.gestmissionbackend.enums.AssignmentStatus;
import ci.projet.gestmissionbackend.enums.MovingMeans;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.*;

@Data
public class AssignmentDTO {
    private Long assignmentId;
    private String assignmentPattern;
    private MovingMeans movingMeans;
    private MissionTypeDTO type;
    private LocalDateTime assignmentDate;
    private LocalDateTime assignmentDateOfDeparture;
    private LocalDateTime assigmentReturnDate;
    private AssignmentStatus assignmentStatus;
    private Set<AssignmentStateDTO> assignmentStates = new HashSet<>();
    private AssignementStateEnum currentState;
    private String stateCurrent;
    private AssignementStateEnum nextState;
    private List<VehicleDTO> vehicleDTOS= new ArrayList<>();;
    private List<WorksiteDTO> worksiteDTOS = new ArrayList<>();
    private PersonalDTO personalDTO;
    private List<PersonalDTO> personalDTOS = new ArrayList<>();
    private CostsDTO costsDTO;
    private String uuid;

}
