package ci.projet.gestmissionbackend.repositories;

import ci.projet.gestmissionbackend.entities.Costs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostsRepository extends JpaRepository<Costs,Long> {
}
