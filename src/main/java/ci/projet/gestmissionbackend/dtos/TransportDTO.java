package ci.projet.gestmissionbackend.dtos;

import ci.projet.gestmissionbackend.enums.MovingMeans;
import lombok.Data;

@Data

public class TransportDTO {
    private Long id;
    private MovingMeans movingMeans;
    private double taux;
}
