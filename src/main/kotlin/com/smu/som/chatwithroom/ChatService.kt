package com.smu.som.chatwithroom

import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import java.util.Collections

@Service
@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor
class ChatService(
    var chatRooms: HashMap<String, ChatRoom> = LinkedHashMap<String,ChatRoom>()
) {
    fun findAllRoom():List<ChatRoom>{
        var result = ArrayList(chatRooms.values)
        result.reverse()

        return result
    }

    fun findById(roomId:String):ChatRoom?{
        return chatRooms.get(roomId)
    }

    fun createRoom(name:String):ChatRoom{
        var chatRoom:ChatRoom = ChatRoom.of(name)
        chatRooms.put(chatRoom.roomId!!, chatRoom)
        return chatRoom
    }
}
