package ca.gc.aafc.seqdb.api.security;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.entities.Account;
import lombok.NonNull;

/**
 * Extends DaoAuthenticationProvider; Authenticates with LDAP when the user is an LDAP user, and
 * authenticates with a local account record otherwise.
 */
@Named
public class SeqdbDaoAuthenticationProvider extends DaoAuthenticationProvider {

  @NonNull
  private final AccountRepository accountRepository;

  @Inject
  public SeqdbDaoAuthenticationProvider(
      UserDetailsService userDetailsService,
      AccountRepository accountRepository,
      PasswordEncoder passwordEncoder
  ) throws Exception {
    super();
    this.accountRepository = accountRepository;
    this.setUserDetailsService(userDetailsService);
    this.setPasswordEncoder(passwordEncoder);
    this.afterPropertiesSet();
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Account databaseAccount = accountRepository
        .findByAccountNameIgnoreCase(authentication.getName());

    if (databaseAccount == null || StringUtils.isEmpty(databaseAccount.getLdapDn())) {
      // If they are not an LDAP user, carry on as usual (Authenticate using the credentials stored
      // in the database).
      return super.authenticate(authentication);
    } else {
      // Returns null when this provider cannot handle the authentication. This causes the next
      // AuthenticationProvider to be used instead. In this case when the account is found to have
      // an LDAP DN, we try to authenticate the account using Spring's LDAP.
      return null;
    }
  }

}
