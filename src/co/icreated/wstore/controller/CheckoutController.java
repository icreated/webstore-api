package co.icreated.wstore.controller;

import java.util.Properties;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Context;

import org.compiere.model.MOrder;
import org.compiere.model.X_C_Payment;
import org.compiere.util.CLogger;
import org.compiere.util.Trx;

import co.icreated.wstore.api.model.OrderDto;
import co.icreated.wstore.api.model.PaymentParamDto;
import co.icreated.wstore.api.service.CheckoutApi;
import co.icreated.wstore.service.OrderService;
import co.icreated.wstore.service.PaymentService;

@RolesAllowed({"USER"})
public class CheckoutController implements CheckoutApi {


  CLogger log = CLogger.getCLogger(CheckoutController.class);

  @Context
  Properties ctx;

  @Context
  OrderService orderService;

  @Context
  PaymentService paymentService;

  @Override
  public OrderDto createOrder(@Valid @NotNull OrderDto orderDto) {
    if (orderDto.getShipAddress() == null || orderDto.getBillAddress() == null
        || orderDto.getShipper() == null || orderDto.getLines() == null)
      throw new BadRequestException("Missing order data");

    return orderService.createOrder(orderDto);
  }



  @Override
  public void payment(@Valid @NotNull PaymentParamDto paymentParamDto) {

    String type = paymentParamDto.getType() == null || paymentParamDto.getType().equals("check")
        ? X_C_Payment.TENDERTYPE_Check
        : X_C_Payment.TENDERTYPE_DirectDeposit;

    MOrder order = new MOrder(ctx, paymentParamDto.getOrderId(), null);
    if (!orderService.orderBelongsToUser(order))
      throw new ForbiddenException("Forbidden access");

    String trxName = Trx.createTrxName("createPayment");
    Trx trx = Trx.get(trxName, true);
    order.set_TrxName(trxName);
    paymentService.createSimplePayment(order, type);
    boolean ok = trx.commit();
    if (ok) {
      log.info("Transaction payment Check validate #" + order.getDocumentNo());
    } else {
      log.warning("Transaction payment Check aborted for order #" + order.getDocumentNo());
      trx.rollback();
    }
    trx.close();
    trx = null;

    if (!ok) {
      throw new InternalServerErrorException("Payment Transaction failed");
    }
  }



  @Override
  public void voidOrder(@Valid @NotNull OrderDto orderDto) {
    if (orderDto.getId() <= 0) {
      throw new BadRequestException("Order id not defined");
    }

    MOrder morder = new MOrder(ctx, orderDto.getId(), null);
    if (!orderService.orderBelongsToUser(morder))
      throw new ForbiddenException("Forbidden access");

    boolean ok = orderService.processOrder(MOrder.ACTION_Void, morder);
    if (!ok) {
      throw new InternalServerErrorException("Order creation failed");
    }
  }



}
