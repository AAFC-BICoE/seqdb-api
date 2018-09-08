package ca.gc.aafc.seqdb.api.security.ldap;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.stereotype.Component;

import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.api.security.SecurityRepositories.CountryRepository;
import ca.gc.aafc.seqdb.api.security.SecurityRepositories.ProvinceRepository;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Address;
import ca.gc.aafc.seqdb.entities.Country;
import ca.gc.aafc.seqdb.entities.EmailAddr;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.People;
import ca.gc.aafc.seqdb.entities.PeopleAddress;
import ca.gc.aafc.seqdb.entities.Province;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Maps LDAP user details to our own user entities. Creates new local DB records for an LDAP user
 * when they log in for the first time.
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Inject, access = AccessLevel.PACKAGE)
class SeqdbLdapUserDetailsMapper extends LdapUserDetailsMapper {

  @NonNull
  private final PasswordEncoder passwordEncoder;

  @NonNull
  private final EntityManager entityManager;

  @NonNull
  private final AccountRepository accountRepository;

  @NonNull
  private final CountryRepository countryRepository;

  @NonNull
  private final ProvinceRepository provinceRepository;

  @Transactional
  @Override
  public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
      Collection<? extends GrantedAuthority> authorities) {

    String hashedLdapPw = Optional.ofNullable(ctx.getObjectAttribute("userPassword"))
        .map(this::mapPassword)
        .map(passwordEncoder::encode)
        .orElse("");

    // Get the account from the database, or create a new one if it does not exist.
    Account account = Optional
        .ofNullable(accountRepository.findByAccountNameIgnoreCase(username))
        .orElse(new Account());
    
    if (account.getAccountId() == null) { // user doesn't exist
      
      People newPerson = new People();
      newPerson.setNameGiven(ctx.getStringAttribute("givenName"));
      newPerson.setNameFamily(ctx.getStringAttribute("sn"));
      newPerson
          .setNote("Auto-generated LDAP user for " + username + " on " + new Date().toString());
      entityManager.persist(newPerson);

      EmailAddr newEmail = new EmailAddr();
      newEmail.setEmailAddr(ctx.getStringAttribute("userPrincipalName"));
      newEmail.setEmailType("Work");
      newEmail.setPeople(newPerson);
      newEmail.setPrimaryEmail(true);
      entityManager.persist(newEmail);

      Address newAddress = new Address();
      newAddress.setPostalCode(ctx.getStringAttribute("postalCode"));
      newAddress.setOrganisationUnit(ctx.getStringAttribute("department"));
      newAddress.setStreet(
          ctx.getStringAttribute("streetAddress")
          + System.getProperty("line.separator")
          + ctx.getStringAttribute("physicalDeliveryOfficeName")
      );
      newAddress.setLocality(ctx.getStringAttribute("l"));

      String country = ctx.getStringAttribute("co");
      Country pC = countryRepository.findByNameIgnoreCaseOrAbbrevIgnoreCase(country, country);
      if (pC != null) {
        String prov = ctx.getStringAttribute("st");
        newAddress.setCountry(pC);
        Province pP = provinceRepository.findByCountryIdAndNameOrAbbrev(pC.getCountryId(), prov);
        newAddress.setStateProvince(pP);
      }
      entityManager.persist(newAddress);

      PeopleAddress newPAddress = new PeopleAddress();
      newPAddress.setAddrType("Work");
      newPAddress.setPrimaryAddr(true);
      newPAddress.setTelFax(ctx.getStringAttribute("facsimileTelephoneNumber"));
      newPAddress.setTelVoice1(ctx.getStringAttribute("telephoneNumber"));
      newPAddress.setTelCell(ctx.getStringAttribute("mobile"));
      newPAddress.setAddress(newAddress);
      newPAddress.setPeople(newPerson);
      entityManager.persist(newPAddress);

      // Create the Account
      account.setAccountName(username);
      account.setAccountType("User");
      account.setAccountStatus("Active");
      account.setAccountPw(hashedLdapPw);
      account.setLastLogin(new Timestamp(System.currentTimeMillis()));
      account.setLdapDn(ctx.getNameInNamespace());
      account.setPeople(newPerson);
      entityManager.persist(account);

      Group defaultGroup = new Group();
      defaultGroup.setGroupName(account.getAccountName());
      defaultGroup.setDefaultRights("0000");
      entityManager.persist(defaultGroup);

      AccountsGroup groupOwnerPermissions = new AccountsGroup();
      groupOwnerPermissions.setAccount(account);
      groupOwnerPermissions.setGroup(defaultGroup);
      groupOwnerPermissions.setAdmin(true);
      groupOwnerPermissions.setRights("1111");
      entityManager.persist(groupOwnerPermissions);
      
    } else { // user exists
      
      account.setAccountPw(hashedLdapPw);
      account.setLastLogin(new Timestamp(System.currentTimeMillis()));
      // maybe an existing user is logging in through LDAP for first time
      account.setLdapDn(ctx.getNameInNamespace());
      
    }

    return new User(
        account.getAccountName(),
        account.getAccountPw(),
        Arrays.asList(() -> account.getAccountType().toString().toLowerCase())
    );
  }

}
