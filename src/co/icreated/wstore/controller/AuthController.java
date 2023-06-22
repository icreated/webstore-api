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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;

import co.icreated.wstore.api.model.TokenDto;
import co.icreated.wstore.api.model.UserCredentialsDto;
import co.icreated.wstore.api.service.AuthApi;
import co.icreated.wstore.exception.UnauthorizedException;
import co.icreated.wstore.model.SessionUser;
import co.icreated.wstore.service.AuthService;

public class AuthController implements AuthApi {

  @Context
  AuthService authService;


  @Override
  public TokenDto authenticateUser(@Valid @NotNull UserCredentialsDto userCredentialsDto) {

    try {
      SessionUser sessionUser = authService.authenticate(userCredentialsDto.getUsername(),
          userCredentialsDto.getPassword());
      String token = authService.issueToken(sessionUser);
      return new TokenDto().token(token);

    } catch (Exception e) {
      throw new UnauthorizedException("Not authenticated");
    }
  }



}

