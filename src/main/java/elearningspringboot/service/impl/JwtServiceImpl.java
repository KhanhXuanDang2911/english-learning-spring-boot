package elearningspringboot.service.impl;

import elearningspringboot.entity.User;
import elearningspringboot.enumeration.TokenType;
import elearningspringboot.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Value("${jwt.refreshKey}")
    private String REFRESH_KEY;

    @Value("${jwt.resetKey}")
    private String RESET_KEY;

    @Value("${jwt.confirmKey}")
    private String CONFIRM_KEY;

    @Value("${jwt.expiryHour}")
    private long expiryHour;

    @Value("${jwt.expiryDay}")
    private long expiryDay;

    @Value("${jwt.expiryMinute}")
    private long expiryMinute;

    @Value("${spring.application.name}")
    private String provider;

    public String generateAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expiryHour))
                .setIssuer(provider)
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuer(provider)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateToken(UserDetails userDetails, TokenType tokenType) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuer(provider)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expiryHour))
                .signWith(getKey(tokenType))
                .compact();
    }

    public Claims extractAllClaims(String token, TokenType tokenType) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey(tokenType))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BadCredentialsException("Token has expired", e);
        } catch (MalformedJwtException e) {
            throw new BadCredentialsException("Invalid token format", e);
        } catch (SignatureException e) {
            throw new BadCredentialsException("Invalid token signature", e);
        }
    }

    public String extractEmail(String token, TokenType tokenType) {
        return extractAllClaims(token, tokenType).getSubject();
    }

    public Date extractExpiration(String token, TokenType tokenType) {
        return extractAllClaims(token, tokenType).getExpiration();
    }

    public boolean isTokenValid(String token, UserDetails userDetails, TokenType tokenType) {
        final String email = extractEmail(token, tokenType);
        return userDetails.isEnabled() && email.equals(userDetails.getUsername()) && !isTokenExpired(extractExpiration(token, tokenType));
    }

    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date(System.currentTimeMillis()));
    }

    private Key getKey(TokenType tokenType) {
        byte[] keyBytes;
        keyBytes = switch (tokenType) {
            case ACCESS_TOKEN -> Decoders.BASE64.decode(SECRET_KEY);
            case REFRESH_TOKEN -> Decoders.BASE64.decode(REFRESH_KEY);
            case RESET_TOKEN -> Decoders.BASE64.decode(RESET_KEY);
            case CONFIRM_TOKEN -> Decoders.BASE64.decode(CONFIRM_KEY);
        };
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
