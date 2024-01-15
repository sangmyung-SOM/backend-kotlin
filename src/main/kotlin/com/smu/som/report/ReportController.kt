package com.smu.som.report

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/report")
class ReportController(
	private val reportService: ReportService
) {

	@PostMapping("/{gameRoomId}/qna")
	fun sendQnA(
		@PathVariable(name = "gameRoomId") gameroomId: String,
		@RequestBody qna: ReadReportDTO
	) : ResponseEntity<Any> {
		return ResponseEntity.ok().body(reportService.sendQnA(gameroomId, qna))
	}

	@GetMapping("/{gameRoomId}/qna")
	fun getQnA(
		@PathVariable(name = "gameRoomId") gameroomId: String
	) : List<ReadReportDTO> {
		return reportService.getQnA(gameroomId)
	}

}
