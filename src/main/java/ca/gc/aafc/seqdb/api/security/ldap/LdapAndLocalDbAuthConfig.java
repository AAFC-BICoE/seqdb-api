package ca.gc.aafc.seqdb.api.security.ldap;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.AbstractContextSource;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import ca.gc.aafc.seqdb.api.security.SeqdbDaoAuthenticationProvider;
import ca.gc.aafc.seqdb.api.security.trustedservice.TrustedServiceAuthenticationFilter;
import ca.gc.aafc.seqdb.api.security.trustedservice.TrustedServiceAuthenticationProvider;


/**
 * Configures authentication with LDAP, with fallback to local Accounts stored in the database when
 * the user is not an LDAP user.
 */
@Configuration
// This is the default auth config when no other WebSecurityConfigurerAdapter is used.
@ConditionalOnProperty(value = "keycloak.enabled", havingValue = "false")
public class LdapAndLocalDbAuthConfig extends WebSecurityConfigurerAdapter {

  @Inject
  private ContextSource contextSource;

  @Inject
  private LdapProperties ldapProperties;

  @Value("${spring.ldap.searchBase:}")
  private String ldapSearchBase;

  @Value("${spring.ldap.searchFilter:}")
  private String ldapSearchFilter;

  @Inject
  private SeqdbDaoAuthenticationProvider daoAuthProvider;
  
  @Value("${seqdb.trusted-service-api-keys:}")
  private String[] apiKeys;

  @Inject
  private SeqdbLdapUserDetailsMapper seqdbLdapUserDetailsMapper;
  
  @Inject
  private UserDetailsService userDetailsService;
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    
    // Add trusted service auth as the first AuthenticationProvider if enabled.
    if (apiKeys.length > 0) {
      auth.authenticationProvider(
          new TrustedServiceAuthenticationProvider(apiKeys, userDetailsService));
    }
    
    // Add authentication against database as the second AuthenticationProvider.
    auth.authenticationProvider(daoAuthProvider);

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
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http.csrf().disable();
    
    // Add trusted service auth filter if enabled. 
    if (apiKeys.length > 0) {
      http.addFilterAfter(
          new TrustedServiceAuthenticationFilter(authenticationManager()),
          BasicAuthenticationFilter.class
      );
    }
  }
  
  /**
   * Exposes the authentication manager as a bean.
   */
  @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
  
  /**
   * Separate config class to provide the PasswordEncoder because providing it from
   * LdapAndLocalDbAuthConfig would create a circular dependency with the
   * SeqdbDaoAuthenticationProvider.
   */
  @Configuration
  public static class PasswordEncoderConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }
  }

}