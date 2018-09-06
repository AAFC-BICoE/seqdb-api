package ca.gc.aafc.seqdb.api.security.keycloak;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;

import org.junit.Test;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.collect.Sets;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.People;

public class KeycloakAccountRegistrationFilterIT extends BaseIntegrationTest {

  @Inject
  private AutowireCapableBeanFactory beanFactory;
  
  @Inject
  private AccountRepository accountRepository;
  
  private static String TEST_ACCOUNT_NAME = "testKeycloakAccount";
  
  @Test
  public void runFilterWithKeycloakAuth_whenAccountDoesntExistInLocalDb_createLocalAccountRecord()
      throws IOException, ServletException {
    JpaSpecificationExecutor<AccountsGroup> accountsGroupRepo =
        new SimpleJpaRepository<>(AccountsGroup.class, entityManager);
    
    KeycloakAccountRegistrationFilter filter = beanFactory
        .createBean(KeycloakAccountRegistrationFilter.class);
    
    // Set a dummy KeycloakAuthenticationToken authentication with just the data used by the test.
    SecurityContextHolder.getContext().setAuthentication(this.testKeycloakAuthToken());
    
    // Do the filter
    filter.doFilter(
        new MockHttpServletRequest(),
        new MockHttpServletResponse(),
        new MockFilterChain()
    );
    
    Account newAccount = accountRepository.findByAccountNameIgnoreCase(TEST_ACCOUNT_NAME);
    assertEquals(TEST_ACCOUNT_NAME, newAccount.getAccountName());
    assertEquals("Active", newAccount.getAccountStatus());
    
    People newPerson = newAccount.getPeople();
    assertEquals("Mat", newPerson.getNameGiven());
    assertEquals("Poff", newPerson.getNameFamily());
    assertTrue(
        newPerson.getNote().contains(
            "Auto-generated Keycloak user for " + newAccount.getAccountName() + " on "
        )
    );
    
    AccountsGroup newGroupOwnerPermissions = accountsGroupRepo
        .findOne((root, query, cb) -> cb.equal(root.get("account"), newAccount)).get();
    assertTrue(newGroupOwnerPermissions.getAdmin());
    assertEquals("1111", newGroupOwnerPermissions.getRights());
    
    Group newGroup = newGroupOwnerPermissions.getGroup();
    assertEquals(TEST_ACCOUNT_NAME, newGroup.getGroupName());
    assertEquals("0000", newGroup.getDefaultRights());
  }
  
  @Test
  public void runFilterWithKeycloakAuth_whenAccountExistsInLocalDb_doNothing()
      throws IOException, ServletException {
    // Create an account with the 
    Account newAccount = new Account();
    newAccount.setAccountName(TEST_ACCOUNT_NAME);
    newAccount.setAccountType("User");
    newAccount.setAccountStatus("Active");
    entityManager.persist(newAccount);
    
    KeycloakAccountRegistrationFilter filter = beanFactory
        .createBean(KeycloakAccountRegistrationFilter.class);
    
    SecurityContextHolder.getContext().setAuthentication(this.testKeycloakAuthToken());
    
    // Do the filter
    filter.doFilter(
        new MockHttpServletRequest(),
        new MockHttpServletResponse(),
        new MockFilterChain()
    );
  }
  
  private KeycloakAuthenticationToken testKeycloakAuthToken() {
    // Create a test access token with a first and last name.
    AccessToken accessToken = new AccessToken();
    accessToken.setGivenName("Mat");
    accessToken.setFamilyName("Poff");
    
    return new KeycloakAuthenticationToken(
        new SimpleKeycloakAccount(
            new KeycloakPrincipal<KeycloakSecurityContext>(
                TEST_ACCOUNT_NAME,
                new KeycloakSecurityContext(null, accessToken, null, null)
            ),
            Sets.newHashSet("user"),
            null
        ),
        false
    );
  }
  
}
