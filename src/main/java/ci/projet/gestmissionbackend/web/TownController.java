package ci.projet.gestmissionbackend.web;

import ci.projet.gestmissionbackend.dtos.TownDTO;
import ci.projet.gestmissionbackend.services.TownService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin
public class TownController {
    private TownService townService;

    @PostMapping("/towns")
    @PostAuthorize("hasAuthority('ADMIN')")
    public TownDTO saveTown(@RequestBody TownDTO townDTO){
        return townService.addNewTown(townDTO);
    }
    @GetMapping("/towns")
    @PostAuthorize("hasAuthority('USER')")
    public List<TownDTO> listTown(){
        return townService.townDTOList();
    }

    @GetMapping("/towns/search")
    @PostAuthorize("hasAuthority('USER')")
    public List<TownDTO> searchTown( @RequestParam(name = "keyword" , defaultValue = "") String keyword){
        return townService.searchTown(keyword);
    }
    @GetMapping("/towns/{townId}")
    @PostAuthorize("hasAuthority('USER')")
    public TownDTO getTown(@PathVariable Long townId){
        return townService.findTownById(townId);
    }
    @PutMapping("/towns/{townId}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public TownDTO updateTown(@PathVariable Long townId, @RequestBody TownDTO townDTO){
        townDTO.setTownId(townId);
        return townService.updateTown(townDTO);
    }
    @DeleteMapping("/towns/{townId}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void deleteTown(@PathVariable Long townId){
        townService.deleteTown(townId);
    }

}
