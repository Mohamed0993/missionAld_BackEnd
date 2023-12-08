package ci.projet.gestmissionbackend.entities;

import ci.projet.gestmissionbackend.enums.PersonalRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class PersonalProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PersonalRole type = PersonalRole.USER;
}
