package com.mvp.backend.infrastructure.repository;

import com.mvp.backend.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByCheckoutId(String checkoutId);
}
