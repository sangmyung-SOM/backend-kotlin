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
	private val reportService: ReportService = ReportService()

	@GetMapping("/log")
	fun logTest() : String{
		println("This is a test log message from the server.")
		return "테스트 로그. 서버 동작 확인용"
	}

	@GetMapping("/rooms/v2")
	fun logTest2() : String{
		println("This is a test log message from the server. v2")
		return "테스트 로그 v2. 서버 동작 확인용"
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
				   @RequestParam adult: String,
				   @RequestParam(name="mal") malNumLimit: Int): GameRoomSetting
	{
		println("게임방 생성 v2")

		if(malNumLimit < 1 || 4 < malNumLimit ){
			throw RuntimeException("말의 개수는 1~4 사이어야합니다. 요청한 말 개수 제한=${malNumLimit}")
		}

		return gameRoomService.createGameRoom(name, category, adult, malNumLimit)
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
