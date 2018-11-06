package ca.gc.aafc.seqdb.api.security.trustedservice;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

import lombok.NonNull;

/**
 * AuthenticationProvider implementation to allow authorization as any user by including the
 * username and API key in an HTTP header.
 * 
 * Example curl request;
 *   curl -i -H"Authorization: MatPoff secret-key" localhost:8080/api/region
 */
public class TrustedServiceAuthenticationProvider implements AuthenticationProvider {
  
  private final List<String> trustedServiceApiKeys;
  
  private final UserDetailsService userDetailsService;

  public TrustedServiceAuthenticationProvider(
      @NonNull
      @Value("${seqdb.trusted-service-api-keys}")
      String[] trustedServiceApiKeys,
      @NonNull
      UserDetailsService userDetailsService
  ) {
    this.trustedServiceApiKeys = Arrays.asList(trustedServiceApiKeys);
    this.userDetailsService = userDetailsService;
  }
  
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = Objects.toString(authentication.getPrincipal(), null);
    String apiKey = Objects.toString(authentication.getCredentials(), null);
    
    if (StringUtils.isEmpty(apiKey)) {
      return null;
    }
    
    if (!this.trustedServiceApiKeys.contains(apiKey)) {
      throw new AuthenticationServiceException(
          String.format("Unknown service api key: %s", apiKey));
    }
    
    userDetailsService.loadUserByUsername(username);

    TrustedServiceAuthenticationToken token = new TrustedServiceAuthenticationToken(username,
        apiKey);
    
    token.setAuthenticated(true);
    
    return token;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication != null
        && TrustedServiceAuthenticationToken.class.isAssignableFrom(authentication);
  }
  
}
