package co.icreated.wstore.security;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;


@Provider
@PreMatching
public class CheckRequestFilter implements ContainerRequestFilter {

  @Context
  private HttpServletRequest servletRequest;

  @Context
  ServletContext context;


  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {

    requestContext.setProperty("W_Store_ID", context.getInitParameter("W_Store_ID"));

    if (requestContext.getMediaType() != null
        && requestContext.getMediaType().getType().equals("multipart"))
      return;
    if (requestContext.getMethod().equals("OPTIONS"))
      return;

  }


}
