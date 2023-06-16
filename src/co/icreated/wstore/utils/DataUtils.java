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
package co.icreated.wstore.utils;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import co.icreated.wstore.bean.Shipper;

public class DataUtils {


  private static CLogger log = CLogger.getCLogger(DataUtils.class);



  public static List<Shipper> getShippers(int AD_Client_ID) {

    String sql = "SELECT M_Shipper_ID, Name FROM M_Shipper WHERE isActive='Y' AND AD_Client_ID=?";

    List<Shipper> shippers = new ArrayList<Shipper>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      pstmt = DB.prepareStatement(sql, null);
      pstmt.setInt(1, AD_Client_ID);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        shippers.add(new Shipper(rs.getInt(1), rs.getString(2), null));

      }

    } catch (Exception e) {
      log.log(Level.SEVERE, "", e);

    } finally {
      DB.close(rs, pstmt);
      rs = null;
      pstmt = null;
    }

    return shippers;
  }



  public static boolean isUnique(Properties ctx, String field, String token) {

    if (token == null)
      return false;

    String sql = "SELECT * FROM AD_User WHERE AD_Client_ID=? AND UPPER(%s)=?";
    sql = String.format(sql, field);

    PreparedStatement pstmt = null;
    ResultSet rs = null;
    boolean isUnique = true;
    try {
      pstmt = DB.prepareStatement(sql, null);
      pstmt.setInt(1, Env.getAD_Client_ID(ctx));
      pstmt.setString(2, token.toUpperCase());
      rs = pstmt.executeQuery();
      if (rs.next()) {
        isUnique = false;
      }


    } catch (Exception e) {
      log.log(Level.SEVERE, "", e);
    } finally {
      DB.close(rs, pstmt);
      rs = null;
      pstmt = null;
    }

    return isUnique;
  }



}
