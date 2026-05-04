package kahlua.KahluaProject.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.user.dto.response.TokenResponse;
import kahlua.KahluaProject.global.exception.GeneralException;
import kahlua.KahluaProject.global.redis.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    @Value("${jwt.token.access-expiration-time}")
    private long accessTokenExpirationTime;

    @Value("${jwt.token.refresh-expiration-time}")
    private long refreshTokenExpirationTime;

    private final RedisClient redisClient;

    @PostConstruct
    protected void init() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String createAccessToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getUserType().name())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getUserType().name())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenResponse createToken(User user) {
        return TokenResponse.builder()
                .accessToken(createAccessToken(user))
                .refreshToken(createRefreshToken(user))
                .build();
    }

    public TokenResponse recreate(User user, String refreshToken) {
        String accessToken = createAccessToken(user);

        if(getExpirationTime(refreshToken) <= getExpirationTime(accessToken)) {
            refreshToken = createRefreshToken(user);
        }
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateToken(String token, String tokenType) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            if (Objects.equals(tokenType, "refresh") && redisClient.checkExistsValue(token)) {
                return false;
            }

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Long getExpirationTime(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().getTime();
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Transactional
    public void invalidateTokens(String refreshToken, String accessToken) {
        if (!validateToken(refreshToken, "refresh")) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }
        redisClient.deleteValue(getEmail(refreshToken));
        redisClient.setValue(accessToken, "logout", getExpirationTime(accessToken));
    }

    public String resolveAccessTokenFromHeader(String authHeaderValue) {
        if (authHeaderValue.startsWith("Bearer ")) {
            return authHeaderValue.substring(7).trim();
        }
        return null;
    }
}
