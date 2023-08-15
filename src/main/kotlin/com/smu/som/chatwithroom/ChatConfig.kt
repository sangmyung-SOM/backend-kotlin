package com.smu.som.chatwithroom

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class ChatConfig : WebSocketMessageBrokerConfigurer{
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        //registry.addEndpoint("/ws/chat").withSockJS()
        registry.addEndpoint("/ws")
			.setAllowedOrigins("*");
			//.withSockJS();
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/queue", "/topic")
        registry.setApplicationDestinationPrefixes("/app")
    }
}
