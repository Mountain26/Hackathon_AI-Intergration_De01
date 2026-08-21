package org.example.hackathon_de01.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(12)
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                .defaultSystem("""
                        Bạn là trợ lý bán hàng cho cửa hàng.
                        Nếu câu hỏi liên quan sản phẩm, gọi tool phù hợp để trả về thông tin thực tế.
                        Nếu câu hỏi về cửa hàng, gọi tool getStoreInfo.
                        Nếu người dùng yêu cầu đặt hàng, gọi tool createOrder và chỉ trả về kết quả đơn hàng.
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
