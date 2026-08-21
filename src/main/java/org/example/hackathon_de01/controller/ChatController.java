package org.example.hackathon_de01.controller;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_de01.dto.ChatRequest;
import org.example.hackathon_de01.dto.ChatResponse;
import org.example.hackathon_de01.tools.StoreTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;
    private final StoreTools storeTools;

    @PostMapping
    public ChatResponse chat(@RequestParam String sessionId, @RequestParam String message) {
        ChatRequest request = new ChatRequest(sessionId, message);
        String answer = chatClient.prompt()
                .user(request.message())
                .advisors(a -> a.param("chat_memory_conversation_id", request.sessionId()))
                .tools(storeTools)
                .call()
                .content();
        return new ChatResponse(request.sessionId(), answer);
    }
}
