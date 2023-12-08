package ci.projet.gestmissionbackend.dtos;

import ci.projet.gestmissionbackend.enums.Civilite;
import ci.projet.gestmissionbackend.enums.PersonalClass;
import ci.projet.gestmissionbackend.enums.Sex;
import lombok.Data;

import java.util.*;

@Data
public class PersonalDTO {
    private Long id;
    private Civilite civilite;
    private String personalMatricule;
    private String personalName;
    private String personalFullName;
    private String password;
    private PersonalClass personalClass;
    private String fonction;
    private Sex sex;
    private String personalContact;
    private String email;
    private GradeDTO gradeDTO;
    private boolean director;
    private ServiceDTO serviceDTO;
    private Set<PersonalProfileDTO> personalProfileDTOS = new HashSet<>();
}
