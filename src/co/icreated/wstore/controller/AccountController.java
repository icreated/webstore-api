/*******************************************************************************
 * @author Copyright (C) 2019 ICreated, Sergey Polyarus
 * @date 2019 This program is free software; you can redistribute it and/or modify it under the
 *       terms version 2 of the GNU General Public License as published by the Free Software
 *       Foundation. This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *       WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *       PURPOSE. See the GNU General Public License for more details. You should have received a
 *       copy of the GNU General Public License along with this program; if not, write to the Free
 *       Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 ******************************************************************************/
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
import co.icreated.wstore.api.model.TokenDto;
import co.icreated.wstore.api.service.AccountApi;
import co.icreated.wstore.exception.WebStoreBadRequestException;
import co.icreated.wstore.exception.WebStoreInternalServerException;
import co.icreated.wstore.model.SessionUser;
import co.icreated.wstore.security.TokenHandler;
import co.icreated.wstore.service.AccountService;
import co.icreated.wstore.service.AuthService;
import co.icreated.wstore.service.OrderService;


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
      throw new WebStoreBadRequestException("Old password not correct");
    }

    boolean ok = accountService.changePassword(passwordDto.getConfirmPassword());
    if (ok) {
      final SessionUser authenticatedUser =
          authService.loadUserByUsername(sessionUser.getEmail(), true, true);
      TokenHandler tokenHandler = new TokenHandler(authService);
      token = tokenHandler.createTokenForUser(authenticatedUser);
      return new TokenDto().token(token);
    }

    throw new WebStoreInternalServerException("Password not changed");

  }


  @Override
  public AddressDto createAddress(@Valid @NotNull AddressDto addressDto) {
    return accountService.createAddress(addressDto);
  }


  @Override
  public void updateAddress(@Valid @NotNull AddressDto addressDto) {
    accountService.updateAddress(addressDto);
  }


  @Override
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
        throw new WebStoreBadRequestException("Account already exists");
      } 
      
     MUser user = accountService.createUserAccount(newAccountFormDto);
    if (user.getAD_User_ID() > 0) {
      final SessionUser sessionUser = authService.loadUserByUsername(user.getEMail(), true, true);
      TokenHandler tokenHandler = new TokenHandler(authService);
      String token = tokenHandler.createTokenForUser(sessionUser);
      return new TokenDto().token(token);
    }
    throw new WebStoreInternalServerException("Account not created");
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
  public File getOrderFile(String type, Integer id) {
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



}
