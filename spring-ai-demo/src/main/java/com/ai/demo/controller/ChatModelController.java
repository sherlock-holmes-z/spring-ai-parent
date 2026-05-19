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


        /**
         * 处理用户消息并返回AI模型的流式响应
         * 该接口使用系统消息设定AI角色为金融客服，适用于金融服务场景的对话
         *
         * 真正 Reactive Stream，底层实现是Reactor，少量 EventLoop，可以处理并发请求，且性能更高
         * boot3使用
         *
         * @param message 用户输入的询问消息
         * @return Flux<String> 流式返回AI模型的响应内容
         */
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


        /**
         * 通过SSE（Server-Sent Events）方式流式返回AI模型的响应结果
         * 该接口支持实时推送AI回复内容，适用于需要流式展示的场景,与Flux接口相比，SSE接口更适合实时推送数据，且更简单易用。
         * 传统tomcat长连接，需要线程池，请求响应多时容易线程爆炸，boot2使用
         *
         * @param message 用户输入的询问消息
         * @return SseEmitter对象，用于向客户端持续发送流式数据
         */
        @GetMapping(value = "/prompt/sse", produces = "text/event-stream")
        public SseEmitter promptSse(@RequestParam(name = "message") String message) {
            UserMessage userMessage = new UserMessage(message);
            DefaultChatOptions chatOptions = new DefaultChatOptions();
            chatOptions.setTemperature(0.0);
            chatOptions.setMaxTokens(1024);
            chatOptions.setModel("glm-4-flash");
            Prompt prompt = new Prompt(userMessage, chatOptions);
            Flux<ChatResponse> responseFlux = chatModel.stream(prompt);

            SseEmitter sseEmitter = new SseEmitter();

            /*
             * 订阅流式响应，将AI模型的输出通过SSE发送到客户端
             * 包含完整的错误处理和连接管理
             */
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

            /*
             * 配置SSE连接的生命周期回调
             * 包括超时处理、正常结束和异常情况的处理逻辑
             */
            sseEmitter.onTimeout(sseEmitter::complete);
            sseEmitter.onCompletion(() -> System.out.println("SSE 连接正常结束"));
            sseEmitter.onError(sseEmitter::completeWithError);
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
