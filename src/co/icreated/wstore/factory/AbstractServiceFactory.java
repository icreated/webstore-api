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
