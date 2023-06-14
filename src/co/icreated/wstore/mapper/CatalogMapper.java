package co.icreated.wstore.mapper;

import java.util.List;

import org.compiere.model.MProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import co.icreated.wstore.api.model.ProductCategoryDto;


@Mapper
public interface CatalogMapper {
	
  public CatalogMapper INSTANCE = Mappers.getMapper( CatalogMapper.class );


  @Mapping(source = "m_Product_Category_ID", target = "id")
  public abstract ProductCategoryDto toDto(MProductCategory productCategory);


  public abstract List<ProductCategoryDto> toDto(List<MProductCategory> productCategories);

}
