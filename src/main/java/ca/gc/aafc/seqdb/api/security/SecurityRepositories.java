package ca.gc.aafc.seqdb.api.security;

import org.springframework.data.repository.Repository;

import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.Country;
import ca.gc.aafc.seqdb.entities.Province;

/**
 * Repositories implemented automatically by default by Spring Data. Used by the Seqdb API's
 * security package.
 */
public class SecurityRepositories {

  public static interface AccountRepository extends Repository<Account, Integer> {
    public Account findByAccountNameIgnoreCase(String accountName);
  }

  public static interface CountryRepository extends Repository<Country, Integer> {
    public Country findByNameIgnoreCase(String name);
    public Country findByAbbrevIgnoreCase(String abbrev);
  }

  public static interface ProvinceRepository extends Repository<Province, Integer> {
    public Province findByNameIgnoreCaseAndCountryId(String name, Integer countryId);
    public Province findByAbbreviationIgnoreCaseAndCountryId(String abbreviation, Integer countryId);
  }
  
}
