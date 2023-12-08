package ci.projet.gestmissionbackend.repositories;

import ci.projet.gestmissionbackend.entities.MissionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionTypeRepository extends JpaRepository<MissionType, Long> {
    List<MissionType> findByLabelContains(String keyword);
}