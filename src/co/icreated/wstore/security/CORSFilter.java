package co.icreated.wstore.security;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class CORSFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext request, ContainerResponseContext response)
      throws IOException {

    response.getHeaders().add("Access-Control-Allow-Origin", "*");
    response.getHeaders().add("Access-Control-Allow-Headers",
        "Origin, Content-Type, Accept, Authorization, ApplicationId");
    response.getHeaders().add("Access-Control-Allow-Credentials", "true");
    response.getHeaders().add("Access-Control-Allow-Methods",
        "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    // response.getHeaders().add("Access-Control-Max-Age", "1800");//30 min
  }

}

