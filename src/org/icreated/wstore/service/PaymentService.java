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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MBPBankAccount;
import org.compiere.model.MBPartner;
import org.compiere.model.MBankAccount;
import org.compiere.model.MDocType;
import org.compiere.model.MLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentTerm;
import org.compiere.model.MUser;
import org.compiere.model.Query;
import org.compiere.model.X_C_Payment;
import org.compiere.process.DocAction;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.icreated.wstore.bean.PaymentInfo;
import org.icreated.wstore.bean.SessionUser;


public class PaymentService extends AService {
	
	
	
	CLogger log = CLogger.getCLogger(PaymentService.class);
	
	
	public PaymentService(Properties ctx, SessionUser user) {
		
		this.ctx = ctx;
		this.sessionUser = user;
	}
	
	
	public List<MPayment> getPayments(int C_BPartner_ID) {

		List<MPayment> list = new ArrayList<MPayment>();
		
		if (C_BPartner_ID == 0)
			C_BPartner_ID = sessionUser.getC_BPartner_ID();
		
		String sql = "SELECT * FROM C_Payment WHERE C_BPartner_ID=? "
				+ "ORDER BY DocumentNo DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_BPartner_ID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new MPayment (ctx, rs, null));
			}
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		log.log(Level.FINE,"#" + list.size());

		return list;
	}	
	
	
	
	
	public MPayment createPayment(MOrder order, PaymentInfo paymentInfo) {
		
		MPayment payment = new MPayment(ctx, 0, null);
		payment.setAD_Org_ID(order.getAD_Org_ID());
		payment.setIsSelfService(true);
		payment.setAmount (paymentInfo.getC_Currency_ID(), paymentInfo.getAmount()); //	for CC selection
		payment.setIsOnline (true);
		//	Sales CC Trx
		payment.setC_DocType_ID(true);
		payment.setTrxType(MPayment.TRXTYPE_Sales);
		payment.setTenderType(paymentInfo.getTenderType());
		payment.setC_Invoice_ID (order.getC_Invoice_ID());						
		payment.setC_Order_ID (order.getC_Order_ID());

		//	BP Info
		MBPBankAccount account = getBankAccount(order);
		
		String whereClause = "AD_Org_ID=? AND C_Currency_ID=?";
		MBankAccount ba = new Query(order.getCtx(),MBankAccount.Table_Name,whereClause,order.get_TrxName())
			.setParameters(order.getAD_Org_ID(), paymentInfo.getC_Currency_ID())
			.setOrderBy("IsDefault DESC")
			.first();
		
		MDocType[] doctypes = MDocType.getOfDocBaseType(order.getCtx(), MDocType.DOCBASETYPE_ARReceipt);
		MDocType doctype = null;
		for (MDocType doc : doctypes) {
			if (doc.getAD_Org_ID() == order.getAD_Org_ID()) {
				doctype = doc;
				break;
			}
		}
		if (doctype == null)
			doctype = doctypes[0];
			
		payment.setC_DocType_ID(doctype.getC_DocType_ID());
		if (ba != null)
			payment.setC_BankAccount_ID(ba.getC_BankAccount_ID());
		payment.setC_BP_BankAccount_ID(account.getC_BP_BankAccount_ID());
		payment.setBP_BankAccount(account);
		payment.setIsReceipt(true);	
		payment.setOrig_TrxID(paymentInfo.getTransactionId());
			
		
		try {
			
			payment.setA_EMail(sessionUser.getEmail());
			if (paymentInfo.getTenderType().equals(MPayment.TENDERTYPE_CreditCard)) {
				payment.setCreditCardType(paymentInfo.getCreditCardType());
				payment.setCreditCardNumber (paymentInfo.getCreditCardNumber());
				payment.setCreditCardExpMM(paymentInfo.getCreditCardExpMM());
				payment.setCreditCardExpYY(paymentInfo.getCreditCardExpYY());
			}
			payment.saveToBP_BankAccount(account);
							
			if (payment.getPayAmt().compareTo(Env.ZERO) < 0)
				payment.setPayAmt(payment.getPayAmt().abs());
			payment.setIsApproved(true);


			boolean isCompleted = MOrder.STATUS_Completed.equals(order.getDocStatus())
					|| MOrder.STATUS_Closed.equals(order.getDocStatus());
			
					
			if (!isCompleted) {
				
				if (payment.get_ID() == 0)
					payment.saveEx();
				order.setC_Payment_ID (payment.getC_Payment_ID());
				order.setDocAction (MOrder.DOCACTION_WaitComplete);
				order.processIt(MOrder.DOCACTION_WaitComplete);
				order.saveEx();					
				
				payment.setC_Invoice_ID (order.getC_Invoice_ID());						
				payment.setC_Order_ID (order.getC_Order_ID());

			}
			
			if (isCompleted)
				log.log(Level.WARNING, "Order not processed :" + order);

			payment.processIt(DocAction.ACTION_Complete);
			payment.save();
					
		
	    } catch(Exception e){
	    	log.log(Level.WARNING, "ERROR PAYMENT in processPayment :"+e);
	    }	
		return payment;
	}	//	processPayment

	
	
	
	public MPayment createSimplePayment(MOrder order, String tenderType) {
		
		MBPBankAccount account = getBankAccount(order);
		
		MBankAccount ba = new Query(order.getCtx(),MBankAccount.Table_Name,"AD_Org_ID=? AND C_Currency_ID=?",order.get_TrxName())
		.setParameters(order.getAD_Org_ID(), order.getC_Currency_ID())
		.setOrderBy("IsDefault DESC")
		.first();
		
		MPayment payment = new MPayment(ctx, 0, order.get_TrxName());
		payment.setIsSelfService(true);
		payment.setAmount(0, order.getGrandTotal());
		payment.setIsOnline(false);
		payment.setC_DocType_ID(true);
		payment.setTrxType(X_C_Payment.TRXTYPE_Sales);
		payment.setTenderType(tenderType);
		payment.setC_Order_ID(order.getC_Order_ID());
		payment.setC_BankAccount_ID(ba.getC_BankAccount_ID());
		payment.setC_BP_BankAccount_ID(account.getC_BP_BankAccount_ID());
		payment.setBP_BankAccount(account);
		//
		payment.save();
	
	
		boolean isCompleted = MOrder.STATUS_Completed.equals(order.getDocStatus())
				|| MOrder.STATUS_Closed.equals(order.getDocStatus());
		
				
		if (!isCompleted) {
			
			if (payment.get_ID() == 0)
				payment.saveEx();
			if (tenderType.equals(MPayment.TENDERTYPE_Check))
				order.setPaymentRule(MOrder.PAYMENTRULE_Check);
			else if (tenderType.equals(MPayment.TENDERTYPE_DirectDeposit))
				order.setPaymentRule(MOrder.PAYMENTRULE_DirectDeposit);
			
			order.setC_Payment_ID (payment.getC_Payment_ID());
			order.setDocAction (MOrder.DOCACTION_Prepare);
			order.processIt (MOrder.DOCACTION_Prepare);
			order.saveEx();					
									
			payment.setC_Order_ID (order.getC_Order_ID());
			payment.save();
		}
		
	
			
		return payment;
		
		
	}
	
		
	
	public List<MPaymentTerm> getPaymentTerms() {
		
		List<MPaymentTerm> paymentTerm = new ArrayList<MPaymentTerm>();
		String sql = "SELECT * FROM C_PaymentTerm WHERE  AD_Client_ID = ? AND IsActive = 'Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1,Env.getAD_Client_ID(ctx));
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				paymentTerm.add(new MPaymentTerm(ctx, rs, null));
				
			}
			
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"", e);
		} finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		return paymentTerm;
		
	}
	
	
	public MBPBankAccount getBankAccount(MOrder order)
	{
		
		MBPartner bp = MBPartner.get(ctx, order.getC_BPartner_ID());
		MBPBankAccount retValue = null;
		//	Find Bank Account for exact User
		MBPBankAccount[] bas = bp.getBankAccounts(true);
		for (int i = 0; i < bas.length; i++)
		{
			if (bas[i].getAD_User_ID() == sessionUser.getAD_User_ID() && bas[i].isActive())
				retValue = bas[i];
		}

		//	create new
		if (retValue == null)
		{
			MUser user = MUser.get(ctx, sessionUser.getAD_User_ID());
			MLocation location = MLocation.get(ctx, order.getBill_Location_ID(), null);
			retValue = new MBPBankAccount (ctx, bp, user, location);
			retValue.setAD_User_ID(sessionUser.getAD_User_ID());
			retValue.save();
		}
		
		return retValue;
	}	//	getBankAccount		
	
	
	
	public MPayment getPaymentByTrxId(String tx) {
		
		String sql = "SELECT * FROM C_Payment WHERE Orig_TrxId LIKE ? AND IsActive = 'Y'";
		
		MPayment payment = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, tx);
			rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				payment =  new MPayment(ctx, rs, null);
				
			}
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"", e);
		}finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		return payment;
		
	}	
	
	

	
	

}
