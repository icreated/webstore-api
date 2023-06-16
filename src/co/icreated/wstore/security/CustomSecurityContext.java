/*******************************************************************************
 * @author Copyright (C) 2019 ICreated, Sergey Polyarus
 * @date 2019 This program is free software; you can redistribute it and/or modify it under the
 *       terms version 2 of the GNU General Public License as published by the Free Software
 *       Foundation. This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *       WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *       PURPOSE. See the GNU General Public License for more details. You should have received a
 *       copy of the GNU General Public License along with this program; if not, write to the Free
 *       Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 ******************************************************************************/
package co.icreated.wstore.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import co.icreated.wstore.bean.SessionUser;

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
