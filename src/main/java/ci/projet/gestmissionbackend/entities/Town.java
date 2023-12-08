package ci.projet.gestmissionbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Town {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long townId;
    private String townName;
    private double townDistance;
    private double tauxAuto;
    private double tauxCTM;
    private double tauxTRAIN;
    private double tauxAvion;
    private long nombreTickAuto;
    @OneToMany(mappedBy = "town")
    private List<Worksite> worksites;
}
