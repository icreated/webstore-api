package co.icreated.wstore.service;

import java.util.Map;
import java.util.Properties;

import org.compiere.model.MBPartner;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;

import co.icreated.wstore.exception.WstoreNotFoundException;
import co.icreated.wstore.exception.WstoreUnauthorizedException;
import co.icreated.wstore.model.SessionUser;
import co.icreated.wstore.security.TokenHandler;
import co.icreated.wstore.utils.QueryTool;


public class AuthService extends AbstractService {


  CCache<String, SessionUser> s_cache = new CCache<String, SessionUser>("SessionUser", 100, 30);
  private static CLogger log = CLogger.getCLogger(AuthService.class);


  public AuthService(Properties ctx) {
    super(ctx);
  }


  public final SessionUser loadUserByUsername(String username, boolean isEmail, boolean reload) {

    if (username == null || username.length() == 0)
      return null;

    SessionUser retValue = null;
    if (isEmail) {
      retValue = getUser(username, true);
      if (retValue != null && retValue.getUsername() != null)
        s_cache.put(retValue.getUsername(), retValue);
    } else {
      retValue = (SessionUser) s_cache.get(username);
      if (retValue == null || reload) {
        retValue = getUser(username, false);
        if (retValue != null && retValue.getUsername() != null)
          s_cache.put(retValue.getUsername(), retValue);
      }
    }

    if (retValue == null) {
      throw new WstoreNotFoundException("user not found");
    }

    return retValue;
  }


  public String issueToken(SessionUser sessionUser) {

    TokenHandler tokenHandler = new TokenHandler(this);
    String token = tokenHandler.createTokenForUser(sessionUser);
    return token;
  }


  public SessionUser authenticate(String username, String password) throws Exception {


    SessionUser sessionUser = (SessionUser) loadUserByUsername(username, true, false);
    if (sessionUser == null) {
      throw new WstoreUnauthorizedException("User not found");
    }


    boolean isValid = sessionUser.getPassword().equals(password);
    if (!isValid) {
      throw new WstoreUnauthorizedException("Wrong password");
    }

    return sessionUser;
  }


  public SessionUser getUser(String login, boolean isEmail) {

    log.info(login);

    String sql = "SELECT u.AD_User_ID, u.Value, u.Name, u.Email, u.Password, u.salt, "
        + "bp.C_BPartner_ID, bp.C_BP_Group_ID, bp.M_PriceList_ID, bp.C_PaymentTerm_ID, "
        + "u.Description, u.isActive, u.isLocked, bp.isActive, bp.SOCreditStatus "
        + "FROM AD_User u " //
        + "INNER JOIN C_BPartner bp ON bp.C_BPartner_ID = u.C_BPartner_ID "
        + "WHERE u.isActive='Y' AND %s LIKE trim(?) ";

    sql = isEmail ? String.format(sql, "u.EMail") : String.format(sql, "u.Value");

    return QueryTool.nativeFirst(sql, Map.of(1, login.trim()), rs -> {
      boolean enabled = rs.getString(12).equals("Y") & rs.getString(14).equals("Y");
      boolean accountNonLocked = !rs.getString(7).equals(MBPartner.SOCREDITSTATUS_CreditStop);

      return new SessionUser.Builder().AD_User_ID(rs.getInt(1)) //
          .value(rs.getString(2)) //
          .name(rs.getString(3)) //
          .email(rs.getString(4)) //
          .password(rs.getString(5)) //
          .salt(rs.getString(6)) //
          .C_BPartner_ID(rs.getInt(7)) //
          .C_BP_Group_ID(rs.getInt(8)) //
          .M_PriceList_ID(rs.getInt(9)) //
          .C_PaymentTerm_ID(rs.getInt(10)) //
          .enabled(enabled) //
          .accountNonLocked(accountNonLocked) //
          .build();
    });
  }

}
