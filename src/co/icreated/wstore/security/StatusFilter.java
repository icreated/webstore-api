package co.icreated.wstore.security;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;



@Provider
public class StatusFilter implements ContainerResponseFilter {

  @Context
  private ResourceInfo resourceInfo;

  @Override
  public void filter(ContainerRequestContext containerRequestContext,
      ContainerResponseContext containerResponseContext) throws IOException {

    Class<?> clazz = resourceInfo.getResourceClass();
    Method method = resourceInfo.getResourceMethod();
    // Replacing 204 with custom Status
    if (method.isAnnotationPresent(Status.class) && containerResponseContext.getStatus() == 204) {
      containerResponseContext.setStatus(method.getAnnotation(Status.class).value());
    }
  }
}
