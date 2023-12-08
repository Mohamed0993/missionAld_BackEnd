package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.TownDTO;
import ci.projet.gestmissionbackend.dtos.WorksiteDTO;
import ci.projet.gestmissionbackend.entities.Town;
import ci.projet.gestmissionbackend.entities.Worksite;
import ci.projet.gestmissionbackend.exceptions.worksiteNotFoundException;
import ci.projet.gestmissionbackend.mappers.AssignmentMapperImpl;
import ci.projet.gestmissionbackend.repositories.WorksiteRepository;
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
public class WorksiteServiceImpl implements WorksiteService {
    private WorksiteRepository worksiteRepository;
    private AssignmentMapperImpl dtoMapper;
    private TownService townService;
    // Worksite Service
    @Override
    public WorksiteDTO addNewWorksite(WorksiteDTO worksiteDTO) {

        Worksite worksite = dtoMapper.fromWorksiteDTO(worksiteDTO);
        Town town = dtoMapper.fromTownDTO(worksiteDTO.getTownDTO());
        worksite.setTown(town);
        Worksite savedWorksite = worksiteRepository.save(worksite);
        return dtoMapper.fromWorksite(savedWorksite);
    }
    @Override
    public WorksiteDTO findWorksiteByWorksiteId(Long worksiteId) throws worksiteNotFoundException {
        Worksite worksite = worksiteRepository.findById(worksiteId)
                .orElseThrow(()->new worksiteNotFoundException("WorkSite not found"));

        return dtoMapper.fromWorksite(worksite);
    }

    @Override
    public List<WorksiteDTO> worksiteDTOList(){
        List<Worksite> worksiteList = worksiteRepository.findAll();
        List<WorksiteDTO> worksiteDTOS = worksiteList.stream().map(worksite -> dtoMapper.fromWorksite(worksite))
                .collect(Collectors.toList());
        return worksiteDTOS;
    }

    @Override
    public WorksiteDTO updateWorksite(WorksiteDTO worksiteDTO){
        Worksite worksite = dtoMapper.fromWorksiteDTO(worksiteDTO);
        Worksite save = worksiteRepository.save(worksite);
        return dtoMapper.fromWorksite(save);
    }

    @Override
    public void deleteWorksite(Long id) {
        worksiteRepository.deleteById(id);
    }

    @Override
    public List<WorksiteDTO> searchWorksite(String keyword) {
        List<Worksite> worksiteList = worksiteRepository.findByWorksiteObjectContains(keyword);
        List<WorksiteDTO> worksiteDTOS = worksiteList.stream().map(worksite -> dtoMapper.fromWorksite(worksite)).collect(Collectors.toList());
        return worksiteDTOS;
    }


    // End Worksite service
}
