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

public class WStore {

	int W_Store_ID;
	int AD_Client_ID;
	int AD_Org_ID;
	int SalesRep_ID;
	int M_PriceList_ID;
	int M_Warehouse_ID;
	int C_PaymentTerm_ID;
	String name;
	String description;
	String help;
	String webParam1;
	String webParam2;
	String webParam3;
	String webParam4;
	String webParam5;
	String webParam6;
	String stylesheet;
	String url;
	
	
	
	
	
	public WStore(int W_Store_ID, int AD_Client_ID, int AD_Org_ID, int SalesRep_ID, int M_PriceList_ID,
			int M_Warehouse_ID, String name, String description, String help, String webParam1, String webParam2,
			String webParam3, String webParam4, String webParam5, String webParam6, String stylesheet, 
			int C_PaymentTerm_ID, String url) {
		super();
		this.W_Store_ID = W_Store_ID;
		this.AD_Client_ID = AD_Client_ID;
		this.AD_Org_ID = AD_Org_ID;
		this.SalesRep_ID = SalesRep_ID;
		this.M_PriceList_ID = M_PriceList_ID;
		this.M_Warehouse_ID = M_Warehouse_ID;
		this.name = name;
		this.description = description;
		this.help = help;
		this.webParam1 = webParam1;
		this.webParam2 = webParam2;
		this.webParam3 = webParam3;
		this.webParam4 = webParam4;
		this.webParam5 = webParam5;
		this.webParam6 = webParam6;
		this.stylesheet = stylesheet;
		this.C_PaymentTerm_ID = C_PaymentTerm_ID;
		this.url = url;
	}
	
	
	
	public int getW_Store_ID() {
		return W_Store_ID;
	}
	public int getAD_Client_ID() {
		return AD_Client_ID;
	}
	public int getAD_Org_ID() {
		return AD_Org_ID;
	}
	public int getSalesRep_ID() {
		return SalesRep_ID;
	}
	public int getM_PriceList_ID() {
		return M_PriceList_ID;
	}
	public int getM_Warehouse_ID() {
		return M_Warehouse_ID;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getHelp() {
		return help;
	}
	public String getWebParam1() {
		return webParam1;
	}
	public String getWebParam2() {
		return webParam2;
	}
	public String getWebParam3() {
		return webParam3;
	}
	public String getWebParam4() {
		return webParam4;
	}
	public String getWebParam5() {
		return webParam5;
	}
	public String getWebParam6() {
		return webParam6;
	}
	public String getStylesheet() {
		return stylesheet;
	}
	public int getC_PaymentTerm_ID() {
		return C_PaymentTerm_ID;
	}
	public String getUrl() {
		return url;
	}
	
	
	

}
