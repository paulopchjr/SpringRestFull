package curso.rest.full.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.rest.full.DTO.UsuarioDTO;
import curso.rest.full.model.Usuario;
import curso.rest.full.model.UsuarioChart;
import curso.rest.full.model.UsuarioReport;
import curso.rest.full.repository.TelefoneRepository;
import curso.rest.full.repository.UsuarioRepository;
import curso.rest.full.service.ImplementacaoUserDetailsService;
import curso.rest.full.service.ServiceRelatorio;
import curso.rest.full.service.ServicoGrafico;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;

	@Autowired
	private ServiceRelatorio serviceRelatorio;
	
	@Autowired
	 private ServicoGrafico servicoGrafico;  

	@GetMapping(value = "/", produces = "application/json")
	// se tiver auulizações ele vai limpar, remover tudo que estiver parado
	@CacheEvict(value = "cacheusuarios", allEntries = true)
	// indentifica se tiver atualizacoes e trazer no junto com cache || se tiver
	// usuarios novos no banco e traz no cache esses usuarios
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> buscartodos() throws InterruptedException {

		PageRequest page = PageRequest.of(0, 5, Sort.by("nome"));

		Page<Usuario> usuarios = usuarioRepository.findAll(page);

		Thread.sleep(1000); // segurando o codigo por 1 segundo, segurando o processo lento

		return new ResponseEntity<Page<Usuario>>(usuarios, HttpStatus.OK);

	}

	@GetMapping(value = "/page/{pagina}", produces = "application/json")
	// se tiver auulizações ele vai limpar, remover tudo que estiver parado
	@CacheEvict(value = "cacheusuarios", allEntries = true)
	// indentifica se tiver atualizacoes e trazer no junto com cache || se tiver
	// usuarios novos no banco e traz no cache esses usuarios
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> buscartodosPaginado(@PathVariable("pagina") int pagina)
			throws InterruptedException {

		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));

		Page<Usuario> usuarios = usuarioRepository.findAll(page);

		// Thread.sleep(1000); // segurando o codigo por 1 segundo, segurando o processo
		// lento

		return new ResponseEntity<Page<Usuario>>(usuarios, HttpStatus.OK);

	}

	@GetMapping(value = "/usuariopornome/{nome}", produces = "application/json")
	// se tiver auulizações ele vai limpar, remover tudo que estiver parado
	@CacheEvict(value = "cacheusuarios", allEntries = true)
	// indentifica se tiver atualizacoes e trazer no junto com cache || se tiver
	// usuarios novos no banco e traz no cache esses usuarios
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> buscarUsuarioPorNome(@PathVariable("nome") String nome)
			throws InterruptedException {

		PageRequest pageRequest = null;
		Page<Usuario> usuarios = null;

		if (nome == null || (nome != null && nome.isEmpty())) {
			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			usuarios = usuarioRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			usuarios = usuarioRepository.buscaUsuarioPorNome(nome, pageRequest);

		}

		Thread.sleep(1000);
		return new ResponseEntity<Page<Usuario>>(usuarios, HttpStatus.OK);

	}

	@GetMapping(value = "/usuariopornome/{nome}/page/{pagina}", produces = "application/json")
	public ResponseEntity<Page<Usuario>> buscarUsuarioPorNomePaginado(@PathVariable("nome") String nome,
			@PathVariable("pagina") int pagina) {
		PageRequest pageRequest = null;
		Page<Usuario> usuarios = null;

		if (nome == null || (nome != null && nome.isEmpty())) {
			pageRequest = PageRequest.of(pagina, 5, Sort.by("nome"));
			usuarios = usuarioRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(pagina, 5, Sort.by("nome"));
			usuarios = usuarioRepository.buscaUsuarioPorNome(nome, pageRequest);

		}

		return new ResponseEntity<Page<Usuario>>(usuarios, HttpStatus.OK);

	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity buscaUsuarioporId(@PathVariable("id") Long id) {

		Usuario usuario = usuarioRepository.findById(id).get();

		return new ResponseEntity(new UsuarioDTO(usuario), HttpStatus.OK);

	}

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity cadastraUsuario(@RequestBody Usuario usuario) {

		// verifica se há telefones na lista
		for (int posTel = 0; posTel < usuario.getTelefones().size(); posTel++) {

			// se tiver telefone na lista. será cadastro no usuário que esta sendo
			// registrado
			usuario.getTelefones().get(posTel).setUsuario(usuario);

		}

		String senhaCritografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCritografada);
		Usuario usuariosalvo = usuarioRepository.save(usuario);

		implementacaoUserDetailsService.insereAcessoPadrao(usuariosalvo.getId());

		return new ResponseEntity(usuariosalvo, HttpStatus.OK);

	}

	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity atualizaUsuario(@RequestBody Usuario usuario) {

		// verifica se há telefones na lista
		for (int posTel = 0; posTel < usuario.getTelefones().size(); posTel++) {

			// se tiver telefone na lista. será cadastro no usuário que esta sendo
			// registrado
			usuario.getTelefones().get(posTel).setUsuario(usuario);

		}

		Usuario usuarioTemporario = usuarioRepository.buscarUsuarioLogin(usuario.getLogin());

		/* Atualiza a senha */
		if (!usuarioTemporario.getSenha().equals(usuario.getSenha())) {
			String senhaCritografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhaCritografada);

		}

		Usuario usuarioAtualizado = usuarioRepository.save(usuario);

		return new ResponseEntity(usuarioAtualizado, HttpStatus.OK);

	}

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String excluiUsuario(@PathVariable("id") Long id) {

		usuarioRepository.deleteById(id);

		return "Usuário excluid com Sucesso";

	}

	@DeleteMapping(value = "/removertelefone/{id}", produces = "application/text")
	public String excluiTelefoneUsuario(@PathVariable("id") Long id) {
		telefoneRepository.deleteById(id);

		return "Telefone deletado com Sucesso";

	}

	/* End-point que retorna o relatorio de usuario */

	@GetMapping(value="/relatorio",  produces = "application/text")
	public ResponseEntity<String> downoloadRelatorio(HttpServletRequest request) throws Exception{
		byte[] pdf = serviceRelatorio.gerarRelatorio("relatori_usuario",new HashMap<>(), request.getServletContext());
		
		String base64Pdf ="data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		System.out.println(base64Pdf);
		
				return new ResponseEntity<String>(base64Pdf, HttpStatus.OK); 
	}
	
	@PostMapping(value="/relatorio/",  produces = "application/text")
	public ResponseEntity<String> downoloadRelatorioParam(HttpServletRequest request, @RequestBody UsuarioReport usuarioReport) throws Exception{
		
		
		SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dateFormatParam = new SimpleDateFormat("yyyy-MM-dd");
		
		// pega a data da tela(12/06/1988 e converte em uma data padrao para ser pesquisada no banco  (1990-06-12) 
		String dataInicio = dateFormatParam.format(dateFormat.parse(usuarioReport.getDataInicio()));
		
		String dataFinal = dateFormatParam.format(dateFormat.parse(usuarioReport.getDataFim()));
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("DATA_INICIO", dataInicio);
		params.put("DATA_FIM", dataFinal);
		
		
		
		byte[] pdf = serviceRelatorio.gerarRelatorio("relatori_usuario2-param", params, request.getServletContext());
		
		String base64Pdf ="data:application/pdf;base64," + Base64.encodeBase64String(pdf);
		System.out.println(base64Pdf);
		
				return new ResponseEntity<String>(base64Pdf, HttpStatus.OK); 
	}
	
	
	@GetMapping(value = "/grafico", produces = "application/json")
	public ResponseEntity<UsuarioChart>usuarioGrafico(){
		UsuarioChart usuarioChart = new UsuarioChart();
		
		List<String> resultadoListaGrafico = servicoGrafico.listaGraficoRel();

	if (!resultadoListaGrafico.isEmpty()) {
		String nomes = resultadoListaGrafico.get(0).replaceAll("\\{", " ").replaceAll("\\}", ""); // removendo { }
		String salario = resultadoListaGrafico.get(1).replaceAll("\\{", " ").replaceAll("\\}", ""); // removendo { }
		
		usuarioChart.setNome(nomes);
		usuarioChart.setSalario(salario);
	}
		
		return new ResponseEntity<UsuarioChart>(usuarioChart, HttpStatus.OK);
		
	}
	
	

}
