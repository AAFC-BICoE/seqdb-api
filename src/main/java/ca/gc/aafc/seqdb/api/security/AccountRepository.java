package ca.gc.aafc.seqdb.api.security;

import org.springframework.data.repository.Repository;

import ca.gc.aafc.seqdb.entities.Account;

/**
 * Account repository implemented automatically by default by Spring Data.
 */
public interface AccountRepository extends Repository<Account, Integer> {

  /**
   * Find an Account by the account name (case-insensitive).
   * @param accountName the account name
   * @return the Account with that name, or null if no Account has that name.
   */
  public Account findByAccountNameIgnoreCase(String accountName);

}
