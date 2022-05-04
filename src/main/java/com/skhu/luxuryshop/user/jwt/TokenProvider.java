package com.skhu.luxuryshop.user.jwt;

import com.skhu.luxuryshop.user.entity.Authority;
import com.skhu.luxuryshop.user.entity.UserAuthority;
import com.skhu.luxuryshop.user.entity.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {
    protected static final String AUTHORITIES_KEY = "auth";
    protected static final String AUTHORITIES_SPLITTER = ", ";

    private final String secret;
    private final long tokenValidityInMilliseconds;

    private Key key;

    public TokenProvider(
            /* @Value("${jwt.secret}") =  application.properties에 설정된 값을 가지고 오기 */
            /* secret = token 체크시 필요한 암호키 */
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(UserEntity user) {
        List<Authority> userAuthorities = new ArrayList<>();
        for (UserAuthority userAuthority : user.getAuthorities()) {
            userAuthorities.add(userAuthority.getAuth());
        }
        /* userAuthorities 리스트에 담긴 모든 아이템을 하나의 String으로 ", "를 넣어 구분해서 만든다. */
        String authorities = userAuthorities.stream()
                .map(Authority::getAuthorityName)
                .collect(Collectors.joining(AUTHORITIES_SPLITTER));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(user.getEmail())    // 제목
                .claim(AUTHORITIES_KEY, authorities)    // playload 부분에 들어갈 정보
                .signWith(key, SignatureAlgorithm.HS512)    //
                .setExpiration(validity)    // 만료일
                .compact();
    }

    /* 토큰 유효성, 만료일자 검증 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT 토큰이 잘못되었습니다.");
        }
    }

    public Claims getData(String token) {
        if (validateToken(token)) {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }
        return null;
    }
}