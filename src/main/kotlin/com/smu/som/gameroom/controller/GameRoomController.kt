package com.smu.som.gameroom.controller

import com.smu.som.gameroom.GameRoomSetting
import com.smu.som.gameroom.service.GameRoomService
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
class GameRoomController {

	private val gameRoomService: GameRoomService = GameRoomService()

	@PostMapping("/room")
	@ResponseBody
	fun createRoom(@RequestParam name: String,
				   @RequestParam category: String,
				   @RequestParam adult: String ): GameRoomSetting
	{
		return gameRoomService.createGameRoom(name, category, adult)
	}

	@GetMapping("/rooms")
	@ResponseBody
	fun room(): List<GameRoomSetting> {
		return gameRoomService.findAllRoom()
	}

	@DeleteMapping("/room/{roomId}")
	@ResponseBody
	fun deleteRoom(@PathVariable roomId: String) {
		gameRoomService.deleteById(roomId)
	}
}
