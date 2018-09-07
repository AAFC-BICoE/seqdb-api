package ca.gc.aafc.seqdb.api.security.keycloak;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.transaction.Transactional;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.People;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * Creates new local Account and Group DB records a Keycloak-authenticated user when they log in for
 * the first time. This filter should be set to run after KeycloakAuthenticationProcessingFilter,
 * which sets the required authentication in the SecurityContext.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class KeycloakAccountRegistrationFilter extends GenericFilterBean {

  @NonNull
  private final AccountRepository accountRepository;

  @NonNull
  private final EntityManager entityManager;

  @Transactional
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    
    Authentication authentication = SecurityContextHolder
        .getContext()
        .getAuthentication();

    if (authentication instanceof KeycloakAuthenticationToken) {

      String accountName = authentication.getName();

      // Get the account from the database, or create a new one if it does not exist.
      Account dbAccount = Optional
          .ofNullable(accountRepository.findByAccountNameIgnoreCase(accountName))
          .orElse(new Account());

      // If the ID is null, this user has no DB Account, so one will be created here.
      if (dbAccount.getAccountId() == null) {

        KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) authentication;
        KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) authToken.getPrincipal();
        AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();

        People newPerson = new People();
        newPerson.setNameGiven(accessToken.getGivenName());
        newPerson.setNameFamily(accessToken.getFamilyName());
        newPerson.setNote(
            "Auto-generated Keycloak user for " + accountName + " on " + new Date().toString()
        );
        entityManager.persist(newPerson);

        // Create the Account
        dbAccount.setAccountName(accountName);
        dbAccount.setAccountType("User");
        dbAccount.setAccountStatus("Active");
        dbAccount.setPeople(newPerson);
        entityManager.persist(dbAccount);

        // Create the Account's Group.
        Group defaultGroup = new Group();
        defaultGroup.setGroupName(dbAccount.getAccountName());
        defaultGroup.setDefaultRights("0000");
        entityManager.persist(defaultGroup);

        // Create the Account's permissions on its Group.
        AccountsGroup groupOwnerPermissions = new AccountsGroup();
        groupOwnerPermissions.setAccount(dbAccount);
        groupOwnerPermissions.setGroup(defaultGroup);
        groupOwnerPermissions.setAdmin(true);
        groupOwnerPermissions.setRights("1111");
        entityManager.persist(groupOwnerPermissions);

      }

    }

    chain.doFilter(request, response);
  }

}
