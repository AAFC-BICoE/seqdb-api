package ca.gc.aafc.seqdb.api.security;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import ca.gc.aafc.seqdb.entities.Account;
import lombok.NonNull;

/**
 * Extends DaoAuthenticationProvider; Authenticates with LDAP when the user is an LDAP user, and
 * authenticates with a local account record otherwise.
 */
@Named
public class SeqdbDaoAuthenticationProvider extends DaoAuthenticationProvider {

  @NonNull
  private EntityManager entityManager;

  @Inject
  public SeqdbDaoAuthenticationProvider(
      EntityManager entityManager,
      UserDetailsService userDetailsService
  ) throws Exception {
    super();
    this.entityManager = entityManager;
    this.setUserDetailsService(userDetailsService);
    this.setPasswordEncoder(new BCryptPasswordEncoder());
    this.afterPropertiesSet();
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // Do a JPA query for the account.
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Account> criteriaQuery = cb.createQuery(Account.class);
    Root<Account> root = criteriaQuery.from(Account.class);
    criteriaQuery.where(cb.equal(root.get("accountName"), authentication.getName()));
    
    Account databaseAccount;
    try {
      databaseAccount = entityManager.createQuery(criteriaQuery).getSingleResult();
    } catch(NoResultException e) {
      // If the account is not found in the database, carry on as usual (Authenticate using the
      // credentials stored in the database).
      return super.authenticate(authentication);
    }
    
    if (StringUtils.isEmpty(databaseAccount.getLdapDn())){
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
