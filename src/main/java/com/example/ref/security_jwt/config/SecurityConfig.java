package com.example.ref.security_jwt.config;

import com.example.ref.security_jwt.config.jwt.JwtAuthenticationEntryPoint;
import com.example.ref.security_jwt.config.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
// springSecurityFilterChain 은 스프링에서 보안관 관련된 여러 필터 리스트를 갖고 있는 객체로
// 필터 리스트(AuthenticationFilter) 를 순회하면서 필터링을 실시한다.
// AuthenticationFilter 리스트
    // WebAsyncManagerIntegrationFilter
        // SpringSecurityContextHolder는 ThreadLocal기반(하나의 쓰레드에서 SecurityContext 공유하는 방식)으로 동작하는데,
        // 비동기(Async)와 관련된 기능을 쓸 때에도 SecurityContext를 사용할 수 있도록 만들어주는 필터
    //SecurityContextPersistenceFilter
        //SecurityContext가 없으면 만들어주는 필터
        //SecurityContext는 Authentication 객체를 보관하는 보관 인터페이스다. (구현체가 있어야겠지)
    //HeaderWriterFilter
        //응답(Response)에 Security와 관련된 헤더 값을 설정해주는 필터
    // CsrfFilter
        //CSRF 공격을 방어하는 필터
    //LogoutFilter
        //로그아웃 요청을 처리하는 필터
        //DefaultLogoutPageGeneratingFilter가 로그아웃 기본 페이지를 생성함
    //UsernamePasswordAuthenticationFilter → username, password를 쓰는 form기반 인증을 처리하는 필터.
        //AuthenticationManager를 통한 인증 실행
        //성공하면, Authentication 객체를 SecurityContext에 저장 후 AuthenticationSuccessHandler 실행
        //실패하면 AuthenticationFailureHandler 실행
    //RequestCacheAwareFilter
        //인증 후, 원래 Request 정보로 재구성하는 필터
    //SecurityContextHolderAwareRequestFilter
    //AnonymousAuthenticationFilter
        //이 필터에 올 때까지 앞에서 사용자 정보가 인증되지 않았다면, 이 요청은 익명의 사용자가 보낸 것으로 판단하고 처리한다.
        // (Authentication 객체를 새로 생성함(AnonymousAuthenticationToken))
    //SessionManagementFilter
        //세션 변조 공격 방지 (SessionId를 계속 다르게 변경해서 클라이언트에 내려준다)
        //유효하지 않은 세션으로 접근했을 때 URL 핸들링
        //하나의 세션 아이디로 접속하는 최대 세션 수(동시 접속) 설정
        //세션 생성 전략 설정
    //ExceptionTranslationFilter
        //앞선 필터 처리 과정에서 인증 예외(AuthenticationException) 또는 인가 예외(AccessDeniedException)가 발생한 경우,
        // 해당 예외를 캐치하여 처리하는 필터 (모든 예외를 다 이 필터에서 처리하는 건 아님)
    //FilterSecurityInterceptor
        //인가(Authorization)를 결정하는 AccessDecisionManager 에게 접근 권한이 있는지 확인하고 처리하는 필터
@EnableWebSecurity // 스프링 시큐리티 필터를 springFilterChain 에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // 메소드 어노테이션 활성화
// WebSecurityConfigureAdapter deprecated since spring 5.7.0-M2 .
public class SecurityConfig {

    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(JwtRequestFilter jwtRequestFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Value("${spring.security.debug:false}")
    boolean securityDebug;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(securityDebug)
                .ignoring()
                .antMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 인증: 해당 사용자가 본인이 맞는지 확인하는 절차
        // 인가: 인증된 사용자가 요청한 자원에 접근 가능한지 결정하는 절차.

        // 스프링 시큐리티 CORS 허용 정책
        // Cross-Origin Resource Sharing Policy: 다른 서버 간 데이터 통신이 거부된다.
        httpSecurity
                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

        httpSecurity
                // .csrf().disable() : Cross-site Request forgery(사이트간 위조 요청), 즉 정상적인 사용자가 의도치 않은 위조요청을 보내는 것을 말한다.
                // html tag 를 통한 공격 ( api 서버(rest) 이용 시 disable() )
                // 사용자가 보내는 POST, PUT, DELETE 요청으로부터 서버를 보호한다.
                // spring security 에서는 default
                // rest api 를 사용하는 경우 서버에 인증정보를 저장하지 않기에 필요치 않다.
            .csrf().disable()
                // .addFilterBefore(filter, class<filter2>) : class<filter2> 전에 filter1을 거치게 한다.
                // .addFilterAfter(filter, class<filter2>) : class<filter2> 후에 filter1을 거치게 한다.
//            .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
//            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
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

        // 특정 AuthenticationFilter 이전 또는 이후에 customizedFilter 를 추가 한다.
        httpSecurity
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//                .addFilterAfter(afterfilter, UsernamePasswordAuthenticationFilter.class)

        // 예외 발생 시 내가 만든 클래스를 추가,설정한다.
        httpSecurity
                // 예외 발생시 response 를 modify 하게 customize 할 수 있다.
                .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint);
//                    .accessDeniedHandler(sampleDeny);

        // 로그인 창을 customized 하거나 성공, 실패 시 이동하는 주소를 설정할 수 있다.
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

        // 로그아웃 설정을 customized 할 수 있다.
        httpSecurity
                // 로그아웃 기능이 작동되게 한다.
                .logout()
                // 특정 주소 이동시 로그아웃된다.
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // 로그아웃 성공 시 이동되는 페이지
                .logoutSuccessUrl("/login");

        // 세션 관련 설정을 customized 할 수 있다.
        httpSecurity
                // 세션 기능이 작동되게 한다.
                .sessionManagement()
                // 스프링 시큐리티는 기본적으로 세션을 사용하지만, JWT 를 대신 사용하기에 세션을 사용하지 않게 설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                // 최대 허용가능한 세션 수
//                .maximumSessions(1)
                // 허용 세션 수가 초과될 경우 처리.
                // true 이면 요청하는 사용자 인증을 실패시키고,
                // false 라면 기존에 인증된 세션을 만료시킨다.
//                .maxSessionsPreventsLogin(true)
                // 세션이 만료된 후 이동되는 페이지
//                .expiredUrl("/expired");

        // 기타 설정을 customized 할 수 있다.
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
