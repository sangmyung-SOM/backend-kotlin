package com.smu.som.reportQnA

import com.fasterxml.jackson.annotation.JsonProperty


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
