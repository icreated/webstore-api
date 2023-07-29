package co.icreated.wstore.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class WstoreException extends WebApplicationException {

  private static final long serialVersionUID = -7524423480010719556L;

  public WstoreException(Status status, String message) {
    super(Response.status(status).entity(new WstoreError(status.toString(), message))
        .type(MediaType.APPLICATION_JSON).build());
  }

}
