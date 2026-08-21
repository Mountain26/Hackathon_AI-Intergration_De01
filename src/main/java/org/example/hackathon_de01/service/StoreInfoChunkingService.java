package org.example.hackathon_de01.service;

import org.springframework.ai.document.Document;

import java.util.ArrayList;
import java.util.List;

public class StoreInfoChunkingService {

    public List<Document> chunkDocuments(List<Document> documents) {
        List<Document> result = new ArrayList<>();
        for (Document document : documents) {
            String content = document.getText();
            if (content == null || content.isBlank()) {
                continue;
            }
            String[] paragraphs = content.split("\\r?\\n\\s*\\r?\\n");
            for (String paragraph : paragraphs) {
                String trimmed = paragraph.trim();
                if (!trimmed.isBlank()) {
                    result.add(new Document(trimmed, document.getMetadata()));
                }
            }
        }
        return result;
    }
}
