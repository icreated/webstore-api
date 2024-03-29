package co.icreated.wstore.api.service;

import co.icreated.wstore.api.model.AccountInfoDto;
import co.icreated.wstore.api.model.AddressDto;
import co.icreated.wstore.api.model.DocumentDto;
import java.io.File;
import co.icreated.wstore.api.model.NewAccountFormDto;
import co.icreated.wstore.api.model.OrderDto;
import co.icreated.wstore.api.model.PasswordDto;
import co.icreated.wstore.api.model.PaymentParamDto;
import co.icreated.wstore.api.model.TokenDto;

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

    @PUT
    @Path("/password")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    TokenDto changePassword(@Valid @NotNull PasswordDto passwordDto);

    @POST
    @Path("/addresses")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    AddressDto createAddress(@Valid @NotNull AddressDto addressDto);

    @POST
    @Path("/orders")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    OrderDto createOrder(@Valid @NotNull OrderDto orderDto);

    @DELETE
    @Path("/addresses/{id}")
    void deleteAddress(@PathParam("id") Integer id);

    @GET
    @Path("/pdf/{type}/{id}")
    @Produces({ "application/pdf" })
    File downloadDocument(@PathParam("type") String type,@PathParam("id") Integer id);

    @GET
    @Path("/addresses/{id}")
    @Produces({ "application/json" })
    AddressDto getAddress(@PathParam("id") Integer id);

    @GET
    @Path("/addresses")
    @Produces({ "application/json" })
    List<AddressDto> getAddresses();

    @GET
    @Path("/info")
    @Produces({ "application/json" })
    AccountInfoDto getInfo();

    @GET
    @Path("/orders/{id}")
    @Produces({ "application/json" })
    OrderDto getOrder(@PathParam("id") Integer id);

    @GET
    @Path("/orders")
    @Produces({ "application/json" })
    List<DocumentDto> getOrders();

    @POST
    @Path("/orders/{id}/payment")
    @Consumes({ "application/json" })
    void payment(@PathParam("id") Integer id,@Valid @NotNull PaymentParamDto paymentParamDto);

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    TokenDto signup(@Valid @NotNull NewAccountFormDto newAccountFormDto);

    @PUT
    @Path("/info")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    TokenDto updateAccount(@Valid @NotNull AccountInfoDto accountInfoDto);

    @PUT
    @Path("/addresses/{id}")
    @Consumes({ "application/json" })
    void updateAddress(@PathParam("id") Integer id,@Valid @NotNull AddressDto addressDto);

    @DELETE
    @Path("/orders/{id}")
    @Produces({ "application/json" })
    OrderDto voidOrder(@PathParam("id") Integer id);
}
