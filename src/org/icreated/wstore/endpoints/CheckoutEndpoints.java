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

import java.util.Properties;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.compiere.model.MOrder;
import org.compiere.model.X_C_Payment;
import org.compiere.util.CLogger;
import org.compiere.util.Trx;
import org.icreated.wstore.bean.Order;
import org.icreated.wstore.bean.PaymentParam;
import org.icreated.wstore.service.OrderService;
import org.icreated.wstore.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RolesAllowed({"USER"})
@Path("/checkout")
@Tag(name = "Checkout Services")
public class CheckoutEndpoints {
	
	
	CLogger log = CLogger.getCLogger(CheckoutEndpoints.class);
    
    @Context
    Properties ctx;
    
	
	@POST
	@Path("order/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Order Creation", description = "Create order",
    responses = {
    		@ApiResponse(responseCode = "500", description = "Missing address or shipper or lines"),
    		@ApiResponse(responseCode = "200", description = "Order created"),
    })
	public Response createOrder(
			@Parameter(description = "Order Form", schema = @Schema(implementation =  Order.class), required = true) 
			Order order, 
			@Context OrderService orderService) {
		
		if (order.getShipAddress() == null || order.getBillAddress() == null || 
				order.getShipper() == null || order.getLines() == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		
		Order createdOrder = orderService.createOrder(order);
		return Response.status(Response.Status.OK).entity(createdOrder).build();
	}
	
	
	
	@POST
	@Path("order/void")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Void order", description = "Void order",
    responses = {
    		@ApiResponse(responseCode = "500", description = "Order not processed"),
    		@ApiResponse(responseCode = "403", description = "Order forbidden"),
    		@ApiResponse(responseCode = "200", description = "Order processed"),
    })
	public Response voidOrder(
			@Parameter(description = "Order Form", schema = @Schema(implementation =  Order.class), required = true) 
			Order order, 
			@Context OrderService orderService) {
		
		if (order.getId() <= 0)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		
		MOrder morder = new MOrder(ctx, order.getId(), null);
		if (!orderService.orderBelongsToUser(morder))
			return Response.status(Response.Status.FORBIDDEN).build();

		boolean ok = orderService.processOrder(MOrder.ACTION_Void, morder);
		if (ok)
			return Response.status(Response.Status.OK).build();
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}
	

	@POST
	@Path("payment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Payment Creation", description = "Create simple payment",
    responses = {
    		@ApiResponse(responseCode = "500", description = "Order not processed"),
    		@ApiResponse(responseCode = "403", description = "Order forbidden"),
    		@ApiResponse(responseCode = "200", description = "Order processed"),
    })
	public Response payment(
			@Parameter(description = "Payment Param", schema = @Schema(implementation =  PaymentParam.class), required = true) 
			PaymentParam paymentParam,
		    @Context PaymentService paymentService, 
		    @Context OrderService orderService) {
		
		String type = paymentParam.getType() == null || paymentParam.getType().equals("check") ?
				X_C_Payment.TENDERTYPE_Check : X_C_Payment.TENDERTYPE_DirectDeposit;

		MOrder order = new MOrder(ctx, paymentParam.getOrderId(), null);
		if (!orderService.orderBelongsToUser(order))
			return Response.status(Response.Status.FORBIDDEN).build();
		
		String trxName = Trx.createTrxName("createPayment");
		Trx trx = Trx.get(trxName, true);
		order.set_TrxName(trxName);
		paymentService.createSimplePayment(order, type);
		boolean ok = trx.commit();
		if (ok) {
			log.info("Transaction payment Check validate #"+order.getDocumentNo());
		} else {
			log.warning("Transaction payment Check aborted for order #"+order.getDocumentNo());
			trx.rollback();
		}
		trx.close();
		trx = null;
		
		if (ok)
			return Response.status(Response.Status.OK).build();
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		
	}	
	
	


}
