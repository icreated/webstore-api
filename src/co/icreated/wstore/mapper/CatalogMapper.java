package co.icreated.wstore.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.compiere.model.MProduct;
import org.compiere.model.MProductCategory;


import co.icreated.wstore.api.model.PriceListProductDto;
import co.icreated.wstore.api.model.ProductCategoryDto;

public class CatalogMapper {


  public ProductCategoryDto toDto(MProductCategory productCategory) {
    var dto = new ProductCategoryDto();
    dto.id(productCategory.getM_Product_Category_ID());
    dto.name(productCategory.getName());
    dto.description(productCategory.getDescription());
    return dto;
  }

  public List<ProductCategoryDto> toDto(List<MProductCategory> productCategories) {
    return productCategories.stream().map(this::toDto).toList();
  }

  public PriceListProductDto toDto(MProduct product, BigDecimal priceStd) {
    var dto = new PriceListProductDto();
    dto.id(product.getM_Product_ID());
    dto.value(product.getValue());
    dto.name(product.getName());
    dto.description(product.getDescription());
    dto.help(product.getHelp());
    dto.documentNote(product.getDocumentNote());
    dto.imageURL(product.getImageURL());
    dto.price(priceStd);
    dto.qty(0);
    dto.line(0);
    return dto;
  }

}
