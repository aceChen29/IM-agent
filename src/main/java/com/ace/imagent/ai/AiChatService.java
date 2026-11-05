package com.ace.imagent.ai;

import com.ace.imagent.config.McpToolConfig;
import com.ace.imagent.tool.TimeTool;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@Slf4j
public class AiChatService {

    @Resource
    private ChatModel chatModel;
    @Resource
    private McpToolProvider mcpToolProvider;
    @Resource
    private ChatMemoryStore chatMemoryStore;
    @Bean
    //生成AiChat接口的代理对象 AiServices.builder(AiChat.class)
    public AiChat aiChat() {
        return AiServices.builder(AiChat.class)
                .chatModel(chatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .chatMemoryStore(chatMemoryStore)
                        .maxMessages(20)
                        .build())
                .tools(new TimeTool())
                .toolProvider(mcpToolProvider)
                .beforeToolExecution(event ->
                        log.info("调用工具: {}", event.request().name()))
                .afterToolExecution(event ->
                        log.info("工具结束: {}, failed={}, duration={}",
                                event.request().name(), event.hasFailed(), event.duration()))
                .build();
    }

}