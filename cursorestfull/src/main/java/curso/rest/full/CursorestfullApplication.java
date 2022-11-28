package curso.rest.full;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = {"curso.rest.full.model"}) // configuração para todas as classes de modelos
@ComponentScan(basePackages = {"curso.*"})// scaneia todos os componenten do prjeto
@EnableJpaRepositories(basePackages = {"curso.rest.full.repository"}) // libera o jpa para o repositorys
@EnableTransactionManagement // liberando as transações do projeto
@EnableWebMvc
@RestController
@EnableAutoConfiguration
@EnableCaching
public class CursorestfullApplication implements WebMvcConfigurer { /// configurando globalmente o CROOSORIGIN

	public static void main(String[] args) {
		SpringApplication.run(CursorestfullApplication.class, args);
	
		// senha System.out.println(new BCryptPasswordEncoder().encode("admin"));
	}
	
	@Override
		public void addCorsMappings(CorsRegistry registry) { // mapea na arte de segurança
			// liberando o mapeamento de usuario para todas as origins 
			registry.addMapping("/usuario/**")
			.allowedMethods("*")
			.allowedOrigins("*");
			
			
			registry.addMapping("/profissao/**")
			.allowedMethods("*")
			.allowedOrigins("*");
			
			// liberação de cors da url
			registry.addMapping("/recuperar/**")
			.allowedMethods("*")
			.allowedOrigins("*");
		}

}
