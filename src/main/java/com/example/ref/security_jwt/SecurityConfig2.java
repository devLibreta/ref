package com.example.ref.security_jwt;

//import com.cos.securityex01.config.oauth.PrincipalOauth2UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration // IoC 빈(bean)을 등록
//@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
public class SecurityConfig2 extends WebSecurityConfigurerAdapter{
	
//	@Autowired
//	private PrincipalOauth2UserService principalOauth2UserService;
//
//	@Bean
//	public BCryptPasswordEncoder encodePwd() {
//		return new BCryptPasswordEncoder();
//	}
//
    @Override
	protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .and()
                .csrf() // 추가
                .ignoringAntMatchers("/h2-console/**").disable() // 추가
                .httpBasic();

    }

    public void configure(WebSecurity web)throws Exception{
        web.ignoring().antMatchers("/h2-console/**");
    }
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		http.csrf().disable();
//		http.authorizeRequests()
//			.antMatchers("/user/**").authenticated()
//			//.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//			//.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
//			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//			.anyRequest().permitAll()
//		.and()
//			.formLogin()
//			.loginPage("/login")
//			.loginProcessingUrl("/loginProc")
//			.defaultSuccessUrl("/")
//		.and()
//			.oauth2Login()
//			.loginPage("/login")
//			.userInfoEndpoint()
//			.userService(principalOauth2UserService);
//	}
}





