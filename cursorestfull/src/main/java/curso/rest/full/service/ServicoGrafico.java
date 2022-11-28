package curso.rest.full.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServicoGrafico {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	public List<String> listaGraficoRel(){
		
		List<String> resultado =	jdbcTemplate.queryForList("select array_agg(nome) from usuario where salario > 0 and nome <> '' union all select cast (array_agg(salario) as character varying []) from usuario where salario > 0 and nome <> '';", String.class);
		return resultado;
		
		
	} 	
	
}
