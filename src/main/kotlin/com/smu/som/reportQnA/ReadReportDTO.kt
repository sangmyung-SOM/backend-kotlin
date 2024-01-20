package com.smu.som.reportQnA


// 게임에서 사용하는 질문기록 DTO
class ReadReportDTO (
	var answer: String?,
	var question: String?,
	var playerId: String?
) {
	companion object {
		fun of(answer: String, playerId: String, question: String): ReadReportDTO {
			return ReadReportDTO(
				answer = answer,
				question = question,
				playerId = playerId
			)
		}
	}
}
