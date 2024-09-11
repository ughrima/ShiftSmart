package backend.example.backend.Security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import org.springframework.stereotype.Component;

//handles generating,validating and extracting info from JWT Tokens
@Component //makes it injectable
public class JWTTokenProvider {
    
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    //creates new tokens
    public String generateToken(String username){
        Date now=new Date();
        Date expiryDate=new Date(now.getTime()+expiration);

        return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
        
    }

    //gets username from the given JWT Token
    public String getUsernameFromJWT(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //validates the token
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }
        catch(MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex){
            return false;
        }
    }

}
