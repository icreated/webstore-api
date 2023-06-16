package co.icreated.wstore.api.service;

import co.icreated.wstore.api.model.AccountInfoDto;
import co.icreated.wstore.api.model.AddressDto;
import co.icreated.wstore.api.model.DocumentDto;
import co.icreated.wstore.api.model.NewAccountFormDto;
import co.icreated.wstore.api.model.OrderDto;
import co.icreated.wstore.api.model.PasswordDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/account")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AccountApi {

  @POST
  @Path("/password/change")
  @Consumes({"application/json"})
  void changePassword(@Valid @NotNull PasswordDto passwordDto);

  @POST
  @Path("/address/create_update")
  @Consumes({"application/json"})
  void createAddress(@Valid @NotNull AddressDto addressDto);

  @DELETE
  @Path("/address/delete/{id}")
  void deleteAddress(@PathParam("id") Integer id);

  @GET
  @Path("/addresses")
  @Produces({"application/json"})
  List<AddressDto> getAddresses();

  @GET
  @Path("/info")
  @Produces({"application/json"})
  AccountInfoDto getInfo();

  @GET
  @Path("/order/{id}")
  @Produces({"application/json"})
  OrderDto getOrder(@PathParam("id") Integer id);

  @GET
  @Path("/pdf/{type}/{id}")
  @Produces({"application/pdf"})
  void getOrderFile(@PathParam("type") String type, @PathParam("id") Integer id);

  @GET
  @Path("/orders")
  @Produces({"application/json"})
  List<DocumentDto> getOrders();

  @POST
  @Path("/signup")
  @Consumes({"application/json"})
  void signup(@Valid @NotNull NewAccountFormDto newAccountFormDto);

  @POST
  @Path("/info/update")
  void updateAccount(@Valid @NotNull AccountInfoDto accountInfoDto);
}
