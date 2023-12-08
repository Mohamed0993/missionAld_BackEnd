package ci.projet.gestmissionbackend.repositories;

import ci.projet.gestmissionbackend.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findByVehicleRegistration(String registration);
    @Query("select v from Vehicle v, Assignment a where a.assignmentDateOfDeparture = ?1 and a.assigmentReturnDate = ?2")
    List<Vehicle> getVehicleAssignment(LocalDateTime dateOfDeparture, LocalDateTime dateOfReturn);
    List<Vehicle> findByVehicleMarkContains(String keyword);
}
