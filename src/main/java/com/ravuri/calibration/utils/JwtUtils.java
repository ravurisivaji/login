package com.ravuri.calibration.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

//    private static final String SECRET = "secret";
    @Value("${jwt.secret}")
    private String SECRET; // = "SecureRandomGeneratedKeyThatIsAtLeast32CharactersLong!";
    private static final String ISSUER = "issuer";
    private static final String AUDIENCE = "audience";

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())

                .parseClaimsJws(token)
                .getBody();
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private String createToken(Map<String, Object> claims, String subject, String role) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .claim("role", role)
                .setIssuer(ISSUER)
                .setAudience(AUDIENCE)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();
    }
    private SecretKey getSignKey() {
//        byte[] apiKeySecretBytes = Decoders.BASE64.decode(SECRET);
//        return Keys.hmacShaKeyFor(apiKeySecretBytes);
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, role);
    }
}
