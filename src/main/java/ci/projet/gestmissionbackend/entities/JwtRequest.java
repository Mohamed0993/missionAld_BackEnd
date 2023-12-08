package ci.projet.gestmissionbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtRequest {
    private String matricule;
    private String password;

}
