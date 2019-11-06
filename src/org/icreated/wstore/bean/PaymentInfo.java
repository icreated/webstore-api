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
package org.icreated.wstore.bean;

import java.math.BigDecimal;

import org.compiere.model.MPayment;

public class PaymentInfo {
	
	
	public static final String CREDITCARDTYPE_Amex = "A";
	/** MasterCard = M */
	public static final String CREDITCARDTYPE_MasterCard = "M";
	/** Visa = V */
	public static final String CREDITCARDTYPE_Visa = "V";
	/** ATM = C */
	public static final String CREDITCARDTYPE_ATM = "C";
	/** Diners = D */
	public static final String CREDITCARDTYPE_Diners = "D";
	/** Discover = N */
	public static final String CREDITCARDTYPE_Discover = "N";
	/** Purchase Card = P */
	public static final String CREDITCARDTYPE_PurchaseCard = "P";	
	
	
	
	String mpaymentType = MPayment.TENDERTYPE_CreditCard;
	String transactionId = null;
	String creditCardType = null;
	String creditCardNumber = null;
	int creditCardExpMM = 0;
	int creditCardExpYY = 0;
	BigDecimal amount = null;
	int C_Currency_ID = 0;
	
	
	
	
	
	public PaymentInfo(BigDecimal amount, int C_Currency_ID, String tenderType, String transactionId,
			String creditCardType, String creditCardNumber,
			int creditCardExpMM, int creditCardExpYY) {
		super();
		this.amount = amount;
		this.C_Currency_ID = C_Currency_ID;
		this.mpaymentType = tenderType;
		this.transactionId = transactionId;
		this.creditCardType = creditCardType;
		this.creditCardNumber = creditCardNumber;
		this.creditCardExpMM = creditCardExpMM;
		this.creditCardExpYY = creditCardExpYY;
	}


	public PaymentInfo(BigDecimal amount, int C_Currency_ID, String mpaymentType, String transactionId) {
		super();
		this.amount = amount;
		this.C_Currency_ID = C_Currency_ID;
		this.mpaymentType = mpaymentType;
		this.transactionId = transactionId;
	}
	
	
	public String getTenderType() {
		return mpaymentType;
	}
	public void setTenderType(String mpaymentType) {
		this.mpaymentType = mpaymentType;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getCreditCardType() {
		return creditCardType;
	}
	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public int getCreditCardExpMM() {
		return creditCardExpMM;
	}
	public void setCreditCardExpMM(int creditCardExpMM) {
		this.creditCardExpMM = creditCardExpMM;
	}
	public int getCreditCardExpYY() {
		return creditCardExpYY;
	}
	public void setCreditCardExpYY(int creditCardExpYY) {
		this.creditCardExpYY = creditCardExpYY;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public int getC_Currency_ID() {
		return C_Currency_ID;
	}
	public void setC_Currency_ID(int c_Currency_ID) {
		C_Currency_ID = c_Currency_ID;
	}
	
	

}
