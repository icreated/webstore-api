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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Document {
	
	int id = 0;
	String documentNo;
	String poReference;
	String description;
	String docStatus;
	String docStatusName;
	Date date;
	BigDecimal totalLines;
	BigDecimal grandTotal;
	String name;

	
	List<DocumentLine> lines = new ArrayList<DocumentLine>();
	
	Address shipAddress;
	Address billAddress;
	
	public Document() {}


	public Document(int id, String documentNo, String poReference,
			String description, String docStatus, Date date,
			BigDecimal totalLines, BigDecimal grandTotal, String docStatusName) {
		super();
		this.id = id;
		this.documentNo = documentNo;
		this.poReference = poReference;
		this.description = description;
		this.docStatus = docStatus;
		this.docStatusName = docStatusName;
		this.date = date;
		this.totalLines = totalLines;
		this.grandTotal = grandTotal;
		
	}
	
	public List<DocumentLine> getLines() {
		return lines;
	}

	public void setLines(List<DocumentLine> lines) {
		this.lines = lines;
	}

	public int getId() {
		return id;
	}
	public String getDocumentNo() {
		return documentNo;
	}
	public String getPoReference() {
		return poReference;
	}
	public String getDescription() {
		return description;
	}
	public String getDocStatus() {
		return docStatus;
	}
	public Date getDate() {
		return date;
	}
	public BigDecimal getTotalLines() {
		return totalLines;
	}
	public String getDocStatusName() {
		return docStatusName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public Address getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(Address deliveryAddress) {
		this.shipAddress = deliveryAddress;
	}

	public Address getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(Address invoiceAddress) {
		this.billAddress = invoiceAddress;
	}

	public BigDecimal getGrandTotal() {
		return grandTotal;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public void setPoReference(String poReference) {
		this.poReference = poReference;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}

	public void setDocStatusName(String docStatusName) {
		this.docStatusName = docStatusName;
	}

	public void setTotalLines(BigDecimal totalLines) {
		this.totalLines = totalLines;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}	
	
	
	

}
