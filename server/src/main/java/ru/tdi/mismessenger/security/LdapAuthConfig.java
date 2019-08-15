package ru.tdi.mismessenger.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.NestedLdapAuthoritiesPopulator;

@Configuration
public class LdapAuthConfig {
    @Bean
    public LdapUserSearch ldapUserSearch(LdapContextSource contextSource,
                                         @Value("${app.ldap-search-filter}") String ldapSearchFilter) {
        return new FilterBasedLdapUserSearch("", ldapSearchFilter, contextSource);
    }


    @Bean
    public LdapAuthoritiesPopulator ldapAuthoritiesPopulator(LdapContextSource contextSource) {
        return new NestedLdapAuthoritiesPopulator(contextSource, "cn=users");
    }

    @Bean
    public LdapAuthenticator ldapAuthenticator(LdapContextSource contextSource, LdapUserSearch ldapUserSearch )  {
        BindAuthenticator auth =  new BindAuthenticator(contextSource);
        auth.setUserSearch(ldapUserSearch);
        return auth;
    }

    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider(LdapAuthenticator ldapAuthenticator, LdapAuthoritiesPopulator ldapAuthoritiesPopulator)  {
        return new JWTLdapAuthenticationProvider(ldapAuthenticator,ldapAuthoritiesPopulator);
    }
}
