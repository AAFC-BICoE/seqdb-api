package ca.gc.aafc.seqdb.api;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.AbstractContextSource;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Inject
  private ContextSource contextSource;
  
  @Inject
  private LdapProperties ldapProperties;
  
  @Value("${spring.ldap.searchBase:}")
  private String ldapSearchBase;
  
  @Value("${spring.ldap.searchFilter:}")
  private String ldapSearchFilter;
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // If an LDAP URL is specified then enable LDAP auth.
    if (ldapProperties.getUrls() != null) {
      // Set "referral" to "follow" to avoid PartialResultException.
      ((AbstractContextSource) contextSource).setReferral("follow");
      ((AbstractContextSource) contextSource).afterPropertiesSet();
      
      auth.
          ldapAuthentication()
              .userSearchBase(ldapSearchBase)
              .userSearchFilter(ldapSearchFilter)
              .contextSource((BaseLdapPathContextSource) contextSource);
    }
  }
  
}
