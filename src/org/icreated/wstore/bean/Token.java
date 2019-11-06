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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {
	
	@JsonProperty("token")
	String token = null;
	
	@JsonProperty("msg")
	String msg;

	
	public Token() {}
	
	public Token(String token) {
		super();
		this.token = token;
	}
	
	public Token(String token, String msg) {
		super();
		this.token = token;
		this.msg = msg;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
