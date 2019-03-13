package ca.gc.aafc.seqdb.api.security;

import java.util.Arrays;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.entities.Account;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Loads a Spring UserDetails object from the SeqDB database given a username String. Used by Spring
 * Security to perform password authentication.
 */
@Named
@RequiredArgsConstructor(onConstructor_ = @Inject, access = AccessLevel.PACKAGE)
class SeqdbUserDetailsService implements UserDetailsService {

  @NonNull
  private final AccountRepository accountRepository;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account databaseAccount = accountRepository.findByAccountNameIgnoreCase(username);
    if (databaseAccount == null) {
      throw new UsernameNotFoundException("User does not exist in database: " + username);
    }
    
    return new User(
        databaseAccount.getAccountName(),
        Optional.ofNullable(databaseAccount.getAccountPw()).orElse(""),
        Arrays.asList(() -> databaseAccount.getAccountType().toString().toLowerCase())
    );
  }

}
