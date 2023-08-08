package co.icreated.wstore.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.SecurityContext;

import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MUser;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_C_BPartner_Location;
import org.compiere.model.X_C_Location;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

import co.icreated.wstore.api.model.AccountInfoDto;
import co.icreated.wstore.api.model.AddressDto;
import co.icreated.wstore.api.model.NewAccountFormDto;
import co.icreated.wstore.mapper.AccountMapper;
import co.icreated.wstore.utils.Transaction;

public class AccountService extends AbstractService {


  private static CLogger log = CLogger.getCLogger(AccountService.class);


  public AccountService(Properties ctx, SecurityContext securityContext) {
    super(ctx, securityContext);
  }


  public AccountInfoDto getAccountInfo() {
    return AccountMapper.INSTANCE.toDto(MUser.get(getSessionUser().getAD_User_ID()));
  }


  public AccountInfoDto updateUserAccount(AccountInfoDto accountInfoDto) {

    log.log(Level.FINE, "update user account ", accountInfoDto);

    Transaction.run(trxName -> {
      X_AD_User user = new X_AD_User(ctx, getSessionUser().getAD_User_ID(), trxName);
      user.setName(accountInfoDto.getName());
      user.setEMail(accountInfoDto.getEmail());
      user.save();
      return user;
    });
    return accountInfoDto;
  }


  public MUser createUserAccount(NewAccountFormDto newAccountFormDto) {

    log.log(Level.FINE, "new account ", newAccountFormDto);

    int SalesRep_ID = Env.getContextAsInt(ctx, "#SalesRep_ID");

    return Transaction.run(trxName -> {
      MBPartner bp = new MBPartner(ctx);
      bp.setIsCustomer(true);
      bp.setName(newAccountFormDto.getName());
      bp.setSalesRep_ID(SalesRep_ID);
      bp.save(trxName);
      MUser user = new MUser(bp);
      user.setName(newAccountFormDto.getName());
      user.setEMail(newAccountFormDto.getEmail());
      user.setPassword(newAccountFormDto.getPassword());
      user.save(trxName);
      return user;
    });
  }


  public AddressDto createAddress(AddressDto addressDto) {

    log.log(Level.FINE, "new address ", addressDto);

    X_C_BPartner_Location created = Transaction.run(trxName -> {
      X_C_Location location =
          AccountMapper.INSTANCE.to(addressDto.getLocation(), new X_C_Location(ctx, 0, trxName));
      location.save();

      X_C_BPartner_Location bpl =
          AccountMapper.INSTANCE.to(addressDto, new MBPartnerLocation(ctx, 0, trxName));
      bpl.setC_BPartner_ID(getSessionUser().getC_BPartner_ID());
      bpl.setC_Location_ID(location.getC_Location_ID());
      bpl.save();
      return bpl;
    });

    addressDto.setId(created.getC_BPartner_Location_ID());
    return addressDto;
  }


  public void updateAddress(int id, AddressDto addressDto) {

    log.log(Level.FINE, "update address ", addressDto);

    Transaction.run(trxName -> {
      X_C_BPartner_Location bpl =
          AccountMapper.INSTANCE.to(addressDto, new X_C_BPartner_Location(ctx, id, trxName));
      bpl.save();

      X_C_Location location = AccountMapper.INSTANCE.to(addressDto.getLocation(),
          new X_C_Location(ctx, bpl.getC_Location_ID(), trxName));
      location.save();
      return bpl;
    });

  }


  public boolean deleteAddress(int C_BPartner_Location_ID) {

    Transaction.run(trxName -> {
      MBPartnerLocation bpl = new MBPartnerLocation(ctx, C_BPartner_Location_ID, trxName);
      bpl.setIsActive(false);
      bpl.save();
      log.log(Level.FINE, "deactivating address " + bpl.getName());
      return bpl;
    });
    return true;
  }


  public List<AddressDto> getAddresses() {
    return Stream
        .of(MBPartnerLocation.getForBPartner(ctx, getSessionUser().getC_BPartner_ID(), null))
        .filter(bpl -> bpl.isActive()) //
        .map(AccountMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }


  public AddressDto getAddress(int C_BPartner_Location_ID) {
    MBPartnerLocation bpl = new MBPartnerLocation(ctx, C_BPartner_Location_ID, null);
    return AccountMapper.INSTANCE.toDto(bpl);
  }


  public boolean changePassword(String newPassword) {

    Transaction.run(trxName -> {
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

