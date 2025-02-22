package com.aplazo.bnpl.repository;

import com.aplazo.bnpl.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}