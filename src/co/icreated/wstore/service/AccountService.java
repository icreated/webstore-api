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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MLocation;
import org.compiere.model.MUser;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_C_BPartner_Location;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import co.icreated.wstore.api.model.AccountInfoDto;
import co.icreated.wstore.api.model.AddressDto;
import co.icreated.wstore.api.model.NewAccountFormDto;
import co.icreated.wstore.bean.AccountInfo;
import co.icreated.wstore.bean.Address;
import co.icreated.wstore.bean.IdNameBean;
import co.icreated.wstore.bean.NewAccountForm;
import co.icreated.wstore.bean.SessionUser;
import co.icreated.wstore.mapper.AccountMapper;

public class AccountService extends AbstractService {


  private static CLogger log = CLogger.getCLogger(AccountService.class);



  public AccountService(Properties ctx, SessionUser user) {

    this.ctx = ctx;
    this.sessionUser = user;
  }


  public AccountInfoDto getAccountInfo() {
    return AccountMapper.INSTANCE.toDto(MUser.get(sessionUser.getAD_User_ID()));
  }


  public AccountInfoDto updateUserAccount(AccountInfoDto account) {

    X_AD_User user = new X_AD_User(ctx, sessionUser.getAD_User_ID(), null);
    user.setName(account.getName());
    user.setEMail(account.getEmail());
    user.save();
    return account;
  }


  public MUser createNewAccount(NewAccountFormDto newUser) {

    log.log(Level.FINE, "new account ", newUser);
    int SalesRep_ID = Env.getContextAsInt(ctx, "#SalesRep_ID");

    MBPartner bp = new MBPartner(ctx);
    bp.setIsCustomer(true);
    bp.setName(newUser.getName());
    bp.setSalesRep_ID(SalesRep_ID);
    bp.save();
    MUser user = new MUser(bp);
    user.setName(newUser.getName());
    user.setEMail(newUser.getEmail());
    user.setPassword(newUser.getPassword());
    user.save();

    return user;
  }


  public AddressDto saveAddress(AddressDto form) {

    X_C_BPartner_Location bpl = null;
    MLocation loc = null;

    String trxName = Trx.createTrxName("Address");
    Trx trx = Trx.get(trxName, true);

    if (form.getId() > 0) {
      bpl = new X_C_BPartner_Location(ctx, form.getId(), trxName);
      loc = MLocation.get(ctx, bpl.getC_Location_ID(), trxName);
    } else {
      bpl = new MBPartnerLocation(ctx, 0, trxName);
      loc = MLocation.get(ctx, 0, trxName);
    }

    loc.setAddress1(form.getAddress1());
    loc.setAddress2(form.getAddress2());
    loc.setCity(form.getCity());
    loc.setPostal(form.getPostal());
    loc.setC_Country_ID(form.getCountryId());
    loc.save();

    bpl.setName(form.getLabel());
    bpl.setC_BPartner_ID(sessionUser.getC_BPartner_ID());
    bpl.setC_Location_ID(loc.getC_Location_ID());
    bpl.setPhone(form.getPhone());
    bpl.save();

    MUser user = new MUser(ctx, sessionUser.getAD_User_ID(), trxName);
    user.setName(form.getName());
    user.setC_BPartner_Location_ID(bpl.getC_BPartner_Location_ID());
    user.save();

    if (trx.commit()) {
      form.setId(bpl.getC_BPartner_Location_ID());
      return form;
    } else {
      trx.rollback();
      return null;
    }

  }


  public boolean deleteAddress(int C_BPartner_Location_ID) {

    if (C_BPartner_Location_ID > 0) {
      MBPartnerLocation bpl = new MBPartnerLocation(ctx, C_BPartner_Location_ID, null);
      bpl.setIsActive(false);
      return bpl.save();

    }
    return false;
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
      pstmt.setInt(1, sessionUser.getC_BPartner_ID());
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

    MUser user = MUser.get(ctx, sessionUser.getAD_User_ID());
    user.setPassword(newPassword);
    user.setIsLocked(false);
    user.setDatePasswordChanged(new Timestamp(System.currentTimeMillis()));
    user.setEMailVerifyCode(user.getEMailVerifyCode(), "By Changing password");
    return user.save();
  }



}
