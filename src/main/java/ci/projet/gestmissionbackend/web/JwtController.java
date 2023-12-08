package ci.projet.gestmissionbackend.web;

import ci.projet.gestmissionbackend.entities.JwtRequest;
import ci.projet.gestmissionbackend.entities.JwtResponse;
import ci.projet.gestmissionbackend.services.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@AllArgsConstructor
public class JwtController {
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody  JwtRequest jwtRequest) throws Exception {
        return userDetailsService.createJwtToken(jwtRequest);

    }

}
