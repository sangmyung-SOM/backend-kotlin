package com.smu.som.gameroom.controller

import com.smu.som.gameroom.GameRoomSetting
import com.smu.som.gameroom.service.GameRoomService
import com.smu.som.reportQnA.ReadReportDTO
import com.smu.som.reportQnA.ReportService
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
class GameRoomController {

	private val gameRoomService: GameRoomService = GameRoomService()
	private val reportService: ReportService = ReportService()

	@GetMapping("/log")
	@ResponseBody
	fun logTest() : String{
		println("This is a test log message from the server.")
		return "테스트 로그. 서버 동작 확인용"
	}

	@PostMapping("/room/reports/{roomId}/qna")
	@ResponseBody
	fun sendQnA(
		@PathVariable roomId: String,
		@RequestParam answer : String,
		@RequestParam question : String,
		@RequestParam playerId : String
	) : Boolean {
		println("sendQnA answer : ${answer}")
//		return reportService.sendQnA(gameRoomId, qna)
		return reportService.sendQnA(roomId, ReadReportDTO(answer, playerId, question))
	}

	@GetMapping("/room/reports/{roomId}/qna")
	@ResponseBody
	fun getQnA(
		@PathVariable(name = "roomId") gameRoomId: String
	) : List<ReadReportDTO> {
		println("getQnA")
		return reportService.getQnA(gameRoomId)
	}

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
}
