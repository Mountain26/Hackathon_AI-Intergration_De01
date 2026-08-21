package org.example.hackathon_de01.dto;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderResponse(Long orderId, String customerPhone, BigDecimal totalAmount, String status, List<String> itemSummaries) {
}
