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
package org.icreated.wstore.factory;

import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.hk2.api.Factory;
import org.icreated.wstore.bean.SessionUser;
import org.icreated.wstore.service.PaymentService;

public class PaymentServiceFactory implements Factory<PaymentService> {

    private final ContainerRequestContext context;


    @Inject
    public PaymentServiceFactory(@Context ContainerRequestContext context, @Context SecurityContext sc,  @Context Properties ctx) {
    	
        this.context = context;

        SessionUser sessionUser = (SessionUser)sc.getUserPrincipal();
        if (sessionUser == null) {
            context.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("You are not authorized.")
                    .build());
        }
        	
        
        context.setProperty("paymentService",  new PaymentService(ctx, sessionUser));  

    }

    @Override
    public PaymentService provide() {
        return (PaymentService)context.getProperty("paymentService");
    }

    @Override
    public void dispose(PaymentService t) {}  
}
