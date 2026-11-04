package com.web2.equipmentmaintenancecontrol.controller.maintenance;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.web2.equipmentmaintenancecontrol.config.TokenInterceptor;
import com.web2.equipmentmaintenancecontrol.domain.token.TokenClaims;
import com.web2.equipmentmaintenancecontrol.domain.token.TokenManager;
import com.web2.equipmentmaintenancecontrol.exception.GlobalExceptionHandler;
import com.web2.equipmentmaintenancecontrol.model.maintenance.dto.MaintenanceRequestDetails;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import com.web2.equipmentmaintenancecontrol.model.profile.dtos.CustomerResponse;
import com.web2.equipmentmaintenancecontrol.service.maintenance.MaintenanceRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("MaintenanceRequestController (acesso por perfil)")
class MaintenanceRequestControllerAccessTest {

  private static final String TOKEN = "Bearer token-valido";

  @Mock private MaintenanceRequestService service;

  private MockMvc mockMvc;
  private ProfileType authenticatedProfile;

  @BeforeEach
  void setUp() {
    TokenManager tokenManager =
        new TokenManager() {
          @Override
          public String generateToken(Integer userId, ProfileType profileType) {
            return TOKEN;
          }

          @Override
          public TokenClaims parseToken(String token) {
            return new TokenClaims(1, authenticatedProfile);
          }
        };

    mockMvc =
        MockMvcBuilders.standaloneSetup(new MaintenanceRequestController(service))
            .addInterceptors(new TokenInterceptor(tokenManager))
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Nested
  @DisplayName("fluxo exclusivo do cliente")
  class CustomerOnly {

    @ParameterizedTest(name = "funcionário não acessa {0} {1}")
    @CsvSource({
      "POST,/maintenance-request",
      "POST,/maintenance-request/1/approve",
      "POST,/maintenance-request/1/reject",
      "POST,/maintenance-request/1/pay"
    })
    @DisplayName("recusa token de funcionário com 403 ACCESS_DENIED")
    void deniesEmployee(String method, String path) throws Exception {
      authenticatedProfile = ProfileType.EMPLOYEE;

      mockMvc
          .perform(request(method, path))
          .andExpect(status().isForbidden())
          .andExpect(jsonPath("$.code").value("ACCESS_DENIED"));

      verifyNoInteractions(service);
    }
  }

  @Nested
  @DisplayName("fluxo exclusivo do funcionário")
  class EmployeeOnly {

    @ParameterizedTest(name = "cliente não acessa {0} {1}")
    @CsvSource({
      "GET,/maintenance-request",
      "GET,/maintenance-request/open",
      "POST,/maintenance-request/1/budget",
      "POST,/maintenance-request/1/maintenance",
      "POST,/maintenance-request/1/redirect",
      "POST,/maintenance-request/1/finish"
    })
    @DisplayName("recusa token de cliente com 403 ACCESS_DENIED")
    void deniesCustomer(String method, String path) throws Exception {
      authenticatedProfile = ProfileType.CUSTOMER;

      mockMvc
          .perform(request(method, path))
          .andExpect(status().isForbidden())
          .andExpect(jsonPath("$.code").value("ACCESS_DENIED"));

      verifyNoInteractions(service);
    }
  }

  @Nested
  @DisplayName("consulta compartilhada")
  class Shared {

    @ParameterizedTest(name = "{0} consulta a solicitação pelo id")
    @CsvSource({"CUSTOMER", "EMPLOYEE"})
    @DisplayName("GET /{id} atende os dois perfis")
    void allowsBothProfiles(ProfileType profileType) throws Exception {
      authenticatedProfile = profileType;
      when(service.findById(1)).thenReturn(details());

      mockMvc
          .perform(get("/maintenance-request/1").header("Authorization", TOKEN))
          .andExpect(status().isOk());
    }
  }

  private static MockHttpServletRequestBuilder request(String method, String path) {
    MockHttpServletRequestBuilder builder =
        "GET".equals(method) ? get(path) : post(path).contentType(MediaType.APPLICATION_JSON);

    return builder.header("Authorization", TOKEN).content("{}");
  }

  private static MaintenanceRequestDetails details() {
    CustomerResponse customer = new CustomerResponse(1, "Bruno", null, null, null, null);

    return new MaintenanceRequestDetails(
        1,
        null,
        null,
        null,
        "Não liga",
        null,
        null,
        null,
        customer,
        null,
        null,
        null,
        null,
        null,
        null);
  }
}
