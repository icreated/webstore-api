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
    @Path("/order/create")
    @Consumes({ "application/json" })
    void createOrder(@Valid @NotNull OrderDto orderDto);

    @POST
    @Path("/payment")
    @Consumes({ "application/json" })
    void payment(@Valid @NotNull PaymentParamDto paymentParamDto);

    @POST
    @Path("/order/void")
    @Consumes({ "application/json" })
    void voidOrder(@Valid @NotNull OrderDto orderDto);
}
