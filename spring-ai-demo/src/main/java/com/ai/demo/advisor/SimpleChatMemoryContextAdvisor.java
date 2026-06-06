package com.ai.demo.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SimpleChatMemoryContextAdvisor implements BaseAdvisor {
    private static final Map<String, List<Message>> chatMemoryMap = new HashMap<>();

    private static final String CONVERSATION_ID = "conversationId";

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {

        String chatId = chatClientRequest.context().get(CONVERSATION_ID).toString();
        List<Message> messages = chatMemoryMap.get(chatId);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        // 把这次请求的message添加到历史会话中
        List<Message> currentMessages = chatClientRequest.prompt().getInstructions();
        messages.addAll(currentMessages);
        chatMemoryMap.put(chatId, messages);

        // 创建新的request，将所有消息添加到新prompt中
        Prompt oldPrompt = chatClientRequest.prompt();
        Prompt newPrompt = oldPrompt.mutate().messages(messages).build();
        chatClientRequest = chatClientRequest.mutate().prompt(newPrompt).build();
        return chatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        if (chatClientResponse.chatResponse() == null) {
            return chatClientResponse;
        }

        String chatId = chatClientResponse.context().get(CONVERSATION_ID).toString();
        List<Message> hisMessages = chatMemoryMap.get(chatId);
        AssistantMessage assistantMessage = chatClientResponse.chatResponse().getResult().getOutput();
        hisMessages.add(assistantMessage);

        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
