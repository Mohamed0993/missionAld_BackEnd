package ci.projet.gestmissionbackend.entities;

import ci.projet.gestmissionbackend.dtos.PersonalDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtResponse {

    private PersonalDTO personalDTO;
    private String jwtToken;
}
