package co.icreated.wstore.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.compiere.model.MProduct;
import org.compiere.model.MProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import co.icreated.wstore.api.model.PriceListProductDto;
import co.icreated.wstore.api.model.ProductCategoryDto;


@Mapper
public interface CatalogMapper {

  public CatalogMapper INSTANCE = Mappers.getMapper(CatalogMapper.class);


  @Mapping(source = "m_Product_Category_ID", target = "id")
  public ProductCategoryDto toDto(MProductCategory productCategory);

  public List<ProductCategoryDto> toDto(List<MProductCategory> productCategories);

  @Mapping(source = "product.m_Product_ID", target = "id")
  @Mapping(source = "priceStd", target = "price")
  public PriceListProductDto toDto(MProduct product, BigDecimal priceStd);


}
