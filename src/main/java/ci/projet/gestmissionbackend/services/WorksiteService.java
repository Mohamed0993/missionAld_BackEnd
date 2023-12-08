package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.TownDTO;
import ci.projet.gestmissionbackend.dtos.WorksiteDTO;
import ci.projet.gestmissionbackend.exceptions.worksiteNotFoundException;

import java.util.List;

public interface WorksiteService {
    WorksiteDTO addNewWorksite (WorksiteDTO worksiteDTO);
    WorksiteDTO findWorksiteByWorksiteId(Long worksiteId) throws worksiteNotFoundException;

    List<WorksiteDTO> worksiteDTOList();

    WorksiteDTO updateWorksite(WorksiteDTO worksiteDTO);

    void deleteWorksite(Long id);

    List<WorksiteDTO> searchWorksite(String keyword);
}
