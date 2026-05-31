package com.ai.demo.config;

import com.ai.demo.advisor.LoggingAdvisor;
import com.ai.demo.advisor.LoggingCallAdvisor;
import com.ai.demo.advisor.LoggingStreamAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

//    @Bean
//    public ChatClient chatClientTest1(ChatClient.Builder builder) {
//        return builder.build();
//    }
//
//    @Bean
//    public ChatClient chatClientTest2(ChatModel model) {
//        return ChatClient.builder(model).build();
//    }

    @Bean
    public ChatClient chatClient(ZhiPuAiChatModel zhiPuAiChatModel) {
        return ChatClient.builder(zhiPuAiChatModel)
                .defaultAdvisors(new LoggingAdvisor(), new LoggingStreamAdvisor(), new LoggingCallAdvisor())
                .build();
    }


}
