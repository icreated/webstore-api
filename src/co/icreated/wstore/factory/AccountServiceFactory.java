package co.icreated.wstore.factory;

import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import co.icreated.wstore.model.SessionUser;
import co.icreated.wstore.service.AccountService;

public class AccountServiceFactory extends AbstractServiceFactory<AccountService> {

  @Inject
  public AccountServiceFactory(@Context ContainerRequestContext context,
      @Context SecurityContext securityContext, @Context Properties ctx) {
    super(context, "accountService", () -> {
      return new AccountService(ctx, securityContext);
    });
  }
}
