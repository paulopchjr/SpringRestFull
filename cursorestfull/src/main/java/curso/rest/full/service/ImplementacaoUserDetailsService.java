package curso.rest.full.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import curso.rest.full.model.Usuario;
import curso.rest.full.repository.UsuarioRepository;

@Service
public class ImplementacaoUserDetailsService implements UserDetailsService { /// implementa a interface do spring que
																				/// tem o metodo que busca o usuario

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		/* Consulta no banco o usuario */
		Usuario usuario = usuarioRepository.buscarUsuarioLogin(username);

		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não foi encontrado");
		}

		return new User(usuario.getLogin(), usuario.getSenha(), usuario.getAuthorities());
	}

	public void insereAcessoPadrao(Long id) {
		/* 1 Descobre qual é a constraint de restrição */
		String constraint = usuarioRepository.consultaConstraintRole();

		if (constraint != null) { // não foi removido a restricao
			/* 2 Remove a constraint */
			jdbcTemplate.execute("alter table usuarios_role DROP CONSTRAINT " + constraint);
		}

		// cadastra o usuario junto ao role_user
		usuarioRepository.insereAcessoRolePadrao(id);

	}

}
