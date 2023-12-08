package ci.projet.gestmissionbackend.repositories;

import ci.projet.gestmissionbackend.entities.PersonalProfile;
import ci.projet.gestmissionbackend.enums.PersonalRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalProfilRepository extends JpaRepository<PersonalProfile, Long> {
    PersonalProfile findByType(PersonalRole personalRole);
}
