package co.icreated.wstore.mapper;

import org.compiere.model.MCountry;
import org.compiere.model.MShipper;
import co.icreated.wstore.api.model.IdNamePairDto;
import co.icreated.wstore.api.model.ShipperDto;

public class CommonMapper {

  public ShipperDto toDto(MShipper shipper) {
    var dto = new ShipperDto();
    dto.id(shipper.getM_Shipper_ID());
    dto.name(shipper.getName());
    return dto;
  }

  public IdNamePairDto toDto(MCountry country) {
    var dto = new IdNamePairDto();
    dto.id(country.getC_Country_ID());
    dto.name(country.getName());
    return dto;
  }

}
