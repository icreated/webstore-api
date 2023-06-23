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
package co.icreated.wstore.model;

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
  // private Set<GrantedAuthority> authorities;


  public static class Builder {

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



    public SessionUser build() {
      return new SessionUser(this);
    }

    public Builder AD_User_ID(int AD_User_ID) {
      this.AD_User_ID = AD_User_ID;
      return this;
    }

    public Builder C_PaymentTerm_ID(int C_PaymentTerm_ID) {
      this.C_PaymentTerm_ID = C_PaymentTerm_ID;
      return this;
    }

    public Builder M_PriceList_ID(int M_PriceList_ID) {
      this.M_PriceList_ID = M_PriceList_ID;
      return this;
    }

    public Builder C_BP_Group_ID(int C_BP_Group_ID) {
      this.C_BP_Group_ID = C_BP_Group_ID;
      return this;
    }

    public Builder value(String value) {
      this.value = value;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder salt(String salt) {
      this.salt = salt;
      return this;
    }

    public Builder C_BPartner_ID(int C_BPartner_ID) {
      this.C_BPartner_ID = C_BPartner_ID;
      return this;
    }

    public Builder accountNonLocked(boolean isAccountNonLocked) {
      this.accountNonLocked = isAccountNonLocked;
      return this;
    }

    public Builder enabled(boolean isEnabled) {
      this.enabled = isEnabled;
      return this;
    }

  }


  private SessionUser(Builder builder) {
    this.AD_User_ID = builder.AD_User_ID;
    this.C_BPartner_ID = builder.C_BPartner_ID;
    this.C_PaymentTerm_ID = builder.C_PaymentTerm_ID;
    this.M_PriceList_ID = builder.M_PriceList_ID;
    this.C_BP_Group_ID = builder.C_BP_Group_ID;
    this.value = builder.value;
    this.name = builder.name;
    this.username = builder.username;
    this.email = builder.email;
    this.password = builder.password;
    this.salt = builder.salt;
    this.accountNonLocked = builder.accountNonLocked;
    this.expires = builder.expires;
    this.enabled = builder.enabled;
  }


  public String getUsername() {
    return this.value;
  }

  public String getPassword() {
    return this.password;
  }


  public boolean isAccountNonExpired() {
    return true;
  }

  public boolean isAccountNonLocked() {
    return this.accountNonLocked;
  }


  public boolean isCredentialsNonExpired() {
    return true;
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  public boolean equals(Object rhs) {
    if ((rhs instanceof SessionUser)) {
      return this.username.equals(((SessionUser) rhs).username);
    }
    return false;
  }

  public int hashCode() {
    return this.username.hashCode();
  }

  public String toString() {
    StringBuffer sb = new StringBuffer("SessionUser[id=");
    sb.append(this.AD_User_ID).append(", name=").append(this.username).append(", email=")
        .append(this.email).append("]");
    return sb.toString();
  }



  public String getName() {
    return name;
  }

  public String getEmail() {
    return this.email;
  }

  public int getC_PaymentTerm_ID() {
    return this.C_PaymentTerm_ID;
  }

  public int getAD_User_ID() {
    return this.AD_User_ID;
  }

  public int getC_BP_Group_ID() {
    return this.C_BP_Group_ID;
  }

  public int getC_BPartner_ID() {
    return this.C_BPartner_ID;
  }

  public int getM_PriceList_ID() {
    return this.M_PriceList_ID;
  }

  public String getSalt() {
    return this.salt;
  }

  public long getExpires() {
    return this.expires;
  }

  public void setExpires(long expires) {
    this.expires = expires;
  }
}
