package curso.rest.full.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/*filtro onde todas as requisições ser]ao cappturadas para autenticar*/
public class JWTAPIAutenticacaoFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		/* Estabelece autenticacao para requisicao */
		Authentication authentication = new JWTTokenAutenticacaoService()
				.getAuthentication((HttpServletRequest) request, (HttpServletResponse) response); // busca a autenticação gerada pelo addAuthentication

		/* Coloca o proceesso de autenticacao no spring security */
		SecurityContextHolder.getContext().setAuthentication(authentication);

		/* continua o processo */
		chain.doFilter(request, response);
	}

}
