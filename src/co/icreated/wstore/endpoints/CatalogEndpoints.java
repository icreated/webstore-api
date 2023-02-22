/*******************************************************************************
 * @author Copyright (C) 2019 ICreated, Sergey Polyarus
 *  @date 2019
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms version 2 of the GNU General Public License as published
 *  by the Free Software Foundation. This program is distributed in the hope
 *  that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc., 
 *  59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 ******************************************************************************/
package co.icreated.wstore.endpoints;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import co.icreated.wstore.bean.PriceListProduct;
import co.icreated.wstore.bean.ProductCategory;
import co.icreated.wstore.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;


@PermitAll
@Path("/catalog")
@Tag(name = "Catalog Services")
public class CatalogEndpoints {
	

	    
	    @Context
	    CatalogService catalogService;
	    

		@GET
		@Path("/categories")
		@Produces(MediaType.APPLICATION_JSON)
	    @Operation(summary = "Product Category List", description = "Product Categories, active, not discontinued & not BOM")   
		public List<ProductCategory> getCategories() {

			return catalogService.getCategories();
		}
		
		@GET
		@Path("/products/featured")
		@Produces(MediaType.APPLICATION_JSON)
	    @Operation(summary = "Featured products", description = "Featured products - IsWebstoreFeatured = 'Y'")   
		public List<PriceListProduct> getProductsFeatured() {

			return catalogService.getProducts(0, true);
		}
		
		@GET
		@Path("/products/search/{searchString}")
		@Produces(MediaType.APPLICATION_JSON)
	    @Operation(summary = "Search products", description = "Searching products by Name or Description")   
		public List<PriceListProduct> getProductsSearch(
				@Parameter(description = "Searching string", required = true) 
				@PathParam("searchString") String searchString) {

			//	Search Parameter
			String search = searchString;
			if (search != null && (search.length() == 0 || search.equals("%")))
				search = null;
			if (search != null)
			{
				if (!search.endsWith("%"))
					search += "%";
				if (!search.startsWith("%"))
					search = "%" + search;
				search = search.toUpperCase();
			}
			
			return catalogService.doSearch(search);
		}
		
		@GET
		@Path("/products/{categoryId}")
		@Produces(MediaType.APPLICATION_JSON)
	    @Operation(summary = "Category Products", description = "Product Categories, active, not discontinued & not BOM")   
		public List<PriceListProduct> getProducts(
				@Parameter(description = "Product Category ID", required = true) 
				@PathParam("categoryId") int id) {

			return catalogService.getProducts(id, false);
		}
		
		
		@GET
		@Path("/cart")
		@Produces(MediaType.APPLICATION_JSON)
	    @Operation(summary = "Cart Product list", description = "Product List from id product list")   
		public Response getCart(@Parameter(description = "Product Category ID", schema = @Schema(implementation =  List.class), required = true) 
											@QueryParam("id") List<Integer> ids) {

			if (ids == null || ids.isEmpty())
				return Response.ok().entity(new ArrayList<PriceListProduct>()).header("Expires",System.currentTimeMillis() + 14400000).build();
			
			return Response.status(Response.Status.OK).entity(catalogService.getProductsById(ids)).build();

		}
		

}
