package ca.gc.aafc.seqdb.api.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.entities.Account;

public class SeqdbUserDetailsServiceIT extends BaseIntegrationTest {

  @Inject
  private SeqdbUserDetailsService userDetailsService;

  @Test
  public void loadUserByUsername_whenUserExists_returnUserDetails() {
    Account testAccount = new Account();
    testAccount.setAccountName("Mat");
    testAccount.setAccountType("User");
    testAccount.setAccountPw("TestPassword");
    entityManager.persist(testAccount);

    // This lookup should be case-insensitive.
    UserDetails userDetails = userDetailsService.loadUserByUsername("mat");

    assertTrue(userDetails.isEnabled());
    assertTrue(userDetails.isAccountNonLocked());
    assertTrue(userDetails.isAccountNonExpired());
    assertTrue(userDetails.isCredentialsNonExpired());
    assertEquals("Mat", userDetails.getUsername());
    assertEquals("TestPassword", userDetails.getPassword());
    assertFalse(userDetails.getAuthorities().isEmpty());
    userDetails.getAuthorities().forEach(authority -> assertEquals("user", authority.getAuthority()));
  }

  @Test
  public void loadUserByUsername_whenUserDoesNotExist_throwUsernameNotFoundException() {
    assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("Mat"));
  }

}
