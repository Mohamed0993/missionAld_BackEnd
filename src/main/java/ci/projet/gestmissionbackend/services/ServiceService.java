package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.ServiceDTO;
import ci.projet.gestmissionbackend.exceptions.ServicesNotFoundException;

import java.util.List;

public interface ServiceService {

    ServiceDTO addNewService(ServiceDTO serviceDTO);
    List<ServiceDTO> listService();
    ServiceDTO findServiceByServiceName(String serviceName) throws ServicesNotFoundException;
    ServiceDTO findServiceById(Long id);

    List<ServiceDTO> searchService(String keyword);
}
