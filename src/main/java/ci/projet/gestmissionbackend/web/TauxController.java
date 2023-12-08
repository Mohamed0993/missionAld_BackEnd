package ci.projet.gestmissionbackend.web;

import ci.projet.gestmissionbackend.dtos.TauxDTO;
import ci.projet.gestmissionbackend.services.TauxService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin()
public class TauxController {

    private TauxService tauxService;

    @PostMapping("/taux")
    @PostAuthorize("hasAuthority('ADMIN')")
    public TauxDTO saveTaux(@RequestBody TauxDTO tauxDTO){
        return this.tauxService.save(tauxDTO);
    }
    @GetMapping("/taux")
    @PostAuthorize("hasAuthority('USER')")
    public TauxDTO getTaux(){
        return this.tauxService.getTaux();
    }
}
