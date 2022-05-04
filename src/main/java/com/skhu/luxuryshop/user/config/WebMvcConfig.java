package com.skhu.luxuryshop.user.config;

import com.skhu.luxuryshop.user.encoder.BCryptPasswordEncoder;
import com.skhu.luxuryshop.user.jwt.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* 스프링 컨테이너에 새로운 빈 객체를 제공 */
@Configuration
/*
@RequiredArgsConstructor - 초기화 되지 않은 모든 final 필드 & @NonNull 마크가 있는 필드를 초기화하는 생성자 생성
*/
/* @Autowired를 사용하지않고 @RequiredArgsConstructor로 의존성 주입을 한다.(생성자 주입)
 * 생성자 주입의 장점 : 순환 참조 방지, 테스트 코드 작성 용이, 코드 가독성이 좋음, 객체 변이 방지(final 가능)
 */
@RequiredArgsConstructor
/* 인터셉트를 추가하기위해 WebMvcConfigurer를 상속 받아 addInterceptor를 구현한다. */
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)    // 핸들러를 지정
                .excludePathPatterns("api/users/**")    // 인터셉터를 제외할 url 패턴을 등록
                .excludePathPatterns("api/products/**")
                .excludePathPatterns("api/carts/**")
                .excludePathPatterns("api/orders/**");
    }

    /* 비밀번호 암호화를 위한 Encoder 설정 */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}