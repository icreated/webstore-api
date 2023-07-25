package co.icreated.wstore.security;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Status {
  int OK = 200;
  int CREATED = 201;

  int value();
}
