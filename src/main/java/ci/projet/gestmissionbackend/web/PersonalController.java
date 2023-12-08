package ci.projet.gestmissionbackend.web;

import ci.projet.gestmissionbackend.JWTUtil;
import ci.projet.gestmissionbackend.dtos.BankAccountDTO;
import ci.projet.gestmissionbackend.dtos.PersonalDTO;
import ci.projet.gestmissionbackend.entities.Personal;
import ci.projet.gestmissionbackend.exceptions.PersonalNotFoundException;
import ci.projet.gestmissionbackend.services.PersonalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin
public class PersonalController {
    private PersonalService personalService;

    @GetMapping("/personals")
    @PostAuthorize("hasAuthority('USER')")
    public List<PersonalDTO> personals(){return this.personalService.listPersonal();}

    @PostMapping("/personals")
    @PostAuthorize("hasAuthority('ADMIN')")
    public PersonalDTO savePersonal (@RequestBody PersonalDTO personalDTO) throws Exception {
        return this.personalService.addNewPersonal(personalDTO);
    }
    @GetMapping("/personals/{matricule}")
    @PostAuthorize("hasAuthority('USER')")
    public PersonalDTO getPersonal(@PathVariable (name = "matricule") String personalMatricule) throws PersonalNotFoundException {
        return this.personalService.findPersonalByMatricule(personalMatricule);
    }
    @GetMapping("/personals/search")
    @PostAuthorize("hasAuthority('USER')")
    public List<PersonalDTO> searchPersonal(@RequestParam(name = "keyword", defaultValue = "") String keyword) throws PersonalNotFoundException {
        return personalService.searchPersonal(keyword);
    }
    @DeleteMapping("/personals/{id}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void deletePersonal(@PathVariable (name = "id") Long id) throws Exception {personalService.deletePersonal(id);}

/*    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)throws Exception{
        String authToken = request.getHeader(JWTUtil.AUTH_HEADER);
        if(authToken!=null && authToken.startsWith(JWTUtil.PREFIX)) {
            try {
                String jwt = authToken.substring(JWTUtil.PREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String matricule = decodedJWT.getSubject();
                PersonalDTO personalDTO = personalService.findPersonalByMatricule(matricule);
                String jwtAccessToken = JWT.create()
                        .withSubject(personalDTO.getPersonalMatricule())
                        .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_ACCESS_TOKEN))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", personalDTO.getPersonalProfileDTOS().stream().map(r -> r.getType().getLabel()).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> idToken = new HashMap<>();
                idToken.put("jwtToken", jwtAccessToken);
                idToken.put("refresh-token", jwt);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), idToken);
            } catch (Exception e) {
                throw e;
            }
        } else {
            throw new RuntimeException("Refresh token required!!!");
        }

    }*/
    @GetMapping("/profile")
    @PostAuthorize("hasAuthority('USER')")
    public PersonalDTO profil(Principal principal) throws PersonalNotFoundException {
        return personalService.findPersonalByMatricule(principal.getName());
    }

    @PostMapping()
    public BankAccountDTO saveBankAccount(@RequestBody BankAccountDTO bankAccountDTO){
        return this.personalService.addNewBankAccount(bankAccountDTO);
    }

}
