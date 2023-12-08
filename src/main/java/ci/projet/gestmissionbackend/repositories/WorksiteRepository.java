package ci.projet.gestmissionbackend.repositories;

import ci.projet.gestmissionbackend.dtos.WorksiteDTO;
import ci.projet.gestmissionbackend.entities.Worksite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorksiteRepository extends JpaRepository<Worksite,Long> {
    List<Worksite> findByWorksiteObjectContains(String keyword);
}
