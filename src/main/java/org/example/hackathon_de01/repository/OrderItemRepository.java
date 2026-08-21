package org.example.hackathon_de01.repository;

import org.example.hackathon_de01.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
// OrderItemRepository
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
