package co.icreated.wstore.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import static java.util.stream.Collectors.toList;
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
import io.jsonwebtoken.lang.Collections;


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
        .collect(toList());
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
        .filter(Objects::nonNull) //
        .collect(toList());
  }


  public List<PriceListProductDto> getProductsById(List<Object> ids) {

    if (Collections.isEmpty(ids)) {
      return List.of();
    }

    StringBuilder whereClause = new StringBuilder("M_Product_ID IN (") //
        .append(ids.stream().map(v -> "?").collect(joining(","))) //
        .append(")");

    int priceListVersionId = getPriceListVersionId();
    return new PQuery(ctx, MProduct.Table_Name, whereClause.toString(), null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(ids) //
        .setOrderBy("Name") //
        .<MProduct>stream() //
        .map(product -> priceToDto(product, priceListVersionId)) //
        .filter(Objects::nonNull) //
        .collect(toList());
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
