package com.ai.demo.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;


@Slf4j
public class LoggingCallAdvisor implements CallAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        log.info("call request message:{}", chatClientRequest.prompt().getInstructions());
        ChatClientResponse response = callAdvisorChain.nextCall(chatClientRequest);
        log.info("call response message:{}", response.chatResponse().getResult());
        return response;
    }

    @Override
    public String getName() {
        return "LoggingCallAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
