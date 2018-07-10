package ca.gc.aafc.seqdb.api;

import javax.inject.Inject;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.AbstractContextSource;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import ca.gc.aafc.seqdb.api.security.SeqdbDaoAuthenticationProvider;
import ca.gc.aafc.seqdb.api.security.SeqdbLdapUserDetailsMapper;

@Configuration
@EnableJpaRepositories(considerNestedRepositories = true)
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Configuration
  @ConditionalOnProperty(value = "keycloak.enabled", havingValue = "false")
  public static class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {

    @Inject
    private ContextSource contextSource;

    @Inject
    private LdapProperties ldapProperties;

    @Value("${spring.ldap.searchBase:}")
    private String ldapSearchBase;

    @Value("${spring.ldap.searchFilter:}")
    private String ldapSearchFilter;

    @Inject
    private SeqdbDaoAuthenticationProvider seqdbAuthenticationProvider;

    @Inject
    private SeqdbLdapUserDetailsMapper seqdbLdapUserDetailsMapper;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth
          .authenticationProvider(seqdbAuthenticationProvider);

      // If an LDAP URL is specified then enable LDAP auth.
      if (ldapProperties.getUrls() != null) {
        // Set "referral" to "follow" to avoid PartialResultException.
        ((AbstractContextSource) contextSource).setReferral("follow");
        ((AbstractContextSource) contextSource).afterPropertiesSet();

        auth
            .ldapAuthentication()
            .userSearchBase(ldapSearchBase)
            .userSearchFilter(ldapSearchFilter)
            .userDetailsContextMapper(seqdbLdapUserDetailsMapper)
            .contextSource((BaseLdapPathContextSource) contextSource);
      }
    }

  }

  @Configuration
  @ConditionalOnProperty(value = "keycloak.enabled", matchIfMissing = true)
  public static class KeycloakWebSecurityConfigurerAdapterImpl
      extends KeycloakWebSecurityConfigurerAdapter {

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      KeycloakAuthenticationProvider keycloakAuthProvider = keycloakAuthenticationProvider();
      keycloakAuthProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
      auth.authenticationProvider(keycloakAuthProvider);
    }

    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
      return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
      return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
      super.configure(http);
      http
          .authorizeRequests()
          .anyRequest().authenticated();
    }

  }

}
