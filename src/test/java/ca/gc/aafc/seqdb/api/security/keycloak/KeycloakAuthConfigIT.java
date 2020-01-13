package ca.gc.aafc.seqdb.api.security.keycloak;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.TestPropertySource;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;

@TestPropertySource(properties= {"keycloak.enabled: true"})
public class KeycloakAuthConfigIT extends BaseIntegrationTest{

  @Inject
  private WebSecurityConfigurerAdapter webSecurityConfigurerAdapter;

  /**
   * Check that when "keycloak.enabled: true", the KeycloakWebSecurityConfigurerAdapter is used.
   */
  @Test
  public void getWebSecurityConfigurerAdapterBean_whenKeycloakEnabled_getKeycloakWebSecurityConfigurerAdapter() {
    assertTrue(webSecurityConfigurerAdapter instanceof KeycloakAuthConfig);
  }

}
