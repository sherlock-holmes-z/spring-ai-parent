package com.ai.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.*;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class ChatModelController {

    private final ChatModel chatModel;

    private final ZhiPuAiChatModel zhiPuAiChatModel;

    @GetMapping("/hello")
    public String hello() {
        return chatModel.call("你好，你是谁");
    }

    @GetMapping(value = "/flux")
    public Flux<String> flux() {
        return chatModel.stream("你是什么模型");
    }

    @GetMapping(value = "/message")
    public Flux<String> message(@RequestParam(name = "message") String message) {
        SystemMessage systemMessage = new SystemMessage("你是金融客服");
        UserMessage userMessage = new UserMessage(message);
        return chatModel.stream(systemMessage, userMessage);
    }

    @GetMapping(value = "/prompt")
    public ChatResponse prompt(@RequestParam(name = "message") String message) {
        UserMessage userMessage = new UserMessage(message);
        DefaultChatOptions chatOptions = new DefaultChatOptions();
        chatOptions.setTemperature(0.0);
        chatOptions.setMaxTokens(1024);
        chatOptions.setModel("glm-5.1");
        Prompt prompt = new Prompt(userMessage, chatOptions);
        return chatModel.call(prompt);
    }

    @GetMapping(value = "/prompt/sse", produces = "text/event-stream")
    public SseEmitter promptSse(@RequestParam(name = "message") String message) {
        UserMessage userMessage = new UserMessage(message);
        DefaultChatOptions chatOptions = new DefaultChatOptions();
        chatOptions.setTemperature(0.0);
        chatOptions.setMaxTokens(1024);
        chatOptions.setModel("glm-5.1");
        Prompt prompt = new Prompt(userMessage, chatOptions);
        Flux<ChatResponse> responseFlux = chatModel.stream(prompt);

        SseEmitter sseEmitter = new SseEmitter();
        responseFlux.subscribe(response -> {
                    String text = response.getResult().getOutput().getText();
                    try {
                        if (text != null) {
                            sseEmitter.send(SseEmitter.event().data(text));
                        }
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                },
                sseEmitter::completeWithError,
                sseEmitter::complete);
        return sseEmitter;
    }

    @GetMapping(value = "/prompt/zhipu")
    public String zhipuPrompt(@RequestParam(name = "message") String message) {
        UserMessage userMessage = new UserMessage(message);
        ZhiPuAiChatOptions chatOptions = ZhiPuAiChatOptions.builder()
                .model("glm-5.1")
                .maxTokens(1024)
                .temperature(0.2)
                .build();
        Prompt prompt = new Prompt(userMessage, chatOptions);
        return zhiPuAiChatModel.call(prompt).getResult().getOutput().getText();
    }
}
