package co.icreated.wstore.exception;

import javax.ws.rs.core.Response.Status;

public class WstoreUnauthorizedException extends WstoreException {

  private static final long serialVersionUID = -5656768076307809106L;

  public WstoreUnauthorizedException(String message) {
    super(Status.UNAUTHORIZED, message);
  }

}
