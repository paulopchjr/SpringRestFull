package curso.rest.full.DTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import curso.rest.full.model.Profissao;
import curso.rest.full.model.Telefone;
import curso.rest.full.model.Usuario;

public class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long userId;
	private String userLogin;
	private String userNome;
	private String userSenhaDto;
	private List<Telefone> userListTelefonesDTO;
	private String dataNascimentoDto;
	private Profissao userProfissao;
	private BigDecimal userSalarioDTO;

	public UsuarioDTO(Usuario usuario) {
		this.userId = usuario.getId();
		this.userLogin = usuario.getLogin();
		this.userNome = usuario.getNome();
		this.userSenhaDto = usuario.getSenha();
		this.userListTelefonesDTO = usuario.getTelefones();
		this.dataNascimentoDto = new  SimpleDateFormat("dd/MM/yyyy").format(usuario.getDataNascimento()); 
		this.userProfissao = usuario.getProfissao();
		this.userSalarioDTO = usuario.getSalario();
	}
	
	
		

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userLogin
	 */
	public String getUserLogin() {
		return userLogin;
	}

	/**
	 * @param userLogin the userLogin to set
	 */
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	/**
	 * @return the userNome
	 */
	public String getUserNome() {
		return userNome;
	}

	/**
	 * @param userNome the userNome to set
	 */
	public void setUserNome(String userNome) {
		this.userNome = userNome;
	}

	/**
	 * @return the userSenhaDto
	 */
	public String getUserSenhaDto() {
		return userSenhaDto;
	}

	/**
	 * @param userSenhaDto the userSenhaDto to set
	 */
	public void setUserSenhaDto(String userSenhaDto) {

		this.userSenhaDto = userSenhaDto;
	}

	/**
	 * @return the userListTelefonesDTO
	 */
	public List<Telefone> getUserListTelefonesDTO() {
		return userListTelefonesDTO;
	}

	/**
	 * @param userListTelefonesDTO the userListTelefonesDTO to set
	 */
	public void setUserListTelefonesDTO(List<Telefone> userListTelefonesDTO) {
		this.userListTelefonesDTO = userListTelefonesDTO;
	}


	/**
	 * @return the dataNascimentoDto
	 */
	public String getDataNascimentoDto() {
		return dataNascimentoDto;
	}


	/**
	 * @param dataNascimentoDto the dataNascimentoDto to set
	 */
	public void setDataNascimentoDto(String dataNascimentoDto) {
		this.dataNascimentoDto = dataNascimentoDto;
	}




	/**
	 * @return the userProfissao
	 */
	public Profissao getUserProfissao() {
		return userProfissao;
	}




	/**
	 * @param userProfissao the userProfissao to set
	 */
	public void setUserProfissao(Profissao userProfissao) {
		this.userProfissao = userProfissao;
	}




	/**
	 * @return the userSalarioDTO
	 */
	public BigDecimal getUserSalarioDTO() {
		return userSalarioDTO;
	}




	/**
	 * @param userSalarioDTO the userSalarioDTO to set
	 */
	public void setUserSalarioDTO(BigDecimal userSalarioDTO) {
		this.userSalarioDTO = userSalarioDTO;
	}
	
	
	




	
	



}
