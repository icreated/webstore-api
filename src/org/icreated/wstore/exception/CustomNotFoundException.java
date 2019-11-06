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
package org.icreated.wstore.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CustomNotFoundException extends WebApplicationException {
	 
	private static final long serialVersionUID = 3025267653935141609L;

	/**
	  * Create a HTTP 404 (Not Found) exception.
	  */
	  public CustomNotFoundException() {
	    super(Response.status(Response.Status.NOT_FOUND).build());
	  }
	 
	  /**
	  * Create a HTTP 404 (Not Found) exception.
	  * @param message the String that is the entity of the 404 response.
	  */
	  public CustomNotFoundException(String message) {
	    super(Response.status(Response.Status.NOT_FOUND).
	    entity(message).type(MediaType.TEXT_PLAIN).build());
	  }
	}
