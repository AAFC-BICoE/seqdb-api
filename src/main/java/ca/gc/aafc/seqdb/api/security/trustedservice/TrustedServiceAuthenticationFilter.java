package ca.gc.aafc.seqdb.api.security.trustedservice;

import java.io.IOException;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Processes a HTTP request's "TrustedService" authorization header, putting the result into the
 * SecurityContextHolder. This allows authorization as any user by including the username and API
 * key in an HTTP header. Spaces in the username or api-key must be escaped with a backslash.
 * 
 * Example curl request:
 *   curl -i -H"Authorization: MatPoff secret-key" localhost:8080/api/region
 *   
 * Example curl request where the username has a space:
 *   curl -i -H"Authorization: Mat\ Poff secret-key" localhost:8080/api/region
 */
@RequiredArgsConstructor
public class TrustedServiceAuthenticationFilter extends OncePerRequestFilter {

  @NonNull
  private final AuthenticationManager authenticationManager;
  
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader == null || !authorizationHeader.startsWith("TrustedService ")) {
      chain.doFilter(request, response);
      return;
    }
    
    // Split on unescaped spaces
    String[] authParts = Stream.of(authorizationHeader.split("(?<!\\\\)\\s+"))
        // Then unescape the escaped spaces to get the intended username
        .map(part -> part.replaceAll("\\\\ ", " "))
        .toArray(String[]::new);
    
    if (authParts.length != 3) {
      throw new BadCredentialsException("TrustedService authorization header must match "
          + "\"TrustedService <username> <api-key>\"");
    }
    
    String username = authParts[1];
    String apiKey = authParts[2];
    
    TrustedServiceAuthenticationToken authRequest = new TrustedServiceAuthenticationToken(username,
        apiKey);
    
    Authentication authResult = authenticationManager.authenticate(authRequest);
    
    SecurityContextHolder.getContext().setAuthentication(authResult);
    
    chain.doFilter(request, response);
  }

}
