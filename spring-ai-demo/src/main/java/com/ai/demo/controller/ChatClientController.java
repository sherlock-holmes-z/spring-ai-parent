package com.ai.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
public class ChatClientController {

    private final ChatClient chatClient;

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "message") String message) {
        ZhiPuAiChatOptions chatOptions = ZhiPuAiChatOptions.builder()
                .model("glm-4.5")
                .maxTokens(1024)
                .temperature(0.2)
                .build();
        String content = chatClient.prompt()
                .user(message)
                .options(chatOptions)
                .call().content();
        return StringUtils.hasText(content) ? content : "答案过长或生成失败，请重试";
    }
}
