package ru.tdi.mismessenger.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.web.bind.annotation.*;
import ru.tdi.mismessenger.security.UserPrincipal;

import java.util.Collections;
import java.util.Map;

@RestController
public class JWTController {

    @Autowired
    LdapAuthenticationProvider ldapAuthenticationProvider;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @CrossOrigin
    @RequestMapping(value="getJWT", method = RequestMethod.GET)
    public Map getJWT(Authentication auth) {
        return Collections.singletonMap("token", createJWT(auth));
    }

    @CrossOrigin
    @RequestMapping(value="getJWT", method = RequestMethod.POST)
    public Map getJWT(@RequestBody UserPrincipal user) {
        Authentication auth = ldapAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (auth.isAuthenticated()) return Collections.singletonMap("token", createJWT(auth));
        return null;
    }

    private String createJWT(Authentication auth) {
        return jwtTokenProvider.generateToken(auth);
    }
}
