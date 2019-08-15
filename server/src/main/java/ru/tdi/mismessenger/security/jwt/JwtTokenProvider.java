package ru.tdi.mismessenger.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.tdi.mismessenger.security.UserPrincipal;

import java.util.ArrayList;
import java.util.Date;

import static java.util.stream.Collectors.toList;

@Component
public class JwtTokenProvider {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("name", userPrincipal.getName())
                .claim("auth", userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public UserPrincipal getUserPrincipalFromJWT(String jwt) {
        Claims cl = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody();
        UserPrincipal user = new UserPrincipal();
        user.setUsername(cl.getSubject());
        user.setName(cl.get("name",String.class));
        cl.get("auth", ArrayList.class).stream().map(o -> new SimpleGrantedAuthority((String) o)).forEach(o -> user.getAuthorities().add((GrantedAuthority)o));
        return user;
    }


    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public String getUserFromJWT(String jwt) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
    }



}
