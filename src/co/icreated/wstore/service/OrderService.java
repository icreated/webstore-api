package co.icreated.wstore.service;


import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

import co.icreated.wstore.api.model.DocumentDto;
import co.icreated.wstore.api.model.DocumentLineDto;
import co.icreated.wstore.api.model.OrderDto;
import co.icreated.wstore.api.model.PaymentDto;
import co.icreated.wstore.api.model.ShipmentDto;
import co.icreated.wstore.mapper.AccountMapper;
import co.icreated.wstore.utils.PQuery;
import co.icreated.wstore.utils.Transaction;


public class OrderService extends AbstractService {

  CLogger log = CLogger.getCLogger(OrderService.class);


  public OrderService(Properties ctx, SecurityContext securityContext) {
    super(ctx, securityContext);
  }



  public OrderDto createOrder(OrderDto orderDto) {

    // During order creation there are some Env.getCtx() calls which is not defined (same issue as
    // Query loosing ctx)
    // Here MStorageReservationLog is called like this: MStorageReservationLog log = new
    // MStorageReservationLog(Env.getCtx(), 0, trxName);
    // PO checks integrity of AD_Client so we are obliged to defined it with this awful solution to
    // be sure it's not lost again
    Env.setCtx(ctx);

    int C_PaymentTerm_ID =
        getSessionUser().getC_PaymentTerm_ID() > 0 ? getSessionUser().getC_PaymentTerm_ID()
            : Env.getContextAsInt(ctx, "#C_PaymentTerm_ID");

    int M_PriceList_ID = Env.getContextAsInt(ctx, "#M_PriceList_ID");

    int C_BPartner_ID = getSessionUser().getC_BPartner_ID();
    int AD_User_ID = getSessionUser().getAD_User_ID();
    int C_BPartner_Location_ID = orderDto.getShipAddress().getId();
    int Bill_BPartner_Location_ID = orderDto.getBillAddress().getId();

    MOrder createdOrder = Transaction.run(trxName -> {
      MOrder order = new MOrder(ctx, 0, trxName);
      order.setAD_Org_ID(Env.getAD_Org_ID(ctx));
      order.setC_DocTypeTarget_ID(MOrder.DocSubTypeSO_Prepay);
      order.setPaymentRule(MOrder.PAYMENTRULE_OnCredit);
      order.setDeliveryRule(MOrder.DELIVERYRULE_Availability);
      order.setInvoiceRule(MOrder.INVOICERULE_Immediate);
      order.setDeliveryViaRule(MOrder.DELIVERYVIARULE_Shipper);
      order.setIsSelfService(true);

      order.setC_PaymentTerm_ID(C_PaymentTerm_ID);
      order.setM_PriceList_ID(M_PriceList_ID);

      order.setSalesRep_ID(Env.getContextAsInt(ctx, "#SalesRep_ID"));
      order.setC_BPartner_ID(C_BPartner_ID);
      order.setC_BPartner_Location_ID(C_BPartner_Location_ID);
      order.setBill_Location_ID(Bill_BPartner_Location_ID);
      order.setAD_User_ID(AD_User_ID);

      order.setM_Shipper_ID(orderDto.getShipper().getId());
      order.setSendEMail(true);
      order.setDocAction(MOrder.DOCACTION_Prepare);
      order.save();

      for (DocumentLineDto wbl : orderDto.getLines()) {
        MOrderLine ol = new MOrderLine(order);
        ol.setM_Product_ID(wbl.getProductId(), true);
        ol.setDescription(wbl.getDescription());
        ol.setPrice();
        ol.setPrice(wbl.getPrice());
        ol.setTax();
        ol.setQty(wbl.getQty());
        ol.save();
      }
      order.processIt(MOrder.DOCACTION_Prepare);
      order.save();
      return order;
    });

    MOrder order = new MOrder(ctx, createdOrder.getC_Order_ID(), createdOrder.get_TrxName()); // Refresh
    orderDto.setId(order.getC_Order_ID());
    orderDto.setDocumentNo(order.getDocumentNo());
    orderDto.setGrandTotal(order.getGrandTotal());
    orderDto.setTotalLines(order.getTotalLines());
    orderDto.setTaxes(Stream.of(order.getTaxes(true)) //
        .map(AccountMapper.INSTANCE::toDto) //
        .collect(Collectors.toList()));
    orderDto.setLines(Stream.of(order.getLines()) //
        .map(AccountMapper.INSTANCE::toDto) //
        .collect(Collectors.toList()));

    log.log(Level.INFO, "Order created, #" + order.getDocumentNo());
    return orderDto;
  }


  public List<DocumentDto> getOrders() {

    return new PQuery(ctx, MOrder.Table_Name, "C_BPartner_ID=? AND DocStatus NOT IN ('DR')", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(getSessionUser().getC_BPartner_ID()) //
        .setOrderBy("DocumentNo DESC") //
        .<MOrder>stream() //
        .map(AccountMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }


  public boolean processOrder(String DocAction, MOrder order) {
    if (StringUtils.isBlank(DocAction))
      return false;

    order.setDocAction(DocAction, true); // force creation
    boolean ok = order.processIt(DocAction);
    order.save();
    return ok;
  }


  public OrderDto getOrder(int C_Order_ID) {

    MOrder order = new MOrder(ctx, C_Order_ID, null);
    return AccountMapper.INSTANCE.toOrderDto(order);
  }


  public List<ShipmentDto> getShipments(int id, boolean byUser) {

    String whereClause = "docStatus NOT IN ('DR') AND ";
    whereClause += byUser ? "AD_User_ID=?" : "C_BPartner_ID=?";

    return new PQuery(ctx, MInOut.Table_Name, whereClause, null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(id).setOrderBy("DocumentNo DESC") //
        .<MInOut>stream() //
        .map(AccountMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }



  public List<ShipmentDto> getShipments(int C_Order_ID) {

    return new PQuery(ctx, MInOut.Table_Name, "C_Order_ID=? AND DocStatus NOT IN ('DR')", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(C_Order_ID).setOrderBy("DocumentNo DESC") //
        .<MInOut>stream() //
        .map(AccountMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }


  public List<PaymentDto> getPayments(int C_Order_ID) {

    return new PQuery(ctx, MPayment.Table_Name, "C_Order_ID=? AND DocStatus NOT IN ('DR')", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(C_Order_ID).setOrderBy("DocumentNo DESC") //
        .<MPayment>stream() //
        .map(AccountMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }


  public List<DocumentDto> getInvoices(int id, boolean byUser) {

    return new PQuery(ctx, MInvoice.Table_Name, byUser ? "AD_User_ID=?" : "C_BPartner_ID=?", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(id).setOrderBy("DocumentNo DESC") //
        .<MInvoice>stream() //
        .map(AccountMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }



  public List<DocumentDto> getInvoices(int C_Order_ID) {
    return new PQuery(ctx, MInvoice.Table_Name, "C_Order_ID=?", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(C_Order_ID).setOrderBy("DocumentNo DESC") //
        .<MInvoice>stream() //
        .map(AccountMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }



  public boolean orderBelongsToUser(MOrder order) {
    return (getSessionUser().getC_BPartner_ID() == order.getC_BPartner_ID());
  }



}
