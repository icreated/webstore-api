package co.icreated.wstore.security;

import java.security.Key;

import com.google.common.base.Preconditions;

import co.icreated.wstore.exception.WebStoreUnauthorizedException;
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
      username = Jwts.parser() //
    		  .setSigningKey(SECRET) //
    		  .parseClaimsJws(token) //
    		  .getBody() //
    		  .getSubject();
    } catch (SignatureException e) {
      throw new WebStoreUnauthorizedException("Error Decoding JWT Token. Renew it.");
    }

    return authService.loadUserByUsername(username, false, false);
  }

  public String createTokenForUser(SessionUser user) {

    return Jwts.builder() //
    		.setSubject(user.getUsername()) //
    		.claim("name", user.getName()) //
    		.signWith(SECRET) //
    		.compact();
  }


}
