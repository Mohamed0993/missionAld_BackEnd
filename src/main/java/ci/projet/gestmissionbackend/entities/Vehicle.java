package ci.projet.gestmissionbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;
    private String vehicleRegistration;
    private String vehicleMark;
    private String  vehicleModel;
    private int vehiclePlace;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "assignment_vehicle",
            joinColumns = {@JoinColumn(name = "VEHICLE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ASSIGNMENT_ID")})
    private List<Assignment> assignments = new ArrayList<>();
}
