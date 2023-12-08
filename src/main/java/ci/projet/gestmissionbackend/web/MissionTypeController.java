package ci.projet.gestmissionbackend.web;

import ci.projet.gestmissionbackend.dtos.MissionTypeDTO;
import ci.projet.gestmissionbackend.services.MissionTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin
public class MissionTypeController {
    private MissionTypeService missionTypeService;

    @PostMapping("/types")
    @PostAuthorize("hasAuthority('ADMIN')")
    public MissionTypeDTO saveType(@RequestBody MissionTypeDTO typeDTO){
        return missionTypeService.addNewMissionType(typeDTO);
    }

    @GetMapping("/types")
    @PostAuthorize("hasAuthority('USER')")
    public List<MissionTypeDTO> getMissionType(){
        return missionTypeService.listMissionType();
    }

    @DeleteMapping("/types/{id}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void deleteMissionType(@PathVariable  Long id){
        missionTypeService.deleteMissionType(id);
    }
    @GetMapping("/types/search")
    @PostAuthorize("hasAuthority('USER')")
    public List<MissionTypeDTO> searchMissionType(@RequestParam(name = "keyword", defaultValue = "") String keyword){
        return  missionTypeService.searchMissionType(keyword);
    }

}
