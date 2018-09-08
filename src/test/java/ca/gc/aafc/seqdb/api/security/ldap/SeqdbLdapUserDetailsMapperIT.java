package ca.gc.aafc.seqdb.api.security.ldap;

import java.util.Collections;

import javax.inject.Inject;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

import org.junit.Test;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.userdetails.UserDetails;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Address;
import ca.gc.aafc.seqdb.entities.EmailAddr;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.People;
import ca.gc.aafc.seqdb.entities.PeopleAddress;

public class SeqdbLdapUserDetailsMapperIT extends BaseIntegrationTest {

  @Inject
  private SeqdbLdapUserDetailsMapper seqdbLdapUserDetailsMapper;
  
  @Inject
  private AccountRepository accountRepository;
  
  @Test
  public void mapUserFromContext_whenUserDoesNotExistInLocalDb_createLocalAccountRecord() {
    JpaSpecificationExecutor<EmailAddr> emailRepo =
        new SimpleJpaRepository<>(EmailAddr.class, entityManager);
    JpaSpecificationExecutor<PeopleAddress> peopleAddressRepo =
        new SimpleJpaRepository<>(PeopleAddress.class, entityManager);
    JpaSpecificationExecutor<AccountsGroup> accountsGroupRepo =
        new SimpleJpaRepository<>(AccountsGroup.class, entityManager);
    
    UserDetails userDetails = this.seqdbLdapUserDetailsMapper.mapUserFromContext(
        testContextAdapter(),
        "mat",
        Collections.emptyList()
    );
    
    assertEquals("mat", userDetails.getUsername());
    assertEquals("", userDetails.getPassword());
    
    Account newAccount = accountRepository.findByAccountNameIgnoreCase("mat");
    assertEquals("mat", newAccount.getAccountName());
    assertEquals("Active", newAccount.getAccountStatus());
    assertEquals(
        "cn=Poff\\, Mathew,ou=Users,ou=ON-NCR-RES,ou=AAFC,dc=AGR,dc=GC,dc=CA",
        newAccount.getLdapDn()
    );
    
    People newPerson = newAccount.getPeople();
    assertEquals("Mat", newPerson.getNameGiven());
    assertEquals("Poff", newPerson.getNameFamily());
    assertTrue(
        newPerson.getNote().contains(
            "Auto-generated LDAP user for " + newAccount.getAccountName() + " on "
        )
    );
    EmailAddr newEmail = emailRepo
        .findOne((root, query, cb) -> cb.equal(root.get("people"), newPerson)).get();
    assertEquals("mat@example.com", newEmail.getEmailAddr());
    assertEquals("Work", newEmail.getEmailType());
    assertTrue(newEmail.getPrimaryEmail());
    
    PeopleAddress newPeopleAddress = peopleAddressRepo
        .findOne((root, query, cb) -> cb.equal(root.get("people"), newPerson)).get();
    assertEquals("Work", newPeopleAddress.getAddrType());
    
    Address newAddress = newPeopleAddress.getAddress();
    assertEquals("960 Carling" + System.getProperty("line.separator") + "100", newAddress.getStreet());
    
    AccountsGroup newGroupOwnerPermissions = accountsGroupRepo
        .findOne((root, query, cb) -> cb.equal(root.get("account"), newAccount)).get();
    assertTrue(newGroupOwnerPermissions.getAdmin());
    assertEquals("1111", newGroupOwnerPermissions.getRights());
    
    Group newGroup = newGroupOwnerPermissions.getGroup();
    assertEquals("mat", newGroup.getGroupName());
    assertEquals("0000", newGroup.getDefaultRights());
  }
  
  @Test
  public void mapUserFromContext_whenUserExistsInLocalDb_returnExistingUserDetails() {
    Account testAccount = new Account();
    testAccount.setAccountName("Mat");
    testAccount.setAccountType("User");
    testAccount.setAccountStatus("Active");
    testAccount.setAccountPw("mypassword");
    testAccount.setLastLogin(null);
    entityManager.persist(testAccount);
    
    UserDetails userDetails = this.seqdbLdapUserDetailsMapper.mapUserFromContext(
        testContextAdapter(),
        "mat",
        Collections.emptyList()
    );
    
    assertEquals("", testAccount.getAccountPw());
    assertNotNull(testAccount.getLastLogin());
    assertEquals(
        "cn=Poff\\, Mathew,ou=Users,ou=ON-NCR-RES,ou=AAFC,dc=AGR,dc=GC,dc=CA",
        testAccount.getLdapDn()
    );
    
    assertEquals("Mat", userDetails.getUsername());
    assertEquals("", userDetails.getPassword());
  }
  
  private DirContextAdapter testContextAdapter() {
    Attributes testLdapAttrs = new BasicAttributes();
    
    testLdapAttrs.put("givenName", "Mat");
    testLdapAttrs.put("sn", "Poff");
    testLdapAttrs.put("userPrincipalName", "mat@example.com");
    testLdapAttrs.put("postalCode", "K1A0C6");
    testLdapAttrs.put("department", "Entomology");
    testLdapAttrs.put("streetAddress", "960 Carling");
    testLdapAttrs.put("physicalDeliveryOfficeName", "100");
    testLdapAttrs.put("l", "locality");
    testLdapAttrs.put("co", "Canada");
    testLdapAttrs.put("st", "Ontario");
    testLdapAttrs.put("facsimileTelephoneNumber", "testFaxNumber");
    testLdapAttrs.put("telephoneNumber", "testPhoneNumber");
    testLdapAttrs.put("mobile", "testMobileNumber");
    
    return new DirContextAdapter(
        testLdapAttrs,
        LdapUtils.newLdapName(
            "cn=Poff\\, Mathew,ou=Users,ou=ON-NCR-RES,ou=AAFC,dc=AGR,dc=GC,dc=CA"
        )
    );
  }
  
}
