package com.ai.demo.controller;

import com.ai.demo.advisor.SimpleChatMemoryAdvisor;
import com.ai.demo.advisor.SimpleChatMemoryContextAdvisor;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memory")
@AllArgsConstructor
public class ChatMemoryController {

    private final ChatClient chatClient;

    // 创建一个自动上下文Advisor,MessageWindowChatMemory使用内存记忆
    static MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(100).build();
    static MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();


    @GetMapping("/chat")
    public String chat(@RequestParam(name = "message") String  message) {
        return chatClient.prompt()
                .system("你是聊天助手")
                .user(message)
                .advisors(new SimpleChatMemoryAdvisor()) // 添加内存 Advisor,只有该会话内的会话记录才会被记录到内存中
                .call()
                .content();
    }

    @GetMapping("/chat/context")
    public String chatWithContext(@RequestParam(name = "message") String  message, @RequestParam(name = "conversationId") String  conversationId) {
        // 将会话ID为添加到上下文中
        return chatClient.prompt()
                .system("你是聊天助手")
                .user(message)
                .advisors(advisorSpec -> {
                    advisorSpec.advisors(new SimpleChatMemoryContextAdvisor());
                    advisorSpec.param("conversationId", conversationId);
                })
                .call()
                .content();
    }


    @GetMapping("/chat/auto/context")
    public String chatWithAutoContext(@RequestParam(name = "message") String  message,
                                      @RequestParam(name = "conversationId") String  conversationId) {

        // 添加自动上下文
        return chatClient.prompt()
                .system("你是聊天助手")
                .user(message)
                .advisors(advisorSpec -> {
                    advisorSpec.advisors(chatMemoryAdvisor);
                    advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId);
                })
                .call()
                .content();
    }
}
