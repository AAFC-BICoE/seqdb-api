package ca.gc.aafc.seqdb.api.security;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
  private final EntityManager entityManager;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Do a JPA query for the account.
    CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Account> criteriaQuery = cb.createQuery(Account.class);
    Root<Account> root = criteriaQuery.from(Account.class);
    criteriaQuery.where(cb.equal(root.get("accountName"), username));
    
    Account databaseAccount;
    try {
      databaseAccount = this.entityManager.createQuery(criteriaQuery).getSingleResult();
    } catch(NoResultException e) {
      throw new UsernameNotFoundException("User does not exist in database: " + username);
    }
    
    return new User(
        databaseAccount.getAccountName(),
        databaseAccount.getAccountPw(),
        Arrays.asList(() -> databaseAccount.getAccountType().toString().toLowerCase())
    );
  }

}
