package com.ace.imagent.config;

import java.util.Arrays;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;


import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpToolConfig {

    @Value("${bigmodel.api-key}")
    private String apiKey;

    @Bean
    public McpToolProvider mcpToolProvider() {
        //网络连接
        McpTransport searchTransport = StreamableHttpMcpTransport.builder()
                .url("https://open.bigmodel.cn/api/mcp-broker/proxy/web-search/mcp"
                        + "?Authorization=" + apiKey.trim())
                .build();

        McpClient searchClient = new DefaultMcpClient.Builder()
                .key("BigModelSearchMcpClient")
                .transport(searchTransport)
                .build();


        return McpToolProvider.builder()
                .mcpClients(searchClient)
                .build();
    }
}