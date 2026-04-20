package co.icreated.wstore.controller;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;

import co.icreated.wstore.api.model.PriceListProductDto;
import co.icreated.wstore.api.model.ProductCategoryDto;
import co.icreated.wstore.api.service.CatalogApi;
import co.icreated.wstore.service.CatalogService;


@PermitAll
public class CatalogController implements CatalogApi {


  @Context
  CatalogService catalogService;


  @Override
  public List<ProductCategoryDto> getCategories() {
    return catalogService.getCategories();
  }


  @Override
  public List<PriceListProductDto> getProductsFeatured() {
    return catalogService.getProducts(0, true);
  }


  @Override
  public List<PriceListProductDto> getProducts(Integer categoryId) {
    return catalogService.getProducts(categoryId, false);
  }


  @Override
  public List<PriceListProductDto> getCart(@NotNull List<Integer> ids) {
    return catalogService.getProductsById(ids);
  }


  @Override
  public List<PriceListProductDto> getProductsSearch(String searchString) {
    return catalogService.doSearch(searchString);
  }


}
