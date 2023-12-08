package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.ServiceDTO;
import ci.projet.gestmissionbackend.entities.Grade;
import ci.projet.gestmissionbackend.entities.Service;
import ci.projet.gestmissionbackend.enums.GradeType;
import ci.projet.gestmissionbackend.exceptions.ServiceExistsException;
import ci.projet.gestmissionbackend.exceptions.ServicesNotFoundException;
import ci.projet.gestmissionbackend.mappers.PersonalMapperImpl;
import ci.projet.gestmissionbackend.repositories.ServiceRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ServiceServiceImpl implements ServiceService {
    private ServiceRepository serviceRepository;
    private GradeService gradeService;
    private PersonalMapperImpl dtoMapper;
    @Override
    public ServiceDTO addNewService(ServiceDTO serviceDTO) {
        Service service = dtoMapper.fromServiceDTO(serviceDTO);
        if(service.getChef() != null){
            if(service.getChef().getGrade() != gradeService.getDgGrade())
                service.getChef().setGrade(gradeService.getChefGrade());
            service.getChef().setService(service);
        }
        Service savedService = this.serviceRepository.save(service);
        //this.addNewService(service.getChef());
        return dtoMapper.fromService(savedService);
    }

    @Override
    public List<ServiceDTO> listService() {
        List<Service> services = serviceRepository.findAll();
        List<ServiceDTO> serviceDTOS = services.stream().map(service -> dtoMapper.fromService(service))
                .collect(Collectors.toList());
        return serviceDTOS;
    }

    @Override
    public ServiceDTO findServiceByServiceName(String serviceName) throws ServicesNotFoundException {
        Service service = this.serviceRepository.findByServiceName(serviceName);
        ServiceDTO serviceDTO = dtoMapper.fromService(service);
        return serviceDTO;
    }
    @Override
    public ServiceDTO findServiceById(Long id){
        Service service = serviceRepository.findById(id).orElse(null);
        return dtoMapper.fromService(service);
    }

    @Override
    public List<ServiceDTO> searchService(String keyword) {
        List<Service> serviceList = serviceRepository.findByServiceNameContains(keyword);
        List<ServiceDTO> serviceDTOS = serviceList.stream().map(service -> dtoMapper.fromService(service)).collect(Collectors.toList());
        return serviceDTOS;
    }

    public ServiceDTO savedService(ServiceDTO serviceDTO) throws ServiceExistsException {
        ServiceDTO dto = findServiceById(serviceDTO.getId());
        if(dto.getId()!= null){
            throw new ServiceExistsException("Ce service existe deja");
        }
        this.serviceRepository.save(dtoMapper.fromServiceDTO(dto));
        return serviceDTO;
    }

}
