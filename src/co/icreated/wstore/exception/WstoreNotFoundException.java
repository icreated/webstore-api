package co.icreated.wstore.exception;

import javax.ws.rs.core.Response.Status;

public class WstoreNotFoundException extends WstoreException {

  private static final long serialVersionUID = 3025267653935141609L;


  public WstoreNotFoundException(String message) {
    super(Status.NOT_FOUND, message);
  }
}
