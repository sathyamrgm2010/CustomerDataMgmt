package nl.rabobank.cdm.advice;

import static nl.rabobank.cdm.constants.CDMConstants.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.cdm.exception.CDMApiException;
import nl.rabobank.cdm.exception.CDMValidationException;
import nl.rabobank.cdm.dto.ErrorResponse;

@Slf4j
@ControllerAdvice
public class CustomerControllerAdvice {

	@ResponseBody
	@ExceptionHandler(CDMApiException.class)
	public ResponseEntity<ErrorResponse> handleCDMErrors(final CDMApiException cdmApiException){
		HttpStatus httpStatus = null;
		//Check if the validation fails,
		if (cdmApiException instanceof CDMValidationException) {
			httpStatus = HttpStatus.BAD_REQUEST;
		}
		else {
			httpStatus = HttpStatus.NOT_FOUND;
		}
		return ResponseEntity.status(httpStatus).body(new ErrorResponse(STATUS_FAILURE, cdmApiException.getMessage()));
	}
	
	@ResponseBody
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleOtherExceptions(final HttpRequestMethodNotSupportedException exception){
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse(STATUS_FAILURE, WRONG_HTTP_METHOD));
	}
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleOtherExceptions(final Exception exception){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(STATUS_FAILURE, EXCEPTION_IN_PROCESSING));
	}
}
