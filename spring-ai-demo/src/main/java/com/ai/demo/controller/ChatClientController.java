package com.ai.demo.controller;

import com.ai.demo.entity.Book;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
                .system("你是金融专家，只能回答金融问题")
                .user(message)
                .options(chatOptions)
                .call()
                .content();
        return StringUtils.hasText(content) ? content : "答案过长或生成失败，请重试";
    }

    /**
     * ChatResponse 包含输入token，输出token,总token
     * 元数据：
     * 模型信息
     * 结束标识：finishReason：stop正常结束, length（token超限）, TOOL_CALLS（工具调用），CONTENT_FILTER（被拦截）
     *
     */
    @GetMapping("/chatResponse")
    public ChatResponse chatResponse(@RequestParam(name = "message") String message) {
        ZhiPuAiChatOptions chatOptions = ZhiPuAiChatOptions.builder()
                .model("glm-4-flash")
                .maxTokens(1024)
                .temperature(0.2)
                .build();
        return chatClient.prompt()
                .system("你是金融专家，只能回答金融问题")
                .user(message)
                .options(chatOptions)
                .call()
                .chatResponse();
    }

    /**
     * 会将当前实体类的json信息添加到user message中，写了个输出格式提示词给大模型，模型会根据提示词生成json数据
     *
     * @return 响应数据转化为实体类
     */
    @GetMapping("/getEntity")
    public Book getEntity() {
        ZhiPuAiChatOptions chatOptions = ZhiPuAiChatOptions.builder()
                .model("glm-4-flash")
                .maxTokens(1024)
                .temperature(0.2)
                .build();
        Book entity = chatClient.prompt()
                .user("随机生产一本书的书名和作者,要求中文，作者名是个常规的中文名")
                .options(chatOptions)
                .call()
                .entity(Book.class);
        return entity;
    }

    @GetMapping("/flux")
    public Flux<String> flux() {
        ZhiPuAiChatOptions chatOptions = ZhiPuAiChatOptions.builder()
                .model("glm-4-flash")
                .maxTokens(1024)
                .temperature(0.2)
                .build();
        return chatClient.prompt()
                .user("请生成一个10个单词的英文句子")
                .options(chatOptions)
                .stream()
                .content();
    }
}
