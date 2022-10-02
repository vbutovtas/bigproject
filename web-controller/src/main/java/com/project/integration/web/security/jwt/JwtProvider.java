package com.project.integration.web.security.jwt;

import com.project.integration.serv.enums.UserRoles;
import com.project.integration.web.consts.Attributes;
import com.project.integration.web.consts.PropertiesKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
  private String jwtSecret;
  private int jwtExpirationInMs;
  private int ableForRefreshTimeInMs;

  @Value(PropertiesKeys.JWT_SECRET)
  public void setJwtSecret(String jwtSecret) {
    this.jwtSecret = jwtSecret;
  }

  @Value(PropertiesKeys.JWT_EXPIRATION_DATE_IN_SEC)
  public void setJwtExpirationInMs(int jwtExpirationInSec) {
    this.jwtExpirationInMs = jwtExpirationInSec * 1000;
  }

  @Value(PropertiesKeys.JWT_ABLE_FOR_REFRESH_TIME_IN_SEC)
  public void setAbleForRefreshTimeInMs(int ableForRefreshTimeInSec) {
    this.ableForRefreshTimeInMs = ableForRefreshTimeInSec * 1000;
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();

    Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

    //TODO
//    if (roles.contains(new SimpleGrantedAuthority(UserRoles.ADMIN.toString()))) {
//      claims.put(Attributes.IS_ADMIN, true);
//    }
//    if (roles.contains(new SimpleGrantedAuthority(UserRoles.USER.toString()))) {
//      claims.put(Attributes.IS_USER, true);
//    }

    return doGenerateToken(claims, userDetails.getUsername(), jwtExpirationInMs);
  }

  public String generateRefreshedToken(Map<String, Object> claims, String subject) {
    return doGenerateToken(claims, subject, jwtExpirationInMs);
  }

  private String doGenerateToken(Map<String, Object> claims, String subject, int expirationMs) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;
    } catch (SignatureException
             | MalformedJwtException
             | UnsupportedJwtException
             | IllegalArgumentException ex) {
      throw new BadCredentialsException("Invalid credentials", ex);
    }
  }

  public String getLoginFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

    List<SimpleGrantedAuthority> roles = null;

    //TODO
//    Boolean isAdmin = claims.get(Attributes.IS_ADMIN, Boolean.class);
//    Boolean isUser = claims.get(Attributes.IS_USER, Boolean.class);
//
//    if (Boolean.TRUE.equals(isAdmin)) {
//      roles = List.of(new SimpleGrantedAuthority(UserRoles.ADMIN.toString()));
//    }
//
//    if (Boolean.TRUE.equals(isUser)) {
//      roles = List.of(new SimpleGrantedAuthority(UserRoles.USER.toString()));
//    }

    return roles;
  }

  public Boolean isRefreshAvailable(ExpiredJwtException e) {
    long creationTimeInMs = e.getClaims().getIssuedAt().getTime();
    long timePastSinceCreationInMs = System.currentTimeMillis() - creationTimeInMs;
    return timePastSinceCreationInMs > ableForRefreshTimeInMs;
  }
}
