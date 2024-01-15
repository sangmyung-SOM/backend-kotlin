package com.smu.som.report

import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
class ReportController(
	private val reportService: ReportService
) {

	@PostMapping("/{gameRoomId}/qna")
	fun sendQnA(
		@PathVariable(name = "gameRoomId") gameroomId: String,
		@RequestBody qna: ReadReportDTO
	) : ResponseEntity<Any> {
		println("sendQnA answer : ${qna.answer}")
		return ResponseEntity.ok().body(reportService.sendQnA(gameroomId, qna))
	}

	@GetMapping("/{gameRoomId}/qna")
	fun getQnA(
		@PathVariable(name = "gameRoomId") gameroomId: String
	) : List<ReadReportDTO> {
		println("getQnA")
		return reportService.getQnA(gameroomId)
	}

}
