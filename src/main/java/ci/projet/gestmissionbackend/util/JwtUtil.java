package ci.projet.gestmissionbackend.util;

import ci.projet.gestmissionbackend.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {



    public String getMatriculeFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);

    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver){
       final Claims claims =  getAllClaimsFromToken(token);
       return claimResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(JWTUtil.SECRET).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails){
       String matricule = getMatriculeFromToken(token);
       return (matricule.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
       final  Date expirationDate = getExpirationDateFromToken(token);
       return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_ACCESS_TOKEN))
                .signWith(SignatureAlgorithm.HS512, JWTUtil.SECRET)
                .compact()
                ;

    }
}
