package ci.projet.gestmissionbackend.config;

import ci.projet.gestmissionbackend.JWTUtil;
import ci.projet.gestmissionbackend.services.UserDetailsServiceImpl;
import ci.projet.gestmissionbackend.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


       final String header =  request.getHeader(JWTUtil.AUTH_HEADER);
       String jwtToken = null;
       String matricule = null;
       if(header !=null && header.startsWith(JWTUtil.PREFIX)){
           jwtToken=  header.substring(JWTUtil.PREFIX.length());

           try{
               matricule=  jwtUtil.getMatriculeFromToken(jwtToken);

           }catch (IllegalArgumentException e){
               System.out.println("Unable to get JWT Token");
           } catch (ExpiredJwtException e) {
               System.out.println("Jwt token is expired");
           }
       } else {
           System.out.println("Jwt token does not start with Bearer");
       }
       if(matricule != null && SecurityContextHolder.getContext().getAuthentication() == null){
           UserDetails userDetails= userDetailsService.loadUserByUsername(matricule);

           if(jwtUtil.validateToken(jwtToken, userDetails)){
               UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                       new UsernamePasswordAuthenticationToken(userDetails,
                       null,
                       userDetails.getAuthorities());
               usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
           }
       }
       filterChain.doFilter(request,response);

    }
}
