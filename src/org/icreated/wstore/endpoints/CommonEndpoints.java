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
package org.icreated.wstore.endpoints;

import java.util.List;
import java.util.Properties;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.compiere.util.Env;
import org.icreated.wstore.bean.IdNameBean;
import org.icreated.wstore.bean.Shipper;
import org.icreated.wstore.bean.Token;
import org.icreated.wstore.service.AccountService;
import org.icreated.wstore.service.CommonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;


@PermitAll
@Path("/common")
@Tag(name = "Common services")
public class CommonEndpoints {
	
	@Context
	Properties ctx;
	
	
	@GET
	@Path("countries")
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Country List", description = "List of all countries")   
	public List<IdNameBean> getCountries() {
		
		return AccountService.getCountries();
	}
	
	@GET
	@Path("shippers")
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Shipper List", description = "List of all shippers")   
	public List<Shipper> getShippers() {
		
		return CommonService.getShippers(Env.getAD_Client_ID(ctx));
	}
	
	@POST
	@Path("lookup/email")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Email lookup", description = "Lookup Email")  
	public Response lookupEmail(
			@Parameter(description = "Token Form", schema = @Schema(implementation =  Token.class), required = true) 
			Token email) {

		boolean ok = CommonService.isUnique(ctx, "Email", email.getToken());
		System.out.println(email.getToken()+"   "+ok);
		return Response.status(Response.Status.OK).entity(ok).build();

	}	
	

}
