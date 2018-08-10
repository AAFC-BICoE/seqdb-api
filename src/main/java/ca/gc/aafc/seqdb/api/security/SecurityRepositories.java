package ca.gc.aafc.seqdb.api.security;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Country;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.Province;

/**
 * Repositories implemented automatically by default by Spring Data. Used by the Seqdb API's
 * security package.
 */
class SecurityRepositories {

  public static interface AccountRepository extends Repository<Account, Integer> {
    public Account findByAccountNameIgnoreCase(String accountName);
  }

  public static interface CountryRepository extends Repository<Country, Integer> {
    public Country findByNameIgnoreCaseOrAbbrevIgnoreCase(String name, String abbrev);
  }

  public static interface ProvinceRepository extends Repository<Province, Integer> {
    @Query(
        "select p from Province p where p.countryId = :countryId and "
        + "( lower(p.name) = lower(:name) or lower(p.abbreviation) = lower(:abbrev) ) "
    )
    public Province findByCountryIdAndNameOrAbbrev(Integer countryId, String name);
  }
  
  public static interface AccountsGroupRepository extends Repository<AccountsGroup, Integer> {
    public AccountsGroup findByAccountAndGroup(Account account, Group group);
  }
  
}
