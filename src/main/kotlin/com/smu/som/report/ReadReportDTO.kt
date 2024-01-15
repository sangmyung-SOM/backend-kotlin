package com.smu.som.report

import com.fasterxml.jackson.annotation.JsonProperty


class ReadReportDTO (
	@JsonProperty("answer")
	var answer: String,

	@JsonProperty("playerId")
	var playerId: String,

	@JsonProperty("question")
	var question: String,

) {
	companion object {
		fun of(answer: String, playerId: String, question: String): ReadReportDTO {
			return ReadReportDTO(
				answer = answer,
				playerId = playerId,
				question = question
			)
		}
	}
}
