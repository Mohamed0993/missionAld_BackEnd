package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.PersonalDTO;
import ci.projet.gestmissionbackend.entities.JwtRequest;
import ci.projet.gestmissionbackend.entities.JwtResponse;
import ci.projet.gestmissionbackend.exceptions.PersonalNotFoundException;
import ci.projet.gestmissionbackend.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Data
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private PersonalService personalService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    private PersonalDTO personalDTO;

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String matricule = jwtRequest.getMatricule();
        String password = jwtRequest.getPassword();
        authenticate(matricule, password);

        final UserDetails userDetails = loadUserByUsername(matricule);

        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        PersonalDTO personal = personalService.findPersonalByMatricule(matricule);

       return new JwtResponse(personal, newGeneratedToken);
    }
    @Override
    public UserDetails loadUserByUsername(String matricule) throws UsernameNotFoundException {
         PersonalDTO personal = null;
        try {
            personal = personalService.findPersonalByMatricule(matricule);
        } catch (PersonalNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(personal != null){
            return new User(
                    personal.getPersonalMatricule(),
                    personal.getPassword(),
                    getAuthorities(personal)
            );
        } else {
            throw new UsernameNotFoundException("Matricule is not valid");
        }

    }
    private Set getAuthorities(PersonalDTO personal){
         Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        personal.getPersonalProfileDTOS().forEach(role->{
            authorities.add(new SimpleGrantedAuthority(role.getType().getLabel()));
        });
        return authorities;
    }

    private void authenticate(String matricule, String password) throws Exception {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(matricule,password));

        }catch (DisabledException e){
            throw new Exception("User is disabled",e);
        } catch (BadCredentialsException e) {
            throw new Exception("Bad credentials from personal",e);
        }

    }
}
