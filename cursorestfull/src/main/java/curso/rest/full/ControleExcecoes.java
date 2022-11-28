package curso.rest.full;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {

	/*Interceptar erros comuns do projeto*/
	@ExceptionHandler({ Exception.class, RuntimeException.class, Throwable.class })
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String msg = "";
		if (ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> listErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
			for (ObjectError objectError : listErrors) {
				msg += objectError.getDefaultMessage() + "\n";
			}

		} else {
			msg = ex.getMessage();
		}
		ObjectorError error = new ObjectorError();
		error.setError(msg);
		error.setCode(status.value() + "====> " + status.getReasonPhrase());

		return new ResponseEntity<>(error, headers, status);
	}

	/* Tratamento da maioria dos erros a nivel de banco de dados */
	@ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class,
			SQLIntegrityConstraintViolationException.class, PSQLException.class, SQLException.class })
	protected ResponseEntity<Object> HandleExcpetionDataIntegry(Exception ex) {

		String msg = "";
		
		if(ex instanceof DataIntegrityViolationException) {
			msg = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
		}
		else if(ex instanceof ConstraintViolationException) {
			msg = ((ConstraintViolationException) ex).getCause().getCause().getMessage();
		}
		
		else if(ex instanceof SQLIntegrityConstraintViolationException) {
			msg = ((SQLIntegrityConstraintViolationException) ex).getCause().getCause().getMessage();
		}
		
		else if( ex instanceof PSQLException) {
			msg = ((PSQLException) ex).getCause().getCause().getMessage();
		}
		
		else if( ex instanceof  SQLException ) {
			msg = ((PSQLException) ex).getCause().getCause().getMessage();
		}else {
			msg = ex.getMessage();
		}
		
		ObjectorError objectorError = new ObjectorError();
		objectorError.setError(msg);
		objectorError.setCode(
				HttpStatus.INTERNAL_SERVER_ERROR + " ===> " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()); // erro do servidor
		

		return new ResponseEntity<>(objectorError, HttpStatus.INTERNAL_SERVER_ERROR);	
	}

}
