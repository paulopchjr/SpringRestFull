package curso.rest.full.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import curso.rest.full.ApplicationContextLoad;
import curso.rest.full.model.Usuario;
import curso.rest.full.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTTokenAutenticacaoService {

	/* Tempo de expiração do token no caso está em 2 dias.172800000; */
	private static final long EXPIRATION_TIME = 172800000;

	/* Uma senha unica para compor a autenticacao e ajudar na segurança */
	private static final String SECRET = "SenhaSuperSecreta";

	/* Prefixo Padrão de token */
	private static final String TOKEN_PREFIX = "Bearer";

	/* Resposta do cacebalho */
	private static final String HEADER_STRING = "Authorization";

	/*
	 * Gerando token de autenticacao e adicionando o cabelaho e reposta que vai
	 * voltar para o navegador -> http
	 * 
	 * Autenticação quando entra no sistema -> 1 vez
	 */
	public void addAutentication(HttpServletResponse response, String username) throws IOException {

		/* Montagem do token */
		String JWT = Jwts.builder()/* Chama o Gerador de Token */
				.setSubject(username) /* Adiciona o usuario */
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))/* Tempo de Expiação */
				.signWith(SignatureAlgorithm.HS512, SECRET).compact(); /* Compactação e algoritmo geração de senha */

		
		
		/*Atualiza o token no banco de dados quando é realizado o login*/
		ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
		.atualizaTokenUsuario(JWT, username);
		/* Junta o token com prefixo */
		String token = TOKEN_PREFIX + " " + JWT; /* Berarrer uhausoakoa.okaoakosaa.oakoakaok */

		/* adiciona no cabeçalho http */
		response.addHeader(HEADER_STRING, token); /* Authorization: Bearer uhausoakoa.okaoakosaa.oakoakaok */
		
		


		/* Liberando resposta para portas diferentes api no caso clientes web */
		liberacaoCors(response);

		/* Escreve token como resposta no corpo do http */
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}

	/*
	 * Retorna o usuario validado ou null
	 * 
	 * Faz a validação pega o token para fazer algum servico de http, POST, DELETE,
	 * PUT, GET
	 */
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {

		/* pega o token enviado no cabeçalho http */
		String token = request.getHeader(HEADER_STRING);

		try {
			if (token != null) {

				String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

				/*
				 * Faz a validação do token do usuário na requsição passando a senha secreta
				 * para fazer a varredura do token
				 */
				String usuario = Jwts.parser().setSigningKey(SECRET) /* Bearer 5165416544.44ea */
						.parseClaimsJws(tokenLimpo)/* 5165416544.44ea */
						.getBody().getSubject(); /* bastian */
				if (usuario != null) {

					/*
					 * traz todos controlers, repositorys, dados carregado da memoria que o spring
					 * subiu getBean - são classes, serviços , repositorys, são os packages de um
					 * projeto
					 * 
					 * Traz o usuário que está sendo logado .
					 */
					Usuario user = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
							.buscarUsuarioLogin(usuario);

					if (user != null) {

						/* token esta cadastro no banco é o mesmo com esse usuario */
						if (tokenLimpo.equalsIgnoreCase(user.getToken())) {

							return new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha(),
									user.getAuthorities());
						}
					}

				}

			}
		} catch (io.jsonwebtoken.ExpiredJwtException tokenexpirado) {
			try {
				response.getOutputStream()
						.println("Seu token está expirado, faça o login ou informe um novo token para autenticação");
			} catch (IOException e) {

			}

		}
		liberacaoCors(response);
		return null; /* Não autorizado */
	}

	private void liberacaoCors(HttpServletResponse response) {
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}

		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}

		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}

	}

}
