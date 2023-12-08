package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.VehicleDTO;
import ci.projet.gestmissionbackend.entities.Vehicle;
import ci.projet.gestmissionbackend.mappers.AssignmentMapperImpl;
import ci.projet.gestmissionbackend.repositories.VehicleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {
    VehicleRepository vehicleRepository;
    private AssignmentMapperImpl dtoMapper;
    @Override
    public VehicleDTO addNewVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = dtoMapper.fromVehicleDTO(vehicleDTO);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return dtoMapper.fromVehicle(savedVehicle);
    }

    @Override
    public VehicleDTO findVehicleByVehicleRegistration(String vehicleRegistration) {
        Vehicle vehicle = vehicleRepository.findByVehicleRegistration(vehicleRegistration);
        return dtoMapper.fromVehicle(vehicle);
    }

    @Override
    public List<VehicleDTO> listVehicle() {
        List<Vehicle> vehicleList = this.vehicleRepository.findAll();
        List<VehicleDTO> vehicleDTOS = vehicleList.stream().map(vehicle -> dtoMapper.fromVehicle(vehicle))
                .collect(Collectors.toList());
        return vehicleDTOS;
    }


    @Override
    public void deleteVehicle(Long id){
        vehicleRepository.deleteById(id);
    }

    @Override
    public VehicleDTO updateVehicle(VehicleDTO vehicleDTO){
        Vehicle vehicle = dtoMapper.fromVehicleDTO(vehicleDTO);
        Vehicle save = vehicleRepository.save(vehicle);
        return dtoMapper.fromVehicle(save);
    }

    @Override
    public List<VehicleDTO> searchVehicle(String keyword) {
        List<Vehicle> vehicles = vehicleRepository.findByVehicleMarkContains(keyword);
        List<VehicleDTO> vehicleDTOS = vehicles.stream().map(vehicle -> dtoMapper.fromVehicle(vehicle)).collect(Collectors.toList());
        return vehicleDTOS;
    }

}
