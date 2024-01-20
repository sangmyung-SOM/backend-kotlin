package com.smu.som.chatwithroom

import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.web.bind.annotation.RestController
import javax.websocket.OnMessage

@RestController
@RequiredArgsConstructor
@NoArgsConstructor
class MessageController(
    val sendingOperations: SimpMessageSendingOperations,
	var chatService: ChatService
) {
    @MessageMapping("/chat/message")
    fun enter(chatMessage: ChatMessage){
        if (MessageType.ENTER.equals(chatMessage.type)){
            chatMessage.message = chatMessage.sender + " 님이 입장했습니다."
        }
		else {
			// 게임 방으로 메시지 전송
			chatMessage.gameRoomMsg = chatMessage.sender + " : " + chatMessage.message
			chatService.findById(chatMessage.roomId!!)!!.chatList.add(chatMessage)
			sendingOperations.convertAndSend("/topic/game/chat/room/"+chatMessage.roomId,chatMessage)
		}
    }

	// 기존 코드로 테스트 중이라서 잠시 복구 합니다.
	// APIC 으로 테스트 진행
	// 연결 주소 : ws://localhost:8080/ws
	// 구독 주소 : /topic/public
	// 메시지 전송 주소 : /app/chat.sendMessage
	/*
	 JSON
	 {
    "type": "TALK",
    "sender": "jkha22",
    "message": "누구냐"
}
	 */
	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	fun sendMessage(@Payload chatMessage: ChatMessage?): ChatMessage?{
		return chatMessage
	}
}
