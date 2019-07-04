package ca.gc.aafc.seqdb.api.security.trustedservice;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import lombok.Getter;

/**
 * Authentication token for a trusted service to authenticate as a user by using an api-key.
 */
//CHECKSTYLE:OFF AnnotationUseStyle
public class TrustedServiceAuthenticationToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = -1689000801224458018L;

  @Getter(onMethod_ = @Override)
  private final Object principal;

  @Getter(onMethod_ = @Override)
  private final Object credentials;

  public TrustedServiceAuthenticationToken(
      Object principal,
      Object credentials,
      boolean authenticated
  ) {
    super(null);
    this.principal = principal;
    this.credentials = credentials;
    super.setAuthenticated(authenticated);
  }

  public TrustedServiceAuthenticationToken(Object principal, Object credentials) {
    this(principal, credentials, false);
  }

  @Override
  public void setAuthenticated(boolean authenticated) {
    if (authenticated) {
      throw new IllegalArgumentException(
          "Cannot set this token to trusted - use constructor which takes a boolean instead");
    }
    super.setAuthenticated(false);
  }

}
