package ci.projet.gestmissionbackend.dtos;

import lombok.Data;


@Data
public class WorksiteDTO {
    private Long worksiteId;
    private String worksiteObject;
    private int worksiteDuration;
    private TownDTO townDTO ;
   // private List<AssignmentDTO> assignmentDTOS = new ArrayList<>();


}
