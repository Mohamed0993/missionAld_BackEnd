package ci.projet.gestmissionbackend.entities;

import ci.projet.gestmissionbackend.enums.MovingMeans;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private MovingMeans movingMeans;
    private double taux;
}
