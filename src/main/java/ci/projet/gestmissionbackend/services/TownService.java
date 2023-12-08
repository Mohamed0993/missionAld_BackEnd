package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.TownDTO;

import java.util.List;

public interface TownService {
    List<TownDTO> townDTOList();
    TownDTO findTownById(Long townId);
    TownDTO addNewTown(TownDTO townDTO);
    void deleteTown(Long townId);
    TownDTO updateTown(TownDTO townDTO);

    List<TownDTO> searchTown(String keyword);
}
