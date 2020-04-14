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
package org.icreated.wstore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.compiere.model.MClient;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.icreated.wstore.bean.WStore;

public enum Envs
{
	INSTANCE;  

  private static CCache<Integer, Properties> s_cacheCtx = new CCache<Integer, Properties>("EnvCtx", 10, 0);
  public static final String CTX_DOCUMENT_DIR = "documentDir";
  
  static {
	  
	  List<WStore> stores = getListWebStore();
	  for (WStore store : stores) {
		s_cacheCtx.put(store.getW_Store_ID(), getCtx(store));
	  } 
  }
  
  
  public static Properties getCtx(int W_Store_ID) {
	  
	  if (s_cacheCtx == null || s_cacheCtx.size() == 0) {
		  List<WStore> stores = getListWebStore();
		  for (WStore store : stores) {
			s_cacheCtx.put(store.getW_Store_ID(), getCtx(store));
		  } 		  
	  }
	  
	  return s_cacheCtx.get(W_Store_ID);
  }
  
 
  
  private static Properties getCtx(WStore wstore)  {
	  

    Properties newCtx = new Properties();

      Env.setContext(newCtx, "#W_Store_ID", wstore.getW_Store_ID());
      Env.setContext(newCtx, "#AD_Client_ID", wstore.getAD_Client_ID());
      Env.setContext(newCtx, "#AD_Org_ID", wstore.getAD_Org_ID());
      Env.setContext(newCtx, "#SalesRep_ID", wstore.getSalesRep_ID());
      Env.setContext(newCtx, "#M_PriceList_ID", wstore.getM_PriceList_ID());
      Env.setContext(newCtx, "#M_Warehouse_ID", wstore.getM_Warehouse_ID());
      Env.setContext(newCtx, "#C_PaymentTerm_ID", wstore.getC_PaymentTerm_ID());
      Env.setContext(newCtx, "url", wstore.getUrl());
      
      String s = wstore.getWebParam1();
      Env.setContext(newCtx, "webParam1", s == null ? "" : s);
      s = wstore.getWebParam2();
      Env.setContext(newCtx, "webParam2", s == null ? "" : s);
      s = wstore.getWebParam3();
      Env.setContext(newCtx, "webParam3", s == null ? "" : s);
      s = wstore.getWebParam4();
      Env.setContext(newCtx, "webParam4", s == null ? "" : s);
      s = wstore.getWebParam5();
      Env.setContext(newCtx, "webParam5", s == null ? "" : s);
      s = wstore.getWebParam6();
      Env.setContext(newCtx, "webParam6", s == null ? "" : s);
      s = wstore.getStylesheet();
      if (s == null)
      {
        s = "standard";
      }
      else
      {
        int index = s.lastIndexOf('.');
        if (index != -1) {
          s = s.substring(0, index);
        }
      }
      Env.setContext(newCtx, "Stylesheet", s);
      
      Env.setContext(newCtx, "#M_PriceList_ID", wstore.getM_PriceList_ID());
      if (Env.getContextAsInt(newCtx, "#AD_Role_ID") == 0)
      {
        int AD_Role_ID = 0;
        Env.setContext(newCtx, "#AD_Role_ID", AD_Role_ID);
      }
      MClient client = MClient.get(newCtx, wstore.getAD_Client_ID());
      
      Env.setContext(newCtx, "name", wstore.getName());
      Env.setContext(newCtx, "description", wstore.getDescription());
      if ((newCtx.getProperty("#AD_Language") == null) && (client.getAD_Language() != null)) {
        Env.setContext(newCtx, "#AD_Language", client.getAD_Language());
      }
      String docDir = client.getDocumentDir();
      Env.setContext(newCtx, "documentDir", docDir == null ? "" : docDir);
      if (newCtx.getProperty("#AD_Language") == null) {
        Env.setContext(newCtx, "#AD_Language", "en_US");
      }


      return newCtx;
  }
  
  
  
  
  
	private static List<WStore> getListWebStore() {
		
		
		String sql = "SELECT * FROM W_Store WHERE isActive='Y'";
		
		PreparedStatement pstmt = null;
		
		List<WStore> list = new ArrayList<WStore>();
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				WStore store = new WStore(rs.getInt("W_Store_ID"), rs.getInt("AD_Client_ID"), rs.getInt("AD_Org_ID"), 
						rs.getInt("salesRep_ID"), rs.getInt("M_PriceList_ID"), rs.getInt("M_Warehouse_ID"), 
						rs.getString("name"), rs.getString("description"), rs.getString("help"), 
						rs.getString("webParam1"), rs.getString("webParam2"), rs.getString("webParam3"), rs.getString("webParam4"), 
								rs.getString("webParam5"), rs.getString("webParam6"), rs.getString("stylesheet"),
								rs.getInt("C_PaymentTerm_ID"),rs.getString("url"));
				
				list.add(store);
			}
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		catch (Exception e)
		{
		}

		return list;
	}  
  
  
}
