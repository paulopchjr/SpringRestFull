package curso.rest.full.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.rest.full.model.Profissao;
import curso.rest.full.repository.ProfissaoRepository;

@RestController
@RequestMapping("/profissao")
public class ProfissaoController {
	
	@Autowired // injecao de indenpedencias
	private ProfissaoRepository profissaoRepository;

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Profissao>> profissoes(){
		
		List<Profissao> listaProfissao = profissaoRepository.findAll();
		
		return new ResponseEntity<List<Profissao>>(listaProfissao,HttpStatus.OK);
	}
	
	
}
