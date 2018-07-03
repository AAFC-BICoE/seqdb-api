package ca.gc.aafc.seqdb.api.security;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ca.gc.aafc.seqdb.entities.Account;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Loads a Spring UserDetails object from the SeqDB database given a username String.
 */
@Named
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SeqdbUserDetailsService implements UserDetailsService {

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
        databaseAccount.getAccountPw(),
        Arrays.asList(() -> databaseAccount.getAccountType().toString().toLowerCase())
    );
  }

}
