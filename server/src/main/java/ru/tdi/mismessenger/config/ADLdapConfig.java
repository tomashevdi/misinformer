package ru.tdi.mismessenger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.kerberos.client.config.SunJaasKrb5LoginConfig;
import org.springframework.security.kerberos.client.ldap.KerberosLdapContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.NestedLdapAuthoritiesPopulator;
import ru.tdi.mismessenger.security.JWTLdapAuthenticationProvider;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ADLdapConfig {
    @Value("${app.ad-domain}")
    private String adDomain;

    @Value("${app.ad-server}")
    private String adServer;

    @Value("${app.service-principal}")
    private String servicePrincipal;

    @Value("${app.keytab-location}")
    private String keytabLocation;

    @Value("${app.ldap-search-base}")
    private String ldapSearchBase;

    @Value("${app.ldapUser}")
    private String ldapUser;

    @Value("${app.ldapPassword}")
    private String ldapPassword;

    @Value("${app.ldap-search-filter}")
    private String ldapSearchFilter;

    @Bean
    @Primary
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(adServer);
        contextSource.setBase(ldapSearchBase);
        contextSource.setUserDn(ldapUser);
        contextSource.setPassword(ldapPassword);
        Map<String,Object> baseEnvironmentProperties = new HashMap<>();
        baseEnvironmentProperties.put("java.naming.ldap.attributes.binary", "objectSid objectGuid");
        contextSource.setBaseEnvironmentProperties(baseEnvironmentProperties);
        contextSource.afterPropertiesSet();
        return contextSource;
    }


    @Bean
    public LdapTemplate ldapTemplate(LdapContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }

    // Alternative - kerberos LDAP auth
    @Bean
    public KerberosLdapContextSource kerberosLdapContextSource() {
        KerberosLdapContextSource contextSource = new KerberosLdapContextSource(adServer);
        contextSource.setLoginConfig(loginConfig());
        Map<String,Object> baseEnvironmentProperties = new HashMap<>();
        baseEnvironmentProperties.put("java.naming.ldap.attributes.binary", "objectSid objectGuid");
        contextSource.setBaseEnvironmentProperties(baseEnvironmentProperties);
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    @Bean
    public SunJaasKrb5LoginConfig loginConfig() {
        SunJaasKrb5LoginConfig loginConfig = new SunJaasKrb5LoginConfig();
        loginConfig.setKeyTabLocation(new FileSystemResource(keytabLocation));
        loginConfig.setServicePrincipal(servicePrincipal);
        loginConfig.setDebug(true);
        loginConfig.setIsInitiator(true);
        return loginConfig;
    }

}
