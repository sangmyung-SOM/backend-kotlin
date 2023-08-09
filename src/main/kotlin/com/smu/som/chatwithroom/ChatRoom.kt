package com.smu.som.chatwithroom

import lombok.NoArgsConstructor
import java.util.UUID

@NoArgsConstructor
class ChatRoom(
    var roomId:String?,
    var roomName:String?
) {
    companion object {
        fun of(roomName: String):ChatRoom{
            return ChatRoom(
                roomId=UUID.randomUUID().toString(),
                roomName=roomName
            )
        }
    }
}
