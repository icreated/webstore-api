package co.icreated.wstore.api.service;

import co.icreated.wstore.api.model.IdNamePairDto;
import co.icreated.wstore.api.model.ShipperDto;
import co.icreated.wstore.api.model.TokenDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/common")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface CommonApi {

    @GET
    @Path("/countries")
    @Produces({ "application/json" })
    List<IdNamePairDto> getCountries();

    @GET
    @Path("/shippers")
    @Produces({ "application/json" })
    List<ShipperDto> getShippers();

    @POST
    @Path("/lookup/email")
    @Consumes({ "application/json" })
    void lookupEmail(@Valid @NotNull TokenDto tokenDto);
}
