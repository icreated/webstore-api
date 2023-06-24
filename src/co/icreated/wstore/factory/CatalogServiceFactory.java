package co.icreated.wstore.factory;

import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

import co.icreated.wstore.service.CatalogService;

public class CatalogServiceFactory extends AbstractServiceFactory<CatalogService> {

  @Inject
  public CatalogServiceFactory(@Context ContainerRequestContext context, @Context Properties ctx) {
    super(context, "catalogService", () -> new CatalogService(ctx));
  }

}
