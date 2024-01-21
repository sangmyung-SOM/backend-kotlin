package com.smu.som.chatwithroom

import com.google.common.collect.EvictingQueue
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

	fun saveRoom(roomId: String) : Boolean {
		val chatRoom:ChatRoom? = ChatRoom(roomId, "", EvictingQueue.create(20))
		// 이미 존재하는 방이면 false 반환
		if(chatRooms.containsKey(chatRoom!!.roomId!!)) {
			return false
		}
		chatRooms.put(chatRoom!!.roomId!!, chatRoom!!)
		return true
	}
}
