package ca.gc.aafc.seqdb.api.security.trustedservice;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import lombok.Getter;

public class TrustedServiceAuthenticationToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = -1689000801224458018L;

  @Getter(onMethod_ = @Override)
  private final Object principal;
  
  @Getter(onMethod_ = @Override)
  private final Object credentials;
  
  public TrustedServiceAuthenticationToken(Object principal, Object credentials) {
    super(null);
    this.principal = principal;
    this.credentials = credentials;
    this.setAuthenticated(false);
  }
  
}
