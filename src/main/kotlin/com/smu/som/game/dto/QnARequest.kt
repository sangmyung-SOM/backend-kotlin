package com.smu.som.game.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import lombok.Getter
import lombok.NoArgsConstructor

class QnARequest {

	@Getter
	@NoArgsConstructor
	class GetQuestionDTO {

		@JsonProperty("question_id")
		var questionId : Long

		@JsonProperty("question")
		var question : String

		@JsonProperty("player_id")
		var playerId : String

		@JsonProperty("room_id")
		var roomId : String

		constructor(questionId : Long, question : String, playerId : String, roomId : String){
			this.questionId = questionId
			this.question = question
			this.playerId = playerId
			this.roomId = roomId
		}
	}


	@Getter
	@NoArgsConstructor
	class GetAnswerDTO {

		@JsonProperty("answer")
		var answer : String

		@JsonProperty("player_id")
		var playerId : String

		@JsonProperty("room_id")
		var roomId : String

		@JsonProperty("turn_change")
		lateinit var turnChange : String

		constructor(answer : String, playerId : String, roomId : String){
			this.answer = answer
			this.playerId = playerId
			this.roomId = roomId
		}
	}
}
