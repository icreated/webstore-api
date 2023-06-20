package co.icreated.wstore.mapper;

import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.MRefList;
import org.compiere.model.MShipper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import co.icreated.wstore.api.model.DocumentDto;
import co.icreated.wstore.api.model.DocumentLineDto;
import co.icreated.wstore.api.model.PaymentDto;
import co.icreated.wstore.api.model.ShipmentDto;
import co.icreated.wstore.api.model.ShipperDto;

@Mapper
public interface OrderMapper {

  public OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

  @Mapping(source = "c_Order_ID", target = "id")
  public DocumentDto toDto(MOrder order);

  @Mapping(source = "c_OrderLine_ID", target = "id")
  public DocumentLineDto toDto(MOrderLine orderLine);

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


}
