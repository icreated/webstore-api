package co.icreated.wstore.exception;

import javax.ws.rs.core.Response.Status;

public class WstoreInternalServerException extends WstoreException {

  private static final long serialVersionUID = 3025267653935141609L;


  public WstoreInternalServerException(String message) {
    super(Status.INTERNAL_SERVER_ERROR, message);
  }
}
