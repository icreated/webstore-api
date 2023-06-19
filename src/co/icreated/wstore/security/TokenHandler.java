/*******************************************************************************
 * @author Copyright (C) 2019 ICreated, Sergey Polyarus
 * @date 2019 This program is free software; you can redistribute it and/or modify it under the
 *       terms version 2 of the GNU General Public License as published by the Free Software
 *       Foundation. This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *       WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *       PURPOSE. See the GNU General Public License for more details. You should have received a
 *       copy of the GNU General Public License along with this program; if not, write to the Free
 *       Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 ******************************************************************************/
package co.icreated.wstore.security;

import java.security.Key;

import com.google.common.base.Preconditions;

import co.icreated.wstore.exception.UnauthorizedException;
import co.icreated.wstore.model.SessionUser;
import co.icreated.wstore.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;



public final class TokenHandler {

  public final static Key SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  private final AuthService authService;

  public TokenHandler(AuthService authService) {
    this.authService = Preconditions.checkNotNull(authService);
  }

  public SessionUser parseUserFromToken(String token) {

    String username = null;
    try {
      username = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    } catch (SignatureException e) {
      throw new UnauthorizedException("Error signature");
    }

    return authService.loadUserByUsername(username, false, false);
  }

  public String createTokenForUser(SessionUser user) {

    return Jwts.builder().setSubject(user.getUsername()).claim("name", user.getName())
        .signWith(SECRET).compact();
  }


}
