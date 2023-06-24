package co.icreated.wstore.factory;

import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

import co.icreated.wstore.service.CommonService;

public class CommonServiceFactory extends AbstractServiceFactory<CommonService> {

  @Inject
  public CommonServiceFactory(@Context ContainerRequestContext context, @Context Properties ctx) {
    super(context, "commonService", () -> new CommonService(ctx));
  }

}
