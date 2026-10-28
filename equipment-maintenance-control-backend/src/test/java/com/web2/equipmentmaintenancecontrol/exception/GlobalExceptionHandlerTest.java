package com.web2.equipmentmaintenancecontrol.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.web2.equipmentmaintenancecontrol.exception.dto.ErrorResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;

@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  @DisplayName("traduz AppException para o status e o código do ErrorCode")
  void handlesAppException() {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/equipment-type/99");

    ResponseEntity<ErrorResponse> response =
        handler.handleAppException(new AppException(ErrorCode.EQUIPMENT_TYPE_NOT_FOUND), request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().status()).isEqualTo(404);
    assertThat(response.getBody().code()).isEqualTo("EQUIPMENT_TYPE_NOT_FOUND");
    assertThat(response.getBody().message()).isEqualTo("Equipment type not found");
    assertThat(response.getBody().path()).isEqualTo("/equipment-type/99");
    assertThat(response.getBody().timestamp()).isNotNull();
  }

  @Test
  @DisplayName("usa a mensagem detalhada da AppException quando informada")
  void usesDetailMessage() {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/customers/99");

    ResponseEntity<ErrorResponse> response =
        handler.handleAppException(
            new AppException(ErrorCode.CUSTOMER_NOT_FOUND, "Cliente não encontrado com ID: 99"),
            request);

    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().message()).isEqualTo("Cliente não encontrado com ID: 99");
  }

  @Test
  @DisplayName("converte exceção inesperada em 500 INTERNAL_ERROR sem vazar detalhes")
  void handlesUnexpectedException() {
    MockHttpServletRequest request = new MockHttpServletRequest("POST", "/auth/login");

    ResponseEntity<ErrorResponse> response =
        handler.handleUnexpected(
            new IllegalStateException("conexão com o banco falhou: senha=segredo"), request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().code()).isEqualTo("INTERNAL_ERROR");
    assertThat(response.getBody().message()).isEqualTo("Unexpected internal error");
    assertThat(response.getBody().message()).doesNotContain("segredo");
  }

  @Test
  @DisplayName("agrega as mensagens de validação dos campos em VALIDATION_ERROR")
  void handlesValidationErrors() throws Exception {
    BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "createBudget");
    bindingResult.rejectValue(null, "NotNull", "O valor do orçamento é obrigatório");
    bindingResult.addError(
        new org.springframework.validation.FieldError(
            "createBudget", "employeeId", "O ID do funcionário é obrigatório"));

    MethodArgumentNotValidException exception =
        new MethodArgumentNotValidException(methodParameter(), bindingResult);
    ServletWebRequest webRequest =
        new ServletWebRequest(new MockHttpServletRequest("POST", "/maintenance-request/1/budget"));

    ResponseEntity<Object> response =
        handler.handleMethodArgumentNotValid(
            exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    ErrorResponse body = (ErrorResponse) response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.code()).isEqualTo("VALIDATION_ERROR");
    assertThat(body.message()).contains("employeeId: O ID do funcionário é obrigatório");
    assertThat(body.path()).isEqualTo("/maintenance-request/1/budget");
  }

  @Test
  @DisplayName("cai na mensagem padrão quando não há erro de campo descrito")
  void fallsBackToDefaultValidationMessage() throws Exception {
    BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "createBudget");
    MethodArgumentNotValidException exception =
        new MethodArgumentNotValidException(methodParameter(), bindingResult);
    ServletWebRequest webRequest =
        new ServletWebRequest(new MockHttpServletRequest("POST", "/maintenance-request"));

    ResponseEntity<Object> response =
        handler.handleMethodArgumentNotValid(
            exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

    ErrorResponse body = (ErrorResponse) response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.message()).isEqualTo("Validation error");
  }

  @Test
  @DisplayName("padroniza o corpo também para erros resolvidos pelo Spring")
  void standardizesSpringResolvedErrors() {
    ServletWebRequest webRequest =
        new ServletWebRequest(new MockHttpServletRequest("PATCH", "/employees/1"));

    ResponseEntity<Object> response =
        handler.createResponseEntity(
            null, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED, webRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    ErrorResponse body = (ErrorResponse) response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.status()).isEqualTo(405);
    assertThat(body.code()).isEqualTo("METHOD_NOT_ALLOWED");
    assertThat(body.message()).isEqualTo("Method Not Allowed");
    assertThat(body.path()).isEqualTo("/employees/1");
  }

  private MethodParameter methodParameter() throws NoSuchMethodException {
    return new MethodParameter(
        GlobalExceptionHandlerTest.class.getDeclaredMethod("dummyEndpoint", List.class), 0);
  }

  @SuppressWarnings("unused")
  private void dummyEndpoint(List<String> body) {}
}
