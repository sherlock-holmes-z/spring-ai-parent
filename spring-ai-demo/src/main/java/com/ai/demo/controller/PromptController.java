package com.ai.demo.controller;

import com.ai.demo.entity.StreamMessage;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/PromptController")
@AllArgsConstructor
public class PromptController {
    private final ChatClient chatClient;

    static  SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(" 你是{country}的专业导游，不能推荐其他国家的景点");
    static PromptTemplate userPrompt = new PromptTemplate("用户选择了{city}，推荐{num}个景点");

    @PostMapping(value = "/prompt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StreamMessage> prompt(@RequestParam String country, @RequestParam String city, @RequestParam String num) {
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("country", country));
        Message message = userPrompt.createMessage(Map.of("city", city, "num", num));
        Prompt prompt = new Prompt(systemMessage, message);
        return chatClient.prompt(prompt).stream().content().map(StreamMessage::new);
    }
}
