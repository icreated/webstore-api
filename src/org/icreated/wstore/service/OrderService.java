/*******************************************************************************
 * @author Copyright (C) 2019 ICreated, Sergey Polyarus
 *  @date 2019
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms version 2 of the GNU General Public License as published
 *  by the Free Software Foundation. This program is distributed in the hope
 *  that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc., 
 *  59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 ******************************************************************************/
package org.icreated.wstore.service;



import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrderTax;
import org.compiere.model.MProduct;
import org.compiere.model.MRefList;
import org.compiere.model.MTax;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.icreated.wstore.bean.Address;
import org.icreated.wstore.bean.Document;
import org.icreated.wstore.bean.DocumentLine;
import org.icreated.wstore.bean.Order;
import org.icreated.wstore.bean.Payment;
import org.icreated.wstore.bean.SessionUser;
import org.icreated.wstore.bean.Shipment;
import org.icreated.wstore.bean.Shipper;
import org.icreated.wstore.bean.Tax;


public class OrderService extends AService {



	CLogger log = CLogger.getCLogger(OrderService.class);
	
	
	
	public OrderService(Properties ctx, SessionUser user) {
		
		this.ctx = ctx;
		this.sessionUser = user;
		Env.setCtx(ctx);
	}

	

	public Order createOrder(Order orderBean) {
		
		
		int C_PaymentTerm_ID = sessionUser.getC_PaymentTerm_ID() > 0 ?
				sessionUser.getC_PaymentTerm_ID() : Env.getContextAsInt(ctx, "#C_PaymentTerm_ID");
		
		int M_PriceList_ID = Env.getContextAsInt(ctx, "#M_PriceList_ID");
		
		int C_BPartner_ID = sessionUser.getC_BPartner_ID();
		int AD_User_ID = sessionUser.getAD_User_ID();
		int C_BPartner_Location_ID = orderBean.getShipAddress().getId();
		int Bill_BPartner_Location_ID = orderBean.getBillAddress().getId();
		
		String trxName = Trx.createTrxName("createApiOrder");
		Trx trx = Trx.get(trxName, true);
		
		MOrder order = new MOrder (ctx, 0, trxName);
		log.log(Level.FINE,"AD_Client_ID=" + order.getAD_Client_ID() + ",AD_Org_ID=" + order.getAD_Org_ID() + " - " + order);
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
		
		order.setM_Shipper_ID(orderBean.getShipper().getId());
		order.setSendEMail(true);
		order.setDocAction(MOrder.DOCACTION_Prepare);
		
		boolean ok = order.save();
		
		log.log(Level.FINE,"ID=" + order.getC_Order_ID()+ ", DocNo=" + order.getDocumentNo());
		
		
		for (DocumentLine wbl : orderBean.getLines()) {
			
			MOrderLine ol = new MOrderLine(order);
			ol.setM_Product_ID(wbl.getId(), true);
			ol.setDescription(wbl.getDescription());
//			ol.setQtyReliquat(new BigDecimal(backlogNum));			
			
			ol.setPrice();
			ol.setPrice(wbl.getPrice());
			ol.setTax();
			ol.setQty(wbl.getQty());
			ok = ol.save();		
			if (ok)
				wbl.setLine(ol.getLine());

		}	//	for all lines
			
		order.processIt (MOrder.DOCACTION_Prepare);
		order.save();
		
		if (trx.commit()) {	
			
			order = new MOrder(ctx, order.getC_Order_ID(), order.get_TrxName());  // Refresh it!
			orderBean.setId(order.getC_Order_ID());
			orderBean.setDocumentNo(order.getDocumentNo());
			orderBean.setGrandTotal(order.getGrandTotal());
			orderBean.setTotalLines(order.getTotalLines());
			orderBean.setTaxes(Stream.of(order.getTaxes(true))
					.map(t-> new Tax(MTax.get(ctx, t.getC_Tax_ID()).getName(), t.getTaxAmt()))
					.collect(Collectors.toList()));
			
			List<DocumentLine> list = new ArrayList<DocumentLine>();
			for(MOrderLine orderLine : order.getLines()) {
				MProduct product = orderLine.getProduct();
				list.add(new DocumentLine(orderLine.getC_OrderLine_ID(), product.getM_Product_ID(), orderLine.getLine(), product.getName(), 
						product.getDescription(), orderLine.getPriceList(), orderLine.getPriceActual(), orderLine.getQtyOrdered(), orderLine.getLineNetAmt()));
				
			}
			orderBean.setLines(list);
			
			log.log(Level.INFO, "Order created, #"+order.getDocumentNo());	
		} else {
			trx.rollback();
			log.log(Level.WARNING, "Not proceed. Transaction Aborted, #"+order.getDocumentNo());			
		}
		
		trx.close();
		trx = null;
		
		BigDecimal amt = order.getGrandTotal();
		log.info("Amt=" + amt);
		
		return orderBean;
	}	//	createOrder	
	
	
	
	
	
	

	public List<Document> getOrders() {


		List<Document> list = new ArrayList<Document>();
		
		String sql = "SELECT C_Order_ID, DocumentNo, POReference, Description, docStatus, dateOrdered, totalLines, grandTotal FROM C_Order " +
				"WHERE C_BPartner_ID=? "; //
				
				sql+="AND DocStatus NOT IN ('DR') AND C_DocTypeTarget_ID IN (SELECT C_DocType_ID FROM C_DocType WHERE  " +
				"DocBaseType = 'SOO')  " +
				"ORDER BY DocumentNo DESC " ; //  AND DocSubTypeSO IN ('WI' )
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String docStatusName = null;

		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, sessionUser.getC_BPartner_ID());
				
			rs = pstmt.executeQuery();
			while (rs.next()) {
				docStatusName = MRefList.getListName(ctx, 131, rs.getString(5));
				
				Document bean = new Document(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), 
						rs.getString(5), rs.getDate(6), rs.getBigDecimal(7), rs.getBigDecimal(8), docStatusName);
				list.add(bean);
			}
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		log.log(Level.FINE,"#" + list.size());

		return list;
	}	//	getOrders	
	
	

	

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
	
	
	

	public boolean processOrder (String DocAction, MOrder order)
	{
		if (DocAction == null || DocAction.length() == 0)
			return false;

		order.setDocAction (DocAction, true);	//	force creation
		boolean ok = order.processIt (DocAction);
		order.save();
		return ok;
	}	//	processOrder	
	

	
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
	
	
	public Order getOrder(int C_Order_ID) {
		
		String sql = "SELECT o.C_Order_ID, o.DocumentNo, o.POReference, o.Description, o.docStatus, o.dateOrdered, o.totalLines,  " +
				"bpl.C_BPartner_Location_ID, bpl.Name, u.Name, l.Address1, l.Address2, l.Postal, l.City, bpl.phone, l.C_Country_ID, c.Name, " +
				"bill.C_BPartner_Location_ID, bill.Name, null, bl.Address1, bl.Address2, bl.Postal, bl.City, bill.phone, bl.C_Country_ID, bc.Name, " +
				"sh.Name, o.GrandTotal, sh.M_Shipper_ID, o.freightAmt " +
				"FROM C_Order o " +
				"INNER JOIN C_BPartner_Location bpl ON bpl.C_BPartner_Location_ID = o.C_BPartner_Location_ID " +
				"INNER JOIN C_Location l ON l.C_Location_ID = bpl.C_Location_ID " +
				"INNER JOIN C_Country c ON c.C_Country_ID = l.C_Country_ID " +
				"INNER JOIN C_BPartner_Location bill ON bill.C_BPartner_Location_ID = o.Bill_Location_ID " +
				"INNER JOIN C_Location bl ON bl.C_Location_ID = bill.C_Location_ID  " +
				"INNER JOIN C_Country bc ON bc.C_Country_ID = bl.C_Country_ID " +
				"LEFT JOIN AD_User u ON o.AD_User_ID = u.AD_User_ID " +
				"LEFT JOIN M_Shipper sh ON o.M_Shipper_ID = sh.M_Shipper_ID " +
				"WHERE o.C_Order_ID = ? AND o.C_BPartner_ID = ?" ;
		
		Order bean = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String docStatusName;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Order_ID);
			pstmt.setInt(2, sessionUser.getC_BPartner_ID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				docStatusName = MRefList.getListName(ctx, 131, rs.getString(5));
				
				Address deliveryAddress = new Address(rs.getInt(8), rs.getString(9), rs.getString(10), 
						rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), 
						rs.getInt(16), rs.getString(17));
				Address invoiceAddress = new Address(rs.getInt(18), rs.getString(19), rs.getString(20),
						rs.getString(21), rs.getString(22), rs.getString(23), rs.getString(24), rs.getString(25), 
						rs.getInt(26), rs.getString(27));

				bean = new Order(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), 
						rs.getString(5), rs.getDate(6),rs.getBigDecimal(7), rs.getBigDecimal(29), docStatusName);
				
				bean.setShipper(new Shipper(rs.getInt(30), rs.getString(28),null));
				bean.setShipAddress(deliveryAddress);
				bean.setBillAddress(invoiceAddress);
				bean.setLines(getOrderLines(C_Order_ID));
				bean.setShipments(getShipments(C_Order_ID));
				bean.setPayments(getPayments(C_Order_ID));
				bean.setInvoices(getInvoices(C_Order_ID));
				
				List<MOrderTax> orderTaxes = new Query(ctx, MOrderTax.Table_Name, "C_Order_ID=?", null).setParameters(C_Order_ID)
						.list();

				bean.setTaxes(orderTaxes.stream()
						.map(t-> new Tax(MTax.get(ctx, t.getC_Tax_ID()).getName(), t.getTaxAmt()))
						.collect(Collectors.toList()));

			}
			

		}
		catch (Exception e) {
				log.log(Level.SEVERE,"getOrder", e);
		} finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}		
		
		
		return bean;
		
	}
	
	private List<DocumentLine> getOrderLines(int C_Order_ID) {
		
		String sql = "SELECT ol.C_OrderLine_ID, ol.M_Product_ID, ol.Line, p.Name, p.Description, " +
				"ol.PriceList, ol.PriceActual, ol.qtyOrdered, ol.LineNetAmt " +
				"FROM C_OrderLine ol " +
				"INNER JOIN M_Product p ON ol.M_Product_ID = p.M_Product_ID " +
				"WHERE ol.C_Order_ID = ? ORDER BY ol.Line" ;
		
		List<DocumentLine> lines = new ArrayList<DocumentLine>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Order_ID);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				lines.add(new DocumentLine(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
						rs.getBigDecimal(6), rs.getBigDecimal(7), rs.getBigDecimal(8),rs.getBigDecimal(9)));
			}
			

		}
		catch (Exception e) {
				log.log(Level.SEVERE,"getOrderLine", e);
		} finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}				
		
		return lines;
	}

	/**
	 * 	Get Shipments
	 *	@return shipments of BP
	 */
	public List<Shipment> getShipments(int id, boolean byUser)
	{
		List<Shipment> list = new ArrayList<Shipment>();
		String sql = "SELECT io.M_InOut_ID, io.DocumentNo, sh.Name, io.Description, io.docStatus, io.shipDate, io.TrackingNo " +
				"FROM M_InOut io INNER JOIN M_Shipper sh ON sh.M_Shipper_ID=io.M_Shipper_ID " +
				"WHERE ";
				if (byUser)
					sql+="io.AD_User_ID=? ";
				else
					sql+="io.C_BPartner_ID=? ";		
				sql+="AND io.DocStatus NOT IN ('DR') ORDER BY io.DocumentNo DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String docStatusName;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				docStatusName = MRefList.getListName(ctx, 131, rs.getString(5));
				
				Shipment bean = new Shipment(rs.getInt(1), rs.getString(2), null, rs.getString(4), 
						rs.getString(5), rs.getDate(6),null, null, docStatusName);
				bean.setShipper(new Shipper(rs.getString(3)));
				bean.setTrackingNo(rs.getString(7));
				list.add(bean);
			}
			

		}
		catch (Exception e)
		{
				log.log(Level.SEVERE,"getShipments", e);
		} finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return list;
	}	//	getShipments
	
	
	
	public List<Shipment> getShipments( int C_Order_ID)
	{
		List<Shipment> list = new ArrayList<Shipment>();
		String sql = "SELECT io.M_InOut_ID, io.DocumentNo, sh.Name, io.Description, io.docStatus, io.shipDate, io.TrackingNo " +
				"FROM M_InOut io INNER JOIN M_Shipper sh ON sh.M_Shipper_ID=io.M_Shipper_ID " +
				"WHERE io.C_Order_ID=? ";		
				sql+="AND io.DocStatus NOT IN ('DR') ORDER BY io.DocumentNo DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String docStatusName;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Order_ID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				docStatusName = MRefList.getListName(ctx, 131, rs.getString(5));
				
				Shipment bean = new Shipment(rs.getInt(1), rs.getString(2), null, rs.getString(4), 
						rs.getString(5), rs.getDate(6),null, null, docStatusName);
				bean.setShipper(new Shipper(rs.getString(3)));
				bean.setTrackingNo(rs.getString(7));
				list.add(bean);
			}
			
		}
		catch (Exception e)
		{
				log.log(Level.SEVERE,"getShipments", e);
		} finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return list;
	}	//	getShipments	
	
	
	
	
	public List<Payment> getPayments( int C_Order_ID)
	{
		List<Payment> list = new ArrayList<Payment>();
		
		String sql = "SELECT p.C_Payment_ID, p.DocumentNo, p.Description, p.docStatus, p.payAmt, p.orig_trxid, c.iso_code, p.tenderType " +
				"FROM C_Payment p " +
				"INNER JOIN C_Currency c ON p.C_Currency_ID = c.C_Currency_ID " +
				"WHERE C_Order_ID=? " +
				"AND DocStatus NOT IN ('DR') ORDER BY DocumentNo DESC";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Order_ID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Payment bean = 
						new Payment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), 
								rs.getBigDecimal(5),  rs.getString(6),  rs.getString(7),  rs.getString(8));
				list.add(bean);
			}
			
		}
		catch (Exception e)
		{
				log.log(Level.SEVERE,"getPayments", e);
				
		} finally {
			
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return list;
	}	//	getShipments	
	

	
	public List<Document> getInvoices(int id, boolean byUser) {

		List<Document> list = new ArrayList<Document>();
		
		
		String sql = "SELECT C_Invoice_ID, DocumentNo, null, Description, docStatus, dateInvoiced, totalLines, grandTotal FROM C_Invoice WHERE ";
			if (byUser)
				sql+="AD_User_ID=? ";
			else
				sql+="C_BPartner_ID=? ";	
			sql+= "ORDER BY DocumentNo DESC" ;
			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String docStatusName;

		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				docStatusName = MRefList.getListName(ctx, 131, rs.getString(5));
				
				Document bean = new Document(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), 
						rs.getString(5), rs.getDate(6), rs.getBigDecimal(7), rs.getBigDecimal(8), docStatusName);
				list.add(bean);
			}
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		log.log(Level.FINE,"#" + list.size());

		return list;
	}	//	getOrders	
	
	
	
	public List<Document> getInvoices(int C_Order_ID) {

		List<Document> list = new ArrayList<Document>();
		
		
		String sql = "SELECT C_Invoice_ID, DocumentNo, null, Description, docStatus, dateInvoiced, totalLines, grandTotal "
				+ "FROM C_Invoice WHERE C_Order_ID=? ";	
			sql+= "ORDER BY DocumentNo DESC" ;
			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String docStatusName;

		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Order_ID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				docStatusName = MRefList.getListName(ctx, 131, rs.getString(5));
				
				Document bean = new Document(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), 
						rs.getString(5), rs.getDate(6), rs.getBigDecimal(7), rs.getBigDecimal(8), docStatusName);
				list.add(bean);
			}
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		log.log(Level.FINE,"#" + list.size());

		return list;
	}	//	getOrders		
	
	

	
	
	public boolean orderBelongsToUser (MOrder order) {
		
		return (sessionUser.getC_BPartner_ID() == order.getC_BPartner_ID());
	}
	



	
}
