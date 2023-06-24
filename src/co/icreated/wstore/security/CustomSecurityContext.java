package co.icreated.wstore.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import co.icreated.wstore.model.SessionUser;

public class CustomSecurityContext implements SecurityContext {


  private SessionUser user;
  private boolean isSecure = false;;

  public CustomSecurityContext(SessionUser user, boolean isSecure) {
    this.user = user;
    this.isSecure = isSecure;
  }


  @Override
  public Principal getUserPrincipal() {
    return this.user;
  }

  @Override
  public boolean isUserInRole(String s) {

    /*
     * if (user.getRole() != null) { return user.getRole().contains(s); }
     */
    return true;
  }

  @Override
  public boolean isSecure() {
    return this.isSecure;
  }

  @Override
  public String getAuthenticationScheme() {
    return null;
  }
}
