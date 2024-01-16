package com.smu.som.reportQnA

import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
class ReportController(
	private val reportService: ReportService
) {

	@PostMapping("/{gameRoomId}/qna")
	@ResponseBody
	fun sendQnA(
		@PathVariable(name = "gameRoomId") gameRoomId: String,
		@RequestBody qna: ReadReportDTO
	) : ResponseEntity<Any> {
		println("sendQnA answer : ${qna.answer}")
		return ResponseEntity.ok().body(reportService.sendQnA(gameRoomId, qna))
	}

	@GetMapping("/{gameRoomId}/qna")
	@ResponseBody
	fun getQnA(
		@PathVariable(name = "gameRoomId") gameRoomId: String
	) : List<ReadReportDTO> {
		println("getQnA")
		return reportService.getQnA(gameRoomId)
	}

}
