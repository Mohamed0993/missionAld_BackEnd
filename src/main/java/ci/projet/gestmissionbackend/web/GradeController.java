package ci.projet.gestmissionbackend.web;

import ci.projet.gestmissionbackend.dtos.GradeDTO;
import ci.projet.gestmissionbackend.services.GradeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin
public class GradeController {
    private GradeService gradeService;

    @GetMapping("/grades/search")
    @PostAuthorize("hasAuthority('USER')")
    public List<GradeDTO> searchGrade(@RequestParam(name ="keyword", defaultValue = "") String keyword){
        return gradeService.searchGrade(keyword);
    }
    @GetMapping("/grades")
    @PostAuthorize("hasAuthority('USER')")
    public List<GradeDTO> getGrade(){
        return gradeService.findAll();
    }

}
