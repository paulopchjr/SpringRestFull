package curso.rest.full.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
public class Role  implements GrantedAuthority{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_role")
	private  Long id;
	
	private String nomeRole; /*ADMIN, USUARIOCOMUN, GERENRTE*/

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
	 * @return the nomeRole
	 */
	public String getNomeRole() {
		return nomeRole;
	}

	/**
	 * @param nomeRole the nomeRole to set
	 */
	public void setNomeRole(String nomeRole) {
		this.nomeRole = nomeRole;
	}

	@Override
	public String getAuthority() {
		
		return this.nomeRole;
	}
	
	
}
