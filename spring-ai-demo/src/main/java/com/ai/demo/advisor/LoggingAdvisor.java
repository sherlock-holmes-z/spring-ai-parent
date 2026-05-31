package com.ai.demo.advisor;

import java.util.stream.Collectors;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.content.Content;

/**
 * 日志 Advisor：记录每次 AI 调用的请求内容和响应 token 用量
 */
@Slf4j
public class LoggingAdvisor implements BaseAdvisor {

    @Override
    public String getName() {
        return "LoggingAdvisor";
    }

    @Override
    public ChatClientRequest before(ChatClientRequest request, AdvisorChain chain) {
        String userText = request.prompt().getInstructions().stream()
                .filter(m -> m.getMessageType() == MessageType.USER)
                .map(Content::getText)
                .collect(Collectors.joining(", "));
        log.info("[AI 请求] 用户消息: {}", userText);
        return request;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse response, AdvisorChain chain) {
        var chatResponse = response.chatResponse();
        if (chatResponse != null && chatResponse.getMetadata() != null) {
            var usage = chatResponse.getMetadata().getUsage();
            log.info("[AI 响应] token 用量 - input: {}, output: {}, total: {}",
                    usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
        }
        return response;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
