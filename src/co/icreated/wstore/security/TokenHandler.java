package co.icreated.wstore.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import com.google.common.base.Preconditions;

import co.icreated.wstore.exception.WstoreUnauthorizedException;
import co.icreated.wstore.model.SessionUser;
import co.icreated.wstore.service.AuthService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;



public final class TokenHandler {

  // Stable key — change this value to rotate all tokens
  private static final String SECRET_B64 =
      "d2VzdG9yZS1hcGktc2VjcmV0LWtleS1taW5pbXVtLTI1Ni1iaXRz";
  public final static Key SECRET =
      Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_B64));

  private static final long TOKEN_TTL_MS = 24 * 3600 * 1000L; // 24h

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
    } catch (JwtException e) {
      throw new WstoreUnauthorizedException("Error Decoding JWT Token. Renew it.");
    }

    return authService.loadUserByUsername(username, false, false);
  }

  public String createTokenForUser(SessionUser user) {

    return Jwts.builder() //
        .setSubject(user.getUsername()) //
        .claim("name", user.getName()) //
        .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TTL_MS)) //
        .signWith(SECRET) //
        .compact();
  }


}
