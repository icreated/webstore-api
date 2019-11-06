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
package org.icreated.wstore.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.icreated.wstore.bean.SessionUser;
import org.icreated.wstore.bean.Token;
import org.icreated.wstore.bean.UserCredentials;
import org.icreated.wstore.exception.UnauthorizedException;
import org.icreated.wstore.security.IdempiereUserService;
import org.icreated.wstore.security.TokenHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Path("/auth")
@Tag(name = "Authentication services")
public class AuthenticationEndpoints {

    
    
    @POST
    @Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Login", description = "Login",
    responses = {
    		@ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    public Response authenticateUser(
    		@Parameter(description = "Login Form", schema = @Schema(implementation =  UserCredentials.class), required = true) 
    		UserCredentials credentials) {

    	IdempiereUserService userService = new IdempiereUserService();

        try {
            SessionUser sessionUser = authenticate(userService, credentials.getUsername(), credentials.getPassword());
            String token = issueToken(userService, sessionUser);
            return Response.ok(new Token(token)).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }
	
    
    private SessionUser authenticate(IdempiereUserService userService, String username, String password) throws Exception {


    	SessionUser sessionUser = (SessionUser)userService.loadUserByUsername(username, true);  

        if (sessionUser == null) {
            throw new UnauthorizedException("User not found");
        }
        

        boolean isValid = sessionUser.getPassword().equals(password);
        if (!isValid) {
            throw new UnauthorizedException("Wrong password");
        }
        
        return sessionUser;
    }

    
    private String issueToken(IdempiereUserService userService, SessionUser sessionUser) {

    	TokenHandler tokenHandler = new TokenHandler(userService);

        String token = tokenHandler.createTokenForUser(sessionUser);
        return token;
    }
}

