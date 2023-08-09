package com.smu.som.chatwithroom

import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@NoArgsConstructor
class MessageController(
    val sendingOperations: SimpMessageSendingOperations
) {
    @MessageMapping("/chat/message")
    fun enter(chatMessage: ChatMessage){
        if (MessageType.ENTER.equals(chatMessage.type)){
            chatMessage.message = chatMessage.sender + "enter"
        }

        sendingOperations.convertAndSend("/topic/chat/room/"+chatMessage.roomId,chatMessage)
    }
}
