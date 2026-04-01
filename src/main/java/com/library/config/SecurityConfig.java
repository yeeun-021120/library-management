package com.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 활성화 (프로덕션 환경)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")  // API만 제외
            )

            // URL 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 정적 리소스
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // 회원가입/로그인
                .requestMatchers("/member/signup", "/member/login").permitAll()
                // 관리자
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 나머지는 인증 필요
                .anyRequest().authenticated()
            )

            // 폼 로그인
            .formLogin(form -> form
                .loginPage("/member/login")
                .loginProcessingUrl("/member/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/book/list", true)
                .failureUrl("/member/login?error=true")
                .permitAll()
            )

            // 로그아웃
            .logout(logout -> logout
                .logoutUrl("/logout")  // ← 변경: /member/logout → /logout
                .logoutSuccessUrl("/member/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}