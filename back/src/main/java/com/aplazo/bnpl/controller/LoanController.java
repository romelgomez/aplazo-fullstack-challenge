package com.aplazo.bnpl.controller;

import com.aplazo.bnpl.model.dto.LoanRequest;
import com.aplazo.bnpl.model.dto.LoanResponse;
import com.aplazo.bnpl.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {
    
    private final LoanService loanService;
    
    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanRequest request) {
        LoanResponse loan = loanService.createLoan(request);
        return ResponseEntity
            .created(URI.create("/loans/" + loan.getId()))
            .body(loan);
    }
    
    @GetMapping("/{loanId}")
    public ResponseEntity<LoanResponse> getLoan(@PathVariable UUID loanId) {
        return ResponseEntity.ok(loanService.getLoan(loanId));
    }
}