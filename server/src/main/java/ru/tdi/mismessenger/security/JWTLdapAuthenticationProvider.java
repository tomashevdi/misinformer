package ru.tdi.mismessenger.security;

import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

public class JWTLdapAuthenticationProvider extends LdapAuthenticationProvider {

    public JWTLdapAuthenticationProvider(LdapAuthenticator authenticator, LdapAuthoritiesPopulator authoritiesPopulator) {
        super(authenticator, authoritiesPopulator);
        this.userDetailsContextMapper = new JWTLdapUserDetailMapper();
    }

    public JWTLdapAuthenticationProvider(LdapAuthenticator authenticator) {
        super(authenticator);
        this.userDetailsContextMapper = new JWTLdapUserDetailMapper();
    }
}
