/*******************************************************************************
 * @author Copyright (C) 2019 ICreated, Sergey Polyarus
 * @author sergey.polyarus@icreated.fr
 * @date 2019
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 ******************************************************************************/
package org.icreated.wstore.bean;

import java.util.Date;

public class AccountInfo {
	
	int id = 0;
	String value;
	String name;
	String email;
	Date birthday;
	
	
	public AccountInfo() {}
	

	public AccountInfo(int id, String value, String name, String email, Date birthday) {
		super();
		this.id = id;
		this.value = value;
		this.name = name;
		this.email = email;
		this.birthday = birthday;
	}




	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	
	

}
