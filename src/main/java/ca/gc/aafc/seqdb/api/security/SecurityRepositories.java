package ca.gc.aafc.seqdb.api.security;

import org.springframework.data.repository.Repository;

import ca.gc.aafc.seqdb.api.entities.Account;
import ca.gc.aafc.seqdb.api.entities.AccountsGroup;
import ca.gc.aafc.seqdb.api.entities.Group;

/**
 * Repositories used by the Seqdb API's security package.
 *
 * Implemented automatically by default by Spring Data:
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 */
public final class SecurityRepositories {

  private SecurityRepositories() { }

  public interface AccountRepository extends Repository<Account, Integer> {
    Account findByAccountNameIgnoreCase(String accountName);
  }

  public interface AccountsGroupRepository extends Repository<AccountsGroup, Integer> {
    AccountsGroup findByAccountAndGroup(Account account, Group group);
  }
  
}
