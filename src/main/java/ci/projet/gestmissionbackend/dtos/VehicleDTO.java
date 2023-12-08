package ci.projet.gestmissionbackend.dtos;

import lombok.Data;

@Data
public class VehicleDTO {
    private Long vehicleId;
    private String vehicleRegistration;
    private String vehicleMark;
    private String  vehicleModel;
    private int vehiclePlace;
}
