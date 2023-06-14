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
package co.icreated.wstore.controller;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;

import co.icreated.wstore.api.model.PriceListProductDto;
import co.icreated.wstore.api.model.ProductCategoryDto;
import co.icreated.wstore.api.service.CatalogApi;
import co.icreated.wstore.service.CatalogService;


/**
 * @author spok
 *
 */
@PermitAll
public class CatalogController implements CatalogApi {
	

	    
	    @Context
	    CatalogService catalogService;
	    

	    @Override
		public List<ProductCategoryDto> getCategories() {

			return catalogService.getCategories();
		}
		
		
		@Override
		public List<PriceListProductDto> getProductsFeatured() {

			return catalogService.getProducts(0, true);
		}
		

		@Override
		public List<PriceListProductDto> getProducts(Integer categoryId) {
			return catalogService.getProducts(categoryId, false);
		}

		
		@Override
		public List<PriceListProductDto> getCart(@NotNull List<Integer> ids) {
			return catalogService.getProductsById(ids);
		}


		@Override
		public List<PriceListProductDto> getProductsSearch(String searchString) {
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

		

}
