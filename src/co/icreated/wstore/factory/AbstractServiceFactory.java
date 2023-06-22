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
package co.icreated.wstore.factory;

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.Properties;
import java.util.function.Supplier;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.api.Factory;

import co.icreated.wstore.model.SessionUser;
import co.icreated.wstore.service.AbstractService;


public class AbstractServiceFactory<T> implements Factory<T> {

  final String serviceName;
  private final ContainerRequestContext context;

  public AbstractServiceFactory(ContainerRequestContext context, String serviceName,
      Supplier<T> contextSupplier) {
    context.setProperty(serviceName, contextSupplier.get());
    this.context = context;
    this.serviceName = serviceName;
  }

  @Override
  public T provide() {
    return (T) context.getProperty(serviceName);
  }


  @Override
  public void dispose(T instance) {
    // TODO Auto-generated method stub

  }

  public static <T extends AbstractService> T create(Properties ctx, Class<T> type) {

    Class[] cArg = new Class[1];
    cArg[0] = Properties.class;
    // cArg[1] = SessionUser.class;

    try {
      return (T) type.getDeclaredConstructor(cArg).newInstance(ctx);

    } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException
        | SecurityException | InstantiationException | IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }


  public static <T extends AbstractService> T create(Properties ctx, SessionUser sessionUser,
      Class<T> type) {

    Class[] cArg = new Class[2];
    cArg[0] = Properties.class;
    cArg[1] = SessionUser.class;

    try {
      return (T) type.getDeclaredConstructor(cArg).newInstance(ctx, sessionUser);

    } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException
        | SecurityException | InstantiationException | IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }



}
