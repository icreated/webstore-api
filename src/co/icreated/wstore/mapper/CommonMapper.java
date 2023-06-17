package co.icreated.wstore.mapper;

import org.compiere.model.MCountry;
import org.compiere.model.MShipper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import co.icreated.wstore.api.model.IdNameBeanDto;
import co.icreated.wstore.api.model.ShipperDto;


@Mapper
public interface CommonMapper {

  public CommonMapper INSTANCE = Mappers.getMapper(CommonMapper.class);


  @Mapping(source = "m_Shipper_ID", target = "id")
  public ShipperDto toDto(MShipper shipper);

  @Mapping(source = "c_Country_ID", target = "id")
  public IdNameBeanDto toDto(MCountry country);

}
