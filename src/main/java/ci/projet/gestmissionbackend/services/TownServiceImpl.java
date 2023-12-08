package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.TownDTO;
import ci.projet.gestmissionbackend.entities.Town;
import ci.projet.gestmissionbackend.mappers.AssignmentMapperImpl;
import ci.projet.gestmissionbackend.repositories.TownRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TownServiceImpl implements TownService {
    private TownRepository townRepository;
    private AssignmentMapperImpl dtoMapper;
    @Override
    public List<TownDTO> townDTOList() {
        List<Town> townList = townRepository.findAll();
        List<TownDTO> townDTOS = townList.stream().map(town -> dtoMapper.fromTown(town))
                .collect(Collectors.toList());
        return townDTOS;
    }

    @Override
    public TownDTO findTownById(Long townId) {
        Town town = townRepository.findById(townId).orElse(null);
        TownDTO townDTO = dtoMapper.fromTown(town);
        return townDTO;
    }

    @Override
    public TownDTO addNewTown(TownDTO townDTO) {
        Town town = dtoMapper.fromTownDTO(townDTO);
        Town savedTown = townRepository.save(town);
        return dtoMapper.fromTown(savedTown);
    }

    @Override
    public void deleteTown(Long townId) {
        townRepository.deleteById(townId);
    }

    @Override
    public TownDTO updateTown(TownDTO townDTO) {
        Town town = dtoMapper.fromTownDTO(townDTO);
        Town save = townRepository.save(town);
        return dtoMapper.fromTown(save);
    }

    @Override
    public List<TownDTO> searchTown(String keyword) {
        List<Town> townList = townRepository.findByTownNameContains(keyword);
        List<TownDTO> townDTOS = townList.stream().map(town -> dtoMapper.fromTown(town)).collect(Collectors.toList());
        return townDTOS;
    }
}
