package com.smu.som.gameroom.controller

import com.smu.som.gameroom.GameRoomSetting
import com.smu.som.gameroom.service.GameRoomService
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.awt.print.Pageable

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
class GameRoomController {

	private val gameRoomService: GameRoomService = GameRoomService()

	@PostMapping("/room")
	@ResponseBody
	fun createRoom(@RequestParam name: String,
				   @RequestParam category: String,
				   @RequestParam adult: String,
				   @RequestParam(name="mal") malNum: Int): GameRoomSetting
	{
		println("게임방 생성")

		if(malNum < 1 || 4 < malNum ){
			throw RuntimeException("말의 개수는 1~4 사이어야합니다. 요청한 말=${malNum}")
		}

		return gameRoomService.createGameRoom(name, category, adult, malNum)
	}

	@GetMapping("/rooms")
	@ResponseBody
	fun room(): List<GameRoomSetting> {
		return gameRoomService.findAllRoom()
	}

	@GetMapping("/room")
	@ResponseBody
	fun rooms(@RequestParam(defaultValue = "1") pageNumber: Int,
			 @RequestParam(defaultValue = "7") pageSize: Int
	): List<GameRoomSetting> {
		return gameRoomService.findRoom(pageNumber, pageSize)
	}

	@DeleteMapping("/room/{roomId}")
	@ResponseBody
	fun deleteRoom(@PathVariable roomId: String) {
		println("게임방 삭제")
		gameRoomService.deleteById(roomId)
	}

	@PatchMapping("/room/{roomId}/update")
	@ResponseBody
	fun updateRoomState(@PathVariable roomId: String, @RequestParam state : Boolean) : Boolean {
		return gameRoomService.updateState(roomId, state)
	}
}
