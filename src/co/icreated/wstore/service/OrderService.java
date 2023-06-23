/*******************************************************************************
 * @author Copyright (C) 2019 ICreated, Sergey Polyarus
 * @date 2019 This program is free software; you can redistribute it and/or modify it under the
 *       terms version 2 of the GNU General Public License as published by the Free Software
 *       Foundation. This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *       WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *       PURPOSE. See the GNU General Public License for more details. You should have received a
 *       copy of the GNU General Public License along with this program; if not, write to the Free
 *       Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 ******************************************************************************/
package co.icreated.wstore.service;



import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Provider;
import javax.ws.rs.core.SecurityContext;

import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrderTax;
import org.compiere.model.MPayment;
import org.compiere.model.MProduct;
import org.compiere.model.MRefList;
import org.compiere.model.MTax;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import co.icreated.wstore.api.model.AddressDto;
import co.icreated.wstore.api.model.DocumentDto;
import co.icreated.wstore.api.model.DocumentLineDto;
import co.icreated.wstore.api.model.OrderDto;
import co.icreated.wstore.api.model.PaymentDto;
import co.icreated.wstore.api.model.ShipmentDto;
import co.icreated.wstore.api.model.ShipperDto;
import co.icreated.wstore.api.model.TaxDto;
import co.icreated.wstore.mapper.OrderMapper;
import co.icreated.wstore.utils.PQuery;


public class OrderService extends AbstractService {

  CLogger log = CLogger.getCLogger(OrderService.class);


  public OrderService(Properties ctx, SecurityContext securityContext) {
    super(ctx, securityContext);
  }



  public OrderDto createOrder(OrderDto orderDto) {


    int C_PaymentTerm_ID =
        getSessionUser().getC_PaymentTerm_ID() > 0 ? getSessionUser().getC_PaymentTerm_ID()
            : Env.getContextAsInt(ctx, "#C_PaymentTerm_ID");

    int M_PriceList_ID = Env.getContextAsInt(ctx, "#M_PriceList_ID");

    int C_BPartner_ID = getSessionUser().getC_BPartner_ID();
    int AD_User_ID = getSessionUser().getAD_User_ID();
    int C_BPartner_Location_ID = orderDto.getShipAddress().getId();
    int Bill_BPartner_Location_ID = orderDto.getBillAddress().getId();

    String trxName = Trx.createTrxName("createApiOrder");
    Trx trx = Trx.get(trxName, true);

    MOrder order = new MOrder(ctx, 0, trxName);
    log.log(Level.FINE, "AD_Client_ID=" + order.getAD_Client_ID() + ",AD_Org_ID="
        + order.getAD_Org_ID() + " - " + order);
    //
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

    boolean ok = order.save();

    log.log(Level.FINE, "ID=" + order.getC_Order_ID() + ", DocNo=" + order.getDocumentNo());


    for (DocumentLineDto wbl : orderDto.getLines()) {

      MOrderLine ol = new MOrderLine(order);
      ol.setM_Product_ID(wbl.getId(), true);
      ol.setDescription(wbl.getDescription());
      // ol.setQtyReliquat(new BigDecimal(backlogNum));

      ol.setPrice();
      ol.setPrice(wbl.getPrice());
      ol.setTax();
      ol.setQty(wbl.getQty());
      ok = ol.save();
      if (ok)
        wbl.setLine(ol.getLine());

    } // for all lines

    order.processIt(MOrder.DOCACTION_Prepare);
    order.save();

    if (trx.commit()) {

      order = new MOrder(ctx, order.getC_Order_ID(), order.get_TrxName()); // Refresh it!
      orderDto.setId(order.getC_Order_ID());
      orderDto.setDocumentNo(order.getDocumentNo());
      orderDto.setGrandTotal(order.getGrandTotal());
      orderDto.setTotalLines(order.getTotalLines());
      orderDto.setTaxes(Stream.of(order.getTaxes(true))
          .map(t -> new TaxDto().name(MTax.get(ctx, t.getC_Tax_ID()).getName()).tax(t.getTaxAmt()))
          .collect(Collectors.toList()));

      List<DocumentLineDto> list = new ArrayList<DocumentLineDto>();
      for (MOrderLine orderLine : order.getLines()) {
        MProduct product = orderLine.getProduct();
        list.add(new DocumentLineDto().id(orderLine.getC_OrderLine_ID())
            .productId(product.getM_Product_ID()).line(orderLine.getLine()).name(product.getName())
            .description(product.getDescription()).priceList(orderLine.getPriceList())
            .price(orderLine.getPriceActual()).qty(orderLine.getQtyOrdered())
            .lineNetAmt(orderLine.getLineNetAmt()));

      }
      orderDto.setLines(list);

      log.log(Level.INFO, "Order created, #" + order.getDocumentNo());
    } else {
      trx.rollback();
      log.log(Level.WARNING, "Not proceed. Transaction Aborted, #" + order.getDocumentNo());
    }

    trx.close();
    trx = null;

    BigDecimal amt = order.getGrandTotal();
    log.info("Amt=" + amt);

    return orderDto;
  } // createOrder



  public List<DocumentDto> getOrders() {


    List<DocumentDto> list = new ArrayList<DocumentDto>();

    String sql =
        "SELECT C_Order_ID, DocumentNo, POReference, Description, docStatus, dateOrdered, totalLines, grandTotal FROM C_Order "
            + "WHERE C_BPartner_ID=? "; //

    sql +=
        "AND DocStatus NOT IN ('DR') AND C_DocTypeTarget_ID IN (SELECT C_DocType_ID FROM C_DocType WHERE  "
            + "DocBaseType = 'SOO')  " + "ORDER BY DocumentNo DESC "; // AND DocSubTypeSO IN ('WI' )
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String docStatusName = null;

    try {
      pstmt = DB.prepareStatement(sql, null);
      pstmt.setInt(1, getSessionUser().getC_BPartner_ID());

      rs = pstmt.executeQuery();
      while (rs.next()) {
        docStatusName = MRefList.getListName(ctx, 131, rs.getString(5));
        list.add(new DocumentDto().id(rs.getInt(1)) //
            .documentNo(rs.getString(2)) //
            .poReference(rs.getString(3)) //
            .description(rs.getString(4)) //
            .docStatus(rs.getString(5)) //
            .date(rs.getDate(6)) //
            .totalLines(rs.getBigDecimal(7)) //
            .grandTotal(rs.getBigDecimal(8)) //
            .docStatusName(docStatusName) //
        );
      }

    } catch (Exception e) {
      log.log(Level.SEVERE, sql, e);
    } finally {
      DB.close(rs, pstmt);
      rs = null;
      pstmt = null;
    }
    log.log(Level.FINE, "#" + list.size());

    return list;
  } // getOrders



  public void createOrderLine(MOrder order, MProduct product, BigDecimal price) {

    MOrderLine ol = getOrderLine(order, product, true);

    if (ol == null)
      ol = new MOrderLine(order);

    ol.setProduct(product);
    ol.setDescription(product.getDescription());
    ol.setPrice();
    ol.setPrice(price);
    ol.setTax();
    ol.setQty(Env.ONE);
    ol.save();


  }



  public boolean processOrder(String DocAction, MOrder order) {
    if (DocAction == null || DocAction.length() == 0)
      return false;

    order.setDocAction(DocAction, true); // force creation
    boolean ok = order.processIt(DocAction);
    order.save();
    return ok;
  } // processOrder



  public static MOrderLine getOrderLine(MOrder order, MProduct product, boolean reload) {

    MOrderLine[] lines = order.getLines(reload, null);
    MOrderLine ol = null;
    for (int i = 0; i < lines.length; i++) {
      if (lines[i].getM_Product_ID() == product.getM_Product_ID()) {
        ol = lines[i];
        break;
      }
    }
    return ol;

  }


  public OrderDto getOrder(int C_Order_ID) {

    String sql =
        "SELECT o.C_Order_ID, o.DocumentNo, o.POReference, o.Description, o.docStatus, o.dateOrdered, o.totalLines,  "
            + "bpl.C_BPartner_Location_ID, bpl.Name, u.Name, l.Address1, l.Address2, l.Postal, l.City, bpl.phone, l.C_Country_ID, c.Name, "
            + "bill.C_BPartner_Location_ID, bill.Name, null, bl.Address1, bl.Address2, bl.Postal, bl.City, bill.phone, bl.C_Country_ID, bc.Name, "
            + "sh.Name, o.GrandTotal, sh.M_Shipper_ID, o.freightAmt " + "FROM C_Order o "
            + "INNER JOIN C_BPartner_Location bpl ON bpl.C_BPartner_Location_ID = o.C_BPartner_Location_ID "
            + "INNER JOIN C_Location l ON l.C_Location_ID = bpl.C_Location_ID "
            + "INNER JOIN C_Country c ON c.C_Country_ID = l.C_Country_ID "
            + "INNER JOIN C_BPartner_Location bill ON bill.C_BPartner_Location_ID = o.Bill_Location_ID "
            + "INNER JOIN C_Location bl ON bl.C_Location_ID = bill.C_Location_ID  "
            + "INNER JOIN C_Country bc ON bc.C_Country_ID = bl.C_Country_ID "
            + "LEFT JOIN AD_User u ON o.AD_User_ID = u.AD_User_ID "
            + "LEFT JOIN M_Shipper sh ON o.M_Shipper_ID = sh.M_Shipper_ID "
            + "WHERE o.C_Order_ID = ? AND o.C_BPartner_ID = ?";

    OrderDto orderDto = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String docStatusName;
    try {
      pstmt = DB.prepareStatement(sql, null);
      pstmt.setInt(1, C_Order_ID);
      pstmt.setInt(2, getSessionUser().getC_BPartner_ID());
      rs = pstmt.executeQuery();
      if (rs.next()) {
        docStatusName = MRefList.getListName(ctx, 131, rs.getString(5));

        // AddressDto deliveryAddress = new AddressDto() //
        // .id(rs.getInt(8)) //
        // .label(rs.getString(9)) //
        // .name(rs.getString(10)) //
        // .address1(rs.getString(11)) //
        // .address2(rs.getString(12)) //
        // .postal(rs.getString(13)) //
        // .city(rs.getString(14)) //
        // .phone(rs.getString(15)) //
        // .countryId(rs.getInt(16)) //
        // .countryName(rs.getString(17));
        //
        // AddressDto invoiceAddress = new AddressDto() //
        // .id(rs.getInt(18)) //
        // .label(rs.getString(19)) //
        // .name(rs.getString(20)) //
        // .address1(rs.getString(21)) //
        // .address2(rs.getString(22)) //
        // .postal(rs.getString(23)) //
        // .city(rs.getString(24)) //
        // .phone(rs.getString(26)) //
        // .countryName(rs.getString(27));

        orderDto = new OrderDto().id(rs.getInt(1)) //
            .documentNo(rs.getString(2)) //
            .poReference(rs.getString(3)) //
            .description(rs.getString(4)) //
            .docStatus(rs.getString(5)) //
            .date(rs.getDate(6)) //
            .totalLines(rs.getBigDecimal(7)) //
            .grandTotal(rs.getBigDecimal(29)) //
            .docStatusName(docStatusName); //


        orderDto.setShipper(new ShipperDto().id(rs.getInt(30)).name(rs.getString(28)));
        // orderDto.setShipAddress(deliveryAddress);
        // orderDto.setBillAddress(invoiceAddress);
        orderDto.setLines(getOrderLines(C_Order_ID));
        orderDto.setShipments(getShipments(C_Order_ID));
        orderDto.setPayments(getPayments(C_Order_ID));
        orderDto.setInvoices(getInvoices(C_Order_ID));

        List<MOrderTax> orderTaxes = new Query(ctx, MOrderTax.Table_Name, "C_Order_ID=?", null)
            .setParameters(C_Order_ID).list();

        orderDto.setTaxes(orderTaxes.stream()
            .map(
                t -> new TaxDto().name(MTax.get(ctx, t.getC_Tax_ID()).getName()).tax(t.getTaxAmt()))
            .collect(Collectors.toList()));

      }


    } catch (Exception e) {
      log.log(Level.SEVERE, "getOrder", e);
    } finally {
      DB.close(rs, pstmt);
      rs = null;
      pstmt = null;
    }


    return orderDto;

  }

  private List<DocumentLineDto> getOrderLines(int C_Order_ID) {


    return new PQuery(ctx, MOrderLine.Table_Name, "C_Order_ID=?", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(C_Order_ID).setOrderBy("Line") //
        .<MOrderLine>stream() //
        .map(OrderMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }


  public List<ShipmentDto> getShipments(int id, boolean byUser) {

    String whereClause = "docStatus NOT IN ('DR') AND ";
    whereClause += byUser ? "AD_User_ID=?" : "C_BPartner_ID=?";

    return new PQuery(ctx, MInOut.Table_Name, whereClause, null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(id).setOrderBy("DocumentNo DESC") //
        .<MInOut>stream() //
        .map(OrderMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }



  public List<ShipmentDto> getShipments(int C_Order_ID) {

    return new PQuery(ctx, MInOut.Table_Name, "C_Order_ID=? AND DocStatus NOT IN ('DR')", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(C_Order_ID).setOrderBy("DocumentNo DESC") //
        .<MInOut>stream() //
        .map(OrderMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }


  public List<PaymentDto> getPayments(int C_Order_ID) {

    return new PQuery(ctx, MPayment.Table_Name, "C_Order_ID=? AND DocStatus NOT IN ('DR')", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(C_Order_ID).setOrderBy("DocumentNo DESC") //
        .<MPayment>stream() //
        .map(OrderMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }


  public List<DocumentDto> getInvoices(int id, boolean byUser) {

    return new PQuery(ctx, MInvoice.Table_Name, byUser ? "AD_User_ID=?" : "C_BPartner_ID=?", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(id).setOrderBy("DocumentNo DESC") //
        .<MInvoice>stream() //
        .map(OrderMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }



  public List<DocumentDto> getInvoices(int C_Order_ID) {
    return new PQuery(ctx, MInvoice.Table_Name, "C_Order_ID=?", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(C_Order_ID).setOrderBy("DocumentNo DESC") //
        .<MInvoice>stream() //
        .map(OrderMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());

  }



  public boolean orderBelongsToUser(MOrder order) {

    return (getSessionUser().getC_BPartner_ID() == order.getC_BPartner_ID());
  }



}
