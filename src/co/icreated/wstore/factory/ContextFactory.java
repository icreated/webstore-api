package co.icreated.wstore.factory;

import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

import co.icreated.wstore.service.ContextService;

public class ContextFactory extends AbstractServiceFactory<Properties> {

  @Inject
  public ContextFactory(@Context ContainerRequestContext context) {
    super(context, "ctx", () -> {
      int W_Store_ID = Integer.parseInt((String) context.getProperty("W_Store_ID"));
      return ContextService.getCtx(W_Store_ID);
    });
  }
}
