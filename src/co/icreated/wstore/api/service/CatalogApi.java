package co.icreated.wstore.api.service;

import co.icreated.wstore.api.model.PriceListProductDto;
import co.icreated.wstore.api.model.ProductCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/catalog")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface CatalogApi {

    @GET
    @Path("/cart")
    @Produces({ "application/json" })
    void getCart(@QueryParam("id") @NotNull   String id);

    @GET
    @Path("/categories")
    @Produces({ "application/json" })
    List<ProductCategoryDto> getCategories();

    @GET
    @Path("/products/{categoryId}")
    @Produces({ "application/json" })
    List<PriceListProductDto> getProducts(@PathParam("categoryId") Integer categoryId);

    @GET
    @Path("/products/featured")
    @Produces({ "application/json" })
    List<PriceListProductDto> getProductsFeatured();

    @GET
    @Path("/products/search/{searchString}")
    @Produces({ "application/json" })
    List<PriceListProductDto> getProductsSearch(@PathParam("searchString") String searchString);
}
