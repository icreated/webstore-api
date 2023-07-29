package co.icreated.wstore.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;

import co.icreated.wstore.api.model.TokenDto;
import co.icreated.wstore.api.model.UserCredentialsDto;
import co.icreated.wstore.api.service.AuthApi;
import co.icreated.wstore.exception.WstoreUnauthorizedException;
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
      throw new WstoreUnauthorizedException("Not authenticated");
    }
  }



}

