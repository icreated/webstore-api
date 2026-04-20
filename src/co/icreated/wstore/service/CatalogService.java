package co.icreated.wstore.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;

import static java.util.stream.Collectors.joining;


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

  CatalogMapper catalogMapper = new CatalogMapper();

  public CatalogService(Properties ctx) {
    super(ctx);
  }


  public List<ProductCategoryDto> getCategories() {
    return new PQuery(ctx, MProductCategory.Table_Name, "IsSelfService='Y'", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setOrderBy("Name") //
        .<MProductCategory>stream() //
        .map(catalogMapper::toDto) //
        .toList();
  }


  public List<PriceListProductDto> getProducts(int M_Product_Category_ID,
      boolean isWebStoreFeatured) {

    List<Object> params = new ArrayList<Object>();
    StringBuilder whereClause = new StringBuilder("Discontinued='N'");
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
        .filter(Objects::nonNull) //
        .toList();
  }


  public List<PriceListProductDto> doSearch(String searchString) {

    if (searchString == null || searchString.isEmpty() || searchString.equals("%")) {
      return List.of();
    }

    String term = searchString.toUpperCase();
    if (!term.startsWith("%")) term = "%" + term;
    if (!term.endsWith("%")) term = term + "%";

    String whereClause =
        "IsBOM='N' AND Discontinued='N' AND UPPER(Name || Description) LIKE UPPER(?)";
    int priceListVersionId = getPriceListVersionId();

    return new PQuery(ctx, MProduct.Table_Name, whereClause, null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(term) //
        .setOrderBy("Name") //
        .<MProduct>stream() //
        .map(product -> priceToDto(product, priceListVersionId)) //
        .filter(Objects::nonNull) //
        .toList();
  }


  public List<PriceListProductDto> getProductsById(List<Integer> ids) {

    if (ids == null || ids.isEmpty()) {
      return List.of();
    }

    StringBuilder whereClause = new StringBuilder("M_Product_ID IN (") //
        .append(ids.stream().map(v -> "?").collect(joining(","))) //
        .append(")");

    int priceListVersionId = getPriceListVersionId();
    return new PQuery(ctx, MProduct.Table_Name, whereClause.toString(), null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(new ArrayList<Object>(ids)) //
        .setOrderBy("Name") //
        .<MProduct>stream() //
        .map(product -> priceToDto(product, priceListVersionId)) //
        .filter(Objects::nonNull) //
        .toList();
  }


  private PriceListProductDto priceToDto(MProduct product, int priceListVersionId) {
    MProductPrice productPrice =
        MProductPrice.get(ctx, priceListVersionId, product.getM_Product_ID(), null);
    if (productPrice == null) {
      log.log(Level.WARNING, "Price not defined", product.getName());
      return null;
    }
    return catalogMapper.toDto(product, productPrice.getPriceStd());
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
