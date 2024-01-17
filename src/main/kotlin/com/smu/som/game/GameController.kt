package com.smu.som.game

import com.smu.som.chatwithroom.ChatRoom
import com.smu.som.chatwithroom.ChatService
import com.smu.som.reportQnA.ReadReportDTO
import com.smu.som.reportQnA.ReportService
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
class GameController {

	var reportService: ReportService = ReportService()

//	@PostMapping("/room/{roomId}")
//	@ResponseBody
//	fun saveQuestionAndAnswer(@PathVariable roomId: String,
//		@RequestBody qna: ReadReportDTO
//	): Boolean {
//		println("saveQuestionAndAnswer answer : ${qna.answer}")
//		return reportService.sendQnA(roomId, qna)
//	}

	@GetMapping("/room/{roomId}")
	@ResponseBody
	fun getQuestionAndAnswer(@PathVariable roomId: String): List<ReadReportDTO> {
		println("getQuestionAndAnswer")
		return reportService.getQnA(roomId)
	}
}
