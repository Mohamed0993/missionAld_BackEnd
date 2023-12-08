package ci.projet.gestmissionbackend.repositories;

import ci.projet.gestmissionbackend.entities.Town;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TownRepository extends JpaRepository<Town, Long> {
    List<Town> findByTownNameContains(String keywork);
}
