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
package co.icreated.wstore.security;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;


@Provider
@PreMatching
public class CheckRequestFilter implements ContainerRequestFilter {
	
    
    @Context
    private HttpServletRequest servletRequest; 
    
    @Context ServletContext context;
    
	 
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    	
    	requestContext.setProperty("W_Store_ID", context.getInitParameter("W_Store_ID"));
    	
    	if (requestContext.getMediaType() != null && requestContext.getMediaType().getType().equals("multipart"))
    		return;
    	 if (requestContext.getMethod().equals("OPTIONS")) 
    		 return;
        
    }


}
