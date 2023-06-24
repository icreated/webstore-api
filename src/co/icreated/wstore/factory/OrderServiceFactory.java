package co.icreated.wstore.factory;

import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import co.icreated.wstore.service.OrderService;

public class OrderServiceFactory extends AbstractServiceFactory<OrderService> {

  @Inject
  public OrderServiceFactory(@Context ContainerRequestContext context,
      @Context SecurityContext securityContext, @Context Properties ctx) {
    super(context, "orderService", () -> {
      return new OrderService(ctx, securityContext);
    });
  }

}
