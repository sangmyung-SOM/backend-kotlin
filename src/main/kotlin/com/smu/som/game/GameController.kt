package com.smu.som.game

import com.smu.som.chatwithroom.ChatRoom
import com.smu.som.chatwithroom.ChatService
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
class GameController {

	var gameService: GameService = GameService()
	var chatService: ChatService = ChatService()
//
//	@GetMapping("/room/{roomId}")
//	@ResponseBody
//	fun roomInfo(@PathVariable roomId: String): ChatRoom?{
//		return chatService.findById(roomId)
//	}
}
