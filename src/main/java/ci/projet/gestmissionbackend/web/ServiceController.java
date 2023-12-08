package ci.projet.gestmissionbackend.web;

import ci.projet.gestmissionbackend.dtos.ServiceDTO;
import ci.projet.gestmissionbackend.services.ServiceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin
public class ServiceController {
    private ServiceService serviceService;

    @PostMapping("/services")
    @PostAuthorize("hasAuthority('ADMIN')")
    public ServiceDTO saveService(@RequestBody ServiceDTO serviceDTO){
        return serviceService.addNewService(serviceDTO);
    }
    @GetMapping("/services/search")
    @PostAuthorize("hasAuthority('USER')")
    public List<ServiceDTO> searchService(@RequestParam(name = "keyword", defaultValue = "") String keyword){
        return serviceService.searchService(keyword);
    }
    @GetMapping("/services")
    @PostAuthorize("hasAuthority('USER')")
    public List<ServiceDTO> getService(){return serviceService.listService();}

    @GetMapping("/services/{id}")
    @PostAuthorize("hasAuthority('USER')")
    public ServiceDTO findService(@PathVariable (name = "id") Long id){
        return serviceService.findServiceById(id);
    }


}
