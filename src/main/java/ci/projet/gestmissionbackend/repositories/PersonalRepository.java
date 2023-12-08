package ci.projet.gestmissionbackend.repositories;

import ci.projet.gestmissionbackend.entities.Personal;
import ci.projet.gestmissionbackend.entities.Service;
import ci.projet.gestmissionbackend.enums.GradeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PersonalRepository extends JpaRepository<Personal,Long> {

    List<Personal> findByPersonalNameContains(String keyword);
    Personal findByPersonalMatricule(String matricule);
    @Query("select p from Personal p where p.service = ?1 and p.grade.type in ?2")
    List<Personal> getChefAndChefA(Service service, List<GradeType> types);
    @Query("select p from Personal p where p.grade.type in ?1")
    List<Personal>getDgAndDGA(List<GradeType> types);
    @Query("select p from Personal p where p.grade.type = ?1")
    List<Personal> getDg(GradeType type);
    @Query("select p from Personal p, Assignment a where a.assignmentDateOfDeparture = ?1 and a.assigmentReturnDate= ?2")
    List<Personal> getPersonalAssignment(LocalDateTime dateOfDeparture, LocalDateTime dateOfReturn);

}
