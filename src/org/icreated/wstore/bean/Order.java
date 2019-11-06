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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order extends Document {

	
	Shipper shipper;
	List<Shipment> shipments;
	List<Payment> payments;
	List<Document> invoices;
	List<Tax> taxes;
	
	public Order() {}

	public Order(int id, String documentNo, String poReference, String description, String docStatus,
			Date date, BigDecimal totalLines, BigDecimal grandTotal, String docStatusName) {
		
		super(id, documentNo, poReference, description, docStatus, date, totalLines, grandTotal, docStatusName);

	}
	
	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public Shipper getShipper() {
		return shipper;
	}

	public void setShipper(Shipper shipper) {
		this.shipper = shipper;
	}

	public List<Shipment> getShipments() {
		return shipments;
	}

	public void setShipments(List<Shipment> shipments) {
		this.shipments = shipments;
	}

	public List<Document> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Document> invoices) {
		this.invoices = invoices;
	}

	public List<Tax> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<Tax> taxes) {
		this.taxes = taxes;
	}


}
