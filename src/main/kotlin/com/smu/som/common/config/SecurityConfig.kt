package com.smu.som.common.config

import com.smu.som.common.jwt.filter.JwtAuthenticationFilter
import com.smu.som.common.jwt.util.JwtResolver
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import kotlin.jvm.Throws

//보안 관련 config
@EnableWebSecurity
class SecurityConfig(
	private val jwtResolver: JwtResolver
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>(){

	@Throws(Exception::class)
	override fun configure(http: HttpSecurity) {
		http
			.antMatcher("/game/**")
			.authorizeRequests()
			.antMatchers("/game/**", "/reports/**").authenticated()
			.and()
			.addFilterBefore(JwtAuthenticationFilter(jwtResolver), UsernamePasswordAuthenticationFilter::class.java)
	}

	//해당 url로의 접근을 엽니다
	@Bean
	fun webSecurityCustomizer(): WebSecurityCustomizer? {
		return WebSecurityCustomizer { web: WebSecurity ->
			web.ignoring().antMatchers("/api/auth/signin", "/api/auth/refresh", "/api/auth/signup",
				"/api/questions", "/api/question/**", "/admin/**", "/**", "/css/**", "/js/**", "/images/**", "/chat/room"
			,"/ws/chat/**","/queue/**","/topic/**","/app/**","/rooms","/room/**","/**", "/templates/**", "room/enter/**", "/chat/**"
			,"/app/chat/message")
		}
	}

	//jwt와 관련된 해당 url로의 접근을 엽니다
	@Bean
	fun filterChain(http: HttpSecurity?): SecurityFilterChain {
		http!!
			.httpBasic().disable()
			.csrf().disable()
			.exceptionHandling()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/api/auth/signin", "/api/auth/refresh", "/api/auth/signup", "/admin/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(JwtAuthenticationFilter(jwtResolver), UsernamePasswordAuthenticationFilter::class.java)
		return http.build()
	}

	//BCrypt 방식을 통한 암호화를 위한 bean 등록
	@Bean
	fun encoder(): PasswordEncoder = BCryptPasswordEncoder()
}
