package com.smu.som.reportQnA

import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// 질문 기록 조회
@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
class ReportController(
	private val reportService: ReportService
) {

	// 질문 및 답변 저장
	@PostMapping("/{roomId}/qna")
	@ResponseBody
	fun sendQnA(
		@PathVariable(name = "roomId") roomId: String,
		@RequestBody qna: ReadReportDTO
	) : ResponseEntity<Any> {
		return ResponseEntity.ok().body(reportService.sendQnA(roomId, qna))
	}

	// 질문 및 답변 기록들 조회
	@GetMapping("/{roomId}/qna")
	@ResponseBody
	fun getQnA(
		@PathVariable(name = "roomId") roomId: String
	) : List<ReadReportDTO> {
		return reportService.getQnA(roomId)
	}

}
