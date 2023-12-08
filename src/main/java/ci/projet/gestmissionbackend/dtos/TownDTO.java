package ci.projet.gestmissionbackend.dtos;

import lombok.Data;

@Data
public class TownDTO {
    private Long townId;
    private String townName;
    private double townDistance;
    private double tauxAuto;
    private double tauxCTM;
    private double tauxTRAIN;
    private double tauxAvion;
    private long nombreTickAuto;
}
