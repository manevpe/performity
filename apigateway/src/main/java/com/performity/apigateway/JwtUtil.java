package com.performity.apigateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@Component
public class JwtUtil {

  // TODO - get value from API call
  @Value("${jwt.public-key}")
  private String publicKey;

  private PublicKey generateJwtKeyDecryption(String jwtPublicKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    byte[] keyBytes = Base64.decodeBase64(jwtPublicKey);
    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
    return keyFactory.generatePublic(x509EncodedKeySpec);
  }

  public Claims getAllClaimsFromToken(String token)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    token = token.replace("Bearer ", "");
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

}