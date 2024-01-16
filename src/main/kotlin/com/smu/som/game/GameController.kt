package com.smu.som.game

import com.smu.som.chatwithroom.ChatRoom
import com.smu.som.chatwithroom.ChatService
import com.smu.som.reportQnA.ReadReportDTO
import com.smu.som.reportQnA.ReportService
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/game")
class GameController(
	private val reportService: ReportService
) {

	@PostMapping("/{roomId}/qna")
	@ResponseBody
	fun sendQnA(
		@PathVariable(name = "roomId") roomId: String,
		@RequestBody qna: ReadReportDTO
	) : ResponseEntity<Any> {
		println("sendQnA answer : ${qna.answer}")
		return ResponseEntity.ok().body(reportService.sendQnA(roomId, qna))
	}

	@GetMapping("/{roomId}/qna")
	@ResponseBody
	fun getQnA(
		@PathVariable(name = "roomId") roomId: String
	) : List<ReadReportDTO> {
		println("getQnA")
		return reportService.getQnA(roomId)
	}

}
