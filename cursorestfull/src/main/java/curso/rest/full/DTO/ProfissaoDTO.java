package curso.rest.full.DTO;

import java.io.Serializable;

import curso.rest.full.model.Profissao;

public class ProfissaoDTO implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String descricaoProfissaoDto;
	
	
	public ProfissaoDTO(Profissao profissao) {
		this.id = profissao.getId();
		this.descricaoProfissaoDto = profissao.getDescricao();
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the descricaoProfissaoDto
	 */
	public String getDescricaoProfissaoDto() {
		return descricaoProfissaoDto;
	}
	/**
	 * @param descricaoProfissaoDto the descricaoProfissaoDto to set
	 */
	public void setDescricaoProfissaoDto(String descricaoProfissaoDto) {
		this.descricaoProfissaoDto = descricaoProfissaoDto;
	}
	
	
	
}
