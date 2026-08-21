package org.example.hackathon_de01.controller;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_de01.dto.IngestResponse;
import org.example.hackathon_de01.service.RAGService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// Admin
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RAGService ragService;

    @PostMapping("/ingest-store-info")
    public IngestResponse ingestStoreInfo() {
        return ragService.ingestStoreInfoIfNeeded();
    }
}
