package ci.projet.gestmissionbackend.dtos;

import lombok.Data;

@Data
public class MissionTypeDTO {
    private Long id;
    private String label;
    private ServiceDTO serviceDTO;
}
