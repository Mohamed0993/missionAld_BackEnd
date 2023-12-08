package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.MissionTypeDTO;
import ci.projet.gestmissionbackend.dtos.ServiceDTO;
import ci.projet.gestmissionbackend.entities.MissionType;
import ci.projet.gestmissionbackend.mappers.AssignmentMapperImpl;
import ci.projet.gestmissionbackend.mappers.PersonalMapperImpl;
import ci.projet.gestmissionbackend.repositories.MissionTypeRepository;
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
public class MissionTypeImpl implements MissionTypeService {
    private AssignmentMapperImpl dtoMapper;
    private PersonalMapperImpl dtoMapper2;
    private MissionTypeRepository missionTypeRepository;
    private ServiceService serviceService;
    public MissionTypeDTO addNewMissionType(MissionTypeDTO missionTypeDTO){
        MissionType missionType = dtoMapper.fromMissionTypeDTO(missionTypeDTO);
        ci.projet.gestmissionbackend.entities.Service service = dtoMapper2.fromServiceDTO(missionTypeDTO.getServiceDTO());
        missionType.setService(service);
        MissionType save = missionTypeRepository.save(missionType);
        return dtoMapper.fromMissionType(save);
    }

    @Override
    public void deleteMissionType(Long id) {
        missionTypeRepository.deleteById(id);
    }

    @Override
    public List<MissionTypeDTO> listMissionType() {
        List<ci.projet.gestmissionbackend.entities.MissionType> missionTypes = missionTypeRepository.findAll();
        List<MissionTypeDTO> typeDTOS = missionTypes.stream().map(type -> dtoMapper.fromMissionType(type))
                .collect(Collectors.toList());
        return typeDTOS;
    }

    @Override
    public List<MissionTypeDTO> searchMissionType(String keyword) {
        List<MissionType> missionTypes = missionTypeRepository.findByLabelContains(keyword);
        List<MissionTypeDTO> typeDTOS = missionTypes.stream().map(types -> dtoMapper.fromMissionType(types)).collect(Collectors.toList());

        return typeDTOS;
    }
}