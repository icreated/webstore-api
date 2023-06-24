package co.icreated.wstore.factory;

import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

import co.icreated.wstore.service.AuthService;

public class AuthServiceFactory extends AbstractServiceFactory<AuthService> {


  @Inject
  public AuthServiceFactory(@Context ContainerRequestContext context, @Context Properties ctx) {
    super(context, "authService", () -> new AuthService(ctx));
  }


}
