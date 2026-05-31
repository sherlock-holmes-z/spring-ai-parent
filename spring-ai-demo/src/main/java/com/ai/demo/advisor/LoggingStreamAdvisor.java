package com.ai.demo.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

@Slf4j
public class LoggingStreamAdvisor implements StreamAdvisor {
    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        log.info("stream request message:{}", chatClientRequest.prompt().getInstructions());
        Flux<ChatClientResponse> response = streamAdvisorChain.nextStream(chatClientRequest);
        log.info("stream response message:{}", response);
        return response;
    }

    @Override
    public String getName() {
        return "LoggingStreamAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
