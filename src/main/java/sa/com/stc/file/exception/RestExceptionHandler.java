package sa.com.stc.file.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import sa.com.stc.file.dto.ApiErrorDto;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiErrorDto> handleAuthenticationException(AuthenticationException ex) {

		ApiErrorDto error = new ApiErrorDto(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex.getStackTrace());
		return new ResponseEntity<>(error, error.getStatus());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ApiErrorDto> handleNotFound(EntityNotFoundException ex) {
		ApiErrorDto error = new ApiErrorDto(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getStackTrace());
		return new ResponseEntity<>(error, error.getStatus());
	}

	@ExceptionHandler(BusinessValidationException.class)
	public ResponseEntity<ApiErrorDto> handleBusinessValidationException(BusinessValidationException ex) {
		ApiErrorDto error = new ApiErrorDto(HttpStatus.CONFLICT, ex.getMessage(), ex.getStackTrace());
		return new ResponseEntity<>(error, error.getStatus());
	}
	
	  @ExceptionHandler(GenericException.class)
	  public ResponseEntity<ApiErrorDto> handleGenericException(GenericException ex) {
	    ApiErrorDto error =
	        new ApiErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getStackTrace());
	    return new ResponseEntity<>(error, error.getStatus());
	  }
}
