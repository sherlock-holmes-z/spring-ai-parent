package com.ai.demo.controller;

import com.ai.demo.advisor.SimpleChatMemoryAdvisor;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memory")
@AllArgsConstructor
public class ChatMemoryController {

    private final ChatClient chatClient;


    @GetMapping("/chat")
    public String chat(@RequestParam(name = "message") String  message) {
        return chatClient.prompt()
                .system("你是聊天助手")
                .user(message)
                .advisors(new SimpleChatMemoryAdvisor()) // 添加内存 Advisor,只有该会话内的会话记录才会被记录到内存中
                .call()
                .content();
    }
}
