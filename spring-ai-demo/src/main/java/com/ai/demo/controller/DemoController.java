package com.ai.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
public class DemoController {

    private final ChatModel chatModel;

    @GetMapping("/hello")
    public String hello() {
        return chatModel.call("你好，你是谁");
    }

    @GetMapping(value = "/flux")
    public Flux<String> flux() {
        return chatModel.stream("你是什么模型");
    }
}
