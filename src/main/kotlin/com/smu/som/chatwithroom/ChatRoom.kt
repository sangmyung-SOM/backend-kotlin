package com.smu.som.chatwithroom

import com.google.common.collect.EvictingQueue
import lombok.NoArgsConstructor
import java.util.UUID

@NoArgsConstructor
class ChatRoom(
	var roomId: String?,
	var roomName: String?,
	var chatList: EvictingQueue<ChatMessage>

) {
    companion object {
        fun of(roomName: String):ChatRoom{
            return ChatRoom(
                roomId=UUID.randomUUID().toString(),
                roomName=roomName,
				chatList= EvictingQueue.create(20)
            )
        }
    }
}
