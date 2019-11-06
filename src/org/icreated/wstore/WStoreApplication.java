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
package org.icreated.wstore;

import java.util.Properties;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.icreated.wstore.endpoints.AccountEndpoints;
import org.icreated.wstore.endpoints.AuthenticationEndpoints;
import org.icreated.wstore.endpoints.CatalogEndpoints;
import org.icreated.wstore.endpoints.CheckoutEndpoints;
import org.icreated.wstore.endpoints.CommonEndpoints;
import org.icreated.wstore.factory.AccountServiceFactory;
import org.icreated.wstore.factory.CatalogServiceFactory;
import org.icreated.wstore.factory.ContextFactory;
import org.icreated.wstore.factory.OrderServiceFactory;
import org.icreated.wstore.factory.PaymentServiceFactory;
import org.icreated.wstore.security.CORSFilter;
import org.icreated.wstore.security.CheckRequestFilter;
import org.icreated.wstore.security.JwtAuthenticationFilter;
import org.icreated.wstore.service.AccountService;
import org.icreated.wstore.service.CatalogService;
import org.icreated.wstore.service.OrderService;
import org.icreated.wstore.service.PaymentService;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class WStoreApplication extends ResourceConfig {
	
    public WStoreApplication() {
       
 //   	register(RolesAllowedDynamicFeature.class);
    	register(AccountEndpoints.class);
    	register(CatalogEndpoints.class);
    	
    	register(CommonEndpoints.class);
    	register(CheckoutEndpoints.class);
    	
    	register(CORSFilter.class);
    	register(CheckRequestFilter.class);
    	register(JwtAuthenticationFilter.class);
    	register(AuthenticationEndpoints.class);

 //   	register(new LoggingFilter());
    	
//		register(GZipEncoder.class);
		EncodingFilter.enableFor(this, GZipEncoder.class);
    	
    	register(new JacksonJsonProvider());
    	
    	
    	register(new AbstractBinder(){
            @Override
            protected void configure() {
                bindFactory(ContextFactory.class)
                        .to(Properties.class);
            } 
        });
    	
    	register(new AbstractBinder(){
            @Override
            protected void configure() {
                bindFactory(CatalogServiceFactory.class)
                        .to(CatalogService.class);
            } 
        });
    	
    	register(new AbstractBinder(){
            @Override
            protected void configure() {
                bindFactory(AccountServiceFactory.class)
                        .to(AccountService.class);
            } 
        });
    	
    	register(new AbstractBinder(){
            @Override
            protected void configure() {
                bindFactory(OrderServiceFactory.class)
                        .to(OrderService.class);
            } 
        });
    	
    	register(new AbstractBinder(){
            @Override
            protected void configure() {
                bindFactory(PaymentServiceFactory.class)
                        .to(PaymentService.class);
            } 
        });

    	
    }

}
