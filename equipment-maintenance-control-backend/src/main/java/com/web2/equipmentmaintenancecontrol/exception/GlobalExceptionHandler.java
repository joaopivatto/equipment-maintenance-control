package com.web2.equipmentmaintenancecontrol.exception;

import com.web2.equipmentmaintenancecontrol.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Tratamento centralizado de exceções da API. Todas as respostas de erro seguem o formato padrão
 * {@link ErrorResponse}, com timestamp, status, código, mensagem e caminho da requisição.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /** Erros de negócio conhecidos: usa o status e o código definidos no {@link ErrorCode}. */
  @ExceptionHandler(AppException.class)
  public ResponseEntity<ErrorResponse> handleAppException(
      AppException exception, HttpServletRequest request) {
    ErrorCode errorCode = exception.getErrorCode();
    return ResponseEntity.status(errorCode.getStatus())
        .body(
            buildBody(
                errorCode.getStatus().value(),
                errorCode.name(),
                exception.getMessage(),
                request.getRequestURI()));
  }

  /** Erros inesperados: retorna 500 com mensagem genérica, sem expor detalhes internos. */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpected(
      Exception exception, HttpServletRequest request) {
    ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
    return ResponseEntity.status(errorCode.getStatus())
        .body(
            buildBody(
                errorCode.getStatus().value(),
                errorCode.name(),
                errorCode.getMessage(),
                request.getRequestURI()));
  }

  /** Falhas de Bean Validation: concatena os erros de cada campo em uma única mensagem. */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
    String detail =
        exception.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .collect(Collectors.joining("; "));
    return ResponseEntity.status(errorCode.getStatus())
        .body(
            buildBody(
                errorCode.getStatus().value(),
                errorCode.name(),
                detail.isBlank() ? errorCode.getMessage() : detail,
                resolvePath(request)));
  }

  /** Padroniza as demais exceções tratadas pelo Spring MVC no formato {@link ErrorResponse}. */
  @Override
  protected ResponseEntity<Object> createResponseEntity(
      Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
    HttpStatus resolved = HttpStatus.resolve(statusCode.value());
    String code = resolved != null ? resolved.name() : String.valueOf(statusCode.value());
    String message = resolved != null ? resolved.getReasonPhrase() : code;
    return ResponseEntity.status(statusCode)
        .headers(headers)
        .body(buildBody(statusCode.value(), code, message, resolvePath(request)));
  }

  /** Monta o corpo de erro padrão com o instante atual. */
  private ErrorResponse buildBody(int status, String code, String message, String path) {
    return new ErrorResponse(Instant.now(), status, code, message, path);
  }

  /** Obtém a URI da requisição a partir do {@link WebRequest}. */
  private String resolvePath(WebRequest request) {
    if (request instanceof ServletWebRequest servletWebRequest) {
      return servletWebRequest.getRequest().getRequestURI();
    }
    return request.getDescription(false);
  }
}
