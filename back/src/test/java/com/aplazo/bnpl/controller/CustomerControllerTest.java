package com.aplazo.bnpl.controller;

import com.aplazo.bnpl.model.dto.CustomerRequest;
import com.aplazo.bnpl.model.dto.CustomerResponse;
import com.aplazo.bnpl.security.JwtService;
import com.aplazo.bnpl.service.CustomerService;
import com.aplazo.bnpl.config.JacksonConfig;
import com.aplazo.bnpl.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Import({ TestSecurityConfig.class, JacksonConfig.class })
class CustomerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CustomerService customerService;

  @MockitoBean
  private JwtService jwtService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createCustomer_ValidRequest_ReturnsCreated() throws Exception {
    // Arrange
    CustomerRequest request = new CustomerRequest();
    request.setFirstName("John");
    request.setLastName("Doe");
    request.setSecondLastName("Smith");
    request.setDateOfBirth("1990-01-01");

    UUID customerId = UUID.randomUUID();
    CustomerResponse response = new CustomerResponse();
    response.setId(customerId);
    response.setCreditLineAmount(new BigDecimal("1000.00"));
    response.setAvailableCreditLineAmount(new BigDecimal("1000.00"));
    response.setCreatedAt(LocalDateTime.now());

    when(customerService.createCustomer(any())).thenReturn(response);
    when(jwtService.generateToken(any(UUID.class))).thenReturn("test-token");

    // Act & Assert
    mockMvc.perform(post("/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("X-Auth-Token"))
        .andExpect(jsonPath("$.id").value(customerId.toString()))
        .andExpect(jsonPath("$.creditLineAmount").value("1000.00"))
        .andExpect(jsonPath("$.availableCreditLineAmount").value("1000.00"));
  }

  @Test
  @WithMockUser
  void getCustomer_ExistingId_ReturnsCustomer() throws Exception {
    // Arrange
    UUID customerId = UUID.randomUUID();
    CustomerResponse response = new CustomerResponse();
    response.setId(customerId);
    response.setCreditLineAmount(new BigDecimal("1000.00").setScale(2));
    response.setAvailableCreditLineAmount(new BigDecimal("1000.00").setScale(2));
    response.setCreatedAt(LocalDateTime.now());

    when(customerService.getCustomer(customerId)).thenReturn(response);
    when(jwtService.isTokenValid(any())).thenReturn(true);

    // Act & Assert
    mockMvc.perform(get("/customers/{id}", customerId)
        .header("Authorization", "Bearer test-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(customerId.toString()))
        .andExpect(jsonPath("$.creditLineAmount").value("1000.00"))
        .andExpect(jsonPath("$.availableCreditLineAmount").value("1000.00"));
  }

  @Test
  void createCustomer_InvalidRequest_ReturnsBadRequest() throws Exception {
    // Arrange
    CustomerRequest request = new CustomerRequest();
    // Missing required fields

    // Act & Assert
    mockMvc.perform(post("/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}