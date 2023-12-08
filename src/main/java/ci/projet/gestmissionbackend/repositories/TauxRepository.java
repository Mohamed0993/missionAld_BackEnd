package ci.projet.gestmissionbackend.repositories;

import ci.projet.gestmissionbackend.entities.Taux;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TauxRepository extends JpaRepository<Taux, Long> {
}
