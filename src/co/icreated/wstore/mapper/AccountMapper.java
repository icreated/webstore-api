package co.icreated.wstore.mapper;

import java.util.stream.Stream;

import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MCountry;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrderTax;
import org.compiere.model.MPayment;
import org.compiere.model.MRefList;
import org.compiere.model.MShipper;
import org.compiere.model.MTax;
import org.compiere.model.MUser;
import org.compiere.model.X_C_BPartner_Location;
import org.compiere.model.X_C_Location;

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


public class AccountMapper {


  public AccountInfoDto toDto(MUser user) {
    var dto = new AccountInfoDto();
    dto.id(user.getAD_User_ID());
    dto.value(user.getValue());
    dto.name(user.getName());
    dto.email(user.getEMail());
    dto.birthday(user.getBirthday());
    return dto;
  }

  public DocumentDto toDto(MOrder order) {
    var dto = new DocumentDto();
    dto.id(order.getC_Order_ID());
    dto.documentNo(order.getDocumentNo());
    dto.poReference(order.getPOReference());
    dto.description(order.getDescription());
    dto.docStatus(order.getDocStatus());
    dto.docStatusName(order.getDocStatusName());
    dto.date(order.getDateOrdered());
    dto.totalLines(order.getTotalLines());
    dto.grandTotal(order.getGrandTotal());
    return dto;
  }

  public DocumentLineDto toDto(MOrderLine orderLine) {
    var dto = new DocumentLineDto();
    dto.id(orderLine.getC_OrderLine_ID());
    dto.productId(orderLine.getM_Product_ID());
    dto.line(orderLine.getLine());
    dto.name(orderLine.getProduct().getName());
    dto.description(orderLine.getProduct().getDescription());
    dto.priceList(orderLine.getPriceList());
    dto.price(orderLine.getPriceActual());
    dto.qty(orderLine.getQtyOrdered());
    dto.lineNetAmt(orderLine.getLineNetAmt());
    return dto;
  }

  public TaxDto toDto(MOrderTax orderTax) {
    var dto = new TaxDto();
    dto.name(MTax.get(orderTax.getCtx(), orderTax.getC_Tax_ID()).getName());
    dto.taxAmt(orderTax.getTaxAmt());
    return dto;
  }

  // OrderDto
  public OrderDto toOrderDto(MOrder order) {
    var dto = new OrderDto();
    dto.id(order.getC_Order_ID());
    dto.documentNo(order.getDocumentNo());
    dto.poReference(order.getPOReference());
    dto.description(order.getDescription());
    dto.docStatus(order.getDocStatus());
    dto.docStatusName(order.getDocStatusName());
    dto.date(order.getDateOrdered());
    dto.totalLines(order.getTotalLines());
    dto.grandTotal(order.getGrandTotal());
    dto.lines(Stream.of(order.getLines()).map(line -> toDto(line)).toList());
    dto.shipAddress(toDto(new MBPartnerLocation(order.getCtx(), order.getC_BPartner_Location_ID(),
        order.get_TrxName())));
    dto.billAddress(toDto(new org.compiere.model.MBPartnerLocation(order.getCtx(),
        order.getBill_Location_ID(), order.get_TrxName())));
    dto.taxes(Stream.of(order.getTaxes(true)).map(orderTax -> toDto(orderTax)).toList());
    dto.shipper(toDto(new MShipper(order.getCtx(), order.getM_Shipper_ID(), order.get_TrxName())));
    return dto;
  }

  public DocumentDto toDto(MInvoice invoice) {
    var dto = new DocumentDto();
    dto.id(invoice.getC_Invoice_ID());
    dto.documentNo(invoice.getDocumentNo());
    dto.poReference(invoice.getPOReference());
    dto.description(invoice.getDescription());
    dto.docStatus(invoice.getDocStatus());
    dto.docStatusName(invoice.getDocStatusName());
    dto.date(invoice.getDateOrdered());
    dto.totalLines(invoice.getTotalLines());
    dto.grandTotal(invoice.getGrandTotal());
    return dto;
  }

  public PaymentDto toDto(MPayment payment) {
    var dto = new PaymentDto();
    dto.id(payment.getC_Payment_ID());
    dto.documentNo(payment.getDocumentNo());
    dto.description(payment.getDescription());
    dto.docStatus(payment.getDocStatus());
    dto.payAmt(payment.getPayAmt());
    dto.trxid(payment.getOrig_TrxID());
    dto.currency(payment.getCurrencyISO());
    dto.tenderType(payment.getTenderType());
    return dto;
  }

  public ShipperDto toDto(MShipper shipper) {
    var dto = new ShipperDto();
    dto.id(shipper.getM_Shipper_ID());
    dto.name(shipper.getName());
    return dto;
  }

  public ShipmentDto toDto(MInOut shipment) {
    var dto = new ShipmentDto();
    dto.id(shipment.getM_InOut_ID());
    dto.documentNo(shipment.getDocumentNo());
    dto.description(shipment.getDescription());
    dto.docStatus(shipment.getDocStatus());
    dto.docStatusName(MRefList.getListName(shipment.getCtx(), 131, shipment.getDocStatus()));
    dto.date(shipment.getShipDate());
    dto.trackingNo(shipment.getTrackingNo());
    return dto;
  }

  // Partner Location
  public AddressDto toDto(MBPartnerLocation bpl) {
    var dto = new AddressDto();
    dto.id(bpl.getC_BPartner_Location_ID());
    dto.name(bpl.getName());
    dto.phone(bpl.getPhone());
    dto.location(toDto(bpl.getLocation(true)));
    return dto;
  }

  public X_C_BPartner_Location to(AddressDto addressDto, X_C_BPartner_Location bpl) {
    bpl.setC_BPartner_Location_ID(addressDto.getId());
    bpl.setName(addressDto.getName());
    bpl.setPhone(addressDto.getPhone());
    bpl.setC_Location_ID(addressDto.getLocation().getId());
    return bpl;
  }


  // Location
  public LocationDto toDto(MLocation location) {
    var dto = new LocationDto();
    dto.id(location.getC_Location_ID());
    dto.address1(location.getAddress1());
    dto.address2(location.getAddress2());
    dto.postal(location.getPostal());
    dto.city(location.getCity());
    dto.country(toDto(location.getCountry()));
    return dto;
  }

  public X_C_Location to(LocationDto locationDto, X_C_Location location) {
    location.setC_Location_ID(locationDto.getId());
    location.setAddress1(locationDto.getAddress1());
    location.setAddress2(locationDto.getAddress2());
    location.setPostal(locationDto.getPostal());
    location.setCity(locationDto.getCity());
    location.setC_Country_ID(locationDto.getCountry().getId());
    return location;
  }


  // Country
  public CountryDto toDto(MCountry country) {
    var dto = new CountryDto();
    dto.id(country.getC_Country_ID());
    dto.name(country.getName());
    return dto;
  }

  public MCountry to(CountryDto countryDto, MCountry country) {
    country.setC_Country_ID(countryDto.getId());
    country.setName(countryDto.getName());
    return country;
  }

}
