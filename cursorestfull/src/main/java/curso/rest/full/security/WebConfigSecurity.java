package curso.rest.full.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.rest.full.service.ImplementacaoUserDetailsService;

/*MAPEIA url, endereços, autoriza ou bloqueia acessos a urls -> tudo é intercpetado */
@Configuration
@EnableWebSecurity // hablita as configurações de segurança
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;

	/* Configura as solicitações de acesso por HTTP */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		/* Ativando a proteção contra usuário que naão estão validado por token */
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

				/*
				 * Ativando a permisssao para o acesso pagina inicial do sistema Ex:
				 * sistema.com.br
				 */
				.disable().authorizeRequests().antMatchers("/").permitAll().antMatchers("/index", "/recuperar/**").permitAll()

				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // liberando todas as requisções http da api
				
				/* URL de logouto - redireciona após o user deslogar do sistema */
				.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")

				/* Mapeia URL de Logout é invalida o Usuario */
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

				/* Filtra as requisções de login para autenticação */
				.and()
				.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
						UsernamePasswordAuthenticationFilter.class)
				/*
				 * Filtra as demais requisções para valida o token do JWT no CABECALHO HEADER
				 * HTTP
				 */
				.addFilterBefore(new JWTAPIAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		/* Service que ira consulta o usuario no banco de dados */
		auth.userDetailsService(implementacaoUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());

	}

}
