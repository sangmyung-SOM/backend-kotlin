package com.smu.som.controller.api


import com.smu.som.chatwithroom.ChatRoom
import com.smu.som.chatwithroom.ChatService
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
class ChatRoomController(
    var chatService: ChatService
) {
    @GetMapping("/room")
    fun rooms(model: Model): String {
        return "/chat/room"
    }

    @GetMapping("/rooms")
    @ResponseBody
    fun room(): List<ChatRoom> {
        return chatService.findAllRoom()
    }

    @PostMapping("/room")
    @ResponseBody
	fun createRoom(@RequestParam name: String): ResponseEntity<Map<String, String?>> {
		val chatRoom = chatService.createRoom(name)
		val responseData: Map<String, String?> = mapOf("roomId" to chatRoom.roomId)
		return ResponseEntity.ok(responseData)
	}

	@GetMapping("room/enter/{roomId}")
    fun roomDetail(model: Model, @PathVariable roomId:String):String{
        model.addAttribute("roomId",roomId)
        return "/chat/roomdetail"
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    fun roomInfo(@PathVariable roomId: String): ChatRoom?{
        return chatService.findById(roomId)
    }
}
