package ru.tdi.mismessenger.security;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import java.util.Collection;

public class JWTLdapUserDetailMapper implements UserDetailsContextMapper {

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
                                          Collection<? extends GrantedAuthority> authorities) {
        UserPrincipal user = new UserPrincipal();
        user.setUsername(username);
        user.getAuthorities().addAll(authorities);
        user.setName(ctx.getStringAttribute("cn"));
        return user;
    }

    @Override
    public void mapUserToContext(UserDetails userDetails, DirContextAdapter dirContextAdapter) {
        throw new UnsupportedOperationException(
                "LdapUserDetailsMapper only supports reading from a context. Please"
                        + " use a subclass if mapUserToContext() is required.");
    }
}
