package curso.rest.full.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class ServiceEnviaEmail {

	
	private String userName = "paulocezarhenriquejunior1990@gmail.com";
	private String senha = "uccgknnkznjehavf";
	
	public void enviarEmail(String assunto,String emailDestino, String menssagem) throws Exception {
		Properties properties = new Properties(); // propriedades para enviar o email
		properties.put("mail.smtp.ssl.trust","*");
		properties.put("mail.smtp.auth", "true"); /*Login e senha do meu projeto / autorização*/
		properties.put("mail.smtp.starttls", "true"); /*Autenticacao*/
		properties.put("mail.smtp.host", "smtp.gmail.com"); /* Servidro do google*/
		properties.put("mail.smtp.port", "465"); /*porta padrao do gmail*/
		properties.put("mail.smtp.socketFactory.port", "465");/*Especifica a porta skcket*/
		properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory"); /*Classe de conexao socket*/
		
		Session session = Session.getInstance(properties, new Authenticator() {
			
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				/*Email com serviço para mandar, email de recuperação de senha para os usuarios do meu sistema*/
				return new PasswordAuthentication(userName, senha);
			}
		});
		// indica para que sem vai ser enviado
		Address[] toUser = InternetAddress.parse(emailDestino); // para quem vai ser enviado, no caso email do usuario para recuperação
		
		Message message = new MimeMessage(session); // Sessão de autenticacção é passada por meio de envio
		
		message.setFrom(new InternetAddress(userName)); /* Quem esta enviando - equipe do projeto - nos*/
		
		message.setRecipients(Message.RecipientType.TO, toUser);/* Para quem vai o email -> quem ira receber o email*/
		
		message.setSubject(assunto); /* Assunto do email*/
		
		message.setText(menssagem); /*Conteudo*/
		
		
		Transport.send(message);
	
	}
}
