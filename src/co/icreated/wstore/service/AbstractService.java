package co.icreated.wstore.service;

import java.util.Properties;
import javax.ws.rs.core.SecurityContext;
import co.icreated.wstore.model.SessionUser;

public abstract class AbstractService {

  Properties ctx = null;
  private SecurityContext securityContext;

  protected AbstractService(Properties ctx) {
    this.ctx = ctx;
  }

  AbstractService(Properties ctx, SecurityContext securityContext) {
    this.ctx = ctx;
    this.securityContext = securityContext;
  }


  protected SessionUser getSessionUser() {
    return (SessionUser) securityContext.getUserPrincipal();
  }

}
