package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.GradeDTO;
import ci.projet.gestmissionbackend.entities.Grade;

import java.util.List;

public interface GradeService {

    GradeDTO saveGrade(GradeDTO gradeDTO);
    List<GradeDTO> findAll();

    Grade getDgGrade();

    Grade getChefGrade();

    List<GradeDTO> searchGrade(String keyword);
}
