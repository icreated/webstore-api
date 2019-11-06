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
package org.icreated.wstore.security;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.icreated.wstore.bean.SessionUser;
import org.icreated.wstore.exception.CustomNotFoundException;


public class IdempiereUserService {
    

    CCache<String,SessionUser> s_cache = new CCache<String,SessionUser>("SessionUser", 100, 30);
	private static CLogger log = CLogger.getCLogger(IdempiereUserService.class);
	
	boolean reload = false;
	
	public IdempiereUserService() {
		
		this(false);
	}
	
	public IdempiereUserService(boolean reload) {
		
		this.reload = reload;
	}


	public final SessionUser loadUserByUsername(String username, boolean isEmail)  {
		
		if (username == null || username.length()==0)
			return null;
		
		SessionUser retValue = null;
		if (isEmail) {
			retValue = getUser(username, true);
			if (retValue != null && retValue.getUsername() != null)
				s_cache.put(retValue.getUsername(), retValue);
		} else {
			
			retValue = (SessionUser)s_cache.get(username);
			if (retValue == null || reload)
			{
				retValue = getUser(username, false);
				if (retValue != null && retValue.getUsername() != null)
					s_cache.put(retValue.getUsername(), retValue);
			}
		}
		
		if (retValue == null) {
			throw new CustomNotFoundException("user not found");
		}

		return retValue;
	}
	
	
	
	public static SessionUser getUser(String login, boolean isEmail) {
		
		log.info(login);

		String sql = "SELECT u.AD_User_ID, u.Value, u.Name, u.Email, u.Password, u.salt, " +
			"bp.C_BPartner_ID, bp.C_BP_Group_ID, bp.M_PriceList_ID, bp.C_PaymentTerm_ID, " +
			"u.Description, u.isActive, u.isLocked, bp.isActive, bp.SOCreditStatus " +
			"FROM AD_User u " +
			"INNER JOIN C_BPartner bp ON bp.C_BPartner_ID = u.C_BPartner_ID " +
			"WHERE u.isActive='Y' AND %s LIKE trim(?) ";
		
		SessionUser user = null;
		sql = isEmail ? String.format(sql, "u.EMail") :  String.format(sql, "u.Value");
		/*
		if (EmailValidator.validate(login))
			sql = String.format(sql, "u.EMail");
		else
			sql = String.format(sql, "u.Value");
		*/
		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			
			pstmt.setString(1, login.trim());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				
				boolean enabled = rs.getString(12).equals("Y") & rs.getString(14).equals("Y");
				boolean accountNonLocked = !rs.getString(7).equals(MBPartner.SOCREDITSTATUS_CreditStop);
				
				user = new SessionUser(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getString(6), 
						rs.getInt(7), rs.getInt(9), rs.getInt(9), rs.getInt(10), enabled, accountNonLocked);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return user;

}	//	load
	
	
	

	
	
}
