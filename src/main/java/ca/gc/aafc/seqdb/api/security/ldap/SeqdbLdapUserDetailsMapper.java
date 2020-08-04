package ca.gc.aafc.seqdb.api.security.ldap;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
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

import ca.gc.aafc.seqdb.api.entities.Account;
import ca.gc.aafc.seqdb.api.entities.AccountsGroup;
import ca.gc.aafc.seqdb.api.entities.Group;
import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
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
