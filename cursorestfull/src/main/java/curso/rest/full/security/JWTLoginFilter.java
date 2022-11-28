package curso.rest.full.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import curso.rest.full.model.Usuario;

/* Estabelece nosso gerenciador ou gerente de token*/
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	/* Configrando o gerenciador de autenticação */
	protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
		
		/* obriga autenticar a url*/
		super(new AntPathRequestMatcher(url));

		/* Gerenciador de autenticação */
		setAuthenticationManager(authenticationManager);

	}
	/* Retorna o usuario ao processar Autenticação */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		/* Está pegando o token para validar*/
		Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class); // le o json que está vindo da requisição e pega o usuario
		
		/*retorna o usuario login, senha e acesso*/
		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		/*depois de gerar o token, vai para o metodo de autenticacao para ver se ele é válido*/
		new JWTTokenAutenticacaoService().addAutentication(response, authResult.getName());
	}

}
