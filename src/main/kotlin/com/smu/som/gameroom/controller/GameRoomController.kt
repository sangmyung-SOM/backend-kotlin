package com.smu.som.gameroom.controller

import com.smu.som.gameroom.GameRoomSetting
import com.smu.som.gameroom.service.GameRoomService
import com.smu.som.reportQnA.ReadReportDTO
import com.smu.som.reportQnA.ReportService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
class GameRoomController {

	private val gameRoomService: GameRoomService = GameRoomService()


	// 게임방 생성
	@PostMapping("/room")
	@ResponseBody
	fun createRoom(@RequestParam name: String,
				   @RequestParam category: String,
				   @RequestParam adult: String,
				   @RequestParam(name="mal") malNumLimit: Int): GameRoomSetting
	{
		println("게임방 생성 v2")

		if(malNumLimit < 1 || 4 < malNumLimit ){
			throw RuntimeException("말의 개수는 1~4 사이어야합니다. 요청한 말 개수 제한=${malNumLimit}")
		}

		return gameRoomService.createGameRoom(name, category, adult, malNumLimit)
	}

	// 게임방 목록 조회
	@GetMapping("/rooms")
	@ResponseBody
	fun room(): List<GameRoomSetting> {
		return gameRoomService.findAllRoom()
	}

	// 게임방 목록 페이징
	@GetMapping("/room")
	@ResponseBody
	fun rooms(@RequestParam(defaultValue = "1") pageNumber: Int,
			 @RequestParam(defaultValue = "7") pageSize: Int
	): List<GameRoomSetting> {
		return gameRoomService.findRoom(pageNumber, pageSize)
	}

	// 게임방 삭제
	@DeleteMapping("/room/{roomId}")
	@ResponseBody
	fun deleteRoom(@PathVariable roomId: String) {
		println("게임방 삭제")
		gameRoomService.deleteById(roomId)
	}

	// 게임방 게임 시작으로 상태 변경
	@PatchMapping("/room/{roomId}/update")
	@ResponseBody
	fun updateRoomState(@PathVariable roomId: String, @RequestParam state : Boolean) : Boolean {
		return gameRoomService.updateState(roomId, state)
	}
}
