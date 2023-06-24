package co.icreated.wstore.mapper;

import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MCountry;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrderTax;
import org.compiere.model.MPayment;
import org.compiere.model.MShipper;
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
import co.icreated.wstore.api.model.DocumentDto;
import co.icreated.wstore.api.model.DocumentLineDto;
import co.icreated.wstore.api.model.LocationDto;
import co.icreated.wstore.api.model.OrderDto;
import co.icreated.wstore.api.model.PaymentDto;
import co.icreated.wstore.api.model.ShipmentDto;
import co.icreated.wstore.api.model.ShipperDto;
import co.icreated.wstore.api.model.TaxDto;


@Mapper
public interface AccountMapper {

  public AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);


  @Mapping(source = "AD_User_ID", target = "id")
  @Mapping(source = "EMail", target = "email")
  public AccountInfoDto toDto(MUser user);
  
  
  @Mapping(source = "c_Order_ID", target = "id")
  @Mapping(source = "POReference", target = "poReference")
  @Mapping(source = "dateOrdered", target = "date")
  public DocumentDto toDto(MOrder order);

  @Mapping(source = "c_OrderLine_ID", target = "id")
  @Mapping(source = "m_Product_ID", target = "productId")
  @Mapping(source = "qtyOrdered", target = "qty")
  @Mapping(source = "priceActual", target = "price")
  @Mapping(source = "product.name", target = "name")
  @Mapping(source = "product.description", target = "description")
  public DocumentLineDto toDto(MOrderLine orderLine);
  
  @Mapping(target = "name", expression= "java(org.compiere.model.MTax.get(orderTax.getCtx(), orderTax.getC_Tax_ID()).getName())")
  public TaxDto toDto(MOrderTax orderTax);
  
  
  // OrderDto
  @Mapping(source = "c_Order_ID", target = "id")
  @Mapping(source = "POReference", target = "poReference")
  @Mapping(source = "dateOrdered", target = "date")
  @Mapping(target = "shipAddress", 
  expression="java(toDto(new org.compiere.model.MBPartnerLocation(order.getCtx(), order.getC_BPartner_Location_ID(), order.get_TrxName())))")
  @Mapping(target = "billAddress", 
  expression="java(toDto(new org.compiere.model.MBPartnerLocation(order.getCtx(), order.getBill_Location_ID(), order.get_TrxName())))")  
  public OrderDto toOrderDto(MOrder order);
  
 

  @Mapping(source = "c_Invoice_ID", target = "id")
  public DocumentDto toDto(MInvoice invoice);

  @Mapping(source = "c_Payment_ID", target = "id")
  public PaymentDto toDto(MPayment payment);

  @Mapping(source = "m_Shipper_ID", target = "id")
  public ShipperDto toDto(MShipper shipper);

  @Mapping(source = "m_InOut_ID", target = "id")
  @Mapping(target = "docStatusName",
      expression = "java(org.compiere.model.MRefList.getListName(shipment.getCtx(), 131, shipment.getDocStatus()))")
  public ShipmentDto toDto(MInOut shipment);
  
  
  
  
  


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
