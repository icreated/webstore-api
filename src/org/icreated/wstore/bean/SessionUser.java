/*******************************************************************************
 * @author Copyright (C) 2019 ICreated, Sergey Polyarus
 *  @date 2019
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms version 2 of the GNU General Public License as published
 *  by the Free Software Foundation. This program is distributed in the hope
 *  that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc., 
 *  59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 ******************************************************************************/
package org.icreated.wstore.bean;

import java.security.Principal;



public class SessionUser implements Principal {
	
  private int AD_User_ID = 0;
  private int C_BPartner_ID = 0;
  private int C_PaymentTerm_ID = 0;
  private int M_PriceList_ID = 0;
  private int C_BP_Group_ID = 0;
  private String username = null;
  private String password;
  private String value;
  private String name;
  private String email;
  private boolean enabled;
  private boolean accountNonLocked;
  private String salt;
  private long expires;
//  private Set<GrantedAuthority> authorities;
  
  public SessionUser() {}
  
  public SessionUser(int AD_User_ID, String value, String name, String email, String password, String salt, int C_BPartner_ID, int C_BP_Group_ID, int M_PriceList_ID, int C_PaymentTerm_ID, boolean enabled, boolean accountNonLocked)
  {
    this.AD_User_ID = AD_User_ID;
    this.value = value;
    this.username = value;
    this.name = name;
    this.email = email;
    this.password = password;
    this.salt = salt;
    this.C_BPartner_ID = C_BPartner_ID;
    this.C_BP_Group_ID = C_BP_Group_ID;
    this.M_PriceList_ID = M_PriceList_ID;
    this.C_PaymentTerm_ID = C_PaymentTerm_ID;
    this.enabled = enabled;
    this.accountNonLocked = accountNonLocked;
  }
  
  public String getUsername()
  {
    return this.value;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  
  public boolean isAccountNonExpired()
  {
    return true;
  }
  
  public boolean isAccountNonLocked()
  {
    return this.accountNonLocked;
  }
  

  public boolean isCredentialsNonExpired()
  {
    return true;
  }
  
  public boolean isEnabled()
  {
    return this.enabled;
  }
  
  public boolean equals(Object rhs)
  {
    if ((rhs instanceof SessionUser)) {
      return this.username.equals(((SessionUser)rhs).username);
    }
    return false;
  }
  
  public int hashCode()
  {
    return this.username.hashCode();
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer("SessionUser[id=");
    sb.append(this.AD_User_ID).append(", name=").append(this.username)
      .append(", email=").append(this.email)
      .append("]");
    return sb.toString();
  }
  
  
  
  public String getName() {
	return name;
  }

  public String getEmail()
  {
    return this.email;
  }
  
  public int getC_PaymentTerm_ID()
  {
    return this.C_PaymentTerm_ID;
  }
  
  public int getAD_User_ID()
  {
    return this.AD_User_ID;
  }
  
  public int getC_BP_Group_ID()
  {
    return this.C_BP_Group_ID;
  }
  
  public int getC_BPartner_ID()
  {
    return this.C_BPartner_ID;
  }
  
  public int getM_PriceList_ID()
  {
    return this.M_PriceList_ID;
  }
  
  public String getSalt()
  {
    return this.salt;
  }
  
  public long getExpires()
  {
    return this.expires;
  }
  
  public void setExpires(long expires)
  {
    this.expires = expires;
  }
}
