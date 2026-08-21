package org.example.hackathon_de01.dto;

import java.util.List;

public record CreateOrderRequest(String customerPhone, String customerName, String address, List<OrderItemRequest> items) {
}
