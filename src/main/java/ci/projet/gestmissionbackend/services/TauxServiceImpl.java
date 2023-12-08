package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.TauxDTO;
import ci.projet.gestmissionbackend.entities.Taux;
import ci.projet.gestmissionbackend.mappers.AssignmentMapperImpl;
import ci.projet.gestmissionbackend.repositories.TauxRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class TauxServiceImpl implements TauxService {
    private TauxRepository tauxRepository;
    private AssignmentMapperImpl dtoMapper;
    @Override
    public TauxDTO save(TauxDTO tauxDTO) {
        Taux taux = dtoMapper.fromTauxDTO(tauxDTO);
        Taux save = this.tauxRepository.save(taux);
        return dtoMapper.fromTaux(save);
    }

    @Override
    public TauxDTO getTaux() {
        List<Taux> tauxes =this.tauxRepository.findAll();
        List<TauxDTO> tauxDTOS = tauxes.stream().map(taux -> dtoMapper.fromTaux(taux))
                .collect(Collectors.toList());
        return (tauxDTOS.size() !=0 ) ? tauxDTOS.get(0) : null;
    }
}
