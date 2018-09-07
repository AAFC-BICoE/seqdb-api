package ca.gc.aafc.seqdb.api.security.keycloak;

import javax.inject.Inject;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * Configures authentication with Keycloak.
 */
@Configuration
// This config is enabled when the "keycloak.enabled" property is set to true or is missing. 
@ConditionalOnProperty(value = "keycloak.enabled", matchIfMissing = true)
public class KeycloakAuthConfig extends KeycloakWebSecurityConfigurerAdapter {

  @Inject
  private AutowireCapableBeanFactory beanFactory;
  
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
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .addFilterAfter(
            beanFactory.createBean(KeycloakAccountRegistrationFilter.class),
            KeycloakAuthenticationProcessingFilter.class
        );
  }

}