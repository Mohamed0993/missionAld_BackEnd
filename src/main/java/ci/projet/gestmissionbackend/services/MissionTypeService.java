package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.MissionTypeDTO;

import java.util.List;

public interface MissionTypeService {

    MissionTypeDTO addNewMissionType (MissionTypeDTO typeDTO);
    void deleteMissionType(Long id);

    List<MissionTypeDTO> listMissionType();

    List<MissionTypeDTO> searchMissionType(String keyword);
}
