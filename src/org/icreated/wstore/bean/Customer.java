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

import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MLocation;
import org.compiere.model.MUser;
import org.compiere.util.KeyNamePair;

public class Customer  {


	MBPartner  bpartner = null;
	MUser user = null;
	
	String bpartnerName;
	String firstName;
	String lastName;
	String email;
	String phone;
	String phone2;
	String fax;
	String description;
	KeyNamePair greeting = null;
	boolean isDefault = false;
	
	int deliveryLocationID;
	int billLocationID;
	int billUserID;



	public int getBillUserID() {
		return billUserID;
	}



	public void setBillUserID(int billUserID) {
		this.billUserID = billUserID;
	}



	public Customer() {
		
		
	}
	
	
	
	public Customer(MBPartner bpartner, MUser user,  String bpartnerName,
			String firstName, String lastName, String email, String phone,
			String phone2, String fax, String description, boolean isDefault) {
		super();
		this.bpartner = bpartner;
	
		this.user = user;
		this.bpartnerName = bpartnerName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.phone2 = phone2;
		this.fax = fax;
		this.description = description;
		this.isDefault = isDefault;
		
		setDeliveryLocationID(user.getC_BPartner_Location_ID());
		setBillLocationID(user.getC_BPartner_Location_ID());
	}
	
	
	public String getName() {
		
		return lastName+" "+firstName;
	}
	
	
	
	public boolean isValid()
	{
		if (user == null)
			return false;
		boolean ok = user.getAD_User_ID() != 0;
		return ok;
	}	//	isValid
	
	
	
	
	
	public void setBpartner(MBPartner bpartner) {
		this.bpartner = bpartner;
	}
	
	
	
	public void setUser(MUser user) {
		this.user = user;
		setDeliveryLocationID(user.getC_BPartner_Location_ID());
		setBillLocationID(user.getC_BPartner_Location_ID());
	}
	
	
	
	public MBPartner getBPartner() {
		
		return bpartner;
	}
	
	public MUser getUser() {
		
		return user;
	}
	
	public MLocation getUserLocation() {
		
		if (bpartner == null)
			return null;
		
		MBPartnerLocation[] bpl = bpartner.getLocations(false);
		for(int j=0; j<bpl.length;j++) {
			if (bpl[j].getC_BPartner_Location_ID() == user.getC_BPartner_Location_ID())
				return bpl[j].getLocation(true);
		}
		return null;
	}
	
	
	public String getBpartnerName() {
		return bpartnerName;
	}
	public void setBpartnerName(String bpartnerName) {
		this.bpartnerName = bpartnerName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFullName() {
		return this.firstName+" "+this.lastName;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public void setGreeting (KeyNamePair kp)
	{
		greeting = kp;
	
	}
	public KeyNamePair getGreeting()
	{
			return greeting;
		
	}
	
	
	
	public String getDescription() {
		return description;
	}
	
	
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	public int getDeliveryLocationID() {
		return deliveryLocationID;
	}
	
	
	public void setDeliveryLocationID(int deliveryLocationID) {
		this.deliveryLocationID = deliveryLocationID;
	}
	
	
	
	public int getBillLocationID() {
		return billLocationID;
	}
	
	
	public void setBillLocationID(int billLocationID) {
		this.billLocationID = billLocationID;
	}
	
	
	
	
	@Override
		public String toString() {
			return getFullName();
		}
		
		
		
		
}
