package com.example.ref.security_jwt.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // 메소드 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfiguration {
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 인증: 해당 사용자가 본인이 맞는지 확인하는 절차
        // 인가: 인증된 사용자가 요청한 자원에 접근 가능한지 결정하는 절차.
        httpSecurity
                // .csrf().disable() : Cross-site Request forgery(사이트간 위조 요청), 즉 정상적인 사용자가 의도치 않은 위조요청을 보내는 것을 말한다.
                // 사용자가 보내는 POST, PUT, DELETE 요청으로부터 서버를 보호한다.
                // spring security 에서는 default
                // rest api 를 사용하는 경우 서버에 인증정보를 저장하지 않기에 필요치 않다.
            .csrf().disable()
                // .authorizeRequests() : 서블릿요청에 대하여 보안을 적용한다.
            .authorizeRequests()
                // .access() : 주어진 SpEL 표현식이 참이면 접근을 허용한다. param 은 String.
                // SpEl : Spring Expression Language
                // https://docs.spring.io/spring-security/reference/servlet/authorization/expression-based.html#el-common-built-in

                //.anyMatchers() 는 메소드 시큐리티로 대체가능하다.  (중요)
            .antMatchers("/access").access("hasRole('Role_USER') or hasRole('ROLE_MANAGER')")
                // .permitAll() : 접근을 전부 허용한다
            .antMatchers("/permitAll").permitAll()
                // .denyAll() : 접근을 전부 제한한다.
            .antMatchers("/denyAll").denyAll()
                // AUTHORITY 는 기능 단위의 permission 을 의미한다.
                // .hasAuthority() : 특정 권한을 가지는 사용자만 접근할 수 있다.
            .antMatchers("/auth").hasAuthority("ADMIN")
                // .hasAnyAuthority() : 특정 권한을 가지는 사용자만 접근할 수 있다.
            .antMatchers("/auth").hasAnyAuthority("ADMIN","USER")
                // ROLE 작명 시 반드시 "ROLE_"이 prefix 로 있어야한다.
                // ROLE 은 여러 Authorities 를 가질 수 있다.
                // .hasAnyRole() : 한 개의 role 을 지정할 때.
            .antMatchers("/admin").hasRole("ADMIN")
                // .hasAnyRole() : 여러 개의 role 을 요구할 떄
            .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                // .anonymous() : 인증되지 않은 사용자도 접근할 수 있다.
            .antMatchers("/anonymous").anonymous()
                // .fullyAuthenticated() : 완전히 인증된 사용자만 접근할 수 있다.
            .antMatchers("/fullyAuthenticated").fullyAuthenticated()
                // .hasIpAddress() : 특정 IP을 가지는 사용자만 접근할 수 있다.
            .antMatchers("/ip").hasIpAddress("127.0.0")
                // .rememberMe() : 로그인 상태를 기억하여 사용자의 접근을 허용
            .antMatchers("/rememberMe").rememberMe()
                // .not() : 뒤에오는 접근 제한 기능을 해제
            .antMatchers("/not").not().anonymous()
                //.authenticated() : 접근제한 리소스 제외 나머지 리소는
                // 인증은 요구하지만 인가를 요구하지 않는다.
            .anyRequest().authenticated();

        httpSecurity
                // Form 기능(로그인 창)이 작동하게 된다.
                .formLogin()
                // 원하는 로그인 페이지를 설정할 수 있다. 미 설정 시 default 화면이 출력된다.
//                .loginPage("/customLoginPage")
                // 로그인이 성공 시 이동되는 페이지를 설정할 수 있다.
                // 더 세부작업을 할 경우 .successHandler()를 뒤에 붙인다.
                .defaultSuccessUrl("/")
                // 로그인 실패 시 이동되는 페이지를 설정할 수 있다.
                // 더 세부작업을 할 경우 .failureHandler()를 뒤에 붙인다.
                .failureUrl("/login?error=true")
                // username 에 넣을 view(http)에서 넘어오는 param 이름
                .usernameParameter("username")
                // password 에 넣을 view(http)에서 넘어오는 param 이름
                .passwordParameter("password");

        httpSecurity
                // 로그아웃 기능이 작동되게 한다.
                .logout()
                // 특정 주소 이동시 로그아웃된다.
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // 로그아웃 성공 시 이동되는 페이지
                .logoutSuccessUrl("/login");

        httpSecurity
                // 세션 기능이 작동되게 한다.
                .sessionManagement()
                // 최대 허용가능한 세션 수
                .maximumSessions(1)
                // 허용 세션 수가 초과될 경우 처리.
                // true 이면 요청하는 사용자 인증을 실패시키고,
                // false 라면 기존에 인증된 세션을 만료시킨다.
                .maxSessionsPreventsLogin(true)
                // 세션이 만료된 후 이동되는 페이지
                .expiredUrl("/expired");
        httpSecurity
                // 헤더관련 기능이 작동되게 한다.
                .headers()
                .disable()
                // 관련 기능이 작동되게 한다.
                .httpBasic()
                .disable();

        return httpSecurity.build();

    }
}
