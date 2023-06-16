/*******************************************************************************
 * @author Copyright (C) 2019 ICreated, Sergey Polyarus
 * @date 2019 This program is free software; you can redistribute it and/or modify it under the
 *       terms version 2 of the GNU General Public License as published by the Free Software
 *       Foundation. This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *       WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *       PURPOSE. See the GNU General Public License for more details. You should have received a
 *       copy of the GNU General Public License along with this program; if not, write to the Free
 *       Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 ******************************************************************************/
package co.icreated.wstore.security;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import co.icreated.wstore.bean.SessionUser;


// @Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

  @Context
  private ResourceInfo resourceInfo;

  private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
      .entity("You cannot access this resource").build();
  private static final Response ACCESS_FORBIDDEN =
      Response.status(Response.Status.FORBIDDEN).entity("Access blocked for all users !!").build();

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {

    if (requestContext.getMethod().equals("OPTIONS"))
      return;

    Class<?> clazz = resourceInfo.getResourceClass();

    Method method = resourceInfo.getResourceMethod();

    if (!method.isAnnotationPresent(PermitAll.class)
        && (clazz.isAnnotationPresent(RolesAllowed.class)
            || method.isAnnotationPresent(RolesAllowed.class))) {

      /*
       * RolesAllowed rolesAnnotation; if (clazz.isAnnotationPresent(RolesAllowed.class)) {
       * rolesAnnotation = clazz.getAnnotation(RolesAllowed.class); } else { rolesAnnotation =
       * method.getAnnotation(RolesAllowed.class); }
       */

      String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
      if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        throw new NotAuthorizedException("Authorization header must be provided");
      }
      String token = authorizationHeader.substring("Bearer".length()).trim();

      SessionUser sessionUser = getUserFromToken(token);
      if (sessionUser == null) {
        requestContext.abortWith(ACCESS_DENIED);
        return;
      }
      requestContext.setSecurityContext(
          new CustomSecurityContext(sessionUser, requestContext.getSecurityContext().isSecure()));
    }

    if (clazz.isAnnotationPresent(PermitAll.class) || method.isAnnotationPresent(PermitAll.class)) {
      return;
    }

    if (method.isAnnotationPresent(DenyAll.class)) {
      requestContext.abortWith(ACCESS_FORBIDDEN);
      return;
    }


  }


  private SessionUser getUserFromToken(String token) {

    IdempiereUserService userService = new IdempiereUserService();
    TokenHandler tokenHandler = new TokenHandler(userService);
    SessionUser user = tokenHandler.parseUserFromToken(token);
    return user;
  }
}
