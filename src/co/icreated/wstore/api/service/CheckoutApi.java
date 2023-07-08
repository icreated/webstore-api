package co.icreated.wstore.api.service;

import co.icreated.wstore.api.model.OrderDto;
import co.icreated.wstore.api.model.PaymentParamDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/checkout")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface CheckoutApi {

    @POST
    @Path("/order")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    OrderDto createOrder(@Valid @NotNull OrderDto orderDto);

    @POST
    @Path("/payment")
    @Consumes({ "application/json" })
    void payment(@Valid @NotNull PaymentParamDto paymentParamDto);
}
