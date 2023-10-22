package com.performity.apigateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@Component
public class JwtUtil {

  private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

  // TODO - get value from API call
  @Value("${jwt.public-key}")
  private String publicKey;

  public PublicKey generateJwtKeyDecryption(String jwtPublicKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    byte[] keyBytes = Base64.decodeBase64(jwtPublicKey);
    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
    return keyFactory.generatePublic(x509EncodedKeySpec);
  }

  public Claims getAllClaimsFromToken(String token)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    token = token.replace("Bearer ", "");
    validateJwtToken(token, publicKey);
    return Jwts.parser().verifyWith(generateJwtKeyDecryption(publicKey)).build()
        .parseSignedClaims(token).getPayload();
  }

  private boolean isTokenExpired(String token)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
  }

  public boolean isInvalid(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
    return this.isTokenExpired(token);
  }

  public boolean validateJwtToken(String authToken, String jwtPublicKey) {
    try {
      authToken = authToken.replace("Bearer ", "");
      Jwts.parser().verifyWith(generateJwtKeyDecryption(jwtPublicKey)).build()
          .parseSignedClaims(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    } catch (NoSuchAlgorithmException e) {
      logger.error("JWT no such algorithm exception: {}", e.getMessage());
    } catch (InvalidKeySpecException e) {
      logger.error("JWT Invalid key exception: {}", e.getMessage());
    }

    return false;
  }

}