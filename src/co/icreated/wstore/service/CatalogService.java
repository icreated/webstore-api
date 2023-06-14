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
package co.icreated.wstore.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MInvoice;
import org.compiere.model.MProductCategory;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import co.icreated.wstore.utils.PQuery;
import co.icreated.wstore.api.model.PriceListProductDto;
import co.icreated.wstore.api.model.ProductCategoryDto;
import co.icreated.wstore.mapper.CatalogMapper;


public class CatalogService extends AService {
	
	
	CLogger log = CLogger.getCLogger(CatalogService.class);
	CatalogMapper catalogMapper = CatalogMapper.INSTANCE;
	
	Properties ctx;
	
	
	public CatalogService(Properties ctx) {
		
		this.ctx = ctx;
	}
	
	
	public List<ProductCategoryDto> getCategories() {
		
	    List<MProductCategory> categories =
	            new PQuery(ctx, MProductCategory.Table_Name, "isActive='Y' AND IsSelfService='Y' AND AD_Org_ID=?", null) //
	            	.setParameters(Env.getAD_Org_ID(ctx)) //
	                .setOrderBy("Name") //
	                .list();
		
	    return catalogMapper.toDto(categories);
	}	
	
	
	
	public PriceListProductDto getProduct(int M_Product_ID) {
		
		PriceListProductDto product = null;
			
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
				product = new PriceListProductDto().id(rs.getInt(1)).value(rs.getString(2)).name(rs.getString(3)).description(rs.getString(4)).help(rs.getString(5))
						.documentNote(rs.getString(6)).imageURL(rs.getString(7)).price(rs.getBigDecimal(8));		
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
	
	
	
	public  List<PriceListProductDto> getProducts(int M_Product_Category_ID, boolean isWebStoreFeatured) {
		
		 List<PriceListProductDto> list = new  ArrayList<PriceListProductDto>();
			
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
				list.add(new PriceListProductDto().id(rs.getInt(1)).value(rs.getString(2)).name(rs.getString(3)).description(rs.getString(4)).help(rs.getString(5))
						.documentNote(rs.getString(6)).imageURL(rs.getString(7)).price(rs.getBigDecimal(8)));
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
	
	
	public  List<PriceListProductDto> doSearch(String searchString) {
		
		
		 List<PriceListProductDto> list = new  ArrayList<PriceListProductDto>();
			
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
				list.add(new PriceListProductDto().id(rs.getInt(1)).value(rs.getString(2)).name(rs.getString(3)).description(rs.getString(4)).help(rs.getString(5)));
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
	
	
	public  List<PriceListProductDto> getProductsById(List<Integer> ids) {
		
		List<PriceListProductDto> retValue = new ArrayList<PriceListProductDto>();
		
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
				retValue.add(new PriceListProductDto().id(rs.getInt(1)).value(rs.getString(2)).name(rs.getString(3)).description(rs.getString(4)).help(rs.getString(5))
						.documentNote(rs.getString(6)).imageURL(rs.getString(7)).price(rs.getBigDecimal(8)));		
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
