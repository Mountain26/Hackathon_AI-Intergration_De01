package org.example.hackathon_de01.service;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_de01.dto.IngestResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RAGService {

    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public IngestResponse ingestStoreInfoIfNeeded() {
        Integer count = jdbcTemplate.queryForObject("select count(*) from vector_store", Integer.class);
        if (count != null && count > 0) {
            return new IngestResponse(false, count);
        }

        TikaDocumentReader reader = new TikaDocumentReader(new ClassPathResource("QuickMart_Store_Info.pdf"));
        List<Document> documents = reader.get();
        List<Document> chunks = new StoreInfoChunkingService().chunkDocuments(documents);
        vectorStore.add(chunks);
        Integer after = jdbcTemplate.queryForObject("select count(*) from vector_store", Integer.class);
        return new IngestResponse(true, after == null ? 0 : after);
    }
}
