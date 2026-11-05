package com.ace.imagent.config;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisChatMemoryStoreConfig {

    @Bean
    public ChatMemoryStore chatMemoryStore(
            StringRedisTemplate redis,
            @Value("${app.chat-memory.ttl-seconds:3600}") long ttlSeconds) {
        Duration ttl = Duration.ofSeconds(ttlSeconds);
        return new ChatMemoryStore() {
            private String key(Object memoryId) {
                return "im-agent:chat-memory:" + memoryId;
            }

            @Override
            public List<ChatMessage> getMessages(Object memoryId) {
                String json = redis.opsForValue().get(key(memoryId));
                return json == null ? List.of() : ChatMessageDeserializer.messagesFromJson(json);
            }

            @Override
            public void updateMessages(Object memoryId, List<ChatMessage> messages) {
                redis.opsForValue().set(key(memoryId), ChatMessageSerializer.messagesToJson(messages), ttl);
            }

            @Override
            public void deleteMessages(Object memoryId) {
                redis.delete(key(memoryId));
            }
        };
    }
}
