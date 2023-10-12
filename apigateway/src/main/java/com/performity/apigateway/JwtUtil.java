package com.performity.apigateway;

import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

@Component
public class JwtUtil {

    // TODO - get value from API call
    @Value("${jwt.public-key}")
    private String publicKey;

    public PublicKey generateJwtKeyDecryption(String jwtPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] keyBytes = Base64.decodeBase64(jwtPublicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    public Claims getAllClaimsFromToken(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        token = token.replaceAll("Bearer ", "");
        validateJwtToken(token, publicKey);
        return Jwts.parser().verifyWith(generateJwtKeyDecryption(publicKey)).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    public boolean isInvalid(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return this.isTokenExpired(token);
    }

    public boolean validateJwtToken(String authToken,String jwtPublicKey) {
        try {
            authToken = authToken.replaceAll("Bearer ", "");
            Jwts.parser().setSigningKey(generateJwtKeyDecryption(jwtPublicKey)).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: {}"+ e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: {}"+ e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: {}"+ e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: {}"+ e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: {}"+ e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("no such algorithm exception");
        } catch (InvalidKeySpecException e) {
            System.out.println("invalid key exception");
        }

        return false;
    }

}