package ci.projet.gestmissionbackend.repositories;

import ci.projet.gestmissionbackend.entities.Grade;
import ci.projet.gestmissionbackend.enums.GradeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Grade findByType(GradeType type);
    List<Grade> findByLabelContains(String keyword);
}
