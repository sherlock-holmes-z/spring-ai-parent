package com.ai.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.zhipuai.autoconfigure.ZhiPuAiChatAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DemoController {

    private final ChatModel chatModel;

    @GetMapping("/hello")
    public String hello() {
        return chatModel.call("你好，你是谁");
    }
}
