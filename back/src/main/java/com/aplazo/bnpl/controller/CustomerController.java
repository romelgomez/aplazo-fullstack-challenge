package com.aplazo.bnpl.controller;

import com.aplazo.bnpl.model.dto.CustomerRequest;
import com.aplazo.bnpl.model.dto.CustomerResponse;
import com.aplazo.bnpl.security.JwtService;
import com.aplazo.bnpl.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;
  private final JwtService jwtService;

  @PostMapping
  public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
    CustomerResponse customer = customerService.createCustomer(request);
    String token = jwtService.generateToken(customer.getId());

    return ResponseEntity
        .created(URI.create("/customers/" + customer.getId()))
        .header("X-Auth-Token", token)
        .body(customer);
  }

  @GetMapping("/{customerId}")
  public ResponseEntity<CustomerResponse> getCustomer(@PathVariable UUID customerId) {
    return ResponseEntity.ok(customerService.getCustomer(customerId));
  }
}