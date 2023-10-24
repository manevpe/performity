package com.performity.myservice.security;

import com.performity.myservice.exceptions.AccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@Component
public class JwtFilter extends GenericFilterBean {

  @Value("${jwt.public-key}")
  private String jwtPublicKey;

  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver resolver;

  private PublicKey generateJwtKeyDecryption(String key)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    byte[] keyBytes = Base64.decodeBase64(key);
    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
    return keyFactory.generatePublic(x509EncodedKeySpec);
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest,
      ServletResponse servletResponse,
      FilterChain filterChain
  ) throws IOException, ServletException {
    final HttpServletRequest request = (HttpServletRequest) servletRequest;
    final HttpServletResponse response = (HttpServletResponse) servletResponse;
    final String authHeader = request.getHeader("authorization");

    if ("OPTIONS".equals(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
      filterChain.doFilter(request, response);
    } else {
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        resolver.resolveException(
            request,
            response,
            null,
            new AccessDeniedException("No authorization header detected!")
        );
        return;
      }
    }

    final String token = authHeader.substring(7);
    try {
      Claims claims = Jwts.parser().verifyWith(generateJwtKeyDecryption(jwtPublicKey)).build()
          .parseSignedClaims(token).getPayload();
      request.setAttribute("user_email", claims.get("email"));
      request.setAttribute("user_roles", claims.get("realm_access").toString()
          .replace("{roles=[", "").replace("]}", ""));
      filterChain.doFilter(request, response);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      resolver.resolveException(
          request,
          response,
          null,
          new AccessDeniedException("Unable to parse JWT token!")
      );
    }
  }
}