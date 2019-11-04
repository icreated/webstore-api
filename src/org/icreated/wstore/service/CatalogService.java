/*******************************************************************************
 * @author Copyright (C) 2019 ICreated, Sergey Polyarus
 * @author sergey.polyarus@icreated.fr
 * @date 2019
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 ******************************************************************************/
package org.icreated.wstore.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.icreated.wstore.bean.PriceListProduct;
import org.icreated.wstore.bean.ProductCategory;

public class CatalogService extends AService {
	
	
	CLogger log = CLogger.getCLogger(CatalogService.class);
	
	Properties ctx;
	
	
	public CatalogService(Properties ctx) {
		
		this.ctx = ctx;
	}
	
	
	public List<ProductCategory> getCategories() {
		
		List<ProductCategory> retValue = new ArrayList<ProductCategory>();
		 
			String sql = "SELECT t.M_Product_Category_ID, t.Name, t.Description " +
			"FROM M_Product_Category t " +
			"WHERE t.AD_Org_ID= ?  AND t.IsActive='Y' AND t.IsSelfService='Y' " +
			"ORDER BY t.Name";

			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement (sql, null);
				pstmt.setInt (1, Env.getAD_Org_ID(ctx));
				
				rs = pstmt.executeQuery ();

				while (rs.next ()) {
					retValue.add(new ProductCategory(rs.getInt(1), rs.getString(2), rs.getString(3)));
				}
					

			}
			catch (SQLException ex) {
				log.log(Level.SEVERE, "getCategories - " + sql + " - "+ ex);
			}
			
			finally {
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
			
			return retValue;
	}	
	
	
	
	public PriceListProduct getProduct(int M_Product_ID) {
		
		PriceListProduct product = null;
			
		String sql = "SELECT p.M_Product_ID, p.Value, p.Name, p.Description, p.Help, p.DocumentNote, p.ImageURL, pp.PriceStd " +
				"FROM M_Product p " +
				"LEFT JOIN M_ProductPrice pp ON (p.M_Product_ID = pp.M_Product_ID AND pp.M_PriceList_Version_ID = ?) " +
				"WHERE p.isActive='Y' AND p.M_Product_ID = ? AND p.Discontinued='N'";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, CommonService.getM_PriceList_Version_ID(Env.getContextAsInt(ctx, "#M_PriceList_ID"), new Timestamp(System.currentTimeMillis())));		
			pstmt.setInt(2, M_Product_ID);	

			rs = pstmt.executeQuery ();
			
			if (rs.next ()) {
				product = new PriceListProduct(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5), rs.getString(6),rs.getString(7),
						rs.getBigDecimal(8));
			}

		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE,"getProduct - " + sql + " - "+ ex);
		} finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
       }

		return product;		
		
	}
	
	
	
	public  List<PriceListProduct> getProducts(int M_Product_Category_ID, boolean isWebStoreFeatured) {
		
		 List<PriceListProduct> list = new  ArrayList<PriceListProduct>();
			
			String sql = "SELECT p.M_Product_ID, p.Value, p.Name, p.Description, p.Help, p.DocumentNote, p.ImageURL, pp.PriceStd " +
					"FROM M_Product p " +
					"LEFT JOIN M_ProductPrice pp ON (p.M_Product_ID = pp.M_Product_ID AND pp.M_PriceList_Version_ID = ?)  " +
					"WHERE p.IsBOM = 'N' AND p.isActive='Y' AND p.Discontinued='N' ";

			if (M_Product_Category_ID > 0)
				sql +=" AND p.M_Product_Category_ID = ?";
			if (isWebStoreFeatured)
				sql +=" AND p.isWebStoreFeatured = 'Y'";
			sql += " ORDER BY p.Name";
		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, CommonService.getM_PriceList_Version_ID(Env.getContextAsInt(ctx, "#M_PriceList_ID"), new Timestamp(System.currentTimeMillis())));		

			if (M_Product_Category_ID > 0)
				pstmt.setInt (2, M_Product_Category_ID);

			rs = pstmt.executeQuery ();

			while (rs.next ()) {

				list.add(new PriceListProduct(rs.getInt(1),rs.getString(2),rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6),
						rs.getString(7),rs.getBigDecimal(8)));
			}
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, "getProducts - " + sql + " - "+ ex);
		} finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
        }
		
		
		return list;		
	}
	
	
	public  List<PriceListProduct> doSearch(String searchString) {
		
		
		 List<PriceListProduct> list = new  ArrayList<PriceListProduct>();
			
			String sql = "SELECT p.M_Product_ID, p.Value, p.Name, p.Description, p.Help, p.DocumentNote, p.ImageURL, pp.PriceStd " +
					"FROM M_Product p " +
					"LEFT JOIN M_ProductPrice pp ON (p.M_Product_ID = pp.M_Product_ID AND pp.M_PriceList_Version_ID = ?)  " +
					"WHERE p.IsBOM = 'N' AND p.isActive='Y' AND p.Discontinued='N' AND UPPER(p.Name || p.Description) LIKE UPPER(?) " +
					"ORDER BY p.Name";
		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, CommonService.getM_PriceList_Version_ID(Env.getContextAsInt(ctx, "#M_PriceList_ID"), new Timestamp(System.currentTimeMillis())));		
			pstmt.setString (2, searchString);		

			rs = pstmt.executeQuery ();

			while (rs.next ()) {

				list.add(new PriceListProduct(rs.getInt(1),rs.getString(2),rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6),
						rs.getString(7),rs.getBigDecimal(8)));
			}
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, "getProducts - " + sql + " - "+ ex);
		} finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
       }
		
		
		return list;		
	}
	
	
	public  List<PriceListProduct> getProductsById(List<Integer> ids) {
		
		List<PriceListProduct> retValue = new ArrayList<PriceListProduct>();
		
		String sql = "SELECT p.M_Product_ID, p.Value, p.Name, p.Description, p.Help, p.DocumentNote, p.ImageURL, pp.PriceStd " +
			"FROM M_Product p " +
			"LEFT JOIN M_ProductPrice pp ON (p.M_Product_ID = pp.M_Product_ID AND pp.M_PriceList_Version_ID = ?) " +
			"WHERE p.isActive='Y' AND p.Discontinued='N' AND  p.M_Product_ID IN (%s)";
		
		
		StringBuffer sqlBuffer = new StringBuffer();
		for (Integer id : ids) {
			sqlBuffer.append(id).append(",");
		}
		
		sql = String.format(sql, sqlBuffer.substring(0, sqlBuffer.length()-1));
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, CommonService.getM_PriceList_Version_ID(Env.getContextAsInt(ctx, "#M_PriceList_ID"), new Timestamp(System.currentTimeMillis())));		

			rs = pstmt.executeQuery ();
			
			while (rs.next ()) {
				
				retValue.add(new PriceListProduct(rs.getInt(1),rs.getString(2),rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6),
						rs.getString(7),rs.getBigDecimal(8)));
			}

				
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, "getList- " + sql + " - "+ ex);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return retValue;

	}



}
