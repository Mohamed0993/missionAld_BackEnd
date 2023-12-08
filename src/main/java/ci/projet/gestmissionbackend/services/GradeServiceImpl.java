package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.GradeDTO;
import ci.projet.gestmissionbackend.entities.Grade;
import ci.projet.gestmissionbackend.enums.GradeType;
import ci.projet.gestmissionbackend.mappers.PersonalMapperImpl;
import ci.projet.gestmissionbackend.repositories.GradeRepository;
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

public class GradeServiceImpl implements GradeService {
    private GradeRepository gradeRepository;
    private PersonalMapperImpl dtoMapper;
    @Override
    public GradeDTO saveGrade(GradeDTO gradeDTO) {
        Grade grade = dtoMapper.fromGradeDTO(gradeDTO);
        Grade savedGrade = this.gradeRepository.save(grade);

        return dtoMapper.fromGrade(savedGrade);
    }

    @Override
    public List<GradeDTO> findAll() {
        List<Grade> gradeList = gradeRepository.findAll();
        List<GradeDTO> gradeDTOS = gradeList.stream().map(grade -> dtoMapper.fromGrade(grade))
                .collect(Collectors.toList());
        return gradeDTOS;
    }

    @Override
    public Grade getDgGrade(){return gradeRepository.findByType(GradeType.DG);}
    @Override
    public Grade getChefGrade() {
        return this.gradeRepository.findByType(GradeType.CHEF);
    }

    @Override
    public List<GradeDTO> searchGrade(String keyword) {
        List<Grade> gradeList = gradeRepository.findByLabelContains(keyword);
        List<GradeDTO> gradeDTOS = gradeList.stream().map(grade -> dtoMapper.fromGrade(grade)).collect(Collectors.toList());
        return gradeDTOS;
    }

}
