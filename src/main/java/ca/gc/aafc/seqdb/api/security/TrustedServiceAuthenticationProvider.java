package ca.gc.aafc.seqdb.api.security;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Named
@ConditionalOnProperty("seqdb.trusted-service-api-keys")
public class TrustedServiceAuthenticationProvider implements AuthenticationProvider {
  
  public static final String API_KEY_HEADER = "api-key";
  
  private final List<String> trustedServiceApiKeys;
  
  private final UserDetailsService userDetailsService;

  public TrustedServiceAuthenticationProvider(
      @Value("${seqdb.trusted-service-api-keys}")
      String[] trustedServiceApiKeys,
      UserDetailsService userDetailsService
  ) {
    this.trustedServiceApiKeys = Arrays.asList(trustedServiceApiKeys);
    this.userDetailsService = userDetailsService;
  }
  
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getRequest();
    
    String apiKey = request.getHeader(API_KEY_HEADER);
    String username = authentication.getPrincipal().toString();
    
    if (StringUtils.isEmpty(apiKey)) {
      return null;
    }
    
    if (!this.trustedServiceApiKeys.contains(apiKey)) {
      throw new AuthenticationServiceException(
          String.format("Unknown service api key: %s", apiKey));
    }
    
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        userDetails.getUsername(),
        "trusted-service-auth" // Password not needed.
    );
    
    return token;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication != null && Authentication.class.isAssignableFrom(authentication);
  }
  
}
