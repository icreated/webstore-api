package co.icreated.wstore.mapper;

import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MCountry;
import org.compiere.model.MLocation;
import org.compiere.model.MUser;
import org.compiere.model.X_C_BPartner_Location;
import org.compiere.model.X_C_Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import co.icreated.wstore.api.model.AccountInfoDto;
import co.icreated.wstore.api.model.AddressDto;
import co.icreated.wstore.api.model.CountryDto;
import co.icreated.wstore.api.model.LocationDto;


@Mapper
public interface AccountMapper {

  public AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);


  @Mapping(source = "AD_User_ID", target = "id")
  @Mapping(source = "EMail", target = "email")
  public AccountInfoDto toDto(MUser user);


  // Partner Location
  @Mapping(source = "c_BPartner_Location_ID", target = "id")
  @Mapping(expression = "java(toDto(bpl.getLocation(true)))", target = "location")
  public AddressDto toDto(MBPartnerLocation bpl);

  public X_C_BPartner_Location to(AddressDto addressDto, @MappingTarget X_C_BPartner_Location bpl);


  // Location
  @Mapping(source = "c_Location_ID", target = "id")
  public LocationDto toDto(MLocation location);

  @Mapping(source = "id", target = "c_Location_ID")
  @Mapping(source = "locationDto.country.id", target = "c_Country_ID")
  public X_C_Location to(LocationDto locationDto, @MappingTarget X_C_Location location);


  // Country
  @Mapping(source = "c_Country_ID", target = "id")
  public CountryDto toDto(MCountry country);

  @Mapping(source = "id", target = "c_Country_ID")
  public MCountry to(CountryDto countryDto, @MappingTarget MCountry country);

}
