package org.example.hackathon_de01.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public record ChatRequest(String sessionId, String message) {}

    @PostMapping
    public String chat(@RequestBody ChatRequest request) {

        return chatClient.prompt()
                .user(request.message())
                .advisors(a -> a.param("chat_memory_conversation_id", request.sessionId()))
//                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .call()
                .content();
    }



}
