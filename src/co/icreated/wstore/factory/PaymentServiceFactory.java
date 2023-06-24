package co.icreated.wstore.factory;

import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import co.icreated.wstore.model.SessionUser;
import co.icreated.wstore.service.PaymentService;

public class PaymentServiceFactory extends AbstractServiceFactory<PaymentService> {

  @Inject
  public PaymentServiceFactory(@Context ContainerRequestContext context,
      @Context SecurityContext securityContext, @Context Properties ctx) {
    super(context, "paymentService", () -> {
      return new PaymentService(ctx, securityContext);
    });
  }
}
