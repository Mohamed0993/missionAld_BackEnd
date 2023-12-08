package ci.projet.gestmissionbackend.web;

import ci.projet.gestmissionbackend.dtos.TownDTO;
import ci.projet.gestmissionbackend.dtos.WorksiteDTO;
import ci.projet.gestmissionbackend.exceptions.worksiteNotFoundException;
import ci.projet.gestmissionbackend.services.WorksiteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin
public class WorksiteController {
    private WorksiteService worksiteService;

    @PostMapping("/worksites")
    @PostAuthorize("hasAuthority('ADMIN')")
    public WorksiteDTO saveWorksite(@RequestBody WorksiteDTO worksiteDTO){

        return worksiteService.addNewWorksite(worksiteDTO);
    }
    @GetMapping("/worksites/search")
    @PostAuthorize("hasAuthority('USER')")
    public List<WorksiteDTO> searchWorksite( @RequestParam(name="keyword", defaultValue = "") String keyword){
        return worksiteService.searchWorksite(keyword);
    }

    @GetMapping("/worksites")
    @PostAuthorize("hasAuthority('USER')")
    public List<WorksiteDTO> worksites(){

        return worksiteService.worksiteDTOList();
    }
    @GetMapping("/worksites/{worksiteId}")
    @PostAuthorize("hasAuthority('USER')")
    public WorksiteDTO getWorksite(@PathVariable Long worksiteId) throws worksiteNotFoundException {
        return worksiteService.findWorksiteByWorksiteId(worksiteId);
    }
    @PutMapping("/worksites/{worksiteId}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public WorksiteDTO updateWorksite(@PathVariable Long worksiteId, @RequestBody WorksiteDTO worksiteDTO){
        worksiteDTO.setWorksiteId(worksiteId);
        return worksiteService.updateWorksite(worksiteDTO);
    }
    @DeleteMapping("/worksites/{worksiteId}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void deleteWorksite(@PathVariable Long worksiteId){
        worksiteService.deleteWorksite(worksiteId);
    }
}
