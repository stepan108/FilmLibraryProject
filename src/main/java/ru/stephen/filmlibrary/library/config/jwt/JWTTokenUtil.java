package ru.stephen.filmlibrary.library.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JWTTokenUtil {
    public static final long JWT_TOKEN_VALIDITY = 604800000;
    public final String secret = "certified_bing_chilling_moment";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String generateToken(final UserDetails payload) {
        return Jwts.builder()
                .setSubject(payload.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(final String token,
                                  UserDetails userDetails) {
        final String username = getUserNameFromToken(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Boolean isTokenExpired(final String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getUserNameFromToken(final String token) {
        return getStringValueFromTokenByKey(token, "username");
    }

    public String getRoleFromToken(final String token) {
        return getStringValueFromTokenByKey(token, "user_role");
    }

    public String getStringValueFromTokenByKey(final String token, final String key) {
        String claim = getClaimFromToken(token, Claims::getSubject);
        JsonNode claimJSON = null;
        try {
            claimJSON = objectMapper.readTree(claim);
        } catch (JsonProcessingException e) {
            log.error("JWTTokenUtils#getUserNameFromToken(): {}", e.getMessage());
        }

        if (claimJSON != null) {
            return claimJSON.get(key).asText();
        } else {
            return null;
        }
    }

    private <T> T getClaimFromToken(final String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
