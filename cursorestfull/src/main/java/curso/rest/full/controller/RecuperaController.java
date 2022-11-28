package curso.rest.full.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import curso.rest.full.ObjectorError;
import curso.rest.full.model.Usuario;
import curso.rest.full.repository.UsuarioRepository;
import curso.rest.full.service.ServiceEnviaEmail;

@RestController
@RequestMapping(value = "/recuperar")
public class RecuperaController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ServiceEnviaEmail serviceEnviaEmail;
	
	@ResponseBody
	@PostMapping(value = "/")
	public ResponseEntity<ObjectorError>recuperar(@RequestBody Usuario usuario) throws Exception{
		
		ObjectorError objectorError = new ObjectorError();
		Usuario user = usuarioRepository.buscarUsuarioLogin(usuario.getLogin());
		
		if(user == null) {
			objectorError.setCode("404"); /*Não encontrado*/
			objectorError.setError("Usuário não encontrado");
		}else {
			
			/*Gerando senha nova-> Aletoriamente*/
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			/* Pegando a senha nova gerada */
			String senhaNova = dateFormat.format(Calendar.getInstance().getTime());
			
			
			/* aqui temos que passar a criptografia da nova senha */
			String senhaNovaCritografada = new BCryptPasswordEncoder().encode(senhaNova);
			
			/*Atualizando a senha no banco de dados */
			usuarioRepository.updateSenha(senhaNovaCritografada, user.getId());
			
			System.out.println("Começou o envio de email");
			serviceEnviaEmail.enviarEmail("Recuperação de senha", user.getLogin(), "Sua nova senha é : " + senhaNova);
			System.out.println("Fim o envio de email");
			/*Rotina de envio de email*/
			
			
			objectorError.setCode("200"); 
			objectorError.setError("Acesso enviado para seu email");
		}
		
		return new ResponseEntity<ObjectorError>(objectorError, HttpStatus.OK);
	}
	
}
