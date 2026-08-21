package org.example.hackathon_de01.dto;

import java.math.BigDecimal;

public record ProductLookupResponse(Long id, String name, BigDecimal price, Integer stock, String category) {
}
