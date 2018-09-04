package ca.gc.aafc.seqdb.api.security.ldap;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;

public class LdapAndLocalDbAuthConfigIT extends BaseIntegrationTest {

  @Inject
  private WebSecurityConfigurerAdapter webSecurityConfigurerAdapter;
  
  /**
   * Check that when "keycloak.enabled" is false or not passed in as an argument to seqdb, the
   * LdapAndLocalDbAuthConfig is used. This is because LdapAndLocalDbAuthConfig should be the
   * default WebSecurityConfigurerAdapter bean when no other one is specified.
   */
  @Test
  public void getWebSecurityConfigurerAdapterBean_whenKeycloakEnabledNotSet_getKeycloakWebSecurityConfigurerAdapter() {
    assertTrue(webSecurityConfigurerAdapter instanceof LdapAndLocalDbAuthConfig);
  }
  
}
