package com.smu.som.gameroom.controller

import com.smu.som.gameroom.GameRoomSetting
import com.smu.som.gameroom.service.GameRoomService
import com.smu.som.report.ReadReportDTO
import com.smu.som.report.ReportService
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
class GameRoomController {

	private val gameRoomService: GameRoomService = GameRoomService()
	private val reportService: ReportService = ReportService()

	@PostMapping("/room")
	@ResponseBody
	fun createRoom(@RequestParam name: String,
				   @RequestParam category: String,
				   @RequestParam adult: String ): GameRoomSetting
	{
		println("게임방 생성")
		return gameRoomService.createGameRoom(name, category, adult)
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

	@PostMapping("/report/{gameRoomId}/qna")
	@ResponseBody
	fun sendQnA(
		@PathVariable(name = "gameRoomId") gameroomId: String,
		@RequestBody qna: ReadReportDTO
	) : ResponseEntity<Any> {
		println("sendQnA answer : ${qna.answer}")
		return ResponseEntity.ok().body(reportService.sendQnA(gameroomId, qna))
	}

	@GetMapping("/report/{gameRoomId}/qna")
	@ResponseBody
	fun getQnA(
		@PathVariable(name = "gameRoomId") gameroomId: String
	) : List<ReadReportDTO> {
		println("getQnA")
		return reportService.getQnA(gameroomId)
	}
}
