package com.skhu.luxuryshop.user.jwt;

import com.skhu.luxuryshop.user.annotation.AnnotationHandler;
import com.skhu.luxuryshop.user.annotation.PreAuthorize;
import com.skhu.luxuryshop.user.exception.NoTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Optional;

import static com.skhu.luxuryshop.user.jwt.TokenProvider.AUTHORITIES_KEY;
import static com.skhu.luxuryshop.user.jwt.TokenProvider.AUTHORITIES_SPLITTER;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_HEADER = "Bearer ";
    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        HandlerMethod method = (HandlerMethod) handler;
        /* Optional = nullpointexception을 방지하여 데이터를 가져오기 위해 사용하는 클래스 */
        Optional<PreAuthorize> preAuthorize = getPreAuthorize(method);
        Optional<String> jwt = resolveToken(request);

        /* isPresent()로 Optional 객체가 null인지 아닌지 확인하고 있으면 get()으로 저장된 값을 가져온다. */
        if (preAuthorize.isPresent()) {
            /* orElseThrow() = 예외처리 / jwt에 토큰이 안담겨 있으면 (토큰이 존재하지 않습니다.) 예외처리 */
            String token = jwt.orElseThrow(NoTokenException::new);
            tokenProvider.validateToken(token);
            return AnnotationHandler.hasAnyRole(getAuthorities(token), preAuthorize.get());
        }
        return true;
    }

    private Optional<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        /* StringUtils.hasText = null이 아니여야 하고, 길이가 0보다 커야하고, 공백이 아닌 문자가 포함되어있는지 검증한다. */
        /* startsWith(TOKEN_HEADER) = 문자열이 "Bearer "로 시작하는지 검증한다. */
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_HEADER)) {
            /* Optional.of = null이 아닌 객체를 담고 있는 Optional 객체를 생성한다. */
            /* substring() = 문자열에서 특정한 구간의 문자열을 추출한다. */
            /* length() = 문자열의 길이 */
            /* "Bearer "을 제외하고 그 뒤의 문자열을 resolveToken으로 만든다. */
            return Optional.of(bearerToken.substring(TOKEN_HEADER.length()));
        }
        return Optional.empty();
    }

    private String[] getAuthorities(String token) {
        String authorities = (String) tokenProvider.getData(token).get(AUTHORITIES_KEY);
        return authorities.split(AUTHORITIES_SPLITTER);
    }

    private Optional<PreAuthorize>
    getPreAuthorize(HandlerMethod method) {
        for (Annotation annotation : method.getMethod().getAnnotations()) {
            if (annotation instanceof PreAuthorize) {
                return Optional.of((PreAuthorize) annotation);
            }
        }
        /* 빈 객체를 리턴해준다 */
        return Optional.empty();
    }
}