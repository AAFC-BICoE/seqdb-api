package ca.gc.aafc.seqdb.api.security;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Country;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.Province;

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

  public interface CountryRepository extends Repository<Country, Integer> {
    Country findByNameIgnoreCaseOrAbbrevIgnoreCase(String name, String abbrev);
  }

  public interface ProvinceRepository extends Repository<Province, Integer> {
    @Query(
        "select p from Province p where p.countryId = :countryId and "
        + "( lower(p.name) = lower(:name) or lower(p.abbreviation) = lower(:abbrev) ) "
    )
    Province findByCountryIdAndNameOrAbbrev(Integer countryId, String name);
  }

  public interface AccountsGroupRepository extends Repository<AccountsGroup, Integer> {
    AccountsGroup findByAccountAndGroup(Account account, Group group);
  }
  
}
