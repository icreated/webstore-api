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
package co.icreated.wstore.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.ws.rs.core.SecurityContext;

import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MLocation;
import org.compiere.model.MUser;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_C_BPartner_Location;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import co.icreated.wstore.api.model.AccountInfoDto;
import co.icreated.wstore.api.model.AddressDto;
import co.icreated.wstore.api.model.NewAccountFormDto;
import co.icreated.wstore.mapper.AccountMapper;

public class AccountService extends AbstractService {


  private static CLogger log = CLogger.getCLogger(AccountService.class);


  public AccountService(Properties ctx, SecurityContext securityContext) {
    super(ctx, securityContext);
  }


  public AccountInfoDto getAccountInfo() {
    return AccountMapper.INSTANCE.toDto(MUser.get(getSessionUser().getAD_User_ID()));
  }


  public AccountInfoDto updateUserAccount(AccountInfoDto accountInfoDto) {

    transaction(trxName -> {
      X_AD_User user = new X_AD_User(ctx, getSessionUser().getAD_User_ID(), trxName);
      user.setName(accountInfoDto.getName());
      user.setEMail(accountInfoDto.getEmail());
      user.save();
      return user;
    });
    return accountInfoDto;
  }


  public MUser createUserAccount(NewAccountFormDto newUser) {

    log.log(Level.FINE, "new account ", newUser);
    int SalesRep_ID = Env.getContextAsInt(ctx, "#SalesRep_ID");

    return transaction(trxName -> {
      MBPartner bp = new MBPartner(ctx);
      bp.setIsCustomer(true);
      bp.setName(newUser.getName());
      bp.setSalesRep_ID(SalesRep_ID);
      bp.save(trxName);
      MUser user = new MUser(bp);
      user.setName(newUser.getName());
      user.setEMail(newUser.getEmail());
      user.setPassword(newUser.getPassword());
      user.save(trxName);
      return user;
    });
  }


  public AddressDto createAddress(AddressDto addressDto) {

    log.log(Level.FINE, "new address ", addressDto);

    X_C_BPartner_Location created = transaction(trxName -> {
      MLocation location = MLocation.get(ctx, 0, trxName);
      location.setAddress1(addressDto.getAddress1());
      location.setAddress2(addressDto.getAddress2());
      location.setCity(addressDto.getCity());
      location.setPostal(addressDto.getPostal());
      location.setC_Country_ID(addressDto.getCountryId());
      location.save();

      X_C_BPartner_Location bpl = new MBPartnerLocation(ctx, 0, trxName);
      bpl.setName(addressDto.getLabel());
      bpl.setC_BPartner_ID(getSessionUser().getC_BPartner_ID());
      bpl.setC_Location_ID(location.getC_Location_ID());
      bpl.setPhone(addressDto.getPhone());
      bpl.save();

      MUser user = new MUser(ctx, getSessionUser().getAD_User_ID(), trxName);
      user.setName(addressDto.getName());
      user.setC_BPartner_Location_ID(bpl.getC_BPartner_Location_ID());
      user.save();
      return bpl;
    });

    addressDto.setId(created.getC_BPartner_Location_ID());
    return addressDto;
  }


  public void updateAddress(AddressDto addressDto) {

    log.log(Level.FINE, "update address ", addressDto);
    transaction(trxName -> {
      X_C_BPartner_Location bpl = new X_C_BPartner_Location(ctx, addressDto.getId(), trxName);
      bpl.setName(addressDto.getLabel());
      bpl.setPhone(addressDto.getPhone());
      bpl.save();

      MLocation location = MLocation.get(ctx, bpl.getC_Location_ID(), trxName);
      location.setAddress1(addressDto.getAddress1());
      location.setAddress2(addressDto.getAddress2());
      location.setCity(addressDto.getCity());
      location.setPostal(addressDto.getPostal());
      location.setC_Country_ID(addressDto.getCountryId());
      location.save();

      MUser user = new MUser(ctx, getSessionUser().getAD_User_ID(), trxName);
      user.setName(addressDto.getName());
      user.setC_BPartner_Location_ID(bpl.getC_BPartner_Location_ID());
      user.save();
      return bpl;
    });

  }

  public boolean deleteAddress(int C_BPartner_Location_ID) {
	  
    transaction(trxName -> {
      MBPartnerLocation bpl = new MBPartnerLocation(ctx, C_BPartner_Location_ID, trxName);
      bpl.setIsActive(false);
      bpl.save();
      return bpl;
    });
    return true;
  }



  public List<AddressDto> getAddresses() {
	  
    String sql =
        "SELECT bpl.C_BPartner_Location_ID, bpl.Name, u.Name, l.Address1, l.Address2, l.Postal, "
            + "l.City, bpl.phone, l.C_Country_ID, c.Name " + "FROM C_BPartner bp "
            + "INNER JOIN AD_User u ON bp.C_BPartner_ID = u.C_BPartner_ID "
            + "INNER JOIN C_BPartner_Location bpl ON bpl.C_BPartner_ID = bp.C_BPartner_ID "
            + "INNER JOIN C_Location l ON l.C_Location_ID = bpl.C_Location_ID "
            + "INNER JOIN C_Country c ON c.C_Country_ID = l.C_Country_ID "
            + "WHERE bpl.isActive='Y' AND bp.C_BPartner_ID = ?";

    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<AddressDto> list = new ArrayList<AddressDto>();
    try {
      pstmt = DB.prepareStatement(sql, null);
      pstmt.setInt(1, getSessionUser().getC_BPartner_ID());
      rs = pstmt.executeQuery();

      while (rs.next()) {

        list.add(new AddressDto() //
            .id(rs.getInt(1)) //
            .label(rs.getString(2)) //
            .name(rs.getString(3)) //
            .address1(rs.getString(4)) //
            .address2(rs.getString(5)) //
            .postal(rs.getString(6)) //
            .city(rs.getString(7)) //
            .phone(rs.getString(8)) //
            .countryId(rs.getInt(9)) //
            .countryName(rs.getString(10)));
      }
    } catch (Exception e) {
      log.log(Level.SEVERE, "getAddresses", e);

    } finally {
      DB.close(rs, pstmt);
      rs = null;
      pstmt = null;
    }
    return list;
  }


  public boolean changePassword(String newPassword) {
	  
    transaction(trxName -> {
      MUser user = new MUser(ctx, getSessionUser().getAD_User_ID(), trxName);
      user.setPassword(newPassword);
      user.setIsLocked(false);
      user.setDatePasswordChanged(Timestamp.from(Instant.now()));
      user.setEMailVerifyCode(user.getEMailVerifyCode(), "By Changing password");
      user.save();
      return user;
    });
    return true;
  }



}
