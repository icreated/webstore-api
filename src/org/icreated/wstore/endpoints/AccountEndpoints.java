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
package org.icreated.wstore.endpoints;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MUser;
import org.compiere.process.DocAction;
import org.compiere.util.DB;
import org.icreated.wstore.bean.AccountInfo;
import org.icreated.wstore.bean.Address;
import org.icreated.wstore.bean.Document;
import org.icreated.wstore.bean.NewAccountForm;
import org.icreated.wstore.bean.Order;
import org.icreated.wstore.bean.Password;
import org.icreated.wstore.bean.SessionUser;
import org.icreated.wstore.bean.Token;
import org.icreated.wstore.security.IdempiereUserService;
import org.icreated.wstore.security.TokenHandler;
import org.icreated.wstore.service.AccountService;
import org.icreated.wstore.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@RolesAllowed({"ROLE_USER"})
@Path("/account")
@Tag(name = "Account services")
public class AccountEndpoints {
	

    @Context
    Properties ctx;
    
	@POST
	@Path("signup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Account Creation", description = "Create new account",
    responses = {
    		@ApiResponse(responseCode = "400", description = "Missing Account Info or Email"),
    		@ApiResponse(responseCode = "201", description = "Account created, Token in return"),
    })
	@PermitAll
	public Response signup(
			@Parameter(description = "New Account Form", schema = @Schema(implementation =  NewAccountForm.class), required = true) 
			NewAccountForm newAccount, 
			@Context AccountService accountService) {
		
		if (newAccount == null || newAccount.getEmail() == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		String token = null;
		MUser user = null;

		// Social connection
		if (newAccount.getPassword() == null || newAccount.getPassword().trim().length() == 0) {
			int AD_User_ID = DB.getSQLValue(null, "SELECT max(AD_User_ID) FROM AD_User WHERE UPPER(email) LIKE ?", newAccount.getEmail().toUpperCase());
			if (AD_User_ID > 0) {
				user = MUser.get(ctx, AD_User_ID);
			} else {
				user = accountService.createNewAccount(newAccount);
			}
		} else {
			user = accountService.createNewAccount(newAccount);
		}


		if (user.getAD_User_ID() > 0) {
			IdempiereUserService userService = new IdempiereUserService(true);
			final SessionUser sessionUser = userService.loadUserByUsername(user.getEMail(), true);

			
			TokenHandler tokenHandler = new TokenHandler(userService);
	        token = tokenHandler.createTokenForUser(sessionUser);
	          
		}

		return Response.status(Response.Status.CREATED).entity(new Token(token)).build();
	}	
	
	
	@POST
	@Path("password/change")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Password change", description = "Change customer password",
    responses = {
    		@ApiResponse(responseCode = "500", description = "Password not changed"),
    		@ApiResponse(responseCode = "202", description = "Old password not correct"),
    		@ApiResponse(responseCode = "200", description = "Password updated"),
    })
	public Response changePassword(
			@Parameter(description = "Password Form", schema = @Schema(implementation =  Password.class), required = true) 
			Password passwordBean, 
			@Context SecurityContext sc, 
			@Context AccountService accountService) {

		String token = null;
		SessionUser sessionUser = (SessionUser)sc.getUserPrincipal();
        boolean isValid = passwordBean.getPassword().equals(sessionUser.getPassword());

        if (!isValid) {
        	return Response.status(Response.Status.ACCEPTED).entity(new Token(null, "Old password not correct")).build();
        }
		
		boolean ok = accountService.changePassword(passwordBean.getConfirmPassword());
		if (ok) {
			IdempiereUserService userService = new IdempiereUserService(true);
			final SessionUser authenticatedUser = userService.loadUserByUsername(sessionUser.getEmail(), true);
			TokenHandler tokenHandler = new TokenHandler(userService);
	        token = tokenHandler.createTokenForUser(authenticatedUser);
			return Response.status(Response.Status.OK).entity(new Token(token)).build();
		}
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

	}	
	
	
	
	@GET
	@Path("orders")
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Order list", description = "Get customer orders") 
	public List<Document> getOrders(@Context OrderService orderService) {

		return orderService.getOrders();
	}	
	
	@GET
	@Path("order/{id}")
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Order information", description = "Get order by ID") 
	public Order getOrder(
			@Parameter(description = "C_Order_ID", required = true) 
			@PathParam("id") int id, 
			@Context SecurityContext sc, 
			@Context OrderService orderService) {
		

		return orderService.getOrder(id);
	}
	
	
	@GET
	@Path("info")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Account Info", description = "get account information like name, email")  
	public AccountInfo getInfo(@Context AccountService accountService) {

		return accountService.getAccountInfo();
	}
	
	
	@POST
	@Path("info/update")
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update Account Info", description = "Update account info like name, email", 
    responses = {
    		@ApiResponse(responseCode = "500", description = "Account not updated"),
    		@ApiResponse(responseCode = "201", description = "Account updated")
    })
	public Response updateAccount(
			@Parameter(description = "Account Form", schema = @Schema(implementation =  AccountInfo.class), required = true) 
			AccountInfo account, 
			@Context AccountService accountService) {

		account = accountService.updateUserAccount(account);
		if (account == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else {
			
			IdempiereUserService userService = new IdempiereUserService();
			final SessionUser sessionUser = userService.loadUserByUsername(account.getEmail(), true);
			TokenHandler tokenHandler = new TokenHandler(userService);
	        String token = tokenHandler.createTokenForUser(sessionUser);			
			
			return Response.status(Response.Status.CREATED).entity(new Token(token)).build();
		}
	}
	
	
	@GET
	@Path("addresses")
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Address List", description = "Get address list")   
	public List<Address> getAddresses(@Context AccountService accountService) {

		return accountService.getAddresses();
	}	
	

	
	@POST
	@Path("address/create_update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update address info", description = "Updating address info",
    responses = {
    		@ApiResponse(responseCode = "500", description = "Address not created"),
    		@ApiResponse(responseCode = "200", description = "Address updated"),
    })
	public Response createAddress(
			@Parameter(description = "Address Form", schema = @Schema(implementation =  Address.class), required = true) 
			Address address, 
			@Context AccountService accountService) {

		address = accountService.saveAddress(address);
		if (address == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		else
			return Response.status(Response.Status.OK).entity(address).build();
	}
	
	
	@DELETE
	@Path("address/delete/{id}")
    @Operation(summary = "Deactivating address", description = "Deactivating address",
    responses = {
    		@ApiResponse(responseCode = "500", description = "Address not deactivated"),
    		@ApiResponse(responseCode = "202", description = "Address deactivated"),
    })
	public Response deleteAddress(
			@Parameter(description = "C_BPartner_Location_ID", required = true) 
			@PathParam("id") int id, 
			@Context AccountService accountService) {

		boolean ok = accountService.deleteAddress(id);
		if (ok)
			return Response.status(Response.Status.ACCEPTED).build();
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

	}
	
	
	
	@GET
	@Path("/pdf/{type}/{id}")
	@Produces("application/pdf")
    @Operation(summary = "Order Invoice PDF", description = "Get PDF File") 
	public Response getOrderFile(
			@Parameter(description = "Choice between 'order', 'invoice'", required = true) 
			@PathParam("type") String type, 
			@Parameter(description = "C_Order_ID or C_Invoice_ID depending from type", required = true) 
			@PathParam("id") int id) throws FileNotFoundException {

		DocAction document;
		
		if (type == null || type.equals("order")) {
			 document = new MOrder(ctx, id, null);
		} else if (type.equals("invoice")) {
			 document = new MInvoice(ctx, id, null);
		} else {
			 document = new MInOut(ctx, id, null);
		}

		File file = document.createPDF();
		return Response.ok(file, "application/pdf").build();

	}

		

}
