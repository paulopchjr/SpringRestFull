package curso.rest.full.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import curso.rest.full.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query(nativeQuery = true,  value="SELECT * FROM usuario where upper(nome) like upper(trim(concat('%', :nome,'%')))   ")
	Page<Usuario> buscaUsuarioPorNome(String nome, PageRequest pageRequest);

	//Buscar o usuario pelo login
	@Query("SELECT u From Usuario u where u.login=?1")
	Usuario buscarUsuarioLogin(String login);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update usuario set token = ?1 where login =?2")
	void atualizaTokenUsuario(String token, String login);
	
	@Query(nativeQuery = true, value= "SELECT constraint_name from information_schema.constraint_column_usage where table_name = 'usuarios_role' and column_name = 'role_id' and constraint_name <>'unique_role_user'")
	String consultaConstraintRole();
	
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true, value="insert into usuarios_role (usuario_id, role_id) values(?, (select id from role where nome_role ='ROLE_USER'));")
	void insereAcessoRolePadrao(Long idUser);
	
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true, value = "update usuario set senha =?1 where id =?2")
	void updateSenha(String senha, Long codUser);
	
	
	
}
