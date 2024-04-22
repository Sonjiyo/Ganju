package kr.ganjuproject.config;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.CustomAuthFailureHandler;
import kr.ganjuproject.auth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
//@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final PrincipalOauth2UserService principalOauth2UserService;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)->{
            web.ignoring().requestMatchers(new String[]{"/favicon.ico","/resources/**","/error"});
        };
    }

    @Bean
    AuthenticationFailureHandler customAuthFailureHandler(){
        return new CustomAuthFailureHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(
                authz -> authz
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER")
                        .anyRequest().permitAll()

        ).formLogin(
                form->{
                    form.loginPage("/")   // 우리가 만든 로그인페이지로 자동 인터셉트됨
                            .loginProcessingUrl("/login")
                            .failureHandler(customAuthFailureHandler())
                            .defaultSuccessUrl("/",true);  // 로그인 성공하면 돌아올 페이지

                }
        ).oauth2Login(Customizer.withDefaults());

        http.logout(logout -> {
                    logout
                    .logoutUrl("/logout") // 로그아웃을 수행할 URL 설정
                    .addLogoutHandler((request, response, authentication) -> {
                        // 세션 무효화
                        HttpSession session = request.getSession();
                        if (session != null) {
                            session.invalidate();
                        }
                    })  // 로그아웃 핸들러 추가
                    .logoutSuccessHandler((request, response, authentication) -> {
                        // 로그아웃 성공 후 리다이렉트
                        response.sendRedirect("/");
                    });
                });

        return http.build();
    }
}