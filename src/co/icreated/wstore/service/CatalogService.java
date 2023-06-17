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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.compiere.model.MPriceListVersion;
import org.compiere.model.MProduct;
import org.compiere.model.MProductCategory;
import org.compiere.model.MProductPrice;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

import co.icreated.wstore.api.model.PriceListProductDto;
import co.icreated.wstore.api.model.ProductCategoryDto;
import co.icreated.wstore.mapper.CatalogMapper;
import co.icreated.wstore.utils.PQuery;


public class CatalogService extends AbstractService {

  CLogger log = CLogger.getCLogger(CatalogService.class);


  public CatalogService(Properties ctx) {
	super(ctx);
  }


  public List<ProductCategoryDto> getCategories() {
    return new PQuery(ctx, MProductCategory.Table_Name, "IsSelfService='Y'", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setOrderBy("Name") //
        .<MProductCategory>stream() //
        .map(CatalogMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }


  public PriceListProductDto getProduct(int M_Product_ID) {
    return new PQuery(ctx, MProduct.Table_Name, "IsBOM='N' AND Discontinued='N' AND M_Product_ID=?",
        null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(M_Product_ID) //
        .<MProduct>stream() //
        .map(product -> priceToDto(product, getPriceListVersionId())) //
        .findAny().get();
  }



  public List<PriceListProductDto> getProducts(int M_Product_Category_ID,
      boolean isWebStoreFeatured) {

    List<Object> params = List.of();
    StringBuilder whereClause = new StringBuilder("isBOM='N' AND Discontinued='N'");
    if (M_Product_Category_ID > 0) {
      whereClause.append(" AND M_Product_Category_ID=?");
      params.add(M_Product_Category_ID);
    }
    if (isWebStoreFeatured) {
      whereClause.append(" AND isWebStoreFeatured='Y'");
    }

    int priceListVersionId = getPriceListVersionId();
    return new PQuery(ctx, MProduct.Table_Name, whereClause.toString(), null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(params) //
        .setOrderBy("Name") //
        .<MProduct>stream() //
        .map(product -> priceToDto(product, priceListVersionId)) //
        .collect(Collectors.toList());
  }


  public List<PriceListProductDto> doSearch(String searchString) {

    String whereClause =
        "IsBOM='N' AND Discontinued='N' AND UPPER(Name || Description) LIKE UPPER(?)";
    int priceListVersionId = getPriceListVersionId();

    return new PQuery(ctx, MProduct.Table_Name, whereClause, null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(searchString) //
        .setOrderBy("Name") //
        .<MProduct>stream() //
        .map(product -> priceToDto(product, priceListVersionId)) //
        .collect(Collectors.toList());
  }


  public List<PriceListProductDto> getProductsById(List<Object> ids) {

    StringBuilder whereClause = new StringBuilder("M_Product_ID IN (") //
        .append(ids.stream().map(v -> "?").collect(Collectors.joining(","))) //
        .append(")");

    int priceListVersionId = getPriceListVersionId();
    return new PQuery(ctx, MProduct.Table_Name, whereClause.toString(), null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(ids) //
        .setOrderBy("Name") //
        .<MProduct>stream() //
        .map(product -> priceToDto(product, priceListVersionId)) //
        .collect(Collectors.toList());
  }


  private PriceListProductDto priceToDto(MProduct product, int priceListVersionId) {
    MProductPrice productPrice =
        MProductPrice.get(ctx, priceListVersionId, product.getM_Product_ID(), null);
    return CatalogMapper.INSTANCE.toDto(product, productPrice.getPriceStd());
  }


  public int getPriceListVersionId() {
    return new PQuery(ctx, MPriceListVersion.Table_Name, "M_PriceList_ID=? AND ValidFrom<=?", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(Env.getContextAsInt(ctx, "#M_PriceList_ID"), Timestamp.from(Instant.now())) //
        .setOrderBy("ValidFrom DESC") //
        .firstId();
  }



}
