package ci.projet.gestmissionbackend.repositories;

import ci.projet.gestmissionbackend.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    Service findByServiceName(String serviceName);
    List<Service> findByServiceNameContains(String keyword);
}
