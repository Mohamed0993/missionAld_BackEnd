package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.VehicleDTO;

import java.util.List;

public interface VehicleService {
    VehicleDTO addNewVehicle(VehicleDTO vehicleDTO);
    VehicleDTO findVehicleByVehicleRegistration (String vehicleRegistration);
    List<VehicleDTO> listVehicle();

    void deleteVehicle(Long id);

    VehicleDTO updateVehicle(VehicleDTO vehicleDTO);

    List<VehicleDTO> searchVehicle(String keyword);
}
