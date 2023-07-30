package co.icreated.wstore.controller;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MUser;
import org.compiere.process.DocAction;
import org.compiere.util.DB;

import co.icreated.wstore.api.model.AccountInfoDto;
import co.icreated.wstore.api.model.AddressDto;
import co.icreated.wstore.api.model.DocumentDto;
import co.icreated.wstore.api.model.NewAccountFormDto;
import co.icreated.wstore.api.model.OrderDto;
import co.icreated.wstore.api.model.PasswordDto;
import co.icreated.wstore.api.model.PaymentParamDto;
import co.icreated.wstore.api.model.TokenDto;
import co.icreated.wstore.api.service.AccountApi;
import co.icreated.wstore.exception.WstoreBadRequestException;
import co.icreated.wstore.exception.WstoreInternalServerException;
import co.icreated.wstore.exception.WstoreUnauthorizedException;
import co.icreated.wstore.mapper.AccountMapper;
import co.icreated.wstore.model.SessionUser;
import co.icreated.wstore.security.Status;
import co.icreated.wstore.security.TokenHandler;
import co.icreated.wstore.service.AccountService;
import co.icreated.wstore.service.AuthService;
import co.icreated.wstore.service.OrderService;
import co.icreated.wstore.utils.Transaction;


@RolesAllowed({"ROLE_USER"})
public class AccountController implements AccountApi {


  @Context
  Properties ctx;

  @Context
  AuthService authService;

  @Context
  AccountService accountService;

  @Context
  OrderService orderService;

  @Context
  SecurityContext securityContext;


  @Override
  public TokenDto changePassword(@Valid @NotNull PasswordDto passwordDto) {
    String token = null;
    SessionUser sessionUser = (SessionUser) securityContext.getUserPrincipal();
    boolean isValid = passwordDto.getPassword().equals(sessionUser.getPassword());

    if (!isValid) {
      throw new WstoreBadRequestException("Old password not correct");
    }

    boolean ok = accountService.changePassword(passwordDto.getConfirmPassword());
    if (ok) {
      final SessionUser authenticatedUser =
          authService.loadUserByUsername(sessionUser.getEmail(), true, true);
      TokenHandler tokenHandler = new TokenHandler(authService);
      token = tokenHandler.createTokenForUser(authenticatedUser);
      return new TokenDto().token(token);
    }

    throw new WstoreInternalServerException("Password not changed");

  }


  @Override
  public AddressDto createAddress(@Valid @NotNull AddressDto addressDto) {
    return accountService.createAddress(addressDto);
  }


  @Override
  @Status(Status.OK)
  public void updateAddress(Integer id, @Valid @NotNull AddressDto addressDto) {
    accountService.updateAddress(id, addressDto);
  }


  @Override
  @Status(Status.OK)
  public void deleteAddress(Integer id) {
    accountService.deleteAddress(id);
  }


  @Override
  public List<AddressDto> getAddresses() {
    return accountService.getAddresses();
  }


  @Override
  public AccountInfoDto getInfo() {
    return accountService.getAccountInfo();
  }


  @Override
  public OrderDto getOrder(Integer id) {
    return orderService.getOrder(id);
  }


  @Override
  public List<DocumentDto> getOrders() {
    return orderService.getOrders();
  }


  @Override
  @PermitAll
  public TokenDto signup(@Valid @NotNull NewAccountFormDto newAccountFormDto) {

    int AD_User_ID =
        DB.getSQLValue(null, "SELECT max(AD_User_ID) FROM AD_User WHERE UPPER(email) LIKE ?",
            newAccountFormDto.getEmail().toUpperCase());
    if (AD_User_ID > 0) {
      throw new WstoreBadRequestException("Account already exists");
    }

    MUser user = accountService.createUserAccount(newAccountFormDto);
    if (user.getAD_User_ID() > 0) {
      final SessionUser sessionUser = authService.loadUserByUsername(user.getEMail(), true, true);
      TokenHandler tokenHandler = new TokenHandler(authService);
      String token = tokenHandler.createTokenForUser(sessionUser);
      return new TokenDto().token(token);
    }
    throw new WstoreInternalServerException("Account not created");
  }


  @Override
  public TokenDto updateAccount(@Valid @NotNull AccountInfoDto accountInfoDto) {
    accountInfoDto = accountService.updateUserAccount(accountInfoDto);
    final SessionUser sessionUser =
        authService.loadUserByUsername(accountInfoDto.getEmail(), true, false);
    TokenHandler tokenHandler = new TokenHandler(authService);
    String token = tokenHandler.createTokenForUser(sessionUser);
    return new TokenDto().token(token);
  }


  @Override
  public File downloadDocument(String type, Integer id) {
    DocAction document;

    if (type == null || type.equals("order")) {
      document = new MOrder(ctx, id, null);
    } else if (type.equals("invoice")) {
      document = new MInvoice(ctx, id, null);
    } else {
      document = new MInOut(ctx, id, null);
    }
    return document.createPDF();
  }


  @Override
  public OrderDto voidOrder(Integer id) {
    if (id <= 0) {
      throw new WstoreBadRequestException("Order id not defined");
    }
    MOrder voidedOrder = Transaction.run(trxName -> {
      MOrder order = new MOrder(ctx, id, trxName);
      if (!orderService.orderBelongsToUser(order)) {
        throw new WstoreUnauthorizedException("Access to order is unauthorized");
      }
      orderService.processOrder(MOrder.ACTION_Void, order);
      return order;
    });
    return AccountMapper.INSTANCE.toOrderDto(voidedOrder);
  }


  @Override
  public AddressDto getAddress(Integer id) {
    if (id <= 0) {
      throw new WstoreBadRequestException("Address id not defined");
    }
    return accountService.getAddress(id);
  }


  @Override
  public OrderDto createOrder(@Valid @NotNull OrderDto orderDto) {
    if (orderDto.getShipAddress() == null || orderDto.getBillAddress() == null
        || orderDto.getShipper() == null || orderDto.getLines() == null)
      throw new WstoreBadRequestException("Missing order data");

    return orderService.createOrder(orderDto);
  }


  @Override
  @Status(Status.OK)
  public void payment(Integer id, @Valid @NotNull PaymentParamDto paymentParamDto) {
    String type = paymentParamDto.getType() == null
        || paymentParamDto.getType().equals(PaymentParamDto.TypeEnum.CHECK)
            ? MOrder.PAYMENTRULE_Check
            : MOrder.PAYMENTRULE_DirectDeposit;

    MOrder order = new MOrder(ctx, id, null);
    if (!orderService.orderBelongsToUser(order))
      throw new WstoreUnauthorizedException("Access to order is unauthorized");

    Transaction.run(trxName -> {
      order.set_TrxName(trxName);
      return orderService.createPayment(order, type);
    });
  }


}
