package com.ace.imagent.controller;


import com.ace.imagent.ai.AiChat;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AiChatController {

    @Resource
    private AiChat aiChat;

    @GetMapping("/chat")
    public String chat(String sessionId,String prompt) {

        return aiChat.chat(sessionId, prompt);
    }
}