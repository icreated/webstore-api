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

public class Payment {
	
	int id;
	String documentNo;
	String description;
	String docStatus;
	BigDecimal payAmt;
	String trxid;
	String currency;
	String tenderType;
	
	public Payment() {
		
	}
	
	public Payment(int id, String documentNo, String description, String docStatus, BigDecimal payAmt, String trxid,
			String currency, String tenderType) {
		super();
		this.id = id;
		this.documentNo = documentNo;
		this.description = description;
		this.docStatus = docStatus;
		this.payAmt = payAmt;
		this.trxid = trxid;
		this.currency = currency;
		this.tenderType = tenderType;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDocStatus() {
		return docStatus;
	}
	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}
	public BigDecimal getPayAmt() {
		return payAmt;
	}
	public void setPayAmt(BigDecimal payAmt) {
		this.payAmt = payAmt;
	}
	public String getTrxid() {
		return trxid;
	}
	public void setTrxid(String trxid) {
		this.trxid = trxid;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTenderType() {
		return tenderType;
	}
	public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}
	
	

}
