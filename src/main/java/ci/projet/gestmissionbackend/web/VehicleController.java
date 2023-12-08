package ci.projet.gestmissionbackend.web;

import ci.projet.gestmissionbackend.dtos.VehicleDTO;
import ci.projet.gestmissionbackend.services.VehicleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin
public class VehicleController {
    private VehicleService vehicleService;

    @PostMapping("/vehicles")
    @PostAuthorize("hasAuthority('ADMIN')")
    public VehicleDTO saveVehicle(@RequestBody VehicleDTO vehicleDTO){
        return vehicleService.addNewVehicle(vehicleDTO);
    }

    @GetMapping("/vehicles/search")
    @PostAuthorize("hasAuthority('USER')")
    public List<VehicleDTO> searchVehicles(@RequestParam (name = "keyword", defaultValue = "") String keyword){
        return vehicleService.searchVehicle(keyword);
    }

    @GetMapping("/vehicles")
    @PostAuthorize("hasAuthority('USER')")
    public List<VehicleDTO> vehicles(){
        return vehicleService.listVehicle();
    }

    @GetMapping("/vehicles/{vehicleRegistration}")
    public VehicleDTO vehicle(@PathVariable (name = "vehicleRegistration") String vehicleRegistration){
        return this.vehicleService.findVehicleByVehicleRegistration(vehicleRegistration);
    }

    @PutMapping("/vehicles/{vehicleId}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public VehicleDTO updateVehicle(@PathVariable Long vehicleId, @RequestBody VehicleDTO vehicleDTO){
        vehicleDTO.setVehicleId(vehicleId);
        return vehicleService.updateVehicle(vehicleDTO);

    }

    @DeleteMapping("/vehicles/{id}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void deleteVehicle(@PathVariable Long id){
        vehicleService.deleteVehicle(id);
    }

}
