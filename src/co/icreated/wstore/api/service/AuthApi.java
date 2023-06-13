package co.icreated.wstore.api.service;

import co.icreated.wstore.api.model.UserCredentialsDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/auth/login")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AuthApi {

    @POST
    @Consumes({ "application/json" })
    void authenticateUser(@Valid @NotNull UserCredentialsDto userCredentialsDto);
}
