package com.smu.som.reportQnA

import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RequestMapping("/reports")
class ReportController(
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
