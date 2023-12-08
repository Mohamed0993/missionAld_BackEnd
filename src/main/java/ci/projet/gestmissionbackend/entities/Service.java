package ci.projet.gestmissionbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Service {
    @Id
    @Access(AccessType.PROPERTY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;
   @Column(unique = true)
    private String serviceName;
    @OneToOne(fetch = FetchType.EAGER)
    private Personal chef;
    @OneToMany (fetch = FetchType.LAZY, mappedBy = "service")
    private List<Personal> personals;
    @OneToMany(mappedBy = "service")
    private List<MissionType> missionTypes;

    @Override
    public String toString() {
        return String.valueOf(serviceId);
    }

}
