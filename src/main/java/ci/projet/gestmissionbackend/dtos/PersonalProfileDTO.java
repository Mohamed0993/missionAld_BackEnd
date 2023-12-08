package ci.projet.gestmissionbackend.dtos;

import ci.projet.gestmissionbackend.enums.PersonalRole;
import lombok.Data;

@Data
public class PersonalProfileDTO {
    private Long id;
    private PersonalRole type;
}
