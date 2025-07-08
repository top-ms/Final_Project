package com.epam.rd.autocode.assessment.appliances.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "DyZ9O7ykcN6p+TzhCEF6p9Ucjb6e5zdYxLJeLzHWgS8=";
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtService.class);

    public String generateToken(UserDetails userDetails) {
        try {
            Map<String, Object> claims = new HashMap<>();
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElse("ROLE_UNKNOWN");
            claims.put("role", role);
            claims.put("userId", userDetails.getUsername());
            claims.put("name", userDetails.getUsername());
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (IllegalArgumentException e) {
            logger.error("Error creating JWT token - invalid argument: {}", e.getMessage(), e);
            throw new RuntimeException("Could not create JWT token: invalid argument", e);
        } catch (JwtException e) {
            logger.error("Error creating JWT token - JWT exception: {}", e.getMessage(), e);
            throw new RuntimeException("Could not create JWT token: JWT error", e);
        } catch (Exception e) {
            logger.error("Unexpected error creating JWT token for user: {}",
                    userDetails != null ? userDetails.getUsername() : "null", e);
            throw new RuntimeException("Could not create JWT token: unexpected error", e);
        }
    }

    public String extractUserId(String token) {
        try {
            return extractClaim(token, claims -> {
                Object userId = claims.get("userId");
                return userId != null ? userId.toString() : null;
            });
        } catch (Exception e) {
            logger.warn("Could not extract userId from token: {}", e.getMessage());
            return null;
        }
    }

    public String extractRole(String token) {
        try {
            return extractClaim(token, claims -> {
                Object role = claims.get("role");
                return role != null ? role.toString() : "ROLE_UNKNOWN";
            });
        } catch (Exception e) {
            logger.warn("Could not extract role from token: {}", e.getMessage());
            return "ROLE_UNKNOWN";
        }
    }

    public String extractName(String token) {
        try {
            return extractClaim(token, claims -> {
                Object name = claims.get("name");
                return name != null ? name.toString() : null;
            });
        } catch (Exception e) {
            logger.warn("Could not extract name from token: {}", e.getMessage());
            return null;
        }
    }

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            logger.warn("Could not extract username from token: {}", e.getMessage());
            return null;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username != null &&
                    username.equals(userDetails.getUsername()) &&
                    !isTokenExpired(token);
        } catch (Exception e) {
            logger.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            logger.warn("Could not check token expiration: {}", e.getMessage());
            return true;
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            logger.debug("JWT token is expired: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.warn("JWT token is unsupported: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            logger.warn("JWT token is malformed: {}", e.getMessage());
            throw e;
        } catch (SignatureException e) {
            logger.warn("JWT signature validation failed: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.warn("JWT token compact of handler are invalid: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error extracting claims from token: {}", e.getMessage(), e);
            throw new RuntimeException("Could not extract claims from token", e);
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public long getTokenExpirationTime(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.getTime() - System.currentTimeMillis();
        } catch (Exception e) {
            logger.warn("Could not get token expiration time: {}", e.getMessage());
            return 0;
        }
    }

    public boolean isTokenNearExpiry(String token) {
        try {
            long timeLeft = getTokenExpirationTime(token);
            long twoHoursInMillis = 2 * 60 * 60 * 1000; // 2 години
            return timeLeft < twoHoursInMillis && timeLeft > 0;
        } catch (Exception e) {
            logger.warn("Could not check token expiry status: {}", e.getMessage());
            return true;
        }
    }

    public Map<String, Object> extractAllTokenInfo(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Map<String, Object> tokenInfo = new HashMap<>();

            tokenInfo.put("username", claims.getSubject());
            tokenInfo.put("userId", claims.get("userId"));
            tokenInfo.put("role", claims.get("role"));
            tokenInfo.put("name", claims.get("name"));
            tokenInfo.put("issuedAt", claims.getIssuedAt());
            tokenInfo.put("expiration", claims.getExpiration());
            tokenInfo.put("isExpired", isTokenExpired(token));
            tokenInfo.put("timeLeft", getTokenExpirationTime(token));

            return tokenInfo;
        } catch (Exception e) {
            logger.warn("Could not extract all token info: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    public boolean isTokenValidSilent(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public Long extractUserIdAsLong(String token) {
        try {
            String userId = extractUserId(token);
            return userId != null ? Long.parseLong(userId) : null;
        } catch (NumberFormatException e) {
            logger.warn("UserId is not a valid number: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.warn("Could not extract userId as Long: {}", e.getMessage());
            return null;
        }
    }

    public boolean hasRole(String token, String requiredRole) {
        try {
            String userRole = extractRole(token);
            return requiredRole.equals(userRole);
        } catch (Exception e) {
            logger.warn("Could not check user role: {}", e.getMessage());
            return false;
        }
    }

    public boolean isAdmin(String token) {
        return hasRole(token, "ROLE_ADMIN");
    }

    public boolean isUser(String token) {
        return hasRole(token, "ROLE_USER");
    }

    public void logTokenInfo(String token, String action) {
        if (logger.isDebugEnabled()) {
            try {
                Map<String, Object> tokenInfo = extractAllTokenInfo(token);
                logger.debug("🔐 JWT Token {} - User: {}, Role: {}, Expires: {}, TimeLeft: {}ms",
                        action,
                        tokenInfo.get("username"),
                        tokenInfo.get("role"),
                        tokenInfo.get("expiration"),
                        tokenInfo.get("timeLeft"));
            } catch (Exception e) {
                logger.debug("Could not log token info for action {}: {}", action, e.getMessage());
            }
        }
    }

    public String refreshToken(String oldToken) {
        try {
            Claims claims = extractAllClaims(oldToken);
            String username = claims.getSubject();
            Map<String, Object> newClaims = new HashMap<>();
            newClaims.put("userId", claims.get("userId"));
            newClaims.put("role", claims.get("role"));
            newClaims.put("name", claims.get("name"));
            return Jwts.builder()
                    .setClaims(newClaims)
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 годин
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("Could not refresh token: {}", e.getMessage(), e);
            throw new RuntimeException("Could not refresh token", e);
        }
    }

    public boolean isValidJwtFormat(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        String[] parts = token.split("\\.");
        return parts.length == 3;
    }

    public String cleanToken(String token) {
        if (token == null) return null;
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token.trim();
    }

    public String getTokenSummary(String token) {
        try {
            String username = extractUsername(token);
            String role = extractRole(token);
            boolean expired = isTokenExpired(token);

            return String.format("User: %s, Role: %s, Expired: %s",
                    username != null ? username : "unknown",
                    role != null ? role : "unknown",
                    expired);
        } catch (Exception e) {
            return "Invalid token";
        }
    }
}
